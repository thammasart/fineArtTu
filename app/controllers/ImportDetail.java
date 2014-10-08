package controllers;

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

public class ImportDetail extends Controller {
	
	@Security.Authenticated(Secured.class)
    public static Result importsInstituteDetail(long id) {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        
        Company c = Company.find.byId(id);
        
        return ok(importsInstituteDetail.render(user,c));
    }
	
	
	
	 public static Result saveNewInstitute(){
		 
		 	
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	FilePart filePart = body.getFile("attachFile");
	    	if (filePart != null) {
				String fileName = filePart.getFilename();
				String contentType = filePart.getContentType(); 
				File file = filePart.getFile();		
				//save file to new path
			} else {
				flash("error", "Missing file");
			}
	    	
	    	////////////////////////////////////////////////////////////////////////////////////
	        DynamicForm form = Form.form().bindFromRequest();

	        String typeDurableArticles="";                                  //save all list
	        String typeDurableGoods="";
	        String typeConsumableGoods="";

	            for(int i=1;i<=17;i++)
	            {
	                String dA = form.get("durableArticlesType"+i);          //save 1 list
	                String dG = form.get("durableGoodsType"+i);
	                String cG = form.get("consumableGoodsType"+i);

	                if(dA!=null)
	                {
	                    typeDurableArticles=typeDurableArticles+dA+",";    //process to save all list
	                }
	                if(dG!=null)
	                {
	                    typeDurableGoods=typeDurableGoods+dG+",";
	                }
	                if(cG!=null)
	                {
	                    typeConsumableGoods=typeConsumableGoods+cG+",";
	                }
	            }

	        
	        typeDurableArticles = typeDurableArticles.length() > 0 ? typeDurableArticles.substring(0,typeDurableArticles.length()-1) : "";
	        typeDurableGoods = typeDurableGoods.length() > 0 ? typeDurableGoods.substring(0,typeDurableGoods.length()-1) : "";
	        typeConsumableGoods = typeConsumableGoods.length() > 0 ? typeConsumableGoods.substring(0,typeConsumableGoods.length()-1) : "";
	        
	        
	        
	        Company c = Company.find.byId(Long.parseLong(form.get("id")));
  

	        if(form.get("payPeriod2").equals(""))
	            c.payPeriod=0;
	        else
	            c.payPeriod=Integer.parseInt(form.get("payPeriod2"));
	        if(form.get("sendPeriod2").equals(""))
	            c.sendPeriod=0;
	        else
	            c.sendPeriod=Integer.parseInt(form.get("sendPeriod2"));

	        c.durableArticlesType = typeDurableArticles;
	        c.durableGoodsType = typeDurableGoods;
	        c.consumableGoodsType = typeConsumableGoods;

	        
	        c.address.buildingNo=form.get("buildingNo");
	        c.address.village=form.get("village");
	        c.address.alley=form.get("alley");
	        c.address.road=form.get("road");
	        c.address.parish=form.get("parish");
	        c.address.district=form.get("district");
	        c.address.province=form.get("province");
	        c.address.telephoneNumber=form.get("telephoneNumber");
	        c.address.fax=form.get("fax");
	        c.address.postCode=form.get("postCode");
	        c.address.email=form.get("email");
	        
	        c.address.update();
	        
	    	
	    	c.typeEntrepreneur =form.get("typeEntrepreneur"); 
	    	c.typedealer =form.get("typedealer"); 
	    	c.nameEntrepreneur =form.get("nameEntrepreneur"); 
	    	c.nameDealer =form.get("nameDealer"); 
	    	c.payCodition =form.get("payCodition"); 
	    	c.otherDetail = form.get("otherDetail");

	        c.update();



	        return redirect(routes.Import.importsInstitute());
	    }

	 @Security.Authenticated(Secured.class)
	 public static Result importsMaterialDurableArticlesDetail(String id)
	 {
		User user = User.find.where().eq("username", session().get("username")).findUnique();
		List<FSN_Type> groupType = FSN_Type.find.all(); 
		FSN_Description fsn = FSN_Description.find.byId(id);
		
		System.out.println(fsn.descriptionId);
		System.out.println(fsn.typ.typeId);
		
		return ok(importsMaterialDurableArticlesDetail.render(groupType,user,fsn));
	 }
	 
	 
	 public static Result saveNewMaterialDurableArticles(){
		 DynamicForm form = Form.form().bindFromRequest();
		 
		 FSN_Description fd = FSN_Description.find.byId(form.get("descriptionId"));
		 fd.otherDetail = form.get("otherDetail");
		 
		 fd.update();
		 
		 return redirect(routes.Import.importsMaterial());
		 
	 }
}
