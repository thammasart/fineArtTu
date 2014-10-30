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

public class ExportRepair extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result exportRepairing() {
        User user = User.find.byId(session().get("username"));
        List<Repairing> initList = Repairing.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Repairing> repairingList = Repairing.find.where().eq("status", ExportStatus.REPAIRING).orderBy("id desc").findList();
        List<Repairing> successList = Repairing.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportRepairing.render(user,initList, repairingList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateRepairing() {
        Repairing temp =  new Repairing();
        temp.dateOfSentToRepair = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportRepair.exportRepairingAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportRepairingAdd(long id) {
        User user = User.find.byId(session().get("username"));
        Repairing repair = Repairing.find.byId(id);
        if(repair == null || repair.status != ExportStatus.INIT ){
            return redirect(routes.ExportRepair.exportRepairing());
        }
        return ok(exportRepairingAdd.render(user, repair));
    }

    @Security.Authenticated(Secured.class)
    public static Result receiveFromRepair(long id){
        User user = User.find.byId(session().get("username"));
        Repairing repair = Repairing.find.byId(id);
        if(repair == null || repair.status != ExportStatus.REPAIRING){
            return redirect(routes.ExportRepair.exportRepairing());
        }
        repair.dateOfReceiveFromRepair = new Date();
        return ok(exportRepairingReceive.render(user, repair));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        Repairing repair = Repairing.find.byId(id);
        if(repair == null || repair.status != ExportStatus.SUCCESS ){
            return redirect(routes.ExportRepair.exportRepairing());
        }
        return ok(exportRepairingViewDetail.render(user, repair));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveRepairing(long id) {
        User user = User.find.byId(session().get("username"));
        Repairing repair = Repairing.find.byId(id);
        if(repair != null && repair.status == ExportStatus.INIT){
        	DynamicForm f = Form.form().bindFromRequest();
            repair.title = f.get("title");
            repair.number = f.get("number");
            for(RepairingDetail detail : repair.detail){
                if(detail.durableArticles.status == SuppliesStatus.NORMAL){
                    detail.durableArticles.status = SuppliesStatus.REPAIRING;
                    detail.durableArticles.update();
                }
            }
            repair.status = ExportStatus.REPAIRING;
            repair.update();
        }
        return redirect(routes.ExportRepair.exportRepairing());
    }

    @Security.Authenticated(Secured.class)
    public static Result saveReceive(long id) {
        User user = User.find.byId(session().get("username"));
        Repairing repair = Repairing.find.byId(id);
        System.out.println("saveReceive");
        if(repair != null && repair.status == ExportStatus.REPAIRING){
            DynamicForm f = Form.form().bindFromRequest();
            repair.setDateOfReceiveFromRepair(f.get("dateOfResiveFromRepair"));
            String repairCosts = f.get("repairCosts");
            if(repairCosts == null){
                repairCosts = "0.00";
            }
            repair.repairCosts = Double.parseDouble(repairCosts);
            for(RepairingDetail detail : repair.detail){
                if(detail.durableArticles.status == SuppliesStatus.REPAIRING){
                    detail.durableArticles.status = SuppliesStatus.NORMAL;
                    detail.durableArticles.update();
                }
            }
            repair.status = ExportStatus.SUCCESS;
            repair.update();
            System.out.println("saveReceive");
        }
        return redirect(routes.ExportRepair.exportRepairing());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelRepairing(long id){
        User user = User.find.byId(session().get("username"));
        Repairing repair = Repairing.find.byId(id);
        if(repair != null && repair.status == ExportStatus.INIT){
            repair.status = ExportStatus.CANCEL;
            repair.update();
        }
        return redirect(routes.ExportRepair.exportRepairing());
    }

    @Security.Authenticated(Secured.class)
    public static Result deleteRepairing(long id){
        User user = User.find.byId(session().get("username"));
        Repairing repair = Repairing.find.byId(id);
        if(repair != null && repair.status == ExportStatus.INIT){
            for(RepairingDetail detail : repair.detail){
                if(detail.durableArticles.status == SuppliesStatus.REPAIRING){
                    detail.durableArticles.status = SuppliesStatus.NORMAL;
                    detail.durableArticles.update();
                }
            }
            repair.status = ExportStatus.DELETE;
            repair.update();
        }
        return redirect(routes.ExportRepair.exportRepairing());
    }


    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result saveRepairingDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            String description = json.get("description").asText();
            Repairing repair =Repairing.find.byId(id);
            if(repair != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    DurableArticles durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                    if(durableArticles != null){
                        RepairingDetail newDetail = new RepairingDetail();
                        newDetail.durableArticles = durableArticles;
                        newDetail.repairing = repair;
                        newDetail.description = description;
                        newDetail.save();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found repair id:" + id);
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
    public static Result deleteRepairingDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Repairing repair = Repairing.find.byId(id);
            if(repair != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    RepairingDetail detail = RepairingDetail.find.byId(id);
                    if(detail != null && repair.equals(detail.repairing)){
                        detail.delete();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found repairing id:" + id);
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
    public static Result loadRepairingDetail(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            Repairing repair = Repairing.find.byId(id);
            if(repair != null){
                ObjectMapper mapper = new ObjectMapper();
                String jsonArray = mapper.writeValueAsString(repair.detail);
                json = Json.parse(jsonArray);
                result.put("details",json);
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found repair id:" + id);
                result.put("status", "error");
            }
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

}