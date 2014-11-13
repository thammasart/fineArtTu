package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.mvc.Http.RequestBody;

import views.html.*;
import views.html.export.*;

import models.*;
import models.consumable.*;
import models.durableArticles.*;
import models.type.ExportStatus;
import models.type.SuppliesStatus;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;                                                                                         
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;  

public class ExportSold extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result exportSold() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        List<Auction> initList = Auction.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Auction> successList = Auction.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportSold.render(user,initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateSold() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Auction temp =  new Auction ();
        temp.approveDate = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportSold.exportSoldAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportSoldAdd(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Auction sold = Auction.find.byId(id);
        if(sold == null || sold.status != ExportStatus.INIT){
            return redirect(routes.ExportSold.exportSold());
        }
        List<Company> allCompany = Company.find.all();
        return ok(exportSoldAdd.render(user, sold, allCompany));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewDetail(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Auction sold = Auction.find.byId(id);
        if(sold == null || sold.status != ExportStatus.SUCCESS){
            return redirect(routes.ExportSold.exportSold());
        }
        List<Company> allCompany = Company.find.all();
        return ok(exportSoldViewDetail.render(user, sold, allCompany));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveAuction(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Auction auction = Auction.find.byId(id);
        if(auction != null && (auction.status == ExportStatus.INIT || auction.status == ExportStatus.SUCCESS) ){
            DynamicForm f = Form.form().bindFromRequest();
            auction.title = f.get("title");
            auction.contractNo = f.get("contractNo");
            auction.setApproveDate(f.get("approveDate"));

            // save sold destination
            String soldDestination = f.get("soldDestination");
            long companyId = Long.parseLong(soldDestination);
            Company company = Company.find.byId(companyId);
            if(company != null){
                auction.company = company;
            }
            
            // save FF committee
            List<Auction_FF_Committee> ffCommittee = auction.ffCommittee;
            String numbetOfcommittee = f.get("numberOf_FF_committee");
            if(numbetOfcommittee != null){
                int count = Integer.parseInt(numbetOfcommittee);
                for(int i=0; i<count; i++){
                    String num  = Integer.toString(i); 
                    String committeeFirstNmae = f.get("FF_firstName" + num);
                    String committeeLastNmae = f.get("FF_lastName" + num);
                    String committeePosition = f.get("FF_position" + num);

                    List<User> users = User.find.where().eq("firstName",committeeFirstNmae).eq("lastName",committeeLastNmae).eq("position",committeePosition).findList();
                    if(users.size() == 1){
                        int index = 0;
                        for(Auction_FF_Committee committee : ffCommittee){
                            if(committee.user.equals(users.get(0))){
                                committee.employeesType = f.get("FF_cType" + num);
                                committee.committeePosition = f.get("FF_cPosition" + num);
                                committee.update();
                                break;
                            }
                            index++;
                        }
                        if(index < ffCommittee.size()){
                            ffCommittee.remove(index);
                        }
                        else{
                            Auction_FF_Committee newCommittee = new Auction_FF_Committee();
                            newCommittee.user = users.get(0);
                            newCommittee.auction = auction;
                            newCommittee.employeesType = f.get("FF_cType" + num);
                            newCommittee.committeePosition = f.get("FF_cPosition" + num);
                            newCommittee.save();
                        }
                    }
                }
            }
            // save E committee
            List<Auction_E_Committee> eCommittee = auction.eCommittee;
            numbetOfcommittee = f.get("numberOf_E_committee");
            if(numbetOfcommittee != null){
                int count = Integer.parseInt(numbetOfcommittee);
                for(int i=0; i<count; i++){
                    String num  = Integer.toString(i); 
                    String committeeFirstNmae = f.get("E_firstName" + num);
                    String committeeLastNmae = f.get("E_lastName" + num);
                    String committeePosition = f.get("E_position" + num);

                    List<User> users = User.find.where().eq("firstName",committeeFirstNmae).eq("lastName",committeeLastNmae).eq("position",committeePosition).findList();
                    if(users.size() == 1){
                        int index = 0;
                        for(Auction_E_Committee committee : eCommittee){
                            if(committee.user.equals(users.get(0))){
                                committee.employeesType = f.get("E_cType" + num);
                                committee.committeePosition = f.get("E_cPosition" + num);
                                committee.update();
                                break;
                            }
                            index++;
                        }
                        if(index < ffCommittee.size()){
                            ffCommittee.remove(index);
                        }
                        else{
                            Auction_E_Committee newCommittee = new Auction_E_Committee();
                            newCommittee.user = users.get(0);
                            newCommittee.auction = auction;
                            newCommittee.employeesType = f.get("E_cType" + num);
                            newCommittee.committeePosition = f.get("E_cPosition" + num);
                            newCommittee.save();
                        }
                    }
                }
            }
            // save D committee
            List<Auction_D_Committee> dCommittee = auction.dCommittee;
            numbetOfcommittee = f.get("numberOf_D_committee");
            if(numbetOfcommittee != null){
                int count = Integer.parseInt(numbetOfcommittee);
                for(int i=0; i<count; i++){
                    String num  = Integer.toString(i); 
                    String committeeFirstNmae = f.get("D_firstName" + num);
                    String committeeLastNmae = f.get("D_lastName" + num);
                    String committeePosition = f.get("D_position" + num);

                    List<User> users = User.find.where().eq("firstName",committeeFirstNmae).eq("lastName",committeeLastNmae).eq("position",committeePosition).findList();
                    if(users.size() == 1){
                        int index = 0;
                        for(Auction_D_Committee committee : dCommittee){
                            if(committee.user.equals(users.get(0))){
                                committee.employeesType = f.get("D_cType" + num);
                                committee.committeePosition = f.get("D_cPosition" + num);
                                committee.update();
                                break;
                            }
                            index++;
                        }
                        if(index < ffCommittee.size()){
                            ffCommittee.remove(index);
                        }
                        else{
                            Auction_D_Committee newCommittee = new Auction_D_Committee();
                            newCommittee.user = users.get(0);
                            newCommittee.auction = auction;
                            newCommittee.employeesType = f.get("D_cType" + num);
                            newCommittee.committeePosition = f.get("D_cPosition" + num);
                            newCommittee.save();
                        }
                    }
                }
            }

            // delete committee when edit
            for(Auction_FF_Committee committee : ffCommittee){
                committee.delete();
            }
            for(Auction_E_Committee committee : eCommittee){
                committee.delete();
            }
            for(Auction_D_Committee committee : dCommittee){
                committee.delete();
            }

            // update status durableArticles to AUCTION
            for(AuctionDetail detail : auction.detail){
                if(detail.durableArticles.status == SuppliesStatus.NORMAL){
                    detail.durableArticles.status = SuppliesStatus.AUCTION;
                    detail.durableArticles.update();
                }
            }

            // update status donantion
            auction.status = ExportStatus.SUCCESS;
            auction.update();
        }
        return redirect(routes.ExportSold.exportSold());
    }

    @Security.Authenticated(Secured.class)
    public static Result cancelAuction(long id){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        Auction sold = Auction.find.byId(id);
        if(sold != null && sold.status == ExportStatus.INIT){
            sold.status = ExportStatus.CANCEL;
            sold.update();
        }
        return redirect(routes.ExportSold.exportSold());
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteAuction(){
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            for (final JsonNode objNode : json.get("detail")) {
                Long id = Long.parseLong(objNode.toString());
                Auction auction = Auction.find.byId(id);
                if(auction != null){
                    if(auction.status == ExportStatus.INIT){
                        auction.status = ExportStatus.DELETE;
                        auction.update();
                    }
                    else if(auction.status == ExportStatus.SUCCESS){
                        for(AuctionDetail detail : auction.detail){
                            if(detail.durableArticles.status == SuppliesStatus.AUCTION){
                                detail.durableArticles.status = SuppliesStatus.NORMAL;
                                detail.durableArticles.update();
                            }
                        }
                        auction.status = ExportStatus.DELETE;
                        auction.update();
                    }
                }
            }
            result.put("status", "SUCCESS");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result saveAuctionDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Auction auction = Auction.find.byId(id);
            if(auction != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    DurableArticles durableArticles = DurableArticles.find.where().eq("id",id).eq("status",SuppliesStatus.NORMAL).findUnique();
                    if(durableArticles != null){
                        List<AuctionDetail> details = AuctionDetail.find.where().eq("auction",auction).eq("durableArticles",durableArticles).findList();
                        if(details.size() == 0){
                            AuctionDetail newDetail = new AuctionDetail();
                            newDetail.durableArticles = durableArticles;
                            newDetail.auction = auction;
                            newDetail.save();

                            if(auction.status == ExportStatus.SUCCESS){
                                durableArticles.status = SuppliesStatus.AUCTION;
                                durableArticles.save();
                            }
                        }
                    }
                    else{
                        System.out.println(id);
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found action id:" + id);
                result.put("status", "error");
            }
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result deleteAuctionDetail() {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        try {
            RequestBody body = request().body();
            JsonNode json = body.asJson();
            long id = Long.parseLong(json.get("id").asText());
            Auction auction = Auction.find.byId(id);
            if(auction != null){
                for (final JsonNode objNode : json.get("detail")) {
                    id = Long.parseLong(objNode.toString());
                    AuctionDetail detail = AuctionDetail.find.byId(id);
                    if(detail != null && auction.equals(detail.auction)){
                        if(detail.durableArticles.status == SuppliesStatus.AUCTION){
                            detail.durableArticles.status = SuppliesStatus.NORMAL;
                            detail.durableArticles.update();
                        }
                        detail.delete();
                    }
                }
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found auction id:" + id);
                result.put("status", "error");
            }
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result loadAuctionDetail(long id) {
        User user = User.find.byId(session().get("username"));
        if(!user.status.module3){
            return redirect(routes.Application.home());
        }
        ObjectNode result = Json.newObject();
        JsonNode json;
        try { 
            Auction auction = Auction.find.byId(id);
            if(auction != null){
                ObjectMapper mapper = new ObjectMapper();
                String jsonArray = mapper.writeValueAsString(auction.detail);
                json = Json.parse(jsonArray);
                result.put("details",json);
                result.put("status", "SUCCESS");
            }
            else{
                result.put("message","not Found action id:" + id);
                result.put("status", "error");
            }

        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }

}
