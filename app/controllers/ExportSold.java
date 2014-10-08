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

public class ExportSold extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result exportSold() {
        User user = User.find.byId(session().get("username"));
        List<Auction> initList = Auction.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Auction> successList = Auction.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportSold.render(user,initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateSold() {
        Auction temp =  new Auction ();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportSold.exportSoldAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportSoldAdd(long auctionId) {
        User user = User.find.byId(session().get("username"));
        Auction sold = Auction.find.byId(auctionId);
        if(sold == null){
            return redirect(routes.ExportSold.exportSold());
        }
        return ok(exportSoldAdd.render(user, sold));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportSoldAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportSoldAddDetail.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveAuction(long auctionId){

        System.out.println("save Auction");

        User user = User.find.byId(session().get("username"));
        Auction auction = Auction.find.byId(auctionId);

        DynamicForm f = Form.form().bindFromRequest();
        auction.title = f.get("title");
        auction.contractNo = f.get("contractNo");
        auction.setApproveDate(f.get("approveDate"));
        auction.status = ExportStatus.SUCCESS;
        auction.update();

        System.out.println(auction.id + " " + auction.title);

        return redirect(routes.ExportSold.exportSold());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveAuctionDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Auction auction = Auction.find.byId(id);
            for (final JsonNode objNode : json.get("detail")) {
                AuctionDetail newDetail = new AuctionDetail();
                id = Long.parseLong(objNode.toString());
                newDetail.durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                newDetail.auction = auction;
                newDetail.save();
            }
            result.put("status", "SUCCESS");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        System.out.println("saveAuctionDetail");

        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result loadAuctionDetail(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            Auction auction = Auction.find.byId(id);
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(auction.detail);
            json = Json.parse(jsonArray);
            result.put("details",json);
            result.put("status", "SUCCESS");
            System.out.println("load detail SUCCESS");

        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

}