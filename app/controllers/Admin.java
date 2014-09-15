package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Admin extends Controller {

    public static Result index() {
        User user = User.find.byId(session().get("username"));
        List<User> users = User.find.all(); 
        return ok(admin.render(users, user));
    }

    public static Result saveNewUser() {
        DynamicForm f = Form.form().bindFromRequest();
        Form<User> newUserFrom = Form.form(User.class).bindFromRequest();
        System.out.println(newUserFrom);
        User newUser = newUserFrom.get();    
        newUser.status = UserStatus.find.byId(f.get("statusName"));
        newUser.save();
        return redirect(routes.Admin.index());
    }
    

    public static Result addUser() {
        User user = User.find.byId(session().get("username"));
        List<UserStatus> allStatus = UserStatus.find.all();
        return ok(addUser.render(user,allStatus));
    }

    public static Result manageRole() {
        User user = User.find.byId(session().get("username"));
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
    	userStatus.save();
    	return redirect(routes.Admin.manageRole());
    }
    
    public static Result removeRole(){
    	// no handle exception with userStatus that bind with user
    	DynamicForm form = Form.form().bindFromRequest();
    	UserStatus userStatus = UserStatus.find.byId(form.get("name"));
    	userStatus.delete();
    	return redirect(routes.Admin.manageRole());
    }

    public static Result removeUser(){
    	DynamicForm form = Form.form().bindFromRequest();
    	
    	String[] usernames = form.get("data").split(",");
    	for(int i=0;i<usernames.length;i++){
    		User user = User.find.byId(usernames[i]);
    		user.delete();
    	}
    	return redirect(routes.Admin.index());
    }
}
