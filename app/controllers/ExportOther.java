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

public class ExportOther extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result exportOther() {
        User user = User.find.byId(session().get("username"));
        List<OtherTransfer> initList = OtherTransfer.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<OtherTransfer> successList = OtherTransfer.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok( exportOther.render(user, initList, successList));
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

    @Security.Authenticated(Secured.class)
    public static Result saveOther(long id){
        /*
        System.out.println(" save donate");

        User user = User.find.byId(session().get("username"));
        Donation donate = Donation.find.byId(donationId);

        DynamicForm f = Form.form().bindFromRequest();
        donate.title = f.get("title");
        donate.contractNo = f.get("contractNo");
        donate.setApproveDate(f.get("approveDate"));
        donate.status = ExportStatus.SUCCESS;
        donate.update();

        System.out.println(donate.id + " " + donate.title);*/

        return redirect(routes.ExportDonate.exportDonate());
    }


}