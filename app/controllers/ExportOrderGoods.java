package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.mvc.Http.RequestBody;

import views.html.*;
import views.html.export.*;

import models.*;
import models.consumable.*;
import models.durableGoods.*;
import models.type.ExportStatus;
import models.type.SuppliesStatus;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;                                                                                         
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;  

public class ExportOrderGoods extends Controller {
	// เยิกจ่าย วัสดุสิ้นเปลือง
    @Security.Authenticated(Secured.class)
    public static Result exportOrderGoods() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        List<OrderGoods> initList = OrderGoods.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<OrderGoods> successList = OrderGoods.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportOrderGoods.render(user, initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateOrderGoods() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OrderGoods temp =  new OrderGoods();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportOrderGoods.exportOrderGoodsAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOrderGoodsAdd(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OrderGoods req = OrderGoods.find.byId(id);
        if(req == null || req.status != ExportStatus.INIT){
            return redirect(routes.ExportOrderGoods.exportOrderGoods());
        }
        return ok(exportOrderGoodsAdd.render(user,req));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OrderGoods req = OrderGoods.find.byId(id);
        if(req == null || req.status != ExportStatus.SUCCESS){
            return redirect(routes.ExportOrderGoods.exportOrderGoods());
        }
        return ok(exportOrderGoodsViewDetail.render(user,req));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveOrderGoods(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OrderGoods order = OrderGoods.find.byId(id);

        if(order != null && (order.status == ExportStatus.INIT || order.status == ExportStatus.SUCCESS) ){
            DynamicForm f = Form.form().bindFromRequest();
            order.title = f.get("title");
            order.number = f.get("number");
            order.setApproveDate(f.get("approveDate"));

            // save withdrawer
            String firstName = f.get("firstName");
            String lastName = f.get("lastName");
            String position = f.get("position");
            List<User> employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() == 1){
                order.user = employees.get(0);
            }

            // save approver
            firstName = f.get("approverFirstName");
            lastName = f.get("approverLastName");
            position = f.get("approverPosition");
            employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() == 1){
                order.approver = employees.get(0);
            }

            // update Material remain
            for(OrderGoodsDetail detail: order.details){
            	detail.goods.status = SuppliesStatus.WITHDRAW;
                detail.goods.update();
                //detail.update();
            }

            // updatae status OrderGoods
            order.status = ExportStatus.SUCCESS;
            order.update();
        }

        return redirect(routes.ExportOrderGoods.exportOrderGoods());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelOrderGoods(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OrderGoods req = OrderGoods.find.byId(id);
        if(req != null && req.status == ExportStatus.INIT){
            req.status = ExportStatus.CANCEL;
            req.update();
        }
        return redirect(routes.ExportOrderGoods.exportOrderGoods());
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteOrderGoods(){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            for (final JsonNode objNode : json.get("detail")) {
                Long id = Long.parseLong(objNode.toString());
                OrderGoods order = OrderGoods.find.byId(id);
                if(order != null){
                    if(order.status == ExportStatus.INIT){
                        order.status = ExportStatus.DELETE;
                        order.update();
                    }
                    else if(order.status == ExportStatus.SUCCESS){
                        for(OrderGoodsDetail detail: order.details){
                        	detail.goods.status = SuppliesStatus.NORMAL;
                			detail.goods.update();
                            //detail.update();
                        }
                        order.status = ExportStatus.DELETE;
                        order.update();
                    }
                }
            }
            result.put("status", "SUCCESS");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result saveOrderGoodsDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            OrderGoods order = OrderGoods.find.byId(id);





            if(order != null){
                for (final JsonNode objNode : json.get("detail")) {
                    OrderGoodsDetail newDetail = new OrderGoodsDetail();
                    id = Long.parseLong(objNode.toString());
                    DurableGoods goods = DurableGoods.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                    if(goods != null){
                        newDetail.department = json.get("department").asText();
                        newDetail.room = json.get("room").asText();
                        newDetail.floorLevel = json.get("floorLevel").asText();
                        newDetail.firstName = json.get("recieveTitle").asText();
                        newDetail.firstName = json.get("recieveFirstName").asText();
                        newDetail.lastName = json.get("recieveLastName").asText();
                        newDetail.position = json.get("recievePosition").asText();
                        newDetail.goods = goods;
                        if(order.status == ExportStatus.SUCCESS){
                            goods.department = newDetail.department;
                            goods.room = newDetail.room;
                            goods.floorLevel = newDetail.floorLevel;
                            goods.firstName = newDetail.firstName;
                            goods.firstName = newDetail.firstName;
                            goods.lastName = newDetail.lastName;
                            goods.status = SuppliesStatus.WITHDRAW;
                            goods.update();
                        }
                        newDetail.order = order;
                        newDetail.save();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message", "ไม่สามารถเพิ่มรายการเบิกใรก ใบเบิก เลขที่" + json.get("id") + "ได้");
                result.put("status", "error");
            }
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error2");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result editOrderGoodsDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();

            OrderGoods order = OrderGoods.find.byId(new Long(json.get("orderGoodsId").asText()));
            OrderGoodsDetail detail = OrderGoodsDetail.find.byId((new Long(json.get("id").asText())));
            if( detail != null && order != null ){
                detail.department = json.get("department").asText();
                detail.room = json.get("room").asText();
                detail.floorLevel = json.get("floorLevel").asText();
                detail.title = json.get("title").asText();
                detail.firstName = json.get("firstName").asText();
                detail.lastName = json.get("lastName").asText();
                detail.position = json.get("position").asText();
                detail.update();
            }
            else{
                result.put("message", "cannot find order or detail");
                result.put("status", "error");
            }

            result.put("status", "SUCCESS");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteOrderGoodsDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            OrderGoods order = OrderGoods.find.byId(id);
            if(order != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    OrderGoodsDetail detail = OrderGoodsDetail.find.byId(id);
                    if(detail != null && order.equals(detail.order)){
                        if(detail.order.status == ExportStatus.SUCCESS){
                            detail.goods.status = SuppliesStatus.NORMAL;
                			detail.goods.update();
                        }
                        detail.delete();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found order id:" + id);
                result.put("status", "error");
            }
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result loadOrderGoodsDetail(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            OrderGoods order = OrderGoods.find.byId(id);
            if(order != null){
                ObjectMapper mapper = new ObjectMapper();
                String jsonArray = mapper.writeValueAsString(order.details);
                json = Json.parse(jsonArray);
                result.put("details",json);
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found order id:" + id);
                result.put("status", "error");
            }

        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
        }
        return ok(result);
    }
}
