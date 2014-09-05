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

    @Security.Authenticated(Secured.class)
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

   


    @Security.Authenticated(Secured.class)
        public static Result report() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(report.render(user));
    }

    @Security.Authenticated(Secured.class)
        public static Result reportRemainingMaterial() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(reportRemainingMaterial.render(user));
    }
    
    @Security.Authenticated(Secured.class)
        public static Result reportDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(reportDurableArticles.render(user));
    }

    @Security.Authenticated(Secured.class)
        public static Result reportRemainingMaterialConclusion() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(reportRemainingMaterialConclusion.render(user));
    }
    
    @Security.Authenticated(Secured.class)
        public static Result reportImportDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(reportImportDurableArticle.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportExportDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(reportExportDurableArticle.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportExchangeDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok( reportExchangeDurableArticle.render(user));
    }
}
