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

public class ExportTransferInside extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result exportTransferInside() {
        User user = User.find.byId(session().get("username"));
        List<InternalTransfer> initList = InternalTransfer.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<InternalTransfer> successList = InternalTransfer.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportTransferInside.render(user, initList , successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateInternalTransfer() {
        InternalTransfer temp =  new InternalTransfer();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportTransferInside.exportTransferInsideAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAdd(long id) {
        User user = User.find.byId(session().get("username"));
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside == null || inside.status != ExportStatus.INIT){
            return redirect(routes.ExportTransferInside.exportTransferInside());
        }
        return ok(exportTransferInsideAdd.render(user,inside));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside == null || inside.status != ExportStatus.SUCCESS){
            return redirect(routes.ExportTransferInside.exportTransferInside());
        }
        return ok(exportTransferInsideViewDetail.render(user,inside));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveInternalTransfer(long id){
        User user = User.find.byId(session().get("username"));
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside != null && inside.status == ExportStatus.INIT){
            DynamicForm f = Form.form().bindFromRequest();
            inside.title = f.get("title");
            inside.number = f.get("number");
            inside.setApproveDate(f.get("approveDate"));
            inside.status = ExportStatus.SUCCESS;
            inside.update();

            for(InternalTransferDetail detail : inside.detail){
                if(detail.durableArticles.status == SuppliesStatus.NORMAL){
                    //ทำอะไรดี
                    //detail.durableArticles.status = SuppliesStatus.TRANSFERED;
                    detail.durableArticles.update();
                }
            }

        }
        return redirect(routes.ExportTransferInside.exportTransferInside());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelInternalTransfer(long id){
        User user = User.find.byId(session().get("username"));
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside != null && inside.status == ExportStatus.INIT){
            inside.status = ExportStatus.CANCEL;
            inside.update();
        }
        return redirect(routes.ExportTransferInside.exportTransferInside());
    }

    @Security.Authenticated(Secured.class)
    public static Result deleteInternalTransfer(long id){
        User user = User.find.byId(session().get("username"));
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside != null && inside.status == ExportStatus.INIT){
            for(InternalTransferDetail detail : inside.detail){
                if(detail.durableArticles.status == SuppliesStatus.NORMAL){
                    //ทำอะไรดี
                    //detail.durableArticles.status = SuppliesStatus.NORMAL;
                    detail.durableArticles.update();
                }
            }
            inside.status = ExportStatus.DELETE;
            inside.update();
        }
        return redirect(routes.ExportTransferInside.exportTransferInside());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveTransferInsideDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            String department = json.get("department").asText();
            String room = json.get("room").asText();
            String floorLevel = json.get("floorLevel").asText();
            InternalTransfer inside = InternalTransfer.find.byId(id);
            if(inside != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    DurableArticles durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                    if(durableArticles != null){
                        InternalTransferDetail newDetail = new InternalTransferDetail();
                        newDetail.durableArticles = durableArticles;
                        newDetail.internalTransfer = inside;
                        newDetail.department = department;
                        newDetail.room = room;
                        newDetail.floorLevel = floorLevel;
                        newDetail.save();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found transferInside id:" + id);
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
    public static Result deleteTransferInsideDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            InternalTransfer inside = InternalTransfer.find.byId(id);
            if(inside != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    InternalTransferDetail detail = InternalTransferDetail.find.byId(id);
                    if(detail != null && inside.equals(detail.internalTransfer)){
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
    public static Result loadTransferInsideDetail(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            InternalTransfer inside = InternalTransfer.find.byId(id);
            if(inside != null){
                ObjectMapper mapper = new ObjectMapper();
                String jsonArray = mapper.writeValueAsString(inside.detail);
                json = Json.parse(jsonArray);
                result.put("details",json);
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found transferInside id:" + id);
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
