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
        return redirect(routes.ExportDonate.exportDonateAdd());
    }

    @Security.Authenticated(Secured.class)
    public static Result exportDonateAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonateAdd.render(user,null));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result exportDonateAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonateAddDetail.render(user));
    }

}