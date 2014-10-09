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

public class ExportTransferIndide extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result exportTransferInside() {
        User user = User.find.byId(session().get("username"));
        List<InternalTransfer> initList = InternalTransfer.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<InternalTransfer> successList = InternalTransfer.find.where().eq("status", ExportStatus.SUCCESS).orderBy("id desc").findList();
        return ok(exportTransferInside.render(user, initList , successList));
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

}
