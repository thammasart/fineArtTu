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
    public static Result exportDonateAdd(long donationId) {
        User user = User.find.byId(session().get("username"));
        Donation donate = Donation.find.byId(donationId);
        if(donate == null){
            return redirect(routes.ExportDonate.exportDonate());
        }
        return ok(exportDonateAdd.render(user, donate));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result exportDonateAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonateAddDetail.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveDonation(long donationId){

        System.out.println(" save donate");

        User user = User.find.byId(session().get("username"));
        Donation donate = Donation.find.byId(donationId);

        DynamicForm f = Form.form().bindFromRequest();
        donate.title = f.get("title");
        donate.contractNo = f.get("contractNo");
        donate.setApproveDate(f.get("approveDate"));
        donate.status = ExportStatus.SUCCESS;
        donate.update();

        System.out.println(donate.id + " " + donate.title);

        return redirect(routes.ExportDonate.exportDonate());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveDonateDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Donation donate = Donation.find.byId(id);
            for (final JsonNode objNode : json.get("detail")) {
                DonationDetail newDetail = new DonationDetail();
                id = Long.parseLong(objNode.toString());
                newDetail.durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                newDetail.donation = donate;
                newDetail.save();
            }
            result.put("status", "SUCCESS");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result loadDonateDetail(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            Donation donate = Donation.find.byId(id);
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(donate.detail);
            json = Json.parse(jsonArray);
            result.put("details",json);
            result.put("status", "SUCCESS");
            System.out.println("load detail SUCCESS");

        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
            System.out.println("load detail ERROR");
        }
        return ok(result);
    }

}