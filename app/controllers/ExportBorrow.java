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

public class ExportBorrow extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result exportBorrow() {
        User user = User.find.byId(session().get("username"));
        List<Borrow> initList = Borrow.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Borrow> borrowList = Borrow.find.where().eq("status", ExportStatus.BORROW).orderBy("id desc").findList();
        List<Borrow> successList = Borrow.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportBorrow.render(user,initList, borrowList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateBorrow() {
        Borrow temp =  new Borrow();
        temp.dateOfStartBorrow = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportBorrow.exporBorrowAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exporBorrowAdd(long id) {
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow == null){
            return redirect(routes.ExportBorrow.exportBorrow());
        }
        return ok(exportBorrowAdd.render(user, borrow));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveBorrow(long id) {
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow != null && borrow.status == ExportStatus.INIT){
            DynamicForm f = Form.form().bindFromRequest();
            borrow.title = f.get("title");
            borrow.number = f.get("number");;
            borrow.status = ExportStatus.BORROW;
            borrow.update();

            for(BorrowDetail detail : borrow.detail){
                if(detail.durableArticles.status == SuppliesStatus.NORMAL){
                    detail.durableArticles.status = SuppliesStatus.BORROW;
                    detail.durableArticles.update();
                }
            }
        }
        return redirect(routes.ExportBorrow.exportBorrow());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelBorrow(long id){
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow != null && borrow.status == ExportStatus.INIT){
            borrow.status = ExportStatus.CANCEL;
            borrow.update();
        }
        return redirect(routes.ExportBorrow.exportBorrow());
    }

    @Security.Authenticated(Secured.class)
    public static Result deleteBorrow(long id){
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow != null && borrow.status == ExportStatus.BORROW){
            for(BorrowDetail detail : borrow.detail){
                if(detail.durableArticles.status == SuppliesStatus.BORROW){
                    detail.durableArticles.status = SuppliesStatus.NORMAL;
                    detail.durableArticles.update();
                }
            }
            borrow.status = ExportStatus.DELETE;
            borrow.update();
        }
        return redirect(routes.ExportBorrow.exportBorrow());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveBorrowDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            String description = json.get("description").asText();
            Borrow borrow =Borrow.find.byId(id);
            for (final JsonNode objNode : json.get("detail")) {
                id = Long.parseLong(objNode.toString());
                DurableArticles durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                if(durableArticles != null){
                    BorrowDetail newDetail = new BorrowDetail();
                    newDetail.durableArticles = durableArticles;
                    newDetail.borrow = borrow;
                    newDetail.save();
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
    public static Result loadBorrowDetail(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            Borrow borrow = Borrow.find.byId(id);
            if(borrow != null){
                ObjectMapper mapper = new ObjectMapper();
                String jsonArray = mapper.writeValueAsString(borrow.detail);
                json = Json.parse(jsonArray);
                result.put("details",json);
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found dotation id:" + id);
                result.put("status", "error3");
            }
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
        }
        return ok(result);
    }
}