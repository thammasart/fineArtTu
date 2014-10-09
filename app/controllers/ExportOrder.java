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
import models.durableArticles.*;
import models.type.ExportStatus;

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

public class ExportOrder extends Controller {
	// เยิกจ่าย
    @Security.Authenticated(Secured.class)
    public static Result exportOrder() {
        User user = User.find.byId(session().get("username"));
        List<Requisition> initList = Requisition.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Requisition> successList = Requisition.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportOrder.render(user, initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateOrder() {
        Requisition temp =  new Requisition();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportOrder.exportOrderAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOrderAdd(long id) {
        User user = User.find.byId(session().get("username"));
        Requisition req = Requisition.find.byId(id);
        if(req != null && req.status == ExportStatus.SUCCESS){
            return redirect(routes.ExportOrder.exportOrder());
        }
        return ok(exportOrderAdd.render(user,req));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOrderAddDetail(long id) {
        User user = User.find.byId(session().get("username"));
        DynamicForm f = Form.form().bindFromRequest();
        System.out.println(f.get("title"));
        return ok(exportOrderAddDetail.render(user, Requisition.find.byId(id)));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveRequisition(long id){
        User user = User.find.byId(session().get("username"));
        Requisition req = Requisition.find.byId(id);

        DynamicForm f = Form.form().bindFromRequest();

        req.title = f.get("title");
        req.number = f.get("number");
        req.setApproveDate(f.get("approveDate"));

        String firstName = f.get("firstName");
        String lastName = f.get("lastName");
        String position = f.get("position");
        List<User> employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
        if(employees.size() == 1){
            req.user = employees.get(0);
            System.out.println("user :" + req.user.username);
        }
        firstName = f.get("approverFirstName");
        lastName = f.get("approverLastName");
        position = f.get("approverPosition");
        employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
        if(employees.size() == 1){
            req.approver = employees.get(0);
            System.out.println("appover :" + req.approver.username);
        }
        req.status = ExportStatus.SUCCESS;
        req.update();

        return redirect(routes.ExportOrder.exportOrder());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelRequisition(long id){
        User user = User.find.byId(session().get("username"));
        Requisition req = Requisition.find.byId(id);
        if(req != null && req.status == ExportStatus.INIT){
            req.status = ExportStatus.CANCEL;
            req.update();
        }
        return redirect(routes.ExportOrder.exportOrder());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveOrderDetail() {
        RequestBody body = request().body();
        JsonNode json = body.asJson();
        //System.out.println("saveOrderDetail\n ");
        //System.out.println(json);
        RequisitionDetail newDetail = new RequisitionDetail();

        newDetail.requisition = Requisition.find.byId(new Long(json.get("requisitionId").toString()));
        MaterialCode code =  MaterialCode.find.byId(json.get("code").asText());
        if(code != null){
            newDetail.code = code;
        }
        int quantity = Integer.parseInt(json.get("quantity").asText());
        if(quantity > 0){
            newDetail.quantity = quantity;
        }
        String firstName = json.get("withdrawerNmae").asText();
        String lastName = json.get("withdrawerLastname").asText();
        String position = json.get("withdrawerPosition").asText();
        List<User> withdrawers = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
        if(withdrawers.size() == 1){
            newDetail.withdrawer = withdrawers.get(0);
        }
        newDetail.save();

        return ok(body.asJson());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result loadOrderDetail(long id) {
        ObjectNode result = Json.newObject();
        System.out.println("loadOrderDetail");
        JsonNode json;
        try { 
            Requisition  requisition = Requisition.find.byId(id);
            List<RequisitionDetail> detail = RequisitionDetail.find.where().eq("requisition", requisition).findList();
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(detail);
            json = Json.parse(jsonArray);
            result.put("details",json);
            System.out.println("SUCCESS");

        }
        catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "error1");
            System.out.println("ERROR 1" + e.getMessage());
        }
        catch(RuntimeException e){
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "error2");
            System.out.println("ERROR 2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
            System.out.println("ERROR 3");
        }
        return ok(result);
    }
}