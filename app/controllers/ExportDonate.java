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

public class ExportDonate extends Controller {
	// บริจาค
    @Security.Authenticated(Secured.class)
    public static Result exportDonate() {
        User user = User.find.byId(session().get("username"));
        List<Donation> initList = Donation.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Donation> successList = Donation.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportDonate.render(user,initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateDonate() {
        Donation temp =  new Donation();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportDonate.exportDonateAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportDonateAdd(long id) {
        User user = User.find.byId(session().get("username"));
        Donation donate = Donation.find.byId(id);
        if(donate == null || donate.status != ExportStatus.INIT){
            return redirect(routes.ExportDonate.exportDonate());
        }
        return ok(exportDonateAdd.render(user, donate));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        Donation donate = Donation.find.byId(id);
        if(donate == null || donate.status != ExportStatus.SUCCESS){
            return redirect(routes.ExportDonate.exportDonate());
        }
        return ok(exportDonateViewDetail.render(user, donate));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result saveDonation(long id){
        User user = User.find.byId(session().get("username"));
        Donation donate = Donation.find.byId(id);

        if(donate != null && (donate.status == ExportStatus.INIT || donate.status == ExportStatus.SUCCESS) ){
            DynamicForm f = Form.form().bindFromRequest();
            donate.title = f.get("title");
            donate.contractNo = f.get("contractNo");
            donate.setApproveDate(f.get("approveDate"));

            List<Donation_FF_Committee> ffCommittee = donate.ffCommittee;
            String numbetOfcommittee = f.get("numberOf_FF_committee");
            if(numbetOfcommittee != null){
                int count = Integer.parseInt(numbetOfcommittee);
                for(int i=0; i<count; i++){
                    String num  = Integer.toString(i); 
                    String committeeFirstNmae = f.get("FF_firstName" + num);
                    String committeeLastNmae = f.get("FF_lastName" + num);
                    String committeePosition = f.get("FF_position" + num);

                    List<User> users = User.find.where().eq("firstName",committeeFirstNmae).eq("lastName",committeeLastNmae).eq("position",committeePosition).findList();
                    if(users.size() == 1){
                        int index = 0;
                        for(Donation_FF_Committee committee : ffCommittee){
                            if(committee.user.equals(users.get(0))){
                                committee.employeesType = f.get("FF_cType" + num);
                                committee.committeePosition = f.get("FF_cPosition" + num);
                                committee.update();
                                break;
                            }
                            index++;
                        }
                        if(index < ffCommittee.size()){
                            ffCommittee.remove(index);
                        }
                        else{
                            Donation_FF_Committee newCommittee = new Donation_FF_Committee();
                            newCommittee.user = users.get(0);
                            newCommittee.donation = donate;
                            newCommittee.employeesType = f.get("FF_cType" + num);
                            newCommittee.committeePosition = f.get("FF_cPosition" + num);
                            newCommittee.save();
                        }
                    }
                }
            }
            List<Donation_D_Committee> dCommittee = donate.dCommittee;
            numbetOfcommittee = f.get("numberOf_D_committee");
            if(numbetOfcommittee != null){
                int count = Integer.parseInt(numbetOfcommittee);
                for(int i=0; i<count; i++){
                    String num  = Integer.toString(i); 
                    String committeeFirstNmae = f.get("D_firstName" + num);
                    String committeeLastNmae = f.get("D_lastName" + num);
                    String committeePosition = f.get("D_position" + num);

                    List<User> users = User.find.where().eq("firstName",committeeFirstNmae).eq("lastName",committeeLastNmae).eq("position",committeePosition).findList();
                    if(users.size() == 1){
                        int index = 0;
                        for(Donation_D_Committee committee : dCommittee){
                            if(committee.user.equals(users.get(0))){
                                committee.employeesType = f.get("D_cType" + num);
                                committee.committeePosition = f.get("D_cPosition" + num);
                                committee.update();
                                break;
                            }
                            index++;
                        }
                        if(index < ffCommittee.size()){
                            ffCommittee.remove(index);
                        }
                        else{
                            Donation_D_Committee newCommittee = new Donation_D_Committee();
                            newCommittee.user = users.get(0);
                            newCommittee.donation = donate;
                            newCommittee.employeesType = f.get("D_cType" + num);
                            newCommittee.committeePosition = f.get("D_cPosition" + num);
                            newCommittee.save();
                        }
                    }
                }
            }

            for(Donation_FF_Committee committee : ffCommittee){
                committee.delete();
            }
            for(Donation_D_Committee committee : dCommittee){
                committee.delete();
            }

            donate.status = ExportStatus.SUCCESS;
            donate.update();

            for(DonationDetail detail : donate.detail){
                if(detail.durableArticles.status == SuppliesStatus.NORMAL){
                    detail.durableArticles.status = SuppliesStatus.DONATED;
                    detail.durableArticles.update();
                }
            }
        }
        return redirect(routes.ExportDonate.exportDonate());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelDonation(long id){
        User user = User.find.byId(session().get("username"));
        Donation donate = Donation.find.byId(id);
        if(donate != null && donate.status == ExportStatus.INIT){
            donate.status = ExportStatus.CANCEL;
            donate.update();
        }
        return redirect(routes.ExportDonate.exportDonate());
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteDonation(){
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            for (final JsonNode objNode : json.get("detail")) {
                Long id = Long.parseLong(objNode.toString());
                Donation donate = Donation.find.byId(id);
                if(donate != null){
                    if(donate.status == ExportStatus.INIT){
                        donate.status = ExportStatus.DELETE;
                        donate.update();
                    }
                    else if(donate.status == ExportStatus.SUCCESS){
                        for(DonationDetail detail : donate.detail){
                            if(detail.durableArticles.status == SuppliesStatus.DONATED){
                                detail.durableArticles.status = SuppliesStatus.NORMAL;
                                detail.durableArticles.update();
                            }
                        }
                        donate.status = ExportStatus.DELETE;
                        donate.update();
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
    public static Result saveDonateDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Donation donate = Donation.find.byId(id);
            if(donate != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    DurableArticles durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                    if(durableArticles != null){
                        DonationDetail newDetail = new DonationDetail();
                        newDetail.durableArticles = durableArticles;
                        newDetail.donation = donate;
                        newDetail.save();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found dotation id:" + id);
                result.put("status", "error");
            }
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteDonateDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Donation donate = Donation.find.byId(id);
            if(donate != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    DonationDetail detail = DonationDetail.find.byId(id);
                    if(detail != null && donate.equals(detail.donation)){
                        if(detail.durableArticles.status == SuppliesStatus.DONATED){
                            detail.durableArticles.status = SuppliesStatus.NORMAL;
                            detail.durableArticles.update();
                        }
                        detail.delete();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found donate id:" + id);
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
    public static Result loadDonateDetail(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            Donation donate = Donation.find.byId(id);
            if(donate != null){
                ObjectMapper mapper = new ObjectMapper();
                String jsonArray = mapper.writeValueAsString(donate.detail);
                json = Json.parse(jsonArray);
                result.put("details",json);
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found dotation id:" + id);
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