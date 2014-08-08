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

public class Application extends Controller {

    public static Result index() {
        return ok(login.render());
    }
    
    @Security.Authenticated(Secured.class)
    public static Result authentication(){
        return TODO;
    }

}
