package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.RequestBody;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Admin extends Controller {

    public static Result index() {
        User user = User.find.byId(session().get("username"));
        if(!user.isPermit(1))return ok(permissionDenied.render());
        List<User> users = User.find.all(); 
        List<UserStatus> allStatus = UserStatus.find.all();
        return ok(admin.render(users, user,allStatus));
    }

    public static Result saveNewUser() {
        DynamicForm f = Form.form().bindFromRequest();
        Form<User> newUserFrom = Form.form(User.class).bindFromRequest();

        User user = User.find.byId(f.get("username"));
        if(user == null){
            System.out.println(newUserFrom);
            User newUser = newUserFrom.get();    
            newUser.status = UserStatus.find.byId(f.get("statusName"));
        	newUser.save();
        	return redirect(routes.Admin.index());
        }else {
            flash("sameUser", " This username already exists.");
            return redirect(routes.Admin.addUser());
        }

    }

    public static Result addUser() {
        User user = User.find.byId(session().get("username"));
        if(!user.isPermit(1))return ok(permissionDenied.render());
        List<UserStatus> allStatus = UserStatus.find.all();
        return ok(addUser.render(user,allStatus));
    }

    public static Result manageRole() {
        User user = User.find.byId(session().get("username"));
        if(!user.isPermit(1))return ok(permissionDenied.render());
        List<UserStatus> usersStatus = UserStatus.find.all();
        return ok(manageRole.render(user,usersStatus));
    }
    
    public static Result addRole(){
    	DynamicForm form = Form.form().bindFromRequest();
    	System.out.println(form);
    	UserStatus userStatus = new UserStatus();
    	userStatus.name = form.get("name");
    	if(form.get("module1").equals("true")){
    		userStatus.module1 = true;
    	}
    	if(form.get("module2").equals("true")){
    		userStatus.module2 = true;
    	}
    	if(form.get("module3").equals("true")){
    		userStatus.module3 = true;
    	}
    	if(form.get("module4").equals("true")){
    		userStatus.module4 = true;
    	}
    	if(form.get("module5").equals("true")){
    		userStatus.module5 = true;
    	}
    	if(form.get("module6").equals("true")){
    		userStatus.module6 = true;
    	}
    	userStatus.save();
    	return redirect(routes.Admin.manageRole());
    }
    public static Result adminEditUserRole(){
    	DynamicForm form = Form.form().bindFromRequest();

        User user = User.find.byId(form.get("editName"));
        UserStatus status = UserStatus.find.byId(form.get("newRole"));

        if(user != null && status != null){
            user.status = status;
            user.update();
        }
        return redirect(routes.Admin.index());
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result editRole(){
    	User user = User.find.byId(session().get("username"));
    	if(!user.isPermit(1)){
    		return ok(permissionDenied.render());
    	}
    	RequestBody body = request().body();
    	JsonNode json = body.asJson();
    	//JsonNode arrNode = new ObjectMapper().readTree(json.asText()).get("data");
    	if (json.isArray()) {
    	    for (JsonNode role : json) {
    	        UserStatus userStatus = UserStatus.find.byId(role.get("name").asText());
    	        if(!userStatus.name.equals(user.status.name) && userStatus != null){
    	        	userStatus.module1 = role.get("module1").asBoolean();
    	        	userStatus.module2 = role.get("module2").asBoolean();
    	        	userStatus.module3 = role.get("module3").asBoolean();
    	        	userStatus.module4 = role.get("module4").asBoolean();
    	        	userStatus.module5 = role.get("module5").asBoolean();
    	        	userStatus.module6 = role.get("module6").asBoolean();
    	        	userStatus.update();
    	        }
    	    }
    	}
    	return ok("success");
    }
    
    public static Result removeRole(){
    	// no handle exception with userStatus that bind with user
    	DynamicForm form = Form.form().bindFromRequest();
    	UserStatus userStatus = UserStatus.find.byId(form.get("name"));

        if(userStatus != null && userStatus.numberOfUser.size()==0){
            userStatus.delete();
        }
    	return redirect(routes.Admin.manageRole());
    }

    public static Result removeUser(){
    	DynamicForm form = Form.form().bindFromRequest();
    	User loggedInUser = User.find.byId(session().get("username"));
    	User user;
    	int count = 0;
        if(!form.get("data").equals("")){
    	String[] usernames = form.get("data").split(",");
            for(int i=0;i<usernames.length;i++){
                user = User.find.byId(usernames[i]);
                if(!user.equals(loggedInUser)){
                	count++;
                	user.delete();
                }
            }
            flash("delete","delete " + usernames.length +" account " );
        } else flash("notSelect","please select at least one account");
    	return redirect(routes.Admin.index());
    }
    
}
