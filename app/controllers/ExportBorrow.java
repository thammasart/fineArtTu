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

public class ExportBorrow extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result exportBorrow() {
        User user = User.find.byId(session().get("username"));
        List<Borrow> initList = Borrow.find.where().eq("status", ExportStatus.INIT).orderBy("id desc").findList();
        List<Borrow> successList = Borrow.find.where().eq("status", ExportStatus.REPAIRING).orderBy("id desc").findList();
        return ok(exportBorrow.render(user,initList, successList));
    }

    @Security.Authenticated(Secured.class)
    public static Result exportCreateBorrow() {
        Borrow temp =  new Borrow();
        temp.startBorrow = new Date();
        temp.status = ExportStatus.INIT;
        temp.save();
        return redirect(routes.ExportBorrow.exporBorrowAdd(temp.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result exporBorrowAdd(long id) {
        User user = User.find.byId(session().get("username"));
        Borrow repair = Borrow.find.byId(id);
        if(repair == null){
            return redirect(routes.ExportBorrow.exportBorrow());
        }
        return ok(exportBorrowAdd.render(user, repair));
    }
}