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

public class Admin extends Controller {

    public static Result index() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
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
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<UserStatus> allStatus = UserStatus.find.all();
        return ok(addUser.render(user,allStatus));
    }

    public static Result manageRole() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(manageRole.render(user));
    }


}
