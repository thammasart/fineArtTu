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
    public static Result exportOrder() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportOrder.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOrderAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportOrderAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOrderAddDetail() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportOrderAddDetail.render(user));
    }


    @Security.Authenticated(Secured.class)
    public static Result exportTransferInside() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportTransferInside.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportTransferInsideAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAddDetail() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportTransferInsideAddDetail.render(user));
    }



    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSide() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportTransferOutSide.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSideAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportTransferOutSideAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSideAddDetail() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportTransferOutSideAddDetail.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportDonate() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportDonate.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportDonateAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportDonateAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportDonateAddDetail() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportDonateAddDetail.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportSold() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportSold.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportSoldAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportSoldAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportSoldAddDetail() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(exportSoldAddDetail.render(user));
    }



    @Security.Authenticated(Secured.class)
    public static Result exportOther() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok( exportOther.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOtherAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok( exportOtherAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOtherAddDetail() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok( exportOtherAddDetail.render(user));
    }

}