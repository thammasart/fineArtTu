package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.RequestBody;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import javax.persistence.ManyToOne;
import javax.swing.JOptionPane;

import models.consumable.Procurement;
import models.durableArticles.DurableArticles;
import models.durableArticles.ProcurementDetail;
import models.durableArticles.AI_Committee;
import models.durableArticles.EO_Committee;

import models.durableGoods.DurableGoods;



import models.fsnNumber.*;
import models.type.ImportStatus;
import models.type.SuppliesStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;                                                                                         
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;  


public class Option extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result optionCalculateDepreciate() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<models.durableArticles.Procurement> p = models.durableArticles.Procurement.find.where().eq("status",ImportStatus.SUCCESS).findList();
        
        return ok(optionCalculateDepreciate.render(user,p));
    }
	
	@Security.Authenticated(Secured.class)
    public static Result optionCalculatingDepreciate() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        
        List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().eq("status",ImportStatus.SUCCESS).findList(); 
    
        for(models.durableArticles.Procurement p:ps)
        {
        	if(p.getCurrentYear() == p.yearStatus)
        	{
	        	for(models.durableArticles.ProcurementDetail pd:p.details)
	        	{
	        		pd.depreciationPrice = pd.depreciationPrice-pd.depreciationOfYear;
	        		System.out.println(pd.depreciationPrice);
	        		pd.update();
	        	}
	        	p.yearStatus++;
	        	p.update();
        	}
        	
        }
        
        /////////////////////////////////////////Testtttttttt
        for(models.durableArticles.Procurement p:ps)
        {
    	p.testDay++;
    	p.update();
        }
        /////////////////////////////////////////Testtttttttt
  
        
        
        /*
        List<models.durableArticles.DurableArticles> d = DurableArticles.find.all();
        for(models.durableArticles.DurableArticles ds:d)
        {
        	System.out.println(ds.getDepreciationPrice());
        	break;
        }
*/
        
        
        return ok(optionCalculateDepreciate.render(user,ps));
    }
	
	
	
	@Security.Authenticated(Secured.class)
    public static Result optionIncomingBalance() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(optionIncomingBalance.render(user));
    }
	
	@Security.Authenticated(Secured.class)
    public static Result optionChangeTax() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(optionChangeTax.render(user));
    }
	
	@Security.Authenticated(Secured.class)
    public static Result optionDoChangeTax() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(optionChangeTax.render(user));
    }
	
	
	
	
}
