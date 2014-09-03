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

public class Import extends Controller {

 	@Security.Authenticated(Secured.class)
    public static Result imports() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(imports.render(user));
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


}