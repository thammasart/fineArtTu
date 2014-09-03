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

public class Export extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result export() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(export.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSide() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportTransferOutSide.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInside() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportTransferInside.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportDonate() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportDonate.render(user));
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

}