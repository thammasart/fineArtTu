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

public class ExportOther extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result exportOther() {
        User user = User.find.byId(session().get("username"));
        List<OtherTransfer> initList = OtherTransfer.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<OtherTransfer> successList = OtherTransfer.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok( exportOther.render(user, initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateOther() {
        OtherTransfer temp =  new OtherTransfer();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportOther.exportOtherAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOtherAdd(long id) {
        User user = User.find.byId(session().get("username"));
        OtherTransfer other = OtherTransfer.find.byId(id);
        if(other == null){
            return redirect(routes.ExportOther.exportOther());
        }
        return ok( exportOtherAdd.render(user, other));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveOther(long id){
        System.out.println("save other");
        User user = User.find.byId(session().get("username"));
        OtherTransfer other = OtherTransfer.find.byId(id);
        if(other != null){
            DynamicForm f = Form.form().bindFromRequest();
            other.title = f.get("title");
            other.number = f.get("number");
            other.setApproveDate(f.get("approveDate"));
            other.status = ExportStatus.SUCCESS;
            other.update();

            for(OtherTransferDetail detail : other.detail){
                detail.durableArticles.status = SuppliesStatus.OTHERTRANSFER;
                detail.durableArticles.update();
            }
        }
        return redirect(routes.ExportOther.exportOther());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelOther(long id){
        User user = User.find.byId(session().get("username"));
        OtherTransfer other = OtherTransfer.find.byId(id);
        if(other != null && other.status == ExportStatus.INIT){
            other.status = ExportStatus.CANCEL;
            other.update();
        }
        return redirect(routes.ExportOther.exportOther());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveOtherDetail() {
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            OtherTransfer other = OtherTransfer.find.byId(id);
            for (final JsonNode objNode : json.get("detail")) {
                id = Long.parseLong(objNode.toString());
                DurableArticles durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                if(durableArticles != null){
                    OtherTransferDetail newDetail = new OtherTransferDetail();
                    newDetail.durableArticles = durableArticles;
                    newDetail.otherTransfer = other;
                    newDetail.save();
                }
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
    public static Result loadOtherDetail(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            OtherTransfer other = OtherTransfer.find.byId(id);
            if(other != null){
                ObjectMapper mapper = new ObjectMapper();
                String jsonArray = mapper.writeValueAsString(other.detail);
                json = Json.parse(jsonArray);
                result.put("details",json);
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found other transfer id:" + id);
                result.put("status", "error3");
            }
        }
        catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "error1");
        }
        catch(RuntimeException e){
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
        }
        return ok(result);
    }

}