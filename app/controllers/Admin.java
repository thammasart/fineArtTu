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
        return ok(admin.render(user));
    }

    public static Result saveNewUser() {
        Form<User> newUserFrom = Form.form(User.class).bindFromRequest();
        User newUser = newUserFrom.get();    
        newUser.save();
        return redirect(routes.Admin.index());
    }

    public static Result addUser() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(addUser.render(user));
    }

    public static Result manageRole() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(manageRole.render(user));
    }


}
