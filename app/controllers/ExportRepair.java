package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.mvc.Http.RequestBody;

import views.html.*;
import views.html.export.*;

import models.*;
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
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        List<Repairing> initList = Repairing.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Repairing> repairingList = Repairing.find.where().eq("status", ExportStatus.REPAIRING).orderBy("id desc").findList();
        List<Repairing> successList = Repairing.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportRepairing.render(user,initList, repairingList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateRepairing() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Repairing temp =  new Repairing();
        temp.dateOfSentToRepair = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportRepair.exportRepairingAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportRepairingAdd(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Repairing repair = Repairing.find.byId(id);
        if(repair == null || repair.status != ExportStatus.INIT ){
            return redirect(routes.ExportRepair.exportRepairing());
        }
        List<Company> allCompany = Company.find.all();
        return ok(exportRepairingAdd.render(user, repair, allCompany));
    }

    @Security.Authenticated(Secured.class)
    public static Result receiveFromRepair(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Repairing repair = Repairing.find.byId(id);
        if(repair == null || repair.status != ExportStatus.REPAIRING){
            return redirect(routes.ExportRepair.exportRepairing());
        }
        List<Company> allCompany = Company.find.all();
        repair.dateOfReceiveFromRepair = new Date();
        return ok(exportRepairingReceive.render(user, repair, allCompany));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Repairing repair = Repairing.find.byId(id);
        List<Company> allCompany = Company.find.all();
        if(repair == null){
            return redirect(routes.ExportRepair.exportRepairing());
        }
        else if(repair.status == ExportStatus.SUCCESS ){
            return ok(exportRepairingViewDetail.render(user, repair, allCompany));
        }
        else if(repair.status == ExportStatus.REPAIRING ){
            return ok(exportRepairingViewDetail.render(user, repair, allCompany));
        }
        return redirect(routes.ExportRepair.exportRepairing());
    }

    @Security.Authenticated(Secured.class)
    public static Result saveRepairing(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Repairing repair = Repairing.find.byId(id);
        if(repair != null && (repair.status == ExportStatus.INIT || repair.status == ExportStatus.REPAIRING ||repair.status == ExportStatus.SUCCESS) ){
        	DynamicForm f = Form.form().bindFromRequest();
            repair.title = f.get("title");
            repair.number = f.get("number");

            // save repair shop
            String repairShop = f.get("repairShop");
            long companyId = Long.parseLong(repairShop);
            Company company = Company.find.byId(companyId);
            if(company != null){
                repair.company = company;
            }

            // save dateOfSentToRepair
            repair.setDateOfSentToRepair(f.get("dateOfSentToRepair"));

            // save approver
            String firstName = f.get("approverFirstName");
            String lastName = f.get("approverLastName");
            String position = f.get("approverPosition");
            List<User> employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() == 1){
                repair.approver = employees.get(0);
            }

            //edit status durableArticles to REPAIRING
            if(repair.status == ExportStatus.INIT || repair.status == ExportStatus.REPAIRING){
                for(RepairingDetail detail : repair.detail){
                    if(detail.durableArticles.status == SuppliesStatus.NORMAL){
                        detail.durableArticles.status = SuppliesStatus.REPAIRING;
                        detail.durableArticles.update();
                    }
                }
                repair.status = ExportStatus.REPAIRING;
            }

            // save dateOfResiveFromRepair and repairCosts
            if(repair.status == ExportStatus.SUCCESS){
                repair.setDateOfReceiveFromRepair(f.get("dateOfResiveFromRepair"));
                String repairCosts = f.get("repairCosts");
                if(repairCosts == null){
                    repairCosts = "0.00";
                }
                repair.repairCosts = Double.parseDouble(repairCosts);
            }

            repair.update();
        }
        return redirect(routes.ExportRepair.exportRepairing());
    }

    @Security.Authenticated(Secured.class)
    public static Result saveReceive(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Repairing repair = Repairing.find.byId(id);
        System.out.println("saveReceive");
        if(repair != null && repair.status == ExportStatus.REPAIRING){
            DynamicForm f = Form.form().bindFromRequest();
            repair.setDateOfReceiveFromRepair(f.get("dateOfResiveFromRepair"));
            String repairCosts = f.get("repairCosts");
            if(repairCosts == null){
                repairCosts = "0.00";
            }
            int numberOfDetail = Integer.parseInt(f.get("numberOfDetail"));
            for(int i=0; i<numberOfDetail; i++){
                RepairingDetail detail = repair.detail.get(i);
                String str = f.get("price"+Integer.toString(i));
                double price = str.equals("")? 0 :Double.parseDouble(str); 
                detail.price = price;
                detail.update();
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
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Repairing repair = Repairing.find.byId(id);
        if(repair != null && repair.status == ExportStatus.INIT){
            repair.status = ExportStatus.CANCEL;
            repair.update();
        }
        return redirect(routes.ExportRepair.exportRepairing());
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteRepairing(){
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
                Repairing repair = Repairing.find.byId(id);
                if(repair != null){
                    if(repair.status == ExportStatus.INIT){
                        repair.status = ExportStatus.DELETE;
                        repair.update();
                    }
                    else if(repair.status == ExportStatus.REPAIRING){
                        for(RepairingDetail detail : repair.detail){
                            if(detail.durableArticles.status == SuppliesStatus.REPAIRING){
                                detail.durableArticles.status = SuppliesStatus.NORMAL;
                                detail.durableArticles.update();
                            }
                        }
                        repair.status = ExportStatus.DELETE;
                        repair.update();
                    }
                    else if(repair.status == ExportStatus.SUCCESS){
                        repair.status = ExportStatus.DELETE;
                        repair.update();
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
    public static Result saveRepairingDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
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
                        List<RepairingDetail> details = RepairingDetail.find.where().eq("repairing",repair).eq("durableArticles",durableArticles).findList();
                        if(details.size() == 0){
                            RepairingDetail newDetail = new RepairingDetail();
                            newDetail.durableArticles = durableArticles;
                            newDetail.repairing = repair;
                            newDetail.description = description;
                            newDetail.save();

                            if(repair.status == ExportStatus.REPAIRING){
                                durableArticles.status = SuppliesStatus.REPAIRING;
                                durableArticles.save();
                            }
                        }
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
    public static Result editRepairingDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            RepairingDetail repairDetail = RepairingDetail.find.byId((new Long(json.get("id").asText())));
            Repairing repair = Repairing.find.byId(new Long(json.get("repairingId").asText()));
            if( repairDetail != null && repair != null){
                if(repair.status == ExportStatus.INIT || repair.status == ExportStatus.REPAIRING || repair.status == ExportStatus.SUCCESS){
                    repairDetail.description = json.get("description").asText();
                    repairDetail.update();
                }
                if(repair.status == ExportStatus.SUCCESS){
                    if(json.get("cost") != null){
                        repairDetail.price = Double.parseDouble(json.get("cost").asText());
                        repairDetail.update();

                        int total = 0;
                        for( RepairingDetail detail : repair.detail){
                            total += detail.price;
                        }
                        repair.repairCosts = total;
                        repair.update();
                    }
                }    
                result.put("status", "SUCCESS");

            }
            else{
                result.put("message", "ไม่สามารถแก้ไข รายละเอียดการส่งซ่อม id:" + json.get("repairingId") + " ได้");
                result.put("status", "error");
                return ok(result);
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
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
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
                        if(detail.durableArticles.status == SuppliesStatus.REPAIRING){
                            detail.durableArticles.status = SuppliesStatus.NORMAL;
                            detail.durableArticles.update();
                        }
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
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
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
