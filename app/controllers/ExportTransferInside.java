package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.mvc.Http.RequestBody;

import views.html.*;
import views.html.export.*;

import models.*;
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
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        List<InternalTransfer> initList = InternalTransfer.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<InternalTransfer> successList = InternalTransfer.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportTransferInside.render(user, initList , successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateInternalTransfer() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        InternalTransfer temp =  new InternalTransfer();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportTransferInside.exportTransferInsideAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAdd(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside == null || inside.status != ExportStatus.INIT){
            return redirect(routes.ExportTransferInside.exportTransferInside());
        }
        return ok(exportTransferInsideAdd.render(user,inside));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside == null || inside.status != ExportStatus.SUCCESS){
            return redirect(routes.ExportTransferInside.exportTransferInside());
        }
        return ok(exportTransferInsideViewDetail.render(user,inside));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveInternalTransfer(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside != null && (inside.status == ExportStatus.INIT || inside.status == ExportStatus.SUCCESS) ){
            DynamicForm f = Form.form().bindFromRequest();
            inside.title = f.get("title");
            inside.number = f.get("number");
            inside.setApproveDate(f.get("approveDate"));

            // save approver
            String firstName = f.get("approverFirstName");
            String lastName = f.get("approverLastName");
            String position = f.get("approverPosition");
            List<User> employees = User.find.where().eq("firstName",firstName).eq("lastName",lastName).eq("position",position).findList();
            if(employees.size() == 1){
                inside.approver = employees.get(0);
            }

            // update detail durableArticles
            if(inside.status == ExportStatus.INIT){
                inside.status = ExportStatus.SUCCESS;
                inside.update();
                for(InternalTransferDetail detail : inside.detail){
                    InternalTransferDetail temp = InternalTransferDetail.find.where().eq("durableArticles",detail.durableArticles).eq("internalTransfer.status", ExportStatus.SUCCESS).orderBy("internalTransfer.approveDate desc").findList().get(0);
                    detail.durableArticles.department = temp.newDepartment;
                    detail.durableArticles.room = temp.newRoom;
                    detail.durableArticles.floorLevel = temp.newFloorLevel;
                    detail.durableArticles.firstName = temp.newFirstName;
                    detail.durableArticles.lastName = temp.newLastName;
                    detail.durableArticles.update();

                }
            }

            inside.update();
        }
        return redirect(routes.ExportTransferInside.exportTransferInside());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelInternalTransfer(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        InternalTransfer inside = InternalTransfer.find.byId(id);
        if(inside != null && inside.status == ExportStatus.INIT){
            inside.status = ExportStatus.CANCEL;
            inside.update();
        }
        return redirect(routes.ExportTransferInside.exportTransferInside());
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteTransferInside(){
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
                InternalTransfer inside = InternalTransfer.find.byId(id);
                if(inside != null){
                    inside.status = ExportStatus.DELETE;
                    inside.update();
                    if(inside.status == ExportStatus.SUCCESS){
                        for(InternalTransferDetail detail : inside.detail){
                            List<InternalTransferDetail> temp = InternalTransferDetail.find.where().eq("durableArticles",detail.durableArticles).eq("internalTransferDetail.status", ExportStatus.SUCCESS).orderBy("internalTransferDetail.approveDate desc").findList();
                            if(temp.size() > 0){
                                detail.durableArticles.department = temp.get(0).newDepartment;
                                detail.durableArticles.room = temp.get(0).newRoom;
                                detail.durableArticles.floorLevel = temp.get(0).newFloorLevel;
                                detail.durableArticles.firstName = temp.get(0).newFirstName;
                                detail.durableArticles.lastName = temp.get(0).newLastName;
                                detail.durableArticles.update();
                            }
                            else{
                                detail.durableArticles.department = detail.firstDepartment;
                                detail.durableArticles.room = detail.firstRoom;
                                detail.durableArticles.floorLevel = detail.firstFloorLevel;
                                detail.durableArticles.firstName = detail.firstFirstName;
                                detail.durableArticles.lastName = detail.firstLastName;
                                detail.durableArticles.update();
                            }
                        }
                        inside.status = ExportStatus.DELETE;
                        inside.update();
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
            String recieverFirstName = json.get("recieveFirstName").asText();
            String recieverLastName = json.get("recieveLastName").asText();
            String recieverposition = json.get("recievePosition").asText();
            if(inside != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    DurableArticles durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                    if(durableArticles != null){
                        List<InternalTransferDetail> details = InternalTransferDetail.find.where().eq("internalTransfer",inside).eq("durableArticles",durableArticles).findList();
                        if(details.size() == 0){
                            InternalTransferDetail newDetail = new InternalTransferDetail();
                            newDetail.durableArticles = durableArticles;
                            newDetail.internalTransfer = inside;
                            newDetail.newDepartment = department;
                            newDetail.newRoom = room;
                            newDetail.newFloorLevel = floorLevel;
                            newDetail.newFirstName = recieverFirstName;
                            newDetail.newLastName = recieverLastName;
                            newDetail.newPosition = recieverposition;
                            
                            List<InternalTransferDetail> temp = InternalTransferDetail.find.where().eq("durableArticles",durableArticles).findList();
                            if(temp.size() == 0){
                                newDetail.firstDepartment = durableArticles.department;
                                newDetail.firstRoom = durableArticles.room;
                                newDetail.firstFloorLevel = durableArticles.floorLevel;
                                newDetail.firstFirstName = durableArticles.firstName;
                                newDetail.firstLastName = durableArticles.lastName;
                            }else{
                                newDetail.firstDepartment = temp.get(0).firstDepartment;
                                newDetail.firstRoom = temp.get(0).firstRoom;
                                newDetail.firstFloorLevel = temp.get(0).firstFloorLevel;
                                newDetail.firstFirstName = temp.get(0).firstFirstName;
                                newDetail.firstLastName = temp.get(0).firstLastName;
                            }
                            newDetail.save();

                            if(inside.status == ExportStatus.SUCCESS){
                                newDetail = InternalTransferDetail.find.where().eq("durableArticles",durableArticles).eq("internalTransfer.status", ExportStatus.SUCCESS).orderBy("internalTransfer.approveDate desc").findList().get(0);
                                durableArticles.department = newDetail.firstDepartment;
                                durableArticles.room = newDetail.firstRoom;
                                durableArticles.floorLevel = newDetail.firstFloorLevel;
                                durableArticles.firstName = newDetail.firstFirstName;
                                durableArticles.lastName = newDetail.firstLastName;
                                durableArticles.update();
                            }
                        }
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
    public static Result editTransferInsideDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();

            InternalTransferDetail insideDetail = InternalTransferDetail.find.byId((new Long(json.get("id").asText())));
            InternalTransfer inside = InternalTransfer.find.byId(new Long(json.get("transferInsideId").asText()));
            if( insideDetail != null && inside != null ){
                insideDetail.newDepartment = json.get("department").asText();
                insideDetail.newRoom = json.get("room").asText();
                insideDetail.newFloorLevel = json.get("floorLevel").asText();
                insideDetail.newFirstName = json.get("newFirstName").asText();
                insideDetail.newLastName = json.get("newLastName").asText();
                insideDetail.newPosition = json.get("newPosition").asText();
                insideDetail.update();
                    
                if(inside.status == ExportStatus.SUCCESS){
                    InternalTransferDetail temp = InternalTransferDetail.find.where().eq("durableArticles",insideDetail.durableArticles).eq("internalTransfer.status", ExportStatus.SUCCESS).orderBy("internalTransfer.approveDate desc").findList().get(0);
                    insideDetail.durableArticles.department = temp.newDepartment;
                    insideDetail.durableArticles.room = temp.newRoom;
                    insideDetail.durableArticles.floorLevel = temp.newFloorLevel;
                    insideDetail.durableArticles.firstName = temp.newFirstName;
                    insideDetail.durableArticles.lastName = temp.newLastName;
                    insideDetail.durableArticles.update();
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message", "ไม่สามารถแก้ไข รายละเอียดการส่งซ่อม id:" + json.get("repairingId") + " ได้");
                result.put("status", "error");
                return ok(result);
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
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
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
