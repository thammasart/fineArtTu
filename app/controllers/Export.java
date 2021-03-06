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
import models.durableArticles.*;
import models.durableGoods.*;
import models.type.ExportStatus;
import models.type.SuppliesStatus;

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
    @Security.Authenticated(Secured.class)
    public static Result searchFSN (String code,String description) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try{
            List<DurableArticles> searchResult;
            if(!code.isEmpty() && description.isEmpty()){
                code = '%'+code+'%';
                searchResult = DurableArticles.find.where().ilike("code",code).eq("status",SuppliesStatus.NORMAL).findList();
            }
            else if(code.isEmpty() && !description.isEmpty()){
                description = '%'+description+'%';
                searchResult = DurableArticles.find.where().ilike("detail.fsn.descriptionDescription", description).eq("status",SuppliesStatus.NORMAL).findList();
            }
            else if(!code.isEmpty() && !description.isEmpty()){
                code = '%'+code+'%';
                description = '%'+description+'%';
                searchResult = DurableArticles.find.where().ilike("code",code).ilike("detail.fsn.descriptionDescription", description).eq("status",SuppliesStatus.NORMAL).findList();
            }
            else{
                searchResult = DurableArticles.find.where().eq("status",SuppliesStatus.NORMAL).findList();
            }
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(searchResult);
            json = Json.parse(jsonArray);
            result.put("result",json);
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
    public static Result searchGoods (String code,String description) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try{
            List<DurableGoods > searchResult;
            if(!code.isEmpty() && description.isEmpty()){
                code = '%'+code+'%';
                searchResult = DurableGoods .find.where().ilike("codes",code).eq("status",SuppliesStatus.NORMAL).findList();
            }
            else if(code.isEmpty() && !description.isEmpty()){
                description = '%'+description+'%';
                searchResult = DurableGoods .find.where().ilike("detail.description", description).eq("status",SuppliesStatus.NORMAL).findList();
            }
            else if(!code.isEmpty() && !description.isEmpty()){
                code = '%'+code+'%';
                description = '%'+description+'%';
                searchResult =  searchResult = DurableGoods .find.where().ilike("code",code).ilike("detail.description", description).eq("status",SuppliesStatus.NORMAL).findList();
            }
            else{
                searchResult = DurableGoods .find.where().eq("status",SuppliesStatus.NORMAL).findList();
            }
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(searchResult);
            json = Json.parse(jsonArray);
            result.put("result",json);
            result.put("status", "SUCCESS");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
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

    @Security.Authenticated(Secured.class)
    public static Result autocompleteRepairCommitee(){
        List<User> allUser = User.find.all();
        List<String> name = new ArrayList<String>();        
        List<String> lastname = new ArrayList<String>();
        List<String> position = new ArrayList<String>();
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode result = Json.newObject();
        JsonNode json;

        try{

            for(User us : allUser){ 
                name.add(us.firstName);               
                lastname.add(us.lastName);
                position.add(us.position);
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
