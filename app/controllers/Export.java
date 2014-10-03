package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.mvc.Http.RequestBody;

import views.html.*;
import views.html.export.*;

import models.*;
import models.fsnNumber.FSN_Description;
import models.consumable.*;
import models.durableArticles.*;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;                                                                                         
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;  

public class Export extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result export() {
        User user = User.find.byId(session().get("username"));
        return ok(export.render(user));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result searchFSN (String code,String description) {

        System.out.println("code :|"+code+"|");
        System.out.println("description :|"+description+"|");

        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            if(!code.isEmpty()){
                code = '%'+code+'%';
            }
            if(!description.isEmpty()){
                description = '%'+description+'%';
            }

            //List<DurableArticles> searchResult = DurableArticles.find.all();
            List<DurableArticles> searchResult = DurableArticles.find.where().like("code",code).findList();
            if(searchResult.isEmpty()){
                List<FSN_Description> fanList = FSN_Description.find.where().like("descriptionDescription",description).findList();
                for(FSN_Description fsn : fanList){
                    System.out.println('%'+fsn.descriptionId+'%');
                    searchResult.addAll(DurableArticles.find.where().like("code", '%'+fsn.descriptionId+'%').findList());
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(searchResult);
            json = Json.parse(jsonArray);
            result.put("result",json);
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
/*
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
        return redirect(routes.Export.exportOrderAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOrderAdd(long requisitionId) {
        User user = User.find.byId(session().get("username"));
        Requisition req = Requisition.find.byId(requisitionId);
        if(req != null && req.status == ExportStatus.SUCCESS){
            return redirect(routes.Export.exportOrder());
        }
        else{
            return ok(exportOrderAdd.render(user,req));
        }
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


        //System.out.println(req.user.username);
        //System.out.println(req.approver.username);

        return redirect(routes.Export.exportOrder());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelRequisition(long requisitionId){
        User user = User.find.byId(session().get("username"));
        Requisition req = Requisition.find.byId(requisitionId);

        if(req.status == ExportStatus.INIT){
            req.status = ExportStatus.CANCEL;
        }
        req.update();

        return redirect(routes.Export.exportOrder());
    }

/*
    @Security.Authenticated(Secured.class)
    public static Result saveRequisitionDetail(long requisitionId){
        User user = User.find.byId(session().get("username"));
        Requisition req = Requisition.find.byId(requisitionId);

        RequisitionDetail newDetail = Form.form(RequisitionDetail.class).bindFromRequest().get();
        DynamicForm f = Form.form().bindFromRequest();

        newDetail.requisition = req;
        newDetail.save();

        return redirect(routes.Export.exportOrderAdd(requisitionId));
    }*///
    /*

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveOrderDetail() {
        RequestBody body = request().body();
        JsonNode json = body.asJson();

        System.out.println("saveOrderDetail\n ");
        System.out.println(json);
        /*
        System.out.println("code : " + json.get("code").asText());
        System.out.println("quantity : " + json.get("quantity").asText());
        System.out.println("requisitionId : " + json.get("requisitionId"));
        *//*
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

//*/

    // โอนย้ายภายใน
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInside() {
        User user = User.find.byId(session().get("username"));
        List<InternalTransfer> initList = InternalTransfer.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<InternalTransfer> successList = InternalTransfer.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportTransferInside.render(user, initList , successList));
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


   // จำหน่าย
    @Security.Authenticated(Secured.class)
    public static Result exportSold() {
        User user = User.find.byId(session().get("username"));
        List<Auction> initList = Auction.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Auction> successList = Auction.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportSold.render(user,initList, successList));
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
    @Security.Authenticated(Secured.class)
    public static Result autocompleteExportCommitee (){
        List<User> allUser = User.find.all();
        List<MaterialCode> allMaterial = MaterialCode.find.all();
        List<String> name = new ArrayList<String>();        
        List<String> lastname = new ArrayList<String>();
        List<String> position = new ArrayList<String>();
        List<String> code = new ArrayList<String>();
        List<String> codeName = new ArrayList<String>();
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode result = Json.newObject();
        JsonNode json;

        try{

            for(User us : allUser){ 
                name.add(us.firstName);               
                lastname.add(us.lastName);
                position.add(us.position);
            } 

            for(MaterialCode mat : allMaterial){
                code.add(mat.code);
                codeName.add(mat.description);               
            }

            String jsonArray = mapper.writeValueAsString(name);
            json = Json.parse(jsonArray);
            result.put("name",json);

            jsonArray = mapper.writeValueAsString(lastname);
            json = Json.parse(jsonArray);
            result.put("lastname",json);

            jsonArray = mapper.writeValueAsString(position);
            json = Json.parse(jsonArray);
            result.put("position",json);

            jsonArray = mapper.writeValueAsString(code);
            json = Json.parse(jsonArray);
            result.put("code",json);

            jsonArray = mapper.writeValueAsString(codeName);
            json = Json.parse(jsonArray);
            result.put("codeName",json);
        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("stats","error1");
        }
        catch(JsonProcessingException e){
            result.put("message", e.getMessage());
            result.put("stats","error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("stats","error3");
        }
        return ok(result);
        
    }
}
