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
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        List<OtherTransfer> initList = OtherTransfer.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<OtherTransfer> successList = OtherTransfer.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok( exportOther.render(user, initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateOther() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OtherTransfer temp =  new OtherTransfer();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportOther.exportOtherAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOtherAdd(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OtherTransfer other = OtherTransfer.find.byId(id);
        if(other == null || other.status != ExportStatus.INIT){
            return redirect(routes.ExportOther.exportOther());
        }
        return ok(exportOtherAdd.render(user, other));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OtherTransfer other = OtherTransfer.find.byId(id);
        if(other == null || other.status != ExportStatus.SUCCESS){
            return redirect(routes.ExportOther.exportOther());
        }
        return ok(exportOtherViewDetail.render(user, other));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveOther(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OtherTransfer other = OtherTransfer.find.byId(id);
        if(other != null && (other.status == ExportStatus.INIT || other.status == ExportStatus.SUCCESS) ){
            DynamicForm f = Form.form().bindFromRequest();
            other.title = f.get("title");
            other.number = f.get("number");
            other.setApproveDate(f.get("approveDate"));
            other.description = f.get("description");
            other.update();

            // save approver
            String firstName = f.get("approverFirstName");
            String lastName = f.get("approverLastName");
            String position = f.get("approverPosition");
            List<User> employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() == 1){
                other.approver = employees.get(0);
                other.update();
            }

            // save FF committee
            List<OtherTransfer_FF_Committee> ffCommittee = other.ffCommittee;
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
                        for(OtherTransfer_FF_Committee committee : ffCommittee){
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
                            OtherTransfer_FF_Committee newCommittee = new OtherTransfer_FF_Committee();
                            newCommittee.user = users.get(0);
                            newCommittee.otherTransfer = other;
                            newCommittee.employeesType = f.get("FF_cType" + num);
                            newCommittee.committeePosition = f.get("FF_cPosition" + num);
                            newCommittee.save();
                        }
                    }
                }
            }

            // save D committee
            List<OtherTransfer_D_Committee> dCommittee = other.dCommittee;
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
                        for(OtherTransfer_D_Committee committee : dCommittee){
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
                            OtherTransfer_D_Committee newCommittee = new OtherTransfer_D_Committee();
                            newCommittee.user = users.get(0);
                            newCommittee.otherTransfer = other;
                            newCommittee.employeesType = f.get("D_cType" + num);
                            newCommittee.committeePosition = f.get("D_cPosition" + num);
                            newCommittee.save();
                        }
                    }
                }
            }

            // delete committee when edit
            for(OtherTransfer_FF_Committee committee : ffCommittee){
                committee.delete();
            }
            for(OtherTransfer_D_Committee committee : dCommittee){
                committee.delete();
            }

            // updatae status durableArticles to OTHERTRANSFER
            for(OtherTransferDetail detail : other.detail){
                if(detail.durableArticles.status == SuppliesStatus.NORMAL ){
                    detail.durableArticles.status = SuppliesStatus.OTHERTRANSFER;
                    detail.durableArticles.update();
                }
            }
            
            // updatae status OtherTransfer
            other.status = ExportStatus.SUCCESS;
            other.update();
        }
        return redirect(routes.ExportOther.exportOther());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelOther(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        OtherTransfer other = OtherTransfer.find.byId(id);
        if(other != null && other.status == ExportStatus.INIT){
            other.status = ExportStatus.CANCEL;
            other.update();
        }
        return redirect(routes.ExportOther.exportOther());
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteOtherTransfer(){
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
                OtherTransfer other = OtherTransfer.find.byId(id);
                if(other != null){
                    if(other.status == ExportStatus.INIT){
                        other.status = ExportStatus.DELETE;
                        other.update();
                    }
                    else if(other.status == ExportStatus.SUCCESS){
                        for(OtherTransferDetail detail : other.detail){
                            if(detail.durableArticles.status == SuppliesStatus.OTHERTRANSFER){
                                detail.durableArticles.status = SuppliesStatus.NORMAL;
                                detail.durableArticles.update();
                            }
                        }
                        other.status = ExportStatus.DELETE;
                        other.update();
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
    public static Result saveOtherDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            OtherTransfer other = OtherTransfer.find.byId(id);
            if(other != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    DurableArticles durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                    if(durableArticles != null){
                        List<OtherTransferDetail> details = OtherTransferDetail.find.where().eq("otherTransfer",other).eq("durableArticles",durableArticles).findList();
                        if(details.size() == 0){
                            OtherTransferDetail newDetail = new OtherTransferDetail();
                            newDetail.durableArticles = durableArticles;
                            newDetail.otherTransfer = other;
                            newDetail.save();

                            if(other.status == ExportStatus.SUCCESS){
                                durableArticles.status = SuppliesStatus.OTHERTRANSFER;
                                durableArticles.save();
                            }
                        }
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found other transfer id:" + id);
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
    public static Result deleteOtherDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            OtherTransfer other = OtherTransfer.find.byId(id);
            if(other != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    OtherTransferDetail detail = OtherTransferDetail.find.byId(id);
                    if(detail != null && other.equals(detail.otherTransfer)){
                        if(detail.durableArticles.status == SuppliesStatus.OTHERTRANSFER){
                            detail.durableArticles.status = SuppliesStatus.NORMAL;
                            detail.durableArticles.update();
                        }
                        detail.delete();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found other transfer id:" + id);
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
    public static Result loadOtherDetail(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
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
