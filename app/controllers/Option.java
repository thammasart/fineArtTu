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
import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.ManyToOne;
import javax.swing.JOptionPane;

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

//
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;



public class Option extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result optionCalculateDepreciate() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<models.durableArticles.Procurement> p = models.durableArticles.Procurement.find.where().eq("status",ImportStatus.SUCCESS).findList();
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        int year = localCalendar.get(Calendar.YEAR)+543;
        return ok(optionCalculateDepreciate.render(user,p,year));
    }
	
    @Security.Authenticated(Secured.class)
    public static Result optionCalculatingDepreciate() {
        DynamicForm f = Form.form().bindFromRequest();
        String temp = f.get("years");
        int year = 0;
        try{
            year = Integer.parseInt(temp);
        }catch(NumberFormatException e){
            Date dNow = new Date();
            year = dNow.getYear() + 2443 ;
        }
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        
        List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().eq("status",ImportStatus.SUCCESS).findList(); 
        
        return ok(optionCalculateDepreciate.render(user,ps,year));
    }
    @Security.Authenticated(Secured.class)
    public static Result optionCalculatingDepreciatePrint() {
        DynamicForm f = Form.form().bindFromRequest();
        String temp = f.get("yearsPrint");
        int year = Integer.parseInt(temp);
        
        List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().eq("status",ImportStatus.SUCCESS).findList(); 
        
        return ok(optionCalculateDepreciatePrint.render(ps,year));
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
	
	@Security.Authenticated(Secured.class)
    public static Result optionExportFile() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(optionExportFile.render(user));
    }
	
	@Security.Authenticated(Secured.class)
    public static Result optionExportAllItems() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        
        
        
        File f = new File("ExportData.txt");
        Writer out = null;
        try {
		 out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f), "UTF-8"));
		 
		 List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
		 List<models.durableGoods.Procurement> pgs = models.durableGoods.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
		 
		 for(models.durableArticles.Procurement p : ps)
		 {
			 for(models.durableArticles.ProcurementDetail pd : p.details)
			 {
				 for(models.durableArticles.DurableArticles d : pd.subDetails)
				 {
					 out.write("durableArticles,");	//type symbol
					 out.write(d.barCode+',');
					 out.write(d.department+',');
					 out.write(d.room+',');
					 out.write(d.floorLevel+',');
					 out.write(d.code+',');
					 out.write(d.title+',');
					 out.write(d.firstName+',');
					 out.write(d.lastName+',');
					 out.write(d.codeFromStock+',');
					 out.write(d.detail.description+',');
					 out.write(d.detail.procurement.title+',');
					 out.write(d.detail.procurement.budgetType+',');
					 out.write(d.detail.procurement.budgetYear+"\n");	
				 }
			 }
		 }
		 
		 
		 
		 for(models.durableGoods.Procurement p : pgs)
		 {
			 for(models.durableGoods.ProcurementDetail pd : p.details)
			 {
				 if(pd.typeOfDurableGoods == 1){
					 for(models.durableGoods.DurableGoods d : pd.subDetails)
					 {
						 out.write("Goods,");	//type symbol
						 out.write(d.barCode+',');
						 out.write(d.department+',');
						 out.write(d.room+',');
						 out.write(d.floorLevel+',');
						 out.write(d.codes+',');
						 out.write(d.title+',');
						 out.write(d.firstName+',');
						 out.write(d.lastName+',');
						 out.write("null,");
						 out.write(d.detail.description+',');
						 out.write(d.detail.procurement.title+',');
						 out.write(d.detail.procurement.budgetType+',');
						 out.write(d.detail.procurement.budgetYear+"\n");	
					 }
				 }
			 }
		 }
		 

		 
		 for(models.durableArticles.Procurement p:ps)
		 {
			 out.write("ArticleProcurement,");	//type symbol
			 out.write(p.barCode+',');
			 out.write(p.title+',');
			 out.write(p.contractNo+',');
			 out.write(p.getAddDate()+',');
			 out.write(p.getCheckDate()+',');
			 out.write(p.budgetType+',');
			 out.write(p.company.nameEntrepreneur+',');
			 out.write(p.budgetYear+"\n");			 
		 }
		 
		
		 for(models.durableGoods.Procurement p:pgs)
		 {
			 out.write("GoodProcurement,");	//type symbol
			 out.write(p.barCode+',');
			 out.write(p.title+',');
			 out.write(p.contractNo+',');
			 out.write(p.getAddDate()+',');
			 out.write(p.getCheckDate()+',');
			 out.write(p.budgetType+',');
			 out.write(p.company.nameEntrepreneur+',');
			 out.write(p.budgetYear+"\n");			 
		 }
		 
			out.close();
		}catch(IOException e){
			System.out.println("file not found ");
		}
        
        return ok(f);
    }
	
	
	


	
	
}
