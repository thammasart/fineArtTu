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
    public static Result imports() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(imports.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result export() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(export.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result transferOutSide() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(transferOutSide.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result transferInside() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(transferInside.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result donate() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(donate.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportOrder() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportOrder.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportSold() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportSold.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOther() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok( exportOther.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result importsInstitute() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsInstitute.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result importsInstituteAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsInstituteAdd.render(user));
    }

    @Security.Authenticated(Secured.class)
        public static Result importsMaterial() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterial.render(user));
    }

    @Security.Authenticated(Secured.class)
        public static Result importsMaterialDurableArticlesAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialDurableArticlesAdd.render(user));
    }

    @Security.Authenticated(Secured.class)
        public static Result importsMaterialDurableGoodsAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialDurableGoodsAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsMaterialConsumableGoodsAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialConsumableGoodsAdd.render(user));
    }



    @Security.Authenticated(Secured.class)
        public static Result importsOrder() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrder.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderDurableArticlesAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderDurableArticlesAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderDurableArticlesAddMaterial1() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderDurableArticlesAddMaterial1.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderDurableArticlesAddMaterial2() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderDurableArticlesAddMaterial2.render(user));
    }

    @Security.Authenticated(Secured.class)
        public static Result importsOrderGoodsAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderGoodsAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderGoodsAddMaterial1() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderGoodsAddMaterial1.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderGoodsAddMaterial2() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderGoodsAddMaterial2.render(user));
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
    
}
