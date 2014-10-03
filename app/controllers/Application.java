package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;
import models.fsnNumber.FSN_Description;
import models.fsnNumber.FSN_Type;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Application extends Controller {

    public static Result index() {
        if(session().toString() != "{}"){
            return redirect(routes.Application.home());
        }
        return ok(login.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result home(){
        User user = User.find.byId(request().username());
    	return ok(home.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    public static Result user(){
    	return ok(user.render());
    }
    
    //@Security.Authenticated(Secured.class)
    public static Result authentication(){
    	Form<UserForm> userForm = Form.form(UserForm.class).bindFromRequest();
    	if(userForm.hasErrors()) {
            flash("unauthenticate", "Incorrect Username/Password.");
            return badRequest(login.render());
        }
        else {
            session().clear();
            session("username", userForm.get().username);
            for(int i=0; i<3; i++){
    			FSN_Type type = util.DataRandom.getRandomFsnType();
    			type.save();
    			FSN_Description d = util.DataRandom.getRandomFsnDescription();
    			d.save();
    			models.durableArticles.Procurement pa = util.DataRandom.getRandomArticleProcurement();
    			pa.save();
    			models.durableArticles.ProcurementDetail pad = util.DataRandom.getRandomArticleProcurementDetail(pa);
    			pad.save();
    			models.durableArticles.DurableArticles da = util.DataRandom.getRandomDurableArticles(pad);
    			da.save();
    			models.durableGoods.Procurement pg = util.DataRandom.getRandomGoodsProcurement();
    			pg.save();
    			models.durableGoods.ProcurementDetail pgd = util.DataRandom.getRandomGoodsProcurementDetail(pg);
    			pgd.save();
    			models.durableGoods.DurableGoods dg = util.DataRandom.getRandomDurableGoods(pgd);
    			dg.save();
    		}
            return redirect(routes.Application.home());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result editUser() {
        User user = User.find.byId(request().username());
          return ok(editUser.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveEditUser() {
        DynamicForm f = Form.form().bindFromRequest();
        User user = User.find.byId(request().username());
        Form<User> editUserForm = Form.form(User.class).bindFromRequest();
        User newUser = editUserForm.get();

        String rePassword = f.get("password");
        String rePassword2 = f.get("rePassword");
        String oldPassword = f.get("oldPassword");

        if(rePassword2.equals(rePassword) && oldPassword.equals(user.password)){
            System.out.println("hello");
            user.firstName = newUser.firstName;
            user.lastName = newUser.lastName;
            user.password = newUser.password;
            user.update();
            flash("success","Edit User Profile Complete!!!");
        }else{
            flash("fail"," กรุณาตรวจสอบข้อมูลให้ถูกต้อง ");
        }

        return ok(editUser.render(user));
    }
}
