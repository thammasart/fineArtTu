package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;

import views.html.*;
import views.html.export.*;

import models.*;
import models.consumable.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Export extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result export() {
        User user = User.find.byId(session().get("username"));
        return ok(export.render(user));
    }


    // เยิกจ่าย
    @Security.Authenticated(Secured.class)
    public static Result exportOrder() {
        User user = User.find.byId(session().get("username"));
        List<Requisition> requisitionList = models.consumable.Requisition.find.all();
        return ok(exportOrder.render(user,requisitionList));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOrderAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportOrderAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOrderAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportOrderAddDetail.render(user));
    }


    // โอนย้ายภายใน
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInside() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferInside.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferInsideAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferInsideAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferInsideAddDetail.render(user));
    }


    // โอนย่ายภานนอก
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSide() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSide.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSideAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSideAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportTransferOutSideAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportTransferOutSideAddDetail.render(user));
    }


    // บริจาค
    @Security.Authenticated(Secured.class)
    public static Result exportDonate() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonate.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportDonateAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonateAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportDonateAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportDonateAddDetail.render(user));
    }


    // จำหน่าย
    @Security.Authenticated(Secured.class)
    public static Result exportSold() {
        User user = User.find.byId(session().get("username"));
        return ok(exportSold.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportSoldAdd() {
        User user = User.find.byId(session().get("username"));
        return ok(exportSoldAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportSoldAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok(exportSoldAddDetail.render(user));
    }


    // อื่นๆ
    @Security.Authenticated(Secured.class)
    public static Result exportOther() {
        User user = User.find.byId(session().get("username"));
        return ok( exportOther.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOtherAdd() {
        User user = User.find.byId(session().get("username"));
        return ok( exportOtherAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result exportOtherAddDetail() {
        User user = User.find.byId(session().get("username"));
        return ok( exportOtherAddDetail.render(user));
    }

}