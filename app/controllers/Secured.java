package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
    	String username = ctx.session().get("username");
        if(username != null){
            User user = User.find.byId(username);
            if(user == null){
                ctx.session().clear();
            }
        }
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.index());
    }
}