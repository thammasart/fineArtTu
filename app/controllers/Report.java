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
import java.util.Collections.*;
import java.util.Locale;
import java.text.*;

public class Report  extends Controller {

    @Security.Authenticated(Secured.class)
        public static Result report() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(report.render(user));
    }

    @Security.Authenticated(Secured.class)
        public static Result reportRemainingMaterialPost() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<MaterialCode> mc = MaterialCode.find.all();
        DynamicForm form = Form.form().bindFromRequest();
        int years = 0;
        String year = form.get("year");
        try{
            years = Integer.parseInt(year);
        }catch(NumberFormatException e){
            Date dNow = new Date();
            years = dNow.getYear() + 2443 ;
        }
        return redirect(routes.Report.reportRemainingMaterial(years,1));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportRemainingMaterialPostPrint() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<MaterialCode> mc = MaterialCode.find.all();
        DynamicForm form = Form.form().bindFromRequest();
        String year = form.get("yearPrint");

        return redirect(routes.Report.reportRemainingMaterial(Integer.parseInt(year),2));
    }

    public static int compare(Date d1, Date d2) {
        if (d1.getYear() != d2.getYear()) 
            return d1.getYear() - d2.getYear();
        if (d1.getMonth() != d2.getMonth()) 
            return d1.getMonth() - d2.getMonth();
        return d1.getDate() - d2.getDate();
    }

    @Security.Authenticated(Secured.class)
        public static Result reportRemainingMaterial(int year,int view) {
        
        if(year == -1){
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy");
            year = Integer.parseInt(ft.format(dNow)) +543;
        }

        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<MaterialCode> mc = MaterialCode.find.all();
        List<List<String[]>> material = new ArrayList<List<String[]>>();
        
        for(MaterialCode each : mc){
            List<Double> temp = each.getRemaining(year-1);
            List<String[]> details = new ArrayList<String[]>();

            int totalMaterial = 0;
            double totlePrice = 0.00;
            int totalReciveAmount = 0;
            double totalRecivePrice = 0;
            int totalWithdrawAmount = 0;
            double totalWithdrawPrice = 0;

            List<models.durableGoods.ProcurementDetail> allRemain = each.getProcurementDetailRemaining(year);
            for(models.durableGoods.ProcurementDetail remain : allRemain){

                totalMaterial += remain.quantity;
                totlePrice +=  remain.price * remain.quantity;
                totalReciveAmount += remain.quantity;
                totalRecivePrice += remain.price * remain.quantity;

                String[] detail = new String[13];
                if(details.size() == 0){
                    detail[0] = String.format("%d", material.size() + 1);
                    detail[1] = each.description;
                    detail[2] = each.classifier;
                }   
                else{
                    detail[0] = "";
                    detail[1] = "";
                    detail[2] = "";
                }
                detail[3] = "01/10/"+year;
                detail[4] = "ยอดยกมา";
                detail[5] = "";
                detail[6] = String.format("%.2f", remain.price);
                detail[7] = String.format("%d", remain.quantity);
                detail[8] = String.format("%.2f", remain.price * remain.quantity);
                detail[9] = "";
                detail[10] = "";
                detail[11] = String.format("%d", totalMaterial);
                detail[12] = String.format("%.2f", totlePrice);
                details.add(detail);
            }

            List<models.durableGoods.ProcurementDetail> importDetails = models.durableGoods.ProcurementDetail.find.where().eq("code",each.code).eq("procurement.status",ImportStatus.SUCCESS).between("procurement.addDate",new Date(year-2444,9,1),new Date(year-2443,8,30)).orderBy("procurement.addDate asc").findList();
            List<models.durableGoods.RequisitionDetail> exportDetails = models.durableGoods.RequisitionDetail.find.where().eq("code",each).eq("requisition.status",ExportStatus.SUCCESS).between("requisition.approveDate",new Date(year-2444,9,1),new Date(year-2443,8,30)).orderBy("requisition.approveDate asc").findList();
            SimpleDateFormat fts = new SimpleDateFormat ("dd/MM/yyyy",new Locale("th","th"));
            double totalAmount = temp.get(1);

            double sumOfPrice = temp.get(0) * temp.get(1);
            while(importDetails.size() > 0 && exportDetails.size() > 0 ){
            	models.durableGoods.Procurement procurement = models.durableGoods.Procurement.find.byId(importDetails.get(0).procurement.id);
                if(compare(procurement.addDate,exportDetails.get(0).requisition.approveDate) <= 0){

                    totalMaterial += importDetails.get(0).quantity;
                    totlePrice += importDetails.get(0).price * importDetails.get(0).quantity; 

                    totalReciveAmount += importDetails.get(0).quantity;
                    totalRecivePrice += importDetails.get(0).price * importDetails.get(0).quantity;

                    String[] detail = new String[13];
                    if(details.size() == 0){
                        detail[0] = String.format("%d", material.size() + 1);
                        detail[1] = each.description;
                        detail[2] = each.classifier;
                    }   
                    else{
                        detail[0] = "";
                        detail[1] = "";
                        detail[2] = "";
                    }
                    
                    detail[3] = fts.format(procurement.addDate);
                    if(importDetails.get(0).procurement.company != null)
                        detail[4] = importDetails.get(0).procurement.company.nameEntrepreneur;
                    else
                        detail[4] = "ไม่ระบุ";
                    detail[5] = "";
                    detail[6] = String.format("%.2f", importDetails.get(0).price);
                    detail[7] = String.format("%d", importDetails.get(0).quantity);
                    detail[8] = String.format("%.2f", importDetails.get(0).price * importDetails.get(0).quantity);
                    detail[9] = "";
                    detail[10] = "";
                    detail[11] = String.format("%d", totalMaterial);
                    detail[12] = String.format("%.2f", totlePrice);
                
                    details.add(detail);
                    importDetails.remove(0);
                }
                else{

                    totalMaterial -= exportDetails.get(0).quantity;
                    totlePrice -= exportDetails.get(0).totlePrice;
                    
                    totalWithdrawAmount += exportDetails.get(0).quantity;
                    totalWithdrawPrice += exportDetails.get(0).totlePrice;

                    String[] detail = new String[13];
                    if(details.size() == 0){
                        detail[0] = String.format("%d", material.size() + 1);
                        detail[1] = each.description;
                        detail[2] = each.classifier;
                    }   
                    else{
                        detail[0] = "";
                        detail[1] = "";
                        detail[2] = "";
                    }
                    detail[3] = fts.format(exportDetails.get(0).requisition.approveDate);
                    detail[4] = exportDetails.get(0).withdrawer.firstName;
                    detail[5] = "";
                    detail[6] = "";
                    detail[7] = "";
                    detail[8] = "";
                    detail[9] = String.format("%d", exportDetails.get(0).quantity);
                    detail[10] = String.format("%.2f", exportDetails.get(0).totlePrice);
                    detail[11] = String.format("%d", totalMaterial);
                    detail[12] = String.format("%.2f", totlePrice);
                
                    details.add(detail);
                    exportDetails.remove(0);
                }
            }// end while

            if(importDetails.size() > 0 ){
                while(importDetails.size() > 0){     
                    totalMaterial += importDetails.get(0).quantity;
                    totlePrice += importDetails.get(0).price * importDetails.get(0).quantity; 

                    totalReciveAmount += importDetails.get(0).quantity;
                    totalRecivePrice += importDetails.get(0).price * importDetails.get(0).quantity;

                    String[] detail = new String[13];
                    if(details.size() == 0){
                        detail[0] = String.format("%d", material.size() + 1);
                        detail[1] = each.description;
                        detail[2] = each.classifier;
                    }   
                    else{
                        detail[0] = "";
                        detail[1] = "";
                        detail[2] = "";
                    }
                    models.durableGoods.Procurement procurement = models.durableGoods.Procurement.find.byId(importDetails.get(0).procurement.id);
                    detail[3] = fts.format(procurement.addDate);
                    if(importDetails.get(0).procurement.company != null)
                        detail[4] = importDetails.get(0).procurement.company.nameEntrepreneur;
                    else
                        detail[4] = "ไม่ระบุ";
                    detail[5] = "";
                    detail[6] = String.format("%.2f", importDetails.get(0).price);
                    detail[7] = String.format("%d", importDetails.get(0).quantity);
                    detail[8] = String.format("%.2f", importDetails.get(0).price * importDetails.get(0).quantity);
                    detail[9] = "";
                    detail[10] = "";
                    detail[11] = String.format("%d", totalMaterial);
                    detail[12] = String.format("%.2f", totlePrice);
                
                    details.add(detail);
                    importDetails.remove(0);
                }
            }
            else{
                while(exportDetails.size() > 0){

                    totalMaterial -= exportDetails.get(0).quantity;
                    totlePrice -= exportDetails.get(0).totlePrice;

                    totalWithdrawAmount += exportDetails.get(0).quantity;
                    totalWithdrawPrice += exportDetails.get(0).totlePrice;

                    String[] detail = new String[13];
                    if(details.size() == 0){
                        detail[0] = String.format("%d", material.size() + 1);
                        detail[1] = each.description;
                        detail[2] = each.classifier;
                    }   
                    else{
                        detail[0] = "";
                        detail[1] = "";
                        detail[2] = "";
                    }
                    detail[3] = fts.format(exportDetails.get(0).requisition.approveDate);
                    detail[4] = exportDetails.get(0).withdrawer.firstName;
                    detail[5] = "";
                    detail[6] = "";
                    detail[7] = "";
                    detail[8] = "";
                    detail[9] = String.format("%d", exportDetails.get(0).quantity);
                    detail[10] = String.format("%.2f", exportDetails.get(0).totlePrice);
                    detail[11] = String.format("%d", totalMaterial);
                    detail[12] = String.format("%.2f", totlePrice);
                
                    details.add(detail);
                    exportDetails.remove(0);
                }
            }
                if(!details.isEmpty()){
                    //add conclusion
                    String[] detail = new String[13];
                    detail[0] = "";
                    detail[1] = "ใช้ไป/คงเหลือ";
                    detail[2] = "";
                    detail[3] = "";
                    detail[4] = "";
                    detail[5] = "";
                    detail[6] = "";
                    detail[7] = String.format("%d", totalReciveAmount);
                    detail[8] = String.format("%.2f", totalRecivePrice);
                    detail[9] = String.format("%d", totalWithdrawAmount);
                    detail[10] = String.format("%.2f", totalWithdrawPrice);
                    detail[11] = String.format("%d", totalMaterial);
                    detail[12] = String.format("%.2f", totlePrice);
                    
                    details.add(detail);
                }

            material.add(details);
        }

        if(view == 1){
            return ok(reportRemainingMaterial.render(year,user,material));
        } else {
            return ok(reportRemainingMaterialPrint.render(year,user,material));
        }
    }
    
    @Security.Authenticated(Secured.class)
        public static Result reportDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<models.durableArticles.DurableArticles> da = models.durableArticles.DurableArticles.find.where().eq("status",SuppliesStatus.NORMAL).findList();                    //ครุภัณฑ์
        return ok(reportDurableArticles.render(user,da));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportDurableArticlesPrint() {
        List<models.durableArticles.DurableArticles> da = models.durableArticles.DurableArticles.find.all();                    //ครุภัณฑ์
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();
        return ok(reportDurableArticlesPrint.render(da,date));
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
        public static Result reportRemainingMaterialConclusionPrint() {
        List<MaterialCode> mc = MaterialCode.find.all();
        
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();
        return ok(reportRemainingMaterialConclusionPrint.render(mc,date));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportRemainingMaterialConclusionPrint2() {
        List<DurableGoods> dg = DurableGoods.find.where().eq("typeOfDurableGoods",1).findList();
        List<DurableGoods> dList = new ArrayList<DurableGoods>();
    	HashMap<String, DurableGoods> listResult = new HashMap<String,DurableGoods>();

        for(DurableGoods each : dg){
            listResult.put(each.detail.code,each);
        }

        for(String each : listResult.keySet()){
            dList.add(listResult.get(each));
        }
        
        
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();
        return ok(reportRemainingMaterialConclusionPrint2.render(dList,date));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result reportImportDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<models.durableArticles.DurableArticles> da = models.durableArticles.DurableArticles.find.where().eq("status",SuppliesStatus.NORMAL).findList();         //ครุภัณฑ์
        List<models.durableArticles.DurableArticles> dList = new ArrayList<models.durableArticles.DurableArticles>();
    	HashMap<String,models.durableArticles.DurableArticles> listResult = new HashMap<String,models.durableArticles.DurableArticles>();
    	HashMap<String,Integer> listCount = new HashMap<String,Integer>();
        List<Integer> count = new ArrayList<Integer>();

        if(da.size()>0){
            for(models.durableArticles.DurableArticles each : da){
                listResult.put(each.detail.fsn.typ.groupClass.classId,each);
                if(listCount.get(each.detail.fsn.typ.groupClass.classId) == null){
                    listCount.put(each.detail.fsn.typ.groupClass.classId,1);
                }else{
                    listCount.put(each.detail.fsn.typ.groupClass.classId,listCount.get(each.detail.fsn.typ.groupClass.classId)+1);
                }
            }
            for(String each : listResult.keySet()){
                dList.add(listResult.get(each));
                count.add(listCount.get(each));
            }
        }
        return ok(reportImportDurableArticle.render(user,dList,count));
    }
    @Security.Authenticated(Secured.class)
    public static Result reportImportDurableArticlesPrint() {
        List<models.durableArticles.DurableArticles> da = models.durableArticles.DurableArticles.find.where().eq("status",SuppliesStatus.NORMAL).findList();         //ครุภัณฑ์
        List<models.durableArticles.DurableArticles> dList = new ArrayList<models.durableArticles.DurableArticles>();
    	HashMap<String,models.durableArticles.DurableArticles> listResult = new HashMap<String,models.durableArticles.DurableArticles>();
    	HashMap<String,Integer> listCount = new HashMap<String,Integer>();
        List<Integer> count = new ArrayList<Integer>();

        if(da.size()>0){
            for(models.durableArticles.DurableArticles each : da){
                listResult.put(each.detail.fsn.typ.groupClass.classId,each);
                if(listCount.get(each.detail.fsn.typ.groupClass.classId) == null){
                    listCount.put(each.detail.fsn.typ.groupClass.classId,1);
                }else{
                    listCount.put(each.detail.fsn.typ.groupClass.classId,listCount.get(each.detail.fsn.typ.groupClass.classId)+1);
                }
            }
            for(String each : listResult.keySet()){
                dList.add(listResult.get(each));
                count.add(listCount.get(each));
            }
        }

        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();
        return ok(reportImportDurableArticlePrint.render(dList,date,count));
    }
    @Security.Authenticated(Secured.class)
    public static Result reportDurableArticlesByType() {

        User user = User.find.where().eq("username", session().get("username")).findUnique();
        DynamicForm form = Form.form().bindFromRequest();
        String val = form.get("classIdVal");
        List<models.durableArticles.DurableArticles> da = models.durableArticles.DurableArticles.find.where().eq("status",SuppliesStatus.NORMAL).eq("detail.fsn.typ.groupClass.classId",val).findList();                //ครุภัณฑ์
        List<models.durableArticles.DurableArticles> dList = new ArrayList<models.durableArticles.DurableArticles>();
    	HashMap<String,models.durableArticles.DurableArticles> listResult = new HashMap<String,models.durableArticles.DurableArticles>();
        String temp = "";
        for(models.durableArticles.DurableArticles each : da){
            temp = each.detail.procurement.title + each.detail.description;
            listResult.put(temp,each);
        }

        for(String each : listResult.keySet()){
            dList.add(listResult.get(each));
        }

        return ok(reportDurableArticleByType.render(user,dList));
    }
    @Security.Authenticated(Secured.class)
    public static Result reportDurableArticlesByTypePrint() {

        DynamicForm form = Form.form().bindFromRequest();
        String val = form.get("classIdVal");
        List<models.durableArticles.DurableArticles> da = models.durableArticles.DurableArticles.find.where().eq("status",SuppliesStatus.NORMAL).eq("detail.fsn.typ.groupClass.classId",val).findList();                //ครุภัณฑ์
        List<models.durableArticles.DurableArticles> dList = new ArrayList<models.durableArticles.DurableArticles>();
    	HashMap<String,models.durableArticles.DurableArticles> listResult = new HashMap<String,models.durableArticles.DurableArticles>();
        String temp = "";
        for(models.durableArticles.DurableArticles each : da){
            temp = each.detail.procurement.title + each.detail.description;
            listResult.put(temp,each);
        }

        for(String each : listResult.keySet()){
            dList.add(listResult.get(each));
        }

        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();

        return ok(reportDurableArticleByTypePrint.render(date,dList));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportExportDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<RequisitionDetail> details = RequisitionDetail.find.where().orderBy("requisition.approveDate asc").findList();
        
        List<OrderGoodsDetail> order = OrderGoodsDetail.find.where().orderBy("order.approveDate asc").findList();
        return ok(reportExportDurableArticle.render(user,details,order));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportExportDurableArticlesPrint() {
        List<RequisitionDetail> details = RequisitionDetail.find.where().orderBy("requisition.approveDate asc").findList();
        
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();
        return ok(reportExportDurableArticlePrint.render(date,details));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportExportDurableArticlesPrint2() {
        List<OrderGoodsDetail> order = OrderGoodsDetail.find.where().orderBy("order.approveDate asc").findList();
        
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();
        return ok(reportExportDurableArticlePrint2.render(date,order));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportExchangeDurableArticles() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<InternalTransfer> it =InternalTransfer.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<InternalTransferDetail> itd = new ArrayList<InternalTransferDetail>();
        for(InternalTransfer each : it){
            itd.addAll(each.detail);
        }
        return ok( reportExchangeDurableArticle.render(user,itd));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportExchangeDurableArticlesPrint() {
        List<InternalTransfer> it =InternalTransfer.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<InternalTransferDetail> itd = new ArrayList<InternalTransferDetail>();
        for(InternalTransfer each : it){
            itd.addAll(each.detail);
        }

        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();

        return ok( reportExchangeDurableArticlePrint.render(date,itd));
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
        public static Result reportAuctionPrint() {
        List<Auction> auc =Auction.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<AuctionDetail> ad = new ArrayList<AuctionDetail>();
        for(Auction ac : auc){
            ad.addAll(ac.detail);
        }
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();

        return ok( reportAuctionPrint.render(date,ad));
    }
    @Security.Authenticated(Secured.class)
        public static Result reportDonatePrint() {
        List<Donation> don = Donation.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<DonationDetail> dond  = new ArrayList<DonationDetail>();
        for(Donation dn : don){
            dond.addAll(dn.detail);
        }
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();

        return ok(reportDonatePrint.render(date,dond));
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
        public static Result reportRepairPrint() {
        List<Repairing> rps = Repairing.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<Repairing> rpr = Repairing.find.where().eq("status",ExportStatus.REPAIRING).findList();
        List<RepairingDetail> rd  = new ArrayList<RepairingDetail>();
        for(Repairing repair : rpr){
            rd.addAll(repair.detail);
        }
        for(Repairing repair : rps){
            rd.addAll(repair.detail);
        }
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();

        return ok(reportRepairPrint.render(date,rd));
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
        public static Result reportBorrowPrint() {
        List<Borrow> brs = Borrow.find.where().eq("status",ExportStatus.SUCCESS).findList();
        List<Borrow> brb = Borrow.find.where().eq("status",ExportStatus.BORROW).findList();
        List<BorrowDetail> bd = new ArrayList<BorrowDetail>();
        for(Borrow br: brb){
            bd.addAll(br.detail);
        }
        for(Borrow br : brs){
            bd.addAll(br.detail);
        }
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();

        return ok(reportBorrowPrint.render(date,bd));
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
    @Security.Authenticated(Secured.class)
        public static Result reportOtherPrint() {
        List<OtherTransfer> other = OtherTransfer.find.all();
        List<OtherTransferDetail> otherD = new ArrayList<OtherTransferDetail>();
        for(OtherTransfer ot : other){
            otherD.addAll(ot.detail);
        }
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat (" dd MMMM yyyy",new Locale("th","th"));
        String date = ft.format(dNow).toString();

        return ok(reportOtherPrint.render(date,otherD));
    }
}
