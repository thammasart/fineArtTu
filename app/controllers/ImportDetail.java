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
        if(!user.isPermit(6))return ok(permissionDenied.render());
        Company c = Company.find.byId(id);
        
        return ok(importsInstituteDetail.render(user,c));
    }
	
	
	
	 public static Result saveNewInstitute(){
		 
		 
	    	
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


	    	
			 MultipartFormData body = request().body().asMultipartFormData();
		    	FilePart filePart = body.getFile("attachFile");
				String fileName="";
				String contentType=""; 
				//File file = null;
		    	if (filePart != null) {
					fileName = filePart.getFilename();
					contentType = filePart.getContentType(); 	
			        File file = new File("./public/"+c.path);		//get file------------------------------------------------
					file.delete();									//delete file---------------------------------------------
					
					//write file
					String[] extension=fileName.split("\\.");
					String targetPath = "./public/images/company/" + c.id;
					c.path ="images/company/" + c.id;
					if(extension.length>1)
					{
						targetPath += "." + extension[((extension.length)-1)];
						c.path+="." + extension[((extension.length)-1)];	
						
					}
					c.fileName = fileName;
					filePart.getFile().renameTo(new File(targetPath));
					c.fileType = contentType; 
			        //end write file
				}
	    	
		    c.update();
		    	
	        return redirect(routes.Import.importsInstitute());
	    }

	 

	 
	 @Security.Authenticated(Secured.class)
	 public static Result importsMaterialDurableArticlesDetail(String id)
	 {
		User user = User.find.where().eq("username", session().get("username")).findUnique();
		if(!user.isPermit(6))return ok(permissionDenied.render());
		FSN_Description fsn = FSN_Description.find.byId(id);
		
		System.out.println(fsn.descriptionId);
		System.out.println(fsn.typ.typeId);
		
		return ok(importsMaterialDurableArticlesDetail.render(user,fsn));
	 }
	 
	 
	 public static Result saveNewMaterialDurableArticles(){
		 DynamicForm form = Form.form().bindFromRequest();
		 
		 FSN_Description fd = FSN_Description.find.byId(form.get("descriptionId"));
		 fd.otherDetail = form.get("otherDetail");
		 fd.classifier = form.get("classifier");
		 fd.typ.typeDescription = form.get("typeDescription");
		 fd.descriptionDescription = form.get("descriptionDescription");
		 
		 MultipartFormData body = request().body().asMultipartFormData();
	    	FilePart filePart = body.getFile("attachFile");
			String fileName="";
			String contentType=""; 
			//File file = null;
	    	if (filePart != null) {
				fileName = filePart.getFilename();
				contentType = filePart.getContentType(); 	
		        File file = new File("./public/"+fd.path);		//get file------------------------------------------------
				file.delete();									//delete file---------------------------------------------
				
				//write file
				String[] extension=fileName.split("\\.");
				String targetPath = "./public/images/fsnNumber/" + fd.descriptionId;
				fd.path ="images/fsnNumber/" + fd.descriptionId;
				if(extension.length>1)
				{
					targetPath += "." + extension[((extension.length)-1)];
					fd.path+="." + extension[((extension.length)-1)];	
					
				}
				fd.fileName = fileName;
				filePart.getFile().renameTo(new File(targetPath));
				fd.fileType = contentType; 
		        //end write file
			}
	    	
	    fd.typ.update();
	    fd.update();
		 
		 
		 return redirect(routes.Import.importsMaterial2("1"));
		 
	 }

	 @Security.Authenticated(Secured.class)
	 public static Result importsMaterialConsumableGoodsDetail(String id)
	 {
		User user = User.find.where().eq("username", session().get("username")).findUnique();
		if(!user.isPermit(6))return ok(permissionDenied.render());
		MaterialCode code = MaterialCode.find.byId(id);
		
		System.out.println(code.code);
		
		
		return ok(importsMaterialConsumableGoodsDetail.render(user,code));
	 }
	 
	 public static Result saveNewMaterialConsumableGoods(){
		 DynamicForm form = Form.form().bindFromRequest();

		 MaterialCode mc = MaterialCode.find.byId(form.get("code"));
		 mc.otherDetail = form.get("otherDetail");
		 mc.classifier = form.get("classifier");
		 mc.description  = form.get("description");
		 mc.minNumberToAlert = Integer.parseInt(form.get("minNumberToAlert"));

		 
		 
		 
		 MultipartFormData body = request().body().asMultipartFormData();
	    	FilePart filePart = body.getFile("attachFile");
			String fileName="";
			String contentType=""; 
			//File file = null;
	    	if (filePart != null) {
				fileName = filePart.getFilename();
				contentType = filePart.getContentType(); 	
		        File file = new File("./public/"+mc.path);		//get file------------------------------------------------
				file.delete();									//delete file---------------------------------------------
				
				//write file
				String[] extension=fileName.split("\\.");
				String targetPath = "./public/images/goodsNumber/" + mc.code;
				mc.path ="images/goodsNumber/" + mc.code;
				if(extension.length>1)
				{
					targetPath += "." + extension[((extension.length)-1)];
					mc.path+="." + extension[((extension.length)-1)];	
					
				}
				mc.fileName = fileName;
				filePart.getFile().renameTo(new File(targetPath));
				mc.fileType = contentType; 
		        //end write file
			}
		 
		 mc.update();
		 
		 return redirect(routes.Import.importsMaterial2("2"));
		 
	 }
	 
	 ////////////////////////////////////////////////////////////////////////////////
	 
}
