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
        List<UserStatus> allStatus = UserStatus.find.all();
        return ok(admin.render(users, user,allStatus));
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
    	if(form.get("module6").equals("true")){
    		userStatus.module6 = true;
    	}
    	userStatus.save();
    	return redirect(routes.Admin.manageRole());
    }
    public static Result adminEditUserRole(){
    	DynamicForm form = Form.form().bindFromRequest();

        String editName = form.get("editName");
        String newRole = form.get("newRole");

        User user = User.find.byId(editName);
        UserStatus status = UserStatus.find.byId(newRole);

        if(user != null && status != null){
            user.status = status;
            user.update();
        }

        User userAll = User.find.byId(session().get("username"));
        List<User> users = User.find.all(); 
        List<UserStatus> allStatus = UserStatus.find.all();

        return ok(admin.render(users, userAll,allStatus));
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
    	User user;
        if(!form.get("data").equals("")){
    	String[] usernames = form.get("data").split(",");
    
        
            for(int i=0;i<usernames.length;i++){
                    user = User.find.byId(usernames[i]);
                    user.delete();
            }
            flash("delete","delete " + usernames.length +" account " );
        } else flash("notSelect","please select at least one account");
    	return redirect(routes.Admin.index());
    }
}
