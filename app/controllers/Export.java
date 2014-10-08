package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.mvc.Http.RequestBody;

import views.html.*;
import views.html.export.*;

import models.*;
import models.fsnNumber.FSN_Description;
import models.consumable.*;
import models.durableArticles.*;
import models.type.ExportStatus;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;                                                                                         
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;  

public class Export extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result export() {
        User user = User.find.byId(session().get("username"));
        return ok(export.render(user));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result searchFSN (String code,String description) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try{
            List<DurableArticles> searchResult;
            if(!code.isEmpty() && description.isEmpty()){

                code = '%'+code+'%';

                searchResult = DurableArticles.find.where().ilike("code",code).findList();
            }
            else if(code.isEmpty() && !description.isEmpty()){

                description = '%'+description+'%';

                searchResult = new ArrayList<DurableArticles>();
                List<FSN_Description> fanList = FSN_Description.find.where().like("descriptionDescription",description).findList();
                for(FSN_Description fsn : fanList){
                    List<DurableArticles> searchDetail = DurableArticles.find.where().like("code", '%'+fsn.descriptionId+'%').findList();
                    for(DurableArticles durableArticle : searchDetail){
                        searchResult.add(durableArticle);
                    }
                }

            }
            else if(!code.isEmpty() && !description.isEmpty()){
                code = '%'+code+'%';
                description = '%'+description+'%';

                searchResult = new ArrayList<DurableArticles>();
                List<DurableArticles> searchCode = DurableArticles.find.where().ilike("code",code).findList();
                List<FSN_Description> fanList = FSN_Description.find.where().like("descriptionDescription",description).findList();
                for(FSN_Description fsn : fanList){
                    List<DurableArticles> searchDetail = DurableArticles.find.where().like("code", '%'+fsn.descriptionId+'%').findList();
                    for(DurableArticles durableArticle : searchDetail){
                        if(searchCode.contains(durableArticle) && !searchResult.contains(durableArticle)){
                            searchResult.add(durableArticle);
                        }
                    }
                }
            }
            else{
                searchResult = DurableArticles.find.all();
            }
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(searchResult);
            json = Json.parse(jsonArray);
            result.put("result",json);
            result.put("status", "SUCCESS");
            System.out.println("searchFSN SUCCESS");

        }
        catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "error1");
            System.out.println("searchFSN ERROR 1" + e.getMessage());
        }
        catch(RuntimeException e){
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "error2");
            System.out.println("searchFSN ERROR 2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error3");
            System.out.println("searchFSN ERROR 3");
        }

        return ok(result);
    }

    // โอนย้ายภายใน
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInside() {
        User user = User.find.byId(session().get("username"));
        List<InternalTransfer> initList = InternalTransfer.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<InternalTransfer> successList = InternalTransfer.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportTransferInside.render(user, initList , successList));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferInsideAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferInsideAddDetail.render(user));
    }


    // โอนย่ายภานนอก
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSide() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSide.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSideAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSideAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSideAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSideAddDetail.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result autocompleteExportCommitee (){
        List<User> allUser = User.find.all();
        List<MaterialCode> allMaterial = MaterialCode.find.all();
        List<String> name = new ArrayList<String>();        
        List<String> lastname = new ArrayList<String>();
        List<String> position = new ArrayList<String>();
        List<String> code = new ArrayList<String>();
        List<String> codeName = new ArrayList<String>();
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode result = Json.newObject();
        JsonNode json;

        try{

            for(User us : allUser){ 
                name.add(us.firstName);               
                lastname.add(us.lastName);
                position.add(us.position);
            } 

            for(MaterialCode mat : allMaterial){
                code.add(mat.code);
                codeName.add(mat.description);               
            }

            String jsonArray = mapper.writeValueAsString(name);
            json = Json.parse(jsonArray);
            result.put("name",json);

            jsonArray = mapper.writeValueAsString(lastname);
            json = Json.parse(jsonArray);
            result.put("lastname",json);

            jsonArray = mapper.writeValueAsString(position);
            json = Json.parse(jsonArray);
            result.put("position",json);

            jsonArray = mapper.writeValueAsString(code);
            json = Json.parse(jsonArray);
            result.put("code",json);

            jsonArray = mapper.writeValueAsString(codeName);
            json = Json.parse(jsonArray);
            result.put("codeName",json);
        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("stats","error1");
        }
        catch(JsonProcessingException e){
            result.put("message", e.getMessage());
            result.put("stats","error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("stats","error3");
        }
        return ok(result);
        
    }
}
