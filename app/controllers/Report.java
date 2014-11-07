package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import views.html.*;
import views.html.report.*;
import models.*;
import models.durableGoods.*;
import models.durableArticles.*;
import models.type.*;
import models.fsnNumber.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

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
        List<DurableGoods> dg = DurableGoods.find.where().eq("typeOfDurableGoods",1).findList();
        List<DurableGoods> dList = new ArrayList<DurableGoods>();
    	HashMap<String, DurableGoods> listResult = new HashMap<String,DurableGoods>();

        for(DurableGoods each : dg){
            listResult.put(each.detail.code,each);
        }

        for(String each : listResult.keySet()){
            dList.add(listResult.get(each));
        }
        
        return ok(reportRemainingMaterialConclusion.render(user,mc,mt,dList));
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
        List<Auction> auc =Auction.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<AuctionDetail> ad = new ArrayList<AuctionDetail>();
        for(Auction ac : auc){
            ad.addAll(ac.detail);
        }
        return ok( reportExchangeDurableArticle.render(user,ad));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportAuction() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<Auction> auc =Auction.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<AuctionDetail> ad = new ArrayList<AuctionDetail>();
        for(Auction ac : auc){
            ad.addAll(ac.detail);
        }
        return ok( reportAuction.render(user,ad));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportDonate() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<Donation> don = Donation.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<DonationDetail> dond  = new ArrayList<DonationDetail>();
        for(Donation dn : don){
            dond.addAll(dn.detail);
        }
        return ok(reportDonate.render(user,dond));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportRepair() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<Repairing> rps = Repairing.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<Repairing> rpr = Repairing.find.where().eq("status",ExportStatus.REPAIRING).findList();
        List<RepairingDetail> rd  = new ArrayList<RepairingDetail>();
        for(Repairing repair : rpr){
            rd.addAll(repair.detail);
        }
        for(Repairing repair : rps){
            rd.addAll(repair.detail);
        }
        return ok(reportRepair.render(user,rd));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportBorrow() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<Borrow> brs = Borrow.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<Borrow> brb = Borrow.find.where().eq("status",ExportStatus.BORROW).findList();
        List<BorrowDetail> bd = new ArrayList<BorrowDetail>();
        for(Borrow br: brb){
            bd.addAll(br.detail);
        }
        for(Borrow br : brs){
            bd.addAll(br.detail);
        }
        return ok(reportBorrow.render(user,bd));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportOther() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<OtherTransfer> other = OtherTransfer.find.all();
        List<OtherTransferDetail> otherD = new ArrayList<OtherTransferDetail>();
        for(OtherTransfer ot : other){
            otherD.addAll(ot.detail);
        }
        return ok(reportOther.render(user,otherD));
    }
}
