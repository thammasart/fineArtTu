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

public class Application extends Controller {

    public static Result index() {
        return ok(login.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result home(){
        User user = User.find.byId(request().username());
    	return ok(home.render(user));
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    public static Result user(){
    	return ok(user.render());
    }
    
    //@Security.Authenticated(Secured.class)
    public static Result authentication(){
    	Form<UserForm> userForm = Form.form(UserForm.class).bindFromRequest();
    	if(userForm.hasErrors()) {
            flash("unauthenticate", "Incorrect Username/Password.");
            return badRequest(login.render());
        }
        else {
            session().clear();
            session("username", userForm.get().username);
            return redirect(routes.Application.home());
        }
    }

    public static Result imports() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(imports.render(user));
    }

    public static Result importsInstitute() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsInstitute.render(user));
    }

    public static Result importsInstituteAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsInstituteAdd.render(user));
    }

        public static Result importsMaterialDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialDurableArticles.render(user));
    }

        public static Result importsMaterialDurableArticlesAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialDurableArticlesAdd.render(user));
    }

        public static Result importsMaterialDurableGoods() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialDurableGoods.render(user));
    }
        public static Result importsMaterialConsumable() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialConsumable.render(user));
    }

}
