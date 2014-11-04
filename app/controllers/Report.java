package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import views.html.*;
import views.html.report.*;
import models.*;
import models.durableGoods.*;
import models.fsnNumber.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Report  extends Controller {

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
        List<models.durableArticles.DurableArticles> da = models.durableArticles.DurableArticles.find.all();                    //ครุภัณฑ์
        return ok(reportDurableArticles.render(user,da));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportDurableArticlesPrint() {
        List<models.durableArticles.DurableArticles> da = models.durableArticles.DurableArticles.find.all();                    //ครุภัณฑ์
        return ok(reportDurableArticlesPrint.render(da));
    }

    @Security.Authenticated(Secured.class)
        public static Result reportRemainingMaterialConclusion() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<MaterialCode> mc = MaterialCode.find.all();
        List<MaterialType> mt = MaterialType.find.all();
        
        return ok(reportRemainingMaterialConclusion.render(user,mc,mt));
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
