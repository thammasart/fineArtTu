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
import models.type.ExportStatus;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Export extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result export() {
        User user = User.find.byId(session().get("username"));
        return ok(export.render(user));
    }


    // เยิกจ่าย
    @Security.Authenticated(Secured.class)
    public static Result exportOrder() {
        User user = User.find.byId(session().get("username"));
        List<Requisition> requisitionList = models.consumable.Requisition.find.all();
        return ok(exportOrder.render(user,requisitionList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportNewOrder() {
        Requisition temp =  new Requisition();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.Export.exportOrderAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOrderAdd(long requisitionId) {
        User user = User.find.byId(session().get("username"));
        return ok(exportOrderAdd.render(user,Requisition.find.byId(requisitionId)));
    }


    @Security.Authenticated(Secured.class)
    public static Result exportOrderAddDetail(long requisitionId) {
        User user = User.find.byId(session().get("username"));
        DynamicForm f = Form.form().bindFromRequest();
        System.out.println(f.get("title"));
        return ok(exportOrderAddDetail.render(user, Requisition.find.byId(requisitionId)));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveRequisition(long requisitionId){
        User user = User.find.byId(session().get("username"));
        Requisition req = Requisition.find.byId(requisitionId);

        DynamicForm f = Form.form().bindFromRequest();

        req.title = f.get("title");
        //System.out.println("title : "+req.title);
        req.number = f.get("number");
        //System.out.println("number : "+req.number);
        req.update();

        return redirect(routes.Export.exportOrder());
    }

    @Security.Authenticated(Secured.class)
    public static Result saveRequisitionDetail(long requisitionId){
        User user = User.find.byId(session().get("username"));
        Requisition req = Requisition.find.byId(requisitionId);

        RequisitionDetail newDetail = Form.form(RequisitionDetail.class).bindFromRequest().get();
        DynamicForm f = Form.form().bindFromRequest();

        newDetail.requisition = req;
        newDetail.save();

        return redirect(routes.Export.exportOrderAdd(requisitionId));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveOrderDetail() {
        RequestBody body = request().body();
        JsonNode json = body.asJson();

        System.out.println("saveOrderDetail/n");
        System.out.println(json);
        System.out.println("code : " + json.get("code").asText());
        System.out.println("quantity : " + json.get("quantity").asText());
        System.out.println("requisitionId : " + json.get("requisitionId"));

         RequisitionDetail newDetail = new RequisitionDetail();

        newDetail.requisition = Requisition.find.byId(new Long(json.get("requisitionId").toString()));
        if(json.get("quantity").asText() != "")
            newDetail.quantity = Integer.parseInt(json.get("quantity").asText());
        newDetail.save();

        //List<RequisitionDetail> detail = Requisition.find.byId(id).requisition;

        //  result.put("name","Untitled");
        //  result.put("last","Titled");

        return ok(body.asJson());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result loadOrderDetail(long id) {
//        RequestBody body = request().body();
        ObjectNode result = Json.newObject();
        JsonNode json;
     // System.out.println(body.asJson());
        System.out.println("loadOrderDetail/n id: " + id + "\n\t-------\n");

        //List<RequisitionDetail> detail = Requisition.find.byId(id).requisition;

        //  result.put("name","Untitled");
        //  result.put("last","Titled");
        try { 

            System.out.println("11111");

            List<RequisitionDetail> detail = RequisitionDetail.find.where().eq("requisition", Requisition.find.byId(id)).findList();
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(detail);
            json = Json.parse(jsonArray);

            System.out.println("22222");



            result.put("details",json);
        }catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "error1");
        }catch(RuntimeException e){
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "error2");
        }catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
        }

        return ok(result);
    }






    // โอนย้ายภายใน
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInside() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferInside.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferInsideAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferInsideAddDetail.render(user));
    }


    // โอนย่ายภานนอก
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSide() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSide.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSideAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSideAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSideAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSideAddDetail.render(user));
    }


    // บริจาค
    @Security.Authenticated(Secured.class)
    public static Result exportDonate() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonate.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportDonateAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonateAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportDonateAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonateAddDetail.render(user));
    }


    // จำหน่าย
    @Security.Authenticated(Secured.class)
    public static Result exportSold() {
        User user = User.find.byId(session().get("username"));
        return ok(exportSold.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportSoldAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportSoldAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportSoldAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportSoldAddDetail.render(user));
    }


    // อื่นๆ
    @Security.Authenticated(Secured.class)
    public static Result exportOther() {
        User user = User.find.byId(session().get("username"));
        return ok( exportOther.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOtherAdd() {
        User user = User.find.byId(session().get("username"));
        return ok( exportOtherAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOtherAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok( exportOtherAddDetail.render(user));
    }

}