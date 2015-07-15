package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.mvc.Http.RequestBody;

import views.html.*;
import views.html.export.*;

import models.*;
import models.durableGoods.*;
import models.durableArticles.*;
import models.type.ExportStatus;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;                                                                                         
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;  

public class ExportOrder extends Controller {
	// เยิกจ่าย
    @Security.Authenticated(Secured.class)
    public static Result exportOrder() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        List<Requisition> initList = Requisition.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Requisition> successList = Requisition.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportOrder.render(user, initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateOrder() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Requisition temp =  new Requisition();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportOrder.exportOrderAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOrderAdd(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Requisition req = Requisition.find.byId(id);
        if(req == null || req.status != ExportStatus.INIT){
            return redirect(routes.ExportOrder.exportOrder());
        }
        return ok(exportOrderAdd.render(user,req));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Requisition req = Requisition.find.byId(id);
        if(req == null || req.status != ExportStatus.SUCCESS){
            return redirect(routes.ExportOrder.exportOrder());
        }
        return ok(exportOrderViewDetail.render(user,req));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveRequisition(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Requisition req = Requisition.find.byId(id);

        if(req != null && (req.status == ExportStatus.INIT || req.status == ExportStatus.SUCCESS) ){
            DynamicForm f = Form.form().bindFromRequest();
            req.title = f.get("title");
            req.number = f.get("number");
            req.setApproveDate(f.get("approveDate"));

            // save withdrawer
            String firstName = f.get("firstName");
            String lastName = f.get("lastName");
            String position = f.get("position");
            List<User> employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() > 0){
                req.user = employees.get(0);
            }

            // save approver
            firstName = f.get("approverFirstName");
            lastName = f.get("approverLastName");
            position = f.get("approverPosition");
            employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() > 0){
                req.approver = employees.get(0);
            }

            // updatae status Requisition
            req.status = ExportStatus.SUCCESS;
            req.update();

            // update Material remain
            for(RequisitionDetail detail: req.details){
                detail = RequisitionDetail.find.byId(detail.id);
                if(detail.status == ExportStatus.INIT){
                    detail.code.remain -= detail.quantity;
                    detail.code.update();
                    detail.updateDetail();
                    detail.status = ExportStatus.SUCCESS;
                    RequisitionDetail.updateAfter(detail.requisition.approveDate, detail.code);
                }
                detail.year = req.approveDate.getYear() + 2443;
                detail.update();
            }
        }

        return redirect(routes.ExportOrder.exportOrder());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelRequisition(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Requisition req = Requisition.find.byId(id);
        if(req != null && req.status == ExportStatus.INIT){
            req.status = ExportStatus.CANCEL;
            req.update();
        }
        return redirect(routes.ExportOrder.exportOrder());
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteRequisition(){
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
                Requisition order = Requisition.find.byId(id);
                if(order != null){
                    if(order.status == ExportStatus.INIT){
                        order.status = ExportStatus.DELETE;
                        order.update();
                    }
                    else if(order.status == ExportStatus.SUCCESS){
                        // update Material remain
                        for(RequisitionDetail detail: order.details){
                            if(detail.status == ExportStatus.SUCCESS){
                                detail.code.remain += detail.quantity;
                                detail.code.update();
                                detail.status = ExportStatus.DELETE;
                                detail.update();
                                RequisitionDetail.updateAfter(detail.requisition.approveDate, detail.code);
                            }
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
    public static Result saveOrderDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            Requisition requisition = Requisition.find.byId(new Long(json.get("requisitionId").toString()));
            if(requisition != null && (requisition.status == ExportStatus.INIT || requisition.status == ExportStatus.SUCCESS)){
                RequisitionDetail newDetail = new RequisitionDetail();
                newDetail.requisition = requisition;
                MaterialCode code =  MaterialCode.find.byId(json.get("code").asText());
                if(code != null){
                    newDetail.code = code;
                    code.updateRemain();

                    int allQuantityOrder = 0;
                    if(requisition.status != ExportStatus.SUCCESS){
                        for(RequisitionDetail detail : RequisitionDetail.find.where().eq("requisition",requisition).eq("code",code).findList()){
                            allQuantityOrder += detail.quantity;
                        }
                    }

                    int quantity = Integer.parseInt(json.get("quantity").asText());

                    if(quantity > 0 && allQuantityOrder + quantity <= code.remain){
                        newDetail.quantity = quantity;
                        newDetail.description = json.get("description").asText();
                        String firstName = json.get("withdrawerNmae").asText();
                        String lastName = json.get("withdrawerLastname").asText();
                        String position = json.get("withdrawerPosition").asText();
                        List<User> withdrawers = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
                        if(withdrawers.size() > 0){
                            newDetail.withdrawer = withdrawers.get(0);
                            newDetail.status = ExportStatus.INIT;
                            newDetail.save();
                            if(requisition.status == ExportStatus.SUCCESS){
                                newDetail.code.remain -= quantity;
                                newDetail.code.update();
                                newDetail.update();
                                RequisitionDetail.updateAfter(newDetail.requisition.approveDate, newDetail.code);
                            }
                            result.put("status", "SUCCESS");
                        }
                        else{
                            result.put("message", "ไม่พบผู้เบิก");
                            result.put("status", "error5");
                        }
                    }
                    else{
                        if(quantity > 0)
                            result.put("message", "จำนวนเบิกจ่ายไม่ถูกต้อง \n\nกรุณาระบุจำนวน " + code.description + " ไม่เกิน " + (code.remain-allQuantityOrder) + " " + code.classifier);
                        else
                            result.put("message", "จำนวนเบิกจ่ายไม่ถูกต้อง \n\nกรุณาระบุจำนวน " + code.description + " มากว่า 0 " + code.classifier);
                        result.put("status", "error4");
                    }
                }
                else{
                    result.put("message", "หมายเลขวัสดุไม่ถูกต้อง");
                    result.put("status", "error3");
                }
            }
            else{
                result.put("message", "ไม่สามารถเพิ่มรายการเบิกใรก ใบเบิก เลขที่" + json.get("requisitionId") + "ได้");
                result.put("status", "error1");
            }
        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status","error01");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status","error00");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result editOrderDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
       try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            RequisitionDetail detail = RequisitionDetail.find.byId((new Long(json.get("id").toString())));
            Requisition requisition = Requisition.find.byId(new Long(json.get("requisitionId").toString()));
            if(detail != null && requisition != null && (requisition.status == ExportStatus.INIT || requisition.status == ExportStatus.SUCCESS) ){
                MaterialCode code =  MaterialCode.find.byId(json.get("code").asText());
                if(code != null){
                    detail.code = code;
                    code.updateRemain();
                    int quantity = Integer.parseInt(json.get("quantity").asText());

                    if(quantity > 0 && quantity <= (code.remain+detail.quantity)){
                        detail.description = json.get("description").asText();
                        String firstName = json.get("withdrawerNmae").asText();
                        String lastName = json.get("withdrawerLastname").asText();
                        String position = json.get("withdrawerPosition").asText();
                        List<User> withdrawers = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
                        if(withdrawers.size() > 0){
                            if(requisition.status == ExportStatus.SUCCESS){
                                detail.code.remain += detail.quantity;
                                detail.code.update();
                                detail.code = MaterialCode.find.byId(json.get("code").asText()); // ไม่ควรแก้ detail.code อาจเท่ากะบ code
                                detail.code.remain -= quantity;
                                detail.code.update();
                                detail.quantity = quantity;
                                RequisitionDetail.updateAfter(detail.requisition.approveDate, detail.code);
                            }
                            detail.withdrawer = withdrawers.get(0);
                            detail.update();
                            result.put("status", "SUCCESS");
                        }
                        else{
                            result.put("message", "ไม่พบผู้เบิก");
                            result.put("status", "error4");
                        }
                    }
                    else{
                        if(quantity > 0)
                            result.put("message", "จำนวนเบิกจ่ายไม่ถูกต้อง \n\nกรุณาระบุจำนวน " + code.description + " ไม่เกิน " + (code.remain+detail.quantity) + " " + code.classifier);
                        else
                            result.put("message", "จำนวนเบิกจ่ายไม่ถูกต้อง \n\nกรุณาระบุจำนวน " + code.description + " มากว่า 0 " + code.classifier);
                        result.put("status", "error3");
                    }
                }
                else{
                    result.put("message", "หมายเลขวัสดุไม่ถูกต้อง");
                    result.put("status", "error2");
                }
            }
            else{
                result.put("message", "ไม่สามารถเพิ่มรายการเบิกใรก ใบเบิก เลขที่ " + json.get("requisitionId") + " ได้");
                result.put("status", "error1");
            }
        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status","error01");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status","error00");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteOrderDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Requisition order = Requisition.find.byId(id);
            if(order != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    RequisitionDetail detail = RequisitionDetail.find.byId(id);
                    if(detail != null && order.equals(detail.requisition)){
                        if(detail.status == ExportStatus.SUCCESS){
                            detail.code.remain += detail.quantity;
                            detail.code.update();
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
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status","error01");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status","error00");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result loadOrderDetail(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            Requisition requisition = Requisition.find.byId(id);
            if(requisition != null){
                List<RequisitionDetail> detail = RequisitionDetail.find.where().eq("requisition", requisition).findList();
                ObjectMapper mapper = new ObjectMapper();
                String jsonArray = mapper.writeValueAsString(detail);
                json = Json.parse(jsonArray);
                result.put("details",json);
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found requisition id:" + id);
                result.put("status", "error");
            }

        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
        }
        return ok(result);
    }

    public static Result updateMaterialRemain(){
        List<MaterialCode> allMaterial = MaterialCode.find.all();
        for(MaterialCode material : allMaterial){
            material.updateRemain();
        }
        return TODO;
    }
}
