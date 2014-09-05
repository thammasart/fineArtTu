package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.RequestBody;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Graph extends Controller {

    public static Result index() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(graph.render(user));
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getData() {
      RequestBody body = request().body();
      ObjectNode result = Json.newObject();
      result.put("name","Untitled");
      result.put("last","Titled");
      return ok(body.asJson());
    }

}
