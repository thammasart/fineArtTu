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

public class ExportSold extends Controller {

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

}