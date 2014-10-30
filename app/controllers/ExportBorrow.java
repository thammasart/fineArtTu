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
        return redirect(routes.ExportBorrow.exportBorrowAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportBorrowAdd(long id) {
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow == null || borrow.status != ExportStatus.INIT){
            return redirect(routes.ExportBorrow.exportBorrow());
        }
        return ok(exportBorrowAdd.render(user, borrow));
    }

    @Security.Authenticated(Secured.class)
    public static Result returnFromBorrow(long id){
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow == null || borrow.status != ExportStatus.BORROW){
            return redirect(routes.ExportBorrow.exportBorrow());
        }
        borrow.dateOfEndBorrow = new Date();
        return ok(exportBorrowReturn.render(user, borrow));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow == null || borrow.status != ExportStatus.SUCCESS){
            return redirect(routes.ExportBorrow.exportBorrow());
        }
        return ok(exportBorrowViewDetail.render(user, borrow));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveBorrow(long id) {
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow != null && borrow.status == ExportStatus.INIT){
            DynamicForm f = Form.form().bindFromRequest();
            borrow.title = f.get("title");
            borrow.number = f.get("number");
            borrow.setDateOfStartBorrow(f.get("dateOfStartBorrow"));
            borrow.update();

            String firstName = f.get("withdrawerFirstName");
            String lastName = f.get("withdrawerLastName");
            String position = f.get("withdrawerPosition");
            List<User> employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() == 1){
                borrow.user = employees.get(0);
                borrow.update();
            }
            else{
                System.out.println(firstName + '\t' + lastName + '\t' + position);
                return redirect(routes.ExportBorrow.exportBorrowAdd(id));
            }

            firstName = f.get("approverFirstName");
            lastName = f.get("approverLastName");
            position = f.get("approverPosition");
            employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() == 1){
                borrow.approver = employees.get(0);
                borrow.update();
            }
            else{
                return redirect(routes.ExportBorrow.exportBorrowAdd(id));
            }
            for(BorrowDetail detail : borrow.detail){
                if(detail.durableArticles.status == SuppliesStatus.NORMAL){
                    detail.durableArticles.status = SuppliesStatus.BORROW;
                    detail.durableArticles.update();
                }
            }
            borrow.status = ExportStatus.BORROW;
            borrow.update();
        }
        return redirect(routes.ExportBorrow.exportBorrow());
    }

    @Security.Authenticated(Secured.class)
    public static Result saveReturn(long id) {
        User user = User.find.byId(session().get("username"));
        Borrow borrow = Borrow.find.byId(id);
        if(borrow != null && borrow.status == ExportStatus.BORROW){
            DynamicForm f = Form.form().bindFromRequest();
            borrow.setDateOfEndBorrow(f.get("dateOfEndBorrow"));
            for(BorrowDetail detail : borrow.detail){
                if(detail.durableArticles.status == SuppliesStatus.BORROW){
                    detail.durableArticles.status = SuppliesStatus.NORMAL;
                    detail.durableArticles.update();
                }
            }
            borrow.status = ExportStatus.SUCCESS;
            borrow.update();
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

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteBorrow(){
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            for (final JsonNode objNode : json.get("detail")) {
                Long id = Long.parseLong(objNode.toString());
                Borrow borrow = Borrow.find.byId(id);
                if(borrow != null){
                    if(borrow.status == ExportStatus.INIT){
                        borrow.status = ExportStatus.DELETE;
                        borrow.update();
                    }
                    else if(borrow.status == ExportStatus.BORROW){
                        for(BorrowDetail detail : borrow.detail){
                            if(detail.durableArticles.status == SuppliesStatus.BORROW){
                                detail.durableArticles.status = SuppliesStatus.NORMAL;
                                detail.durableArticles.update();
                            }
                        }
                        borrow.status = ExportStatus.DELETE;
                        borrow.update();
                    }
                    else if(borrow.status == ExportStatus.SUCCESS){
                        borrow.status = ExportStatus.DELETE;
                        borrow.update();
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
    public static Result saveBorrowDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            String description = json.get("description").asText();
            Borrow borrow =Borrow.find.byId(id);
            if(borrow != null){
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
            else{
                result.put("message","not Found borrow id:" + id);
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
    public static Result deleteBorrowDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Borrow borrow =Borrow.find.byId(id);
            if(borrow != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    BorrowDetail detail = BorrowDetail.find.byId(id);
                    if(detail != null && borrow.equals(detail.borrow)){
                        detail.delete();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found borrow id:" + id);
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
                result.put("message","not Found borrow id:" + id);
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