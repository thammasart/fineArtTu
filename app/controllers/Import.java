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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import javax.imageio.ImageIO;
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
import models.type.OrderDetailStatus;
import models.type.SuppliesStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;                                                                                         
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;  

public class Import extends Controller {
	

 	@Security.Authenticated(Secured.class)
    public static Result imports() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(imports.render(user));
    }

    

    @Security.Authenticated(Secured.class)
    public static Result importsInstitute() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<Company> institutes = Company.find.all(); 
        return ok(importsInstitute.render(institutes,user));
    }

    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
	public static Result retriveInstituteDescription(){
    	RequestBody body = request().body();
    	JsonNode json = body.asJson();
    	ObjectNode result = Json.newObject();
    	
    	Company c = Company.find.byId(Long.parseLong(json.get("id").asText()));
    	
    	result.put("typeEntrepreneur", c.typeEntrepreneur);
    	result.put("typedealer", c.typedealer);
    	result.put("nameEntrepreneur", c.nameEntrepreneur);
    	result.put("nameDealer", c.nameDealer);
    	result.put("payCodition", c.payCodition);
    	result.put("payPeriod", c.payPeriod);
    	result.put("sendPeriod", c.sendPeriod);
    	result.put("durableArticlesType", c.durableArticlesType);
    	result.put("durableGoodsType", c.durableGoodsType);
    	result.put("buildingNo", c.address.buildingNo);
    	result.put("village", c.address.village);
    	result.put("alley", c.address.alley);
    	result.put("road", c.address.road);
    	result.put("parish", c.address.parish);
    	result.put("district", c.address.district);
    	result.put("province", c.address.province);
    	result.put("telephoneNumber", c.address.telephoneNumber);
    	result.put("fax", c.address.fax);
    	result.put("postCode", c.address.postCode);
    	result.put("email", c.address.email);
    	return ok(result);
	}



	@Security.Authenticated(Secured.class)
    public static Result importsInstituteAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsInstituteAdd.render(user));
    }

	@Security.Authenticated(Secured.class)
    public static Result saveNewInstitute(){
		
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


        Form<Company> newInstituteFrom = Form.form(Company.class).bindFromRequest();
        Form<Address> newAddressFrom = Form.form(Address.class).bindFromRequest();
        
        Company newInstitute = newInstituteFrom.get();

        if(form.get("payPeriod2").equals(""))
            newInstitute.payPeriod=0;
        else
            newInstitute.payPeriod=Integer.parseInt(form.get("payPeriod2"));
        if(form.get("sendPeriod2").equals(""))
            newInstitute.sendPeriod=0;
        else
            newInstitute.sendPeriod=Integer.parseInt(form.get("sendPeriod2"));

        newInstitute.durableArticlesType = typeDurableArticles;
        newInstitute.durableGoodsType = typeDurableGoods;
        newInstitute.consumableGoodsType = typeConsumableGoods;


        
        Address newInstituteaddress = newAddressFrom.get();
        newInstituteaddress.save();
        newInstitute.address = newInstituteaddress;
        newInstitute.save();

        
        ////////////////////////////////////////////////////////////////////////////file
        MultipartFormData body = request().body().asMultipartFormData();
    	FilePart filePart = body.getFile("attachFile");

        	
    		String fileName="";
    		String contentType=""; 
    		//File file = null;
        	if (filePart != null) //กรณีมีไฟล์
        	{
    			fileName = filePart.getFilename();
    			contentType = filePart.getContentType(); 

			 	newInstitute.fileName = fileName;//get origin name
			 
			 
			 	String[] extension=fileName.split("\\.");							//split .
				String targetPath = "./public/images/company/" + newInstitute.id;
				newInstitute.path ="images/company/" + newInstitute.id;
				if(extension.length>1)
				{
					targetPath += "." + extension[((extension.length)-1)];				//get type file by last spilt
					newInstitute.path+="." + extension[((extension.length)-1)];	
					
				}
				newInstitute.fileName = fileName;										//get traditional file name
				
				filePart.getFile().renameTo(new File(targetPath));						//save file on your path
				newInstitute.fileType = contentType; 
		        //end write file
				newInstitute.update();
    			
    		} 

        return redirect(routes.Import.importsInstitute());
    }
	@Security.Authenticated(Secured.class)
    public static Result removeInstitute(){
    	DynamicForm form = Form.form().bindFromRequest();
    	Company company;
    	
        if(!form.get("institutesTickList").equals("")){
    	String[] institutes = form.get("institutesTickList").split(",");		//แบ่งidเพื่อเข้าloop
    		
    	int del=0;
    	int cantDel=0;
    	
        
            for(int i=0;i<institutes.length;i++){
            		company = Company.find.byId(Long.parseLong(institutes[i]));
            		int x=0;
            		
            		x+= models.durableArticles.Procurement.find.where().eq("company",company).findRowCount();
            		x+= models.durableGoods.Procurement.find.where().eq("company",company).findRowCount();
            		
            		
            		if(x==0)
            		{
            			del++;
            			company.delete();
            			
            			File file = new File("./public/"+company.path);		//get file------------------------------------------------
            			file.delete();										//delete file---------------------------------------------
            			
            		}
            		else
            		{
            			cantDel++;
            		}
            }
            if(del!=0)
            {
            	flash("delete1","ลบสถานประกอบการทั้งหมด " + del +" รายการ ");
            }
            if(cantDel!=0)
            {
            	flash("cantdelete1","ไม่สามารถลบสถานประกอบการได้ " + cantDel +" รายการ เนื่องจากสถานประกอบการเหล่านี้ได้ถูกใช้งานอยู่ในระบบ");	
            }
        } else flash("notSelect","เลือกสถานประกอบการที่ต้องการจะลบ");

    	return redirect(routes.Import.importsInstitute());
    }
    

    //----------------------------------------------------------------------------------------------------


    @Security.Authenticated(Secured.class)
        public static Result importsMaterial() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<FSN_Description> fsns = FSN_Description.find.all();                    //ครุภัณฑ์
        List<MaterialCode> goodsCode = MaterialCode.find.all();                            //วัสดุ
        return ok(importsMaterial.render(fsns,goodsCode,user,"1"));
    }
    
    @Security.Authenticated(Secured.class)
	    public static Result importsMaterial2(String tab) {
	    User user = User.find.where().eq("username", session().get("username")).findUnique();
	    List<FSN_Description> fsns = FSN_Description.find.all();                    //ครุภัณฑ์
	    List<MaterialCode> goodsCode = MaterialCode.find.all();                            //วัสดุ
	    return ok(importsMaterial.render(fsns,goodsCode,user,tab));
	}

    @Security.Authenticated(Secured.class)
        public static Result importsMaterialDurableArticlesAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialDurableArticlesAdd.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveNewMaterialDurableArticles(){

        DynamicForm form = Form.form().bindFromRequest();

        String gId = form.get("groupId");
        String gD = form.get("groupDescription");

        String cId = form.get("classId");
        String cD = form.get("classDescription");

        String tId = form.get("typeId");
        String tD = form.get("typeDescription"); 

        String fsnId = form.get("descriptionId");
        String fsnDes = form.get("descriptionDescription");

        //System.out.println( gId +"\n"+ gD +"\n"+ cId +"\n"+ cD +"\n"+ tId +"\n"+ tD +"\n"+ fsnId +"\n"+ fsnDes );

        Form<FSN_Description> newFsnForm = Form.form(FSN_Description.class).bindFromRequest();
        FSN_Description newFsn = newFsnForm.get();

        newFsn.descriptionId = fsnId;

        FSN_Type type = FSN_Type.find.byId(tId);

        if(type == null){
            type = new FSN_Type();
            type.typeId = tId;
            type.typeDescription = tD;
            type.groupClass = FSN_Class.find.byId(cId);
            type.save();
        }

        newFsn.typ = type;

        newFsn.save();
        
        
        ////////////////////////////////////////////////////////////////////////////file
        
        MultipartFormData body = request().body().asMultipartFormData();
    	FilePart filePart = body.getFile("attachFile");

        	
    		String fileName="";
    		String contentType=""; 
    		//File file = null;
        	if (filePart != null) //กรณีมีไฟล์
        	{
    			fileName = filePart.getFilename();
    			contentType = filePart.getContentType(); 

    			newFsn.fileName = fileName;//get origin name
			 
			 
			 	String[] extension=fileName.split("\\.");							//split .
				String targetPath = "./public/images/fsnNumber/" + newFsn.descriptionId;
				newFsn.path ="images/fsnNumber/" + newFsn.descriptionId;
				if(extension.length>1)
				{
					targetPath += "." + extension[((extension.length)-1)];				//get type file by last spilt
					newFsn.path+="." + extension[((extension.length)-1)];	
					
				}
				newFsn.fileName = fileName;										//get traditional file name
				
				filePart.getFile().renameTo(new File(targetPath));						//save file on your path
				newFsn.fileType = contentType; 
		        //end write file
				newFsn.update();
    			
    		} 
        ////////////////////////////////////////////////////////////////////////////end file

        return redirect(routes.Import.importsMaterial2("1"));
    }   


    //----------------------------------------------------------------------------------------------------

    @Security.Authenticated(Secured.class)
        public static Result importsMaterialConsumableGoodsAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialConsumableGoodsAdd.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveNewMaterialConsumableGoods(){
    	String tab = "";
        String code = "";
        DynamicForm form = Form.form().bindFromRequest();

        Form<MaterialCode> newCodeForm = Form.form(MaterialCode.class).bindFromRequest();
        MaterialCode newCode = newCodeForm.get();



            //newCode.typeOfGood = "วัสดุสิ้นเปลือง";
            tab = "2";
        
        code = newCode.code;
        code = Character.toString(code.charAt(0))+Character.toString(code.charAt(1));
        //System.out.println(code);
        newCode.materialType = MaterialType.find.byId(code);   //connect link
        //newCode.materialType = code[0]+code[1];

        newCode.save();
        
        ////////////////////////////////////////////////////////////////////////////file
        
        MultipartFormData body = request().body().asMultipartFormData();
    	FilePart filePart = body.getFile("attachFile");

        	
    		String fileName="";
    		String contentType=""; 
    		//File file = null;
        	if (filePart != null) //กรณีมีไฟล์
        	{
    			fileName = filePart.getFilename();
    			contentType = filePart.getContentType(); 

    			newCode.fileName = fileName;//get origin name
			 
			 
			 	String[] extension=fileName.split("\\.");							//split .
				String targetPath = "./public/images/goodsNumber/" + newCode.code;
				newCode.path ="images/goodsNumber/" + newCode.code;
				if(extension.length>1)
				{
					targetPath += "." + extension[((extension.length)-1)];				//get type file by last spilt
					newCode.path+="." + extension[((extension.length)-1)];	
					
				}
				newCode.fileName = fileName;										//get traditional file name
				
				filePart.getFile().renameTo(new File(targetPath));						//save file on your path
				newCode.fileType = contentType; 
		        //end write file
				newCode.update();
    			
    		} 
        ////////////////////////////////////////////////////////////////////////////end file

        return redirect(routes.Import.importsMaterial2(tab));
    }
    
    
    @Security.Authenticated(Secured.class)
	public static Result removeFSNCode(){
	DynamicForm form = Form.form().bindFromRequest();
	FSN_Description fsnCode;
	
	 if(!form.get("materialCodeTickList").equals("")){
		 String[] fsn = form.get("materialCodeTickList").split(",");
		
		 int del=0;
		 int cantDel=0;
         for(int i=0;i<fsn.length;i++){
        	 fsnCode = FSN_Description.find.byId(fsn[i]);
        	 int x = ProcurementDetail.find.where().eq("fsn", fsnCode).findRowCount();
        	 
        	 if(x==0)
        	 {
        		 fsnCode.delete();
        		 del++;
        		 
        		File file = new File("./public/"+fsnCode.path);		//get file------------------------------------------------
     			file.delete();										//delete file---------------------------------------------
        	 }
        	 else
        	 {
        		 cantDel++;
        	 }

         }
         if(del!=0)
        	 flash("delete1","ลบรหัส FSN ทั้งหมด " + del +" รายการ ");
         if(cantDel!=0)
        	 flash("cantdelete1","ไม่สามารถลบรหัส FSN ได้ " + cantDel +" รายการ เนื่องจากรหัส FSN เหล่านี้ได้ถูกใช้งานอยู่ในระบบ");
	 }else flash("notSelect1","เลือกรหัส FSN ที่ต้องการจะลบ");
	
	return redirect(routes.Import.importsMaterial2("1"));
    }
    
    @Security.Authenticated(Secured.class)
	public static Result removeCode(){
    DynamicForm form = Form.form().bindFromRequest();
    	MaterialCode code=null;
    	String tab = "";
    	
    	String type = form.get("type");
    	
    	if(!form.get("materialCodeTickList").equals("")){
    		String[] codeInList = form.get("materialCodeTickList").split(",");
    		
	   		 int del=0;
	   		 int cantDel=0;
	   		for(int i=0;i<codeInList.length;i++){
	   			code = MaterialCode.find.byId(codeInList[i]);
	   			
	   			int x = models.durableGoods.ProcurementDetail.find.where().eq("code", code.code).findRowCount();
	   			
	   			if(x==0)
	   			{
		   			code.delete();
		   			del++;
		   			
	        		File file = new File("./public/"+code.path);		//get file------------------------------------------------
	     			file.delete();										//delete file---------------------------------------------
	   			}
	   			else
	   			{
	   				cantDel++;
	   			}
	   		}
	   		
	   			


	   		 if(del!=0)
	             flash("delete2","ลบรหัสวัสดุทั้งหมด " + del +" รายการ ");
             if(cantDel!=0)
	             flash("cantdelete2","ไม่สามารถลบรหัสวัสดุได้ " + cantDel +" รายการ เนื่องจากรหัสวัสดุเหล่านี้ได้ถูกใช้งานอยู่ในระบบ");
             tab = "2";
	   		
	   		
	   		 
    	}
    	else{
    		//System.out.println(type);
    		
        		tab="2";
        		flash("notSelect2","เลือกรหัสวัสดุที่ต้องการจะลบ");
    		
    	}
    	
    	return redirect(routes.Import.importsMaterial2(tab));
    }

    //----------------------------------------------------------------------------------------------------
    @Security.Authenticated(Secured.class)
        public static Result importsOrder() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        //List<models.durableArticles.Procurement> aProcurement = models.durableArticles.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
        List<models.durableArticles.Procurement> aProcurement = models.durableArticles.Procurement.find.all();
        //List<models.durableGoods.Procurement> gProcurement = models.durableGoods.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
        List<models.durableGoods.Procurement> gProcurement = models.durableGoods.Procurement.find.all();
        
        return ok(importsOrder.render(aProcurement,gProcurement,user,"1"));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result importsOrder2(String tab) {
	    User user = User.find.where().eq("username", session().get("username")).findUnique();

	    //List<models.durableArticles.Procurement> aProcurement = models.durableArticles.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
	    List<models.durableArticles.Procurement> aProcurement = models.durableArticles.Procurement.find.all();
	    //List<models.durableGoods.Procurement> gProcurement = models.durableGoods.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
	    List<models.durableGoods.Procurement> gProcurement = models.durableGoods.Procurement.find.all();
    
	    return ok(importsOrder.render(aProcurement,gProcurement,user,tab));
    }
    
    @Security.Authenticated(Secured.class)
	public static Result createImportsOrderDurableArticlesAdd() {
	    User user = User.find.where().eq("username", session().get("username")).findUnique();
	    models.durableArticles.Procurement order= new models.durableArticles.Procurement();
	    order.status = ImportStatus.INIT;
	    order.save();
	    return redirect(routes.Import.importsOrderDurableArticlesAdd(order.id));
	}
    
    @Security.Authenticated(Secured.class)
    public static Result importsOrderDurableArticlesAdd(Long id) {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        models.durableArticles.Procurement order = models.durableArticles.Procurement.find.byId(id);
        List<Company> companies = Company.find.all();
        return ok(importsOrderDurableArticlesAdd.render(order,user,companies));
    }
    
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
	public static Result retriveProcurement(){
    	RequestBody body = request().body();
    	JsonNode json = body.asJson();
    	ArrayNode aiArray = JsonNodeFactory.instance.arrayNode();
    	ArrayNode eoArray = JsonNodeFactory.instance.arrayNode();
    	ObjectNode result = Json.newObject();
    	if(json.get("tab").asText().equals("1")){
    		models.durableArticles.Procurement pa = models.durableArticles.Procurement.find.byId(Long.parseLong(json.get("id").asText()));
    		for(models.durableArticles.AI_Committee ai : pa.aiCommittee){
    			ArrayNode aiItem = JsonNodeFactory.instance.arrayNode();
    			
    			if(ai.committee==null)
    			{
    			aiItem.add("");
    			aiItem.add("");
    			aiItem.add("");
    			aiItem.add("");
    			aiItem.add("");
    			aiItem.add("");
    			}
    			else
    			{
    			aiItem.add(ai.committee.namePrefix);
    			aiItem.add(ai.committee.firstName);
    			aiItem.add(ai.committee.lastName);
    			aiItem.add(ai.committee.position);
    			aiItem.add(ai.employeesType);
    			aiItem.add(ai.committeePosition);
    			}
    			aiArray.add(aiItem);
    		}
    		result.put("ai", aiArray);
    		
    		for(models.durableArticles.EO_Committee eo : pa.eoCommittee){
    			ArrayNode eoItem = JsonNodeFactory.instance.arrayNode();
    			
    			if(eo.committee==null)
    			{
    			eoItem.add("");
    			eoItem.add("");
    			eoItem.add("");
    			eoItem.add("");
    			eoItem.add("");
    			eoItem.add("");
    			}
    			else
    			{
    			eoItem.add(eo.committee.namePrefix);
    			eoItem.add(eo.committee.firstName);
    			eoItem.add(eo.committee.lastName);
    			eoItem.add(eo.committee.position);
    			eoItem.add(eo.employeesType);
    			//System.out.println(eo.employeesType);
    			//System.out.println(eo.committeePosition);
    			eoItem.add(eo.committeePosition);
    			}
    			eoArray.add(eoItem);
    		}
    		result.put("eo", eoArray);
    		List<models.durableArticles.ProcurementDetail> procurementDetails = models.durableArticles.ProcurementDetail.find.where().eq("procurement", pa).findList(); 
    		ArrayNode jsonProcurementDetails = JsonNodeFactory.instance.arrayNode();
    		for(models.durableArticles.ProcurementDetail p : procurementDetails){
    			if(p.status!=OrderDetailStatus.DELETE)
    			{
	    			ObjectNode item = Json.newObject();
	    			item.put("id", p.id);
	    			if(p.fsn != null) item.put("fsn", p.fsn.descriptionId);
	    			else item.put("fsn", "null");
	    			item.put("description", p.description);
	    			item.put("quantity", p.quantity);
	    			item.put("classifier", "อัน");
	    			item.put("price", p.price);
	    			item.put("priceNoVat", p.priceNoVat);
	    			item.put("lifeTime", p.llifeTime);
	    			item.put("fileName", p.fsn.fileName);
	        		item.put("fileType", p.fsn.fileType);
	        		item.put("path", p.fsn.path);
	    			jsonProcurementDetails.add(item);
    			}
    		}
    		result.put("data",jsonProcurementDetails);
    	}else if(json.get("tab").asText().equals("2")){
    		models.durableGoods.Procurement pg = models.durableGoods.Procurement.find.byId(Long.parseLong(json.get("id").asText()));
    		for(models.durableGoods.AI_Committee ai : pg.aiCommittee){
    			ArrayNode aiItem = JsonNodeFactory.instance.arrayNode();
    			if(ai.committee==null)
    			{
    			aiItem.add("");
    			aiItem.add("");
    			aiItem.add("");
    			aiItem.add("");
    			aiItem.add("");
    			aiItem.add("");
    			}
    			else
    			{
    			aiItem.add(ai.committee.namePrefix);
    			aiItem.add(ai.committee.firstName);
    			aiItem.add(ai.committee.lastName);
    			aiItem.add(ai.committee.position);
    			aiItem.add(ai.employeesType);
    			aiItem.add(ai.committeePosition);
    			}
    			aiArray.add(aiItem);
    		}
    		result.put("ai", aiArray);
    		
    		List<models.durableGoods.ProcurementDetail> procurementDetails = models.durableGoods.ProcurementDetail.find.where().eq("procurement", pg).findList(); 
    		ArrayNode jsonProcurementDetails = JsonNodeFactory.instance.arrayNode();
    		for(models.durableGoods.ProcurementDetail p : procurementDetails){
    			
    			if(p.status!=OrderDetailStatus.DELETE)
    			{
    			
	    			FSN_Description fsnCode=null;
	        		MaterialCode consumableGoodCode=null;
	        		
	        		if(p.typeOfDurableGoods==1)//is a durableGood
	        			fsnCode = FSN_Description.find.byId(p.code);
	     
	        		else
	        			consumableGoodCode= MaterialCode.find.byId(p.code);
	    			
	    			ObjectNode item = Json.newObject();
	    			item.put("typeOfGood", p.typeOfDurableGoods);
	        		item.put("id", p.id);
	        		if(p.code!="") item.put("code", p.code);
	        		else item.put("code", "null");
	        		item.put("description", p.description);
	        		item.put("quantity", p.quantity);
	        		item.put("classifier", "อัน");
	        		item.put("price", p.price);
	        		item.put("priceNoVat", p.priceNoVat);
	        		if(p.typeOfDurableGoods==1)//is a durableGood
	        		{
	        			item.put("fileName", fsnCode.fileName);
	        			item.put("fileType", fsnCode.fileType);
	        			item.put("path", fsnCode.path);
	        		}
	        		else
	        		{
	        			item.put("fileName", consumableGoodCode.fileName);
	        			item.put("fileType", consumableGoodCode.fileType);
	        			item.put("path", consumableGoodCode.path);
	        		}
    			
    			jsonProcurementDetails.add(item);
    			}
    		}
    		result.put("data",jsonProcurementDetails);

    	}
    	
    	
    	return ok(result);
	}
    
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
	public static Result retriveProcurementDetail(){
    	RequestBody body = request().body();
    	JsonNode json = body.asJson();
    	ObjectNode result = Json.newObject();
    	if(json.get("tab").asText().equals("1")){
    		ArrayNode subDetail = JsonNodeFactory.instance.arrayNode();
    		
    		
    		models.durableArticles.ProcurementDetail pad = models.durableArticles.ProcurementDetail.find.byId(Long.parseLong(json.get("id").asText()));
    		
    		if(pad.status!=OrderDetailStatus.DELETE)
    		{
	    		for(models.durableArticles.DurableArticles d : pad.subDetails){
	    			ArrayNode subDetailItem = JsonNodeFactory.instance.arrayNode();
	    			subDetailItem.add(d.department);
	    			subDetailItem.add(d.room);
	    			subDetailItem.add(d.floorLevel);
	    			subDetailItem.add(d.code);
	    			subDetailItem.add(d.title);
	    			subDetailItem.add(d.firstName);
	    			subDetailItem.add(d.lastName);
	    			subDetailItem.add(d.codeFromStock);
	    			subDetail.add(subDetailItem);
	    		}
	    		result.put("id", pad.id);
	    		result.put("description", pad.description);
	    		result.put("code", pad.fsn.descriptionId);
	    		result.put("price", pad.price);
	    		result.put("priceNoVat", pad.priceNoVat);
	    		result.put("quantity", pad.quantity);
	    		result.put("llifeTime", pad.llifeTime);
	    		result.put("alertTime", pad.alertTime);
	    		result.put("seller", pad.seller);
	    		result.put("phone", pad.phone);
	    		result.put("brand", pad.brand);
	    		result.put("serialNumber", pad.serialNumber);
	    		result.put("subDetails", subDetail);
	    		String status = "null";
	    		if(pad.status!=null){
	    			status = pad.status.name();
	    		}
	    		result.put("status", status);
    		}
    		
    	}else if(json.get("tab").asText().equals("2")){
    		ArrayNode subDetail = JsonNodeFactory.instance.arrayNode();
    		models.durableGoods.ProcurementDetail pgd = models.durableGoods.ProcurementDetail.find.byId(Long.parseLong(json.get("id").asText()));
    		if(pgd.status!=OrderDetailStatus.DELETE)
    		{
	    		for(models.durableGoods.DurableGoods g : pgd.subDetails){
	    			ArrayNode subDetailItem = JsonNodeFactory.instance.arrayNode();
	    			subDetailItem.add(g.department);
	    			subDetailItem.add(g.room);
	    			subDetailItem.add(g.floorLevel);
	    			subDetailItem.add(g.codes);
	    			subDetailItem.add(g.title);
	    			subDetailItem.add(g.firstName);
	    			subDetailItem.add(g.lastName);
	    			subDetail.add(subDetailItem);
	    		}
	    		result.put("id", pgd.id);
	    		
	    		result.put("typeOfGood", pgd.typeOfDurableGoods);
	    		result.put("description", pgd.description);
	    		result.put("code", pgd.code);
	    		result.put("price", pgd.price);
	    		result.put("priceNoVat", pgd.priceNoVat);
	    		result.put("quantity", pgd.quantity);
	    		result.put("seller", pgd.seller);
	    		result.put("phone", pgd.phone);
	    		result.put("brand", pgd.brand);
	    		result.put("serialNumber", pgd.serialNumber);
	    		result.put("partOfPic", pgd.partOfPic);
	    		result.put("subDetails", subDetail);
	    		String status = "null";
	    		if(pgd.status!=null){
	    			status = pgd.status.name();
	    		}
	    		result.put("status", status);
    		}
    	}
    	
    	
    	return ok(result);
	}
    
    @Security.Authenticated(Secured.class)
    public static Result saveNewArticlesOrder(){
    	DynamicForm form = Form.form().bindFromRequest();
    	//System.out.println(Form.form(models.durableArticles.Procurement.class).bindFromRequest());
    	
    	//models.durableArticles.Procurement articlesOrder = Form.form(models.durableArticles.Procurement.class).bindFromRequest().get();
    	models.durableArticles.Procurement articlesOrder = models.durableArticles.Procurement.find.byId(Long.parseLong(form.get("id")));
    	
    	articlesOrder.title = form.get("title");
    	articlesOrder.contractNo = form.get("contractNo");
    	articlesOrder.budgetType = form.get("budgetType");
    	articlesOrder.institute = form.get("institute");
    	articlesOrder.budgetYear = Integer.parseInt(form.get("budgetYear"));
    	if(form.get("institute")!=null && !form.get("institute").equals("---เลือก---")){
    		//System.out.println(form.get("institute"));
    		articlesOrder.company = Company.find.where().eq("nameEntrepreneur", form.get("institute")).findList().get(0);
    	}
    	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   AI 
    	
    	
    	for(int i =articlesOrder.aiCommittee.size()-1;i>=0;i--){
			models.durableArticles.AI_Committee ai = articlesOrder.aiCommittee.get(i);
			ai.committee = null;
			ai.delete();
			articlesOrder.aiCommittee.remove(i);
		}
		for(int i =articlesOrder.eoCommittee.size()-1;i>=0;i--){
			models.durableArticles.EO_Committee eo = articlesOrder.eoCommittee.get(i);
			eo.committee = null;
			eo.delete();
			articlesOrder.eoCommittee.remove(i);
		}
    	
    	
    	boolean editingMode = true;
    	String[] temp = form.get("aiLists").split(",");
    	String a=form.get("aiLists");
    	if(!a.equals(""))
    	{
	    	for(int i=0;i<temp.length;i++)
	    	{
	    		System.out.println("inlist is: "+temp[i]);
	    		String pTitle=form.get("aiPrefixName"+temp[i]);
	    		String pName=form.get("aiFirstName"+temp[i]);
	    		String pLastName=form.get("aiLastName"+temp[i]);
	    		String pPosition = form.get("aiPosition"+temp[i]);
	    		
	    		User cmt = User.find.where().eq("namePrefix",pTitle).eq("firstName",pName).eq("lastName", pLastName).eq("position", pPosition).findUnique(); 
	    		/*
	    		if(cmt==null)
	    		{
	    			System.out.println("innnnn");
			        cmt = new User();
			        cmt.namePrefix = form.get("aiPrefixName"+temp[i]);		
			        cmt.firstName = form.get("aiFirstName"+temp[i]);
			        cmt.lastName = form.get("aiLastName"+temp[i]);
			        cmt.position = form.get("aiPosition"+temp[i]);			
		
			        cmt.save();
	    		}
	    		*/
		        AI_Committee ai_cmt;
		        if(i<articlesOrder.aiCommittee.size()){
		        	ai_cmt = articlesOrder.aiCommittee.get(i);
			        editingMode = true;
		        }else{
			        ai_cmt = new AI_Committee();
			        editingMode = false;
	    		}
		        ai_cmt.employeesType = form.get("aiCommitteeType"+temp[i]); 
		        ai_cmt.committeePosition = form.get("aiCommitteePosition"+temp[i]);     
		        ai_cmt.procurement = articlesOrder;
		        ai_cmt.committee = cmt;
		        
		        articlesOrder.aiCommittee.add(ai_cmt);
		        
		        if(!editingMode) ai_cmt.save();
		        else ai_cmt.update();
	    	}
    	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   endAI 	
    	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   EO 	
    	temp = form.get("eoLists").split(",");
    	a=form.get("eoLists");
    	if(!a.equals(""))
    	{
	    	for(int i=0;i<temp.length;i++)
	    	{	
	    		System.out.println("inlist is: "+temp[i]);
	    		String pTitle=form.get("eoPrefixName"+temp[i]);
	    		String pName=form.get("eoFirstName"+temp[i]);
	    		String pLastName=form.get("eoLastName"+temp[i]);
	    		String pPosition = form.get("eoPosition"+temp[i]);
	    		
	    		User cmt = User.find.where().eq("namePrefix",pTitle).eq("firstName",pName).eq("lastName", pLastName).eq("position", pPosition).findUnique();   		

	    		/*
	    		if(cmt==null)
	    		{
			        cmt = new User();
			        cmt.namePrefix = form.get("eoPrefixName"+temp[i]);		
			        cmt.firstName = form.get("eoFirstName"+temp[i]);
			        cmt.lastName = form.get("eoLastName"+temp[i]);
			        cmt.position = form.get("eoPosition"+temp[i]);			
		
			        cmt.save();
	    		}
	    		*/
	    		
	    		EO_Committee eo_cmt;
	    		if(i<articlesOrder.eoCommittee.size()){
	    			eo_cmt = articlesOrder.eoCommittee.get(i);
	    			editingMode = true;
	    		}else{
	    			eo_cmt = new EO_Committee();
	    			editingMode = false;
	    		}
		        eo_cmt.employeesType = form.get("eoCommitteeType"+temp[i]); 
		        eo_cmt.committeePosition = form.get("eoCommitteePosition"+temp[i]);     
		        eo_cmt.procurement = articlesOrder;
		        eo_cmt.committee = cmt;
		        
		        articlesOrder.eoCommittee.add(eo_cmt);
		        
		        if(!editingMode) eo_cmt.save();
		        else eo_cmt.update();
	    	}
    	}
    	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   endEO 	   	
    	try {
    		Date date;
	        if(!form.get("addDate_p").equals("")) {
				date = new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).parse(form.get("addDate_p"));
				articlesOrder.addDate = date;
	        }
	        if(!form.get("checkDate_p").equals("")){
	        	date = new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).parse(form.get("checkDate_p"));
	        	articlesOrder.checkDate = date;
	        }
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	//System.out.println(articlesOrder.checkDate.toLocaleString());
        
    	
    	/////////////////////////////////////////////////////////////////////////////////////
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("attachFile");
		
		String fileName="";
		String contentType=""; 
		//File file = null;
		if (filePart != null) //กรณีมีไฟล์
		{
			fileName = filePart.getFilename();
			contentType = filePart.getContentType(); 
			
			articlesOrder.fileName = fileName;//get origin name
			
			
			String[] extension=fileName.split("\\.");							//split .
			String targetPath = "./public/images/articlesOrder/" + articlesOrder.id;
			articlesOrder.path ="images/articlesOrder/" + articlesOrder.id;
			if(extension.length>1)
			{
			targetPath += "." + extension[((extension.length)-1)];				//get type file by last spilt
			articlesOrder.path+="." + extension[((extension.length)-1)];	
			}
			articlesOrder.fileName = fileName;										//get traditional file name
			
		
			filePart.getFile().renameTo(new File(targetPath));						//save file on your path

			articlesOrder.fileType = contentType; 
			//end write file

		} 
    	/////////////////////////////////////////////////////////////////////////////////////
    	
		if(articlesOrder.getMonth()>9)
		{
			articlesOrder.yearStatus = articlesOrder.getYear()+1;
			System.out.println("a: "+articlesOrder.yearStatus);
		}
		else if(articlesOrder.getMonth()==9 && articlesOrder.getDay()>15)
		{
			articlesOrder.yearStatus = articlesOrder.getYear()+1;
			System.out.println("b: "+articlesOrder.yearStatus);
		}
		else
		{
			articlesOrder.yearStatus = articlesOrder.getYear();
			System.out.println("c: "+articlesOrder.yearStatus);
		}
		
		if(articlesOrder.status == ImportStatus.INIT)	//genBarcode
		{
			articlesOrder.barCode = "i01";
			String hex = Integer.toHexString((int)articlesOrder.id);
		    
			if(hex.length()==1)
				hex="00000"+hex;
			else if(hex.length()==2)
				hex="0000"+hex;
			else if(hex.length()==3)
				hex="000"+hex;
			else if(hex.length()==4)
				hex="00"+hex;
			else if(hex.length()==5)
				hex="0"+hex;

		    articlesOrder.barCode=articlesOrder.barCode+hex;
		}
		
		
		
        articlesOrder.status = ImportStatus.SUCCESS;      
        
     
        
        articlesOrder.update();
        //System.out.println(articlesOrder);
        
        return redirect(routes.Import.importsOrder2("1"));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result saveNewGoodsOrder(){
    	DynamicForm form = Form.form().bindFromRequest();
    	//System.out.println(Form.form(models.durableGoods.Procurement.class).bindFromRequest());
    	
    	//System.out.println(Long.parseLong(form.get("id")));
    	models.durableGoods.Procurement goodsOrder = models.durableGoods.Procurement.find.byId(Long.parseLong(form.get("id")));
    	goodsOrder.title = form.get("title");
    	goodsOrder.contractNo = form.get("contractNo");
    	goodsOrder.budgetType = form.get("budgetType");
    	goodsOrder.institute = form.get("institute");
    	goodsOrder.budgetYear = Integer.parseInt(form.get("budgetYear"));
    	
    	if(form.get("institute")!=null && !form.get("institute").equals("---เลือก---"))
    		goodsOrder.company = Company.find.where().eq("nameEntrepreneur", form.get("institute")).findList().get(0);
    	
    	
    	for(int i =goodsOrder.aiCommittee.size()-1;i>=0;i--)
    	{
			models.durableGoods.AI_Committee ai = goodsOrder.aiCommittee.get(i);
			ai.committee = null;
			ai.delete();
			goodsOrder.aiCommittee.remove(i);
		}

    	String[] temp = form.get("aiLists").split(",");
    	String a=form.get("aiLists");
    	
    	//System.out.println("Let me see");
    	//System.out.println(a);
    	//System.out.println(temp.length);
    	
    	boolean editingMode = true;
    	
    	if(!a.equals(""))
    	{
	    	for(int i=0;i<temp.length;i++){
	    		
	    		System.out.println("inlist is: "+temp[i]);

		    	String pTitle=form.get("aiPrefixName"+temp[i]);
	    		String pName=form.get("aiFirstName"+temp[i]);
	    		String pLastName=form.get("aiLastName"+temp[i]);
	    		String pPosition = form.get("aiPosition"+temp[i]);
	    		
	    		User cmt = User.find.where().eq("namePrefix",pTitle).eq("firstName",pName).eq("lastName", pLastName).eq("position", pPosition).findUnique();   		
		    	
	    		/*
		    	if(cmt==null){
			    	cmt = new User();
			    	cmt.namePrefix = form.get("aiPrefixName"+temp[i]);
			    	cmt.firstName = form.get("aiFirstName"+temp[i]);
			    	cmt.lastName = form.get("aiLastName"+temp[i]);
			    	cmt.position = form.get("aiPosition"+temp[i]);
			    	cmt.save();
		    	}
		    	*/
		    	
			    models.durableGoods.AI_Committee ai_cmt;
			    if(i<goodsOrder.aiCommittee.size()){
			    	ai_cmt = goodsOrder.aiCommittee.get(i);
			    	editingMode = true;
			    }else{
			    	ai_cmt = new models.durableGoods.AI_Committee();
			    	editingMode = false;
			    }
		    	ai_cmt.employeesType = form.get("aiCommitteeType"+temp[i]);
		    	ai_cmt.committeePosition = form.get("aiCommitteePosition"+temp[i]);
		    	ai_cmt.procurement = goodsOrder;
		    	ai_cmt.committee = cmt;
		    	
		    	goodsOrder.aiCommittee.add(ai_cmt);
		    	
		    	if(!editingMode) ai_cmt.save();
		    	else ai_cmt.update();
	    	}
    	}
    	
    	
    	try {
    		Date date;
	        if(!form.get("addDate_p").equals("")) {
				date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(util.DataRandom.toStandardYear(form.get("addDate_p")));
				goodsOrder.addDate = date;
	        }
	        if(!form.get("checkDate_p").equals("")){
	        	date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(util.DataRandom.toStandardYear(form.get("checkDate_p")));
	        	goodsOrder.checkDate = date;
	        }
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	//System.out.println(goodsOrder.checkDate.toLocaleString());
    	
        
    	
    	
    	MultipartFormData body = request().body().asMultipartFormData();
    	FilePart filePart = body.getFile("attachFile");
    	
    	String fileName="";
    	String contentType="";
    	//File file = null;
    	if (filePart != null) //กรณีมีไฟล์
    	{
    	fileName = filePart.getFilename();
    	contentType = filePart.getContentType();
    	
    	goodsOrder.fileName = fileName;//get origin name
    	
    	
    	String[] extension=fileName.split("\\."); //split .
    	String targetPath = "./public/images/goodsOrder/" + goodsOrder.id;
    	goodsOrder.path ="images/goodsOrder/" + goodsOrder.id;
    	if(extension.length>1)
    	{
    	targetPath += "." + extension[((extension.length)-1)]; //get type file by last spilt
    	goodsOrder.path+="." + extension[((extension.length)-1)];
    	}
    	goodsOrder.fileName = fileName; //get traditional file name
    	
    	
    	filePart.getFile().renameTo(new File(targetPath)); //save file on your path
    	
    	goodsOrder.fileType = contentType;
    	//end write file
    	
    	} 
    	
		if(goodsOrder.status == ImportStatus.INIT)	//genBarcode
		{
			goodsOrder.barCode = "i02";
			String hex = Integer.toHexString((int)goodsOrder.id);
		    
			if(hex.length()==1)
				hex="00000"+hex;
			else if(hex.length()==2)
				hex="0000"+hex;
			else if(hex.length()==3)
				hex="000"+hex;
			else if(hex.length()==4)
				hex="00"+hex;
			else if(hex.length()==5)
				hex="0"+hex;

			goodsOrder.barCode=goodsOrder.barCode+hex;
		}
    	
    	
    	
    	
    	goodsOrder.status = ImportStatus.SUCCESS;        
    	goodsOrder.update();
    	
    	/*
    	for(models.durableGoods.ProcurementDetail pd:goodsOrder.details) 	//เพิ่มจำนวนวัสดุให้กับclassหมายเลขวัสดุสิ้นเปลือง อะไรก็ไม่รู้
    	{
    		if(pd.typeOfDurableGoods==0)
    		{
    			MaterialCode mc = MaterialCode.find.byId(pd.code);
    			
    			mc.remain = 0;
    			
    			List<models.durableGoods.ProcurementDetail> details = models.durableGoods.ProcurementDetail.find.where().eq("code", pd.code).eq("procurement.status", ImportStatus.SUCCESS).findList();
    			for(models.durableGoods.ProcurementDetail detail : details){
    				mc.remain += detail.quantity;
    				System.out.println("innnnn");
    			}
    			System.out.println("Save new good order"+mc.code);
    			System.out.println(mc.remain);
    			mc.update();
    		}
    	}
    	//System.out.println(goodsOrder);
    	 */

        return redirect(routes.Import.importsOrder2("2"));
    }
    
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveNewGoodsOrderDetail(){
    	RequestBody body = request().body();
    	//System.out.println(body);
    	JsonNode json = body.asJson();
    	models.durableGoods.Procurement procurement = models.durableGoods.Procurement.find.byId(Long.parseLong(json.get("procurementId").asText()));
    	models.durableGoods.ProcurementDetail procurementDetail = null;
    	if(!json.get("procurementDetailId").asText().equals("")){
    		procurementDetail = models.durableGoods.ProcurementDetail.find.byId(Long.parseLong(json.get("procurementDetailId").asText()));
    	}else if(json.get("initFlag").asText().equals("0")){
    		procurementDetail = procurement.details.get(procurement.details.size()-1); // get last procurementDetail
    	}
    	

    	boolean editingMode = true;
    	
    	///Calculateeeeeeeeeeeeeeeeeee
    	double priceSub=-1;
    	int numSub=-1;
    	int typeSub=-1;
    	String codeSub="";
    	///Calculateeeeeeeeeeeeeeeeeee
    	
    	if(procurementDetail == null){ //new
    		procurementDetail = new models.durableGoods.ProcurementDetail();
    		editingMode = false;
    	}
    	///Calculateeeeeeeeeeeeeeeeeee
    	else{//not new
    		priceSub = procurementDetail.price;
    		numSub = procurementDetail.quantity;
    		typeSub = procurementDetail.typeOfDurableGoods;
    		codeSub = procurementDetail.code;
    	}
    	///Calculateeeeeeeeeeeeeeeeeee
    	
    	
    	if(editingMode == false || procurementDetail.status!=OrderDetailStatus.UNCHANGE)
    	{

	    	procurementDetail.description = json.get("description").asText();
	    	procurementDetail.priceNoVat = Double.parseDouble(json.get("priceNoVat").asText());
	    	procurementDetail.price = Double.parseDouble(json.get("price").asText());
	    	procurementDetail.quantity = Integer.parseInt(json.get("quantity").asText());
	    	procurementDetail.remain = Integer.parseInt(json.get("quantity").asText());
	    	
	    	
	    	
	    	//procurementDetail.classifier = json.get("classifier").asText();
	    	procurementDetail.seller =json.get("seller").asText();
	    	procurementDetail.phone =json.get("phone").asText();
	    	procurementDetail.brand = json.get("brand").asText();
	    	procurementDetail.serialNumber = json.get("serialNumber").asText();
	    	//procurementDetail.partOfPic = json.get("serialNumber").asText();
	    	procurementDetail.procurement = procurement;
	    	
	    	String codeId = json.get("code").asText();
	    	
	    	
	    	if(!codeId.equals("")){
	    		
	    		procurementDetail.code = codeId; //fsn or code 5 number
	    		procurementDetail.typeOfDurableGoods = Integer.parseInt(json.get("typeOfGoods").asText());
	
	    		MaterialCode mc=null;
	    		FSN_Description fsn = null;
	    		
	    		System.out.println("Before");
	    		
		    		if(editingMode==false)//+ปกติ
		    		{
		    			if(procurementDetail.typeOfDurableGoods==0)	//จำนวนวัสดุสิ้นเปลือง    
			    		{
				    		mc= MaterialCode.find.byId(codeId);
				    		System.out.println(mc.remain);
				    		System.out.println(mc.pricePerEach);
				    		
			    			double sumPrice=mc.pricePerEach*mc.remain;
			    			sumPrice=sumPrice+(procurementDetail.quantity*procurementDetail.price);
			    			
			    			mc.remain=mc.remain+procurementDetail.quantity;
			    			mc.pricePerEach=sumPrice/mc.remain;
			    			mc.update();
			    			
			    			System.out.println("After");
			    			System.out.println(mc.remain);
				    		System.out.println(mc.pricePerEach);
			    		}
		    			else//เพิ่มจำนวนวัสดุคงทนถาวร
		    			{
		    				fsn= FSN_Description.find.byId(codeId);
				    		System.out.println(fsn.remain);
				    		System.out.println(fsn.pricePerEach);
				    		
				    		double sumPrice=fsn.pricePerEach*fsn.remain;
				    		sumPrice=sumPrice+(procurementDetail.quantity*procurementDetail.price);
				    		
				    		fsn.remain = fsn.remain+procurementDetail.quantity;
				    		fsn.pricePerEach=sumPrice/fsn.remain;
				    		fsn.update();
				    		
			    			System.out.println("After");
			    			System.out.println(fsn.remain);
				    		System.out.println(fsn.pricePerEach);
		    			}
		    		}
		    		else
		    		{
		    				if(procurementDetail.typeOfDurableGoods==1 && typeSub==0 )//เปลี่ยนจากสิ้นเปลืองเป็นคงทนถาวร จะลบออก / และเพิ่มคงทนถาวร
		    				{
		    					///////////////////////////ลดสิ้นเปลือง
			    				mc= MaterialCode.find.byId(codeSub);
			    				/*สิ้นเปลือง
			    	    		System.out.println(mc.remain);
			    	    		System.out.println(mc.pricePerEach);
			    	    		*/
					    		
				    			double sumPrice=mc.pricePerEach*mc.remain;
				    			sumPrice=sumPrice-(numSub*priceSub);
				    			
				    			mc.remain=mc.remain-numSub;
				    			if(mc.remain>0)
				    				mc.pricePerEach=sumPrice/mc.remain;
				    			else
				    				mc.pricePerEach=0;
				    			mc.update();
				    			
				    			/*สิ้นเปลือง
				    			System.out.println("After");
				    			System.out.println(mc.remain);
					    		System.out.println(mc.pricePerEach);
					    		*/
				    			//////////////////////////จบลดสิ้นเปลือง
				    			//////////////////////////เพิ่มคงทนถาวร
				    			fsn=FSN_Description.find.byId(codeId);
			    	    		System.out.println(fsn.remain);
			    	    		System.out.println(fsn.pricePerEach);
			    	    		
			    	    		sumPrice=fsn.pricePerEach*fsn.remain;
			    	    		sumPrice=sumPrice+(procurementDetail.quantity*procurementDetail.price);
			    	    		
			    	    		fsn.remain = fsn.remain+procurementDetail.quantity;
			    	    		fsn.pricePerEach=sumPrice/fsn.remain;
			    	    		fsn.update();
			    	    		
			    	    		System.out.println("After");
				    			System.out.println(fsn.remain);
					    		System.out.println(fsn.pricePerEach);
					    		

		    				}
			    			
			    			else if(procurementDetail.typeOfDurableGoods==0 && typeSub==0 )//เปลี่ยนค่าโดยที่วัสดุเป็นชนิดเดียวกัน /
			    			{
			    				mc= MaterialCode.find.byId(codeId);
			    	    		System.out.println(mc.remain);
			    	    		System.out.println(mc.pricePerEach);
					    		
				    			double sumPrice=mc.pricePerEach*mc.remain;
				    			sumPrice=sumPrice-(numSub*priceSub);
				    			
				    			mc.remain=mc.remain-numSub;
				    			if(mc.remain>0)
				    				mc.pricePerEach=sumPrice/mc.remain;
				    			else
				    				mc.pricePerEach=0;
				    			//////////////////////////////////////////
				    			sumPrice=mc.pricePerEach*mc.remain;
				    			sumPrice=sumPrice+(procurementDetail.quantity*procurementDetail.price);
				    			
				    			mc.remain=mc.remain+procurementDetail.quantity;
				    			
				    			if(mc.remain>0)
				    				mc.pricePerEach=sumPrice/mc.remain;
				    			else
				    				mc.pricePerEach=0;
				    			mc.update();
				    			
				    			System.out.println("After");
				    			System.out.println(mc.remain);
					    		System.out.println(mc.pricePerEach);
			    			}
			    			else if(procurementDetail.typeOfDurableGoods==1 && typeSub==1 )
			    			{
			    				fsn= FSN_Description.find.byId(codeId);
			    	    		System.out.println(fsn.remain);
			    	    		System.out.println(fsn.pricePerEach);
					    		
				    			double sumPrice=fsn.pricePerEach*fsn.remain;
				    			sumPrice=sumPrice-(numSub*priceSub);
				    			
				    			fsn.remain=fsn.remain-numSub;
				    			
				    			if(fsn.remain>0)
				    				fsn.pricePerEach=sumPrice/fsn.remain;
				    			else
				    				fsn.pricePerEach=0;
				    			//////////////////////////////////////////
				    			sumPrice=fsn.pricePerEach*fsn.remain;
				    			sumPrice=sumPrice+(procurementDetail.quantity*procurementDetail.price);
				    			
				    			fsn.remain=fsn.remain+procurementDetail.quantity;
				    			
				    			if(fsn.remain>0)
				    				fsn.pricePerEach=sumPrice/fsn.remain;
				    			else
				    				fsn.pricePerEach=0;
				    			fsn.update();
				    			
				    			System.out.println("After");
				    			System.out.println(fsn.remain);
					    		System.out.println(fsn.pricePerEach);
			    			}
			    			else if(procurementDetail.typeOfDurableGoods==0 && typeSub==1 )//เปลี่ยนจากคงทนถาวรเป็นสิ้นเปลือง จะ+เพิ่มสิ้นเปลือง /และลบวันดุคงทนถาวร
			    			{
			    				//////////////////////////เพิ่มสิ้นเปลือง
					    		mc= MaterialCode.find.byId(codeId);
					    		/*
					    		System.out.println(mc.remain);
					    		System.out.println(mc.pricePerEach);
					    		*/
					    		
				    			double sumPrice=mc.pricePerEach*mc.remain;
				    			sumPrice=sumPrice+(procurementDetail.quantity*procurementDetail.price);
				    			
				    			mc.remain=mc.remain+procurementDetail.quantity;
				    			mc.pricePerEach=sumPrice/mc.remain;
				    			mc.update();
				    			
				    			/*
				    			System.out.println("After");
				    			System.out.println(mc.remain);
					    		System.out.println(mc.pricePerEach);
					    		*/
					    		//////////////////////////จบเพิ่มสิ้นเปลือง
				    			
				    			////////////////////////// ลบคงทนถาวร
				    			fsn = FSN_Description.find.byId(codeSub);
				    			System.out.println(fsn.remain);
				    			System.out.println(fsn.pricePerEach);
				    			
				    			sumPrice = fsn.pricePerEach*fsn.remain;
				    			sumPrice = sumPrice-(numSub*priceSub);
				    			
				    			fsn.remain=fsn.remain-numSub;
				    			
				    			
				    			if(fsn.remain>0)
				    				fsn.pricePerEach=sumPrice/fsn.remain;
				    			else
				    				fsn.pricePerEach=0;
				    			
				    			fsn.update();
				    			
				    			System.out.println("After");
				    			System.out.println(fsn.remain);
					    		System.out.println(fsn.pricePerEach);
				    			////////////////////////// จบลบคงทนถาวร

			    			}
			    			
		    		}

	    	}else{
	    		System.out.println("\n\n Exception MaterialCode Not Found !!!! \n\n\n\n\n");
	    	}
	    	
	    	
	    	if(!editingMode) procurementDetail.save();
	    	else procurementDetail.update();
	    	
	    	int quantity = Integer.parseInt(json.get("quantity").asText());
	    	int init = 1;
	    	int halt = 100;
	    	if(json.get("fracment").asText().equals("true")){
	    		int index = Integer.parseInt(json.get("index").asText())+1;
	    		if(quantity-index < 100){
	    			init = index;
	    			halt = quantity;
	    		}else{
	    			init = index;
	    			halt = index+99;
	    		}
	    	}else{
	    		init = 1;
	    		halt = quantity;
	    	}
	
	    	for(int i=init;i<=halt;i++)
	    	{	
	    		DurableGoods goods;
	    		if((i-1)<procurementDetail.subDetails.size()){
	    			goods = procurementDetail.subDetails.get(i-1);
	    			editingMode = true;
	    		}
	    		else{
	    			goods = new DurableGoods();
	    			editingMode = false;
	    		}
	    		goods.status = SuppliesStatus.NORMAL;
		    	goods.department = json.get("goodDepartment"+i).asText();
		    	goods.room = json.get("goodRoom"+i).asText();
		    	goods.floorLevel = json.get("goodLevel"+i).asText();
		    	goods.codes = json.get("goodFSNCode"+i).asText();
		    	goods.title = json.get("goodPrefixName"+i).asText();			
		    	goods.firstName = json.get("goodFirstName"+i).asText();		
		    	goods.lastName = json.get("goodLastName"+i).asText();	
		    	
		    	goods.typeOfDurableGoods = Integer.parseInt(json.get("typeOfGoods").asText());
		    	
		    	goods.detail = procurementDetail;
		    	
		    	if(!editingMode){ 
		    		goods.save();
		    		
		    		if(goods.typeOfDurableGoods==1)
		    		{
		    		goods.barCode="A";	
		    		String hex = Integer.toHexString((int)goods.id);
					if(hex.length()==1)
						hex="0000000"+hex;
					else if(hex.length()==2)
						hex="000000"+hex;
					else if(hex.length()==3)
						hex="00000"+hex;
					else if(hex.length()==4)
						hex="0000"+hex;
					else if(hex.length()==5)
						hex="000"+hex;
					else if(hex.length()==6)
						hex="00"+hex;
					else if(hex.length()==7)
						hex="0"+hex;
					
					goods.barCode=goods.barCode+hex;
					System.out.println(goods.barCode);
					goods.update();
		    		}
		    	}
		    	else goods.update();
	    	}
    	}//end if(UNCHANGE)
		
    	
    
    	
    	List<models.durableGoods.ProcurementDetail> procurementDetails = models.durableGoods.ProcurementDetail.find.where().eq("procurement", procurement).findList(); 
    	ObjectNode result = Json.newObject();
    	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
    	int i=0;
    	int count =0;
    	for(models.durableGoods.ProcurementDetail p : procurementDetails){
    		
    		if(p.status != OrderDetailStatus.DELETE)
    		{
	    		FSN_Description fsnCode=null;
	    		MaterialCode consumableGoodCode=null;
	    		
	    		if(p.typeOfDurableGoods==1)//is a durableGood
	    			fsnCode = FSN_Description.find.byId(p.code);
	    		else
	    			consumableGoodCode= MaterialCode.find.byId(p.code);

	
	    		
	    		ObjectNode item = Json.newObject();
	    		item.put("id", p.id);
	    		if(p.code!="") item.put("code", p.code);
	    		else item.put("code", p.code);
	    		item.put("description", p.description);
	    		item.put("quantity", p.quantity);
	    		item.put("classifier", "อัน");
	    		item.put("price", p.price);
	    		item.put("priceNoVat", p.priceNoVat);
	    		if(p.typeOfDurableGoods==1)//is a durableGood
	    		{
	    			item.put("fileName", fsnCode.fileName);
	    			item.put("fileType", fsnCode.fileType);
	    			item.put("path", fsnCode.path);
	    		}
	    		else
	    		{
	    			item.put("fileName", consumableGoodCode.fileName);
	    			item.put("fileType", consumableGoodCode.fileType);
	    			item.put("path", consumableGoodCode.path);
	    		}
	    		
	    		jsonArray.insert(i++, item);
    		}
    		else
    			count++;
    	}
    	result.put("type", "goods");
    	result.put("length",(procurementDetails.size()-count));
	    result.put("data",jsonArray);
    
    	return ok(result);
    }
    
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveNewArticlesOrderDetail(){
    	
    	// TODO : save detail
    	RequestBody body = request().body();
    	//System.out.println("du value");
    	///////////////////System.out.println(body);
    	JsonNode json = body.asJson();
    	models.durableArticles.Procurement procurement = models.durableArticles.Procurement.find.byId(Long.parseLong(json.get("procurementId").asText()));
    	models.durableArticles.ProcurementDetail procurementDetail = null;
    	
    	if(!json.get("procurementDetailId").asText().equals("")){
    		procurementDetail = models.durableArticles.ProcurementDetail.find.byId(Long.parseLong(json.get("procurementDetailId").asText()));
    	}else if(json.get("initFlag").asText().equals("0")){
    		procurementDetail = procurement.details.get(procurement.details.size()-1); // get last procurementDetail
    	}
    	
   
    	boolean editingMode = true;
    	
    	if(procurementDetail == null){
    		procurementDetail = new models.durableArticles.ProcurementDetail();
    		editingMode = false;
    	}
    	
    	if(editingMode == false || procurementDetail.status!=OrderDetailStatus.UNCHANGE)
    	{
	    	procurementDetail.description = json.get("description").asText();
	    	procurementDetail.priceNoVat = Double.parseDouble(json.get("priceNoVat").asText());
	    	procurementDetail.price = Double.parseDouble(json.get("price").asText());
	    	
	    	procurementDetail.quantity = Integer.parseInt(json.get("quantity").asText());
	    	
	    	if(procurementDetail.depreciationPrice == 0.0)
	    		procurementDetail.depreciationPrice = procurementDetail.price*procurementDetail.quantity;
	    	//procurementDetail.classifier = json.get("classifier").asText();
	    	procurementDetail.llifeTime = Double.parseDouble(json.get("llifeTime").asText());
	    	procurementDetail.alertTime = Double.parseDouble(json.get("alertTime").asText());
	    	procurementDetail.seller =json.get("seller").asText();
	    	procurementDetail.phone =json.get("phone").asText();
	    	procurementDetail.brand = json.get("brand").asText();
	    	procurementDetail.serialNumber = json.get("serialNumber").asText();
	    	//procurementDetail.partOfPic = json.get("serialNumber").asText();
	    	
	    	procurementDetail.status =  OrderDetailStatus.INIT;
	    	
	    	procurementDetail.procurement = procurement;
	    	
	    	String fsnCode = json.get("fsnCode").asText();
	    	if(!fsnCode.equals("")){
	    		procurementDetail.fsn = FSN_Description.find.byId(fsnCode);
	    	}else{
	    		System.out.println("\n\n Exception FSN code not found in database!!!!!  \n\n\n\n");
	    	}
	    	
	    	if(!editingMode) procurementDetail.save();
	    	else procurementDetail.update();
	    	
	    	
	    	/*durableArticles.code = json.get("code").asText();
	    	durableArticles.codeFromStock = json.get("codeFromStock").asText();
	    	durableArticles.status = SuppliesStatus.NORMAL;
	    	durableArticles.detail = procurementDetail;*/
	    	int quantity = Integer.parseInt(json.get("quantity").asText());
	    	int init = 1;
	    	int halt = 100;
	    	if(json.get("fracment").asText().equals("true")){
	    		int index = Integer.parseInt(json.get("index").asText())+1;
	    		if(quantity-index < 100){
	    			init = index;
	    			halt = quantity;
	    		}else{
	    			init = index;
	    			halt = index+99;
	    		}
	    	}else{
	    		init = 1;
	    		halt = quantity;
	    	}
	    	for(int i=init;i<=halt;i++)
	    	{
	    		DurableArticles dA;
	    		if((i-1)<procurementDetail.subDetails.size()){
	    			dA = procurementDetail.subDetails.get(i-1);
	    			editingMode = true; 
	    		}
	    		else{
		    		dA = new DurableArticles();
		    		editingMode = false;
		    	}			
	    		
		    	dA.status = SuppliesStatus.NORMAL;
		    	dA.department = json.get("articleDepartment"+i).asText();
		    	dA.room = json.get("articleRoom"+i).asText();
		    	dA.floorLevel = json.get("articleLevel"+i).asText();
		    	dA.code = json.get("articleFSNCode"+i).asText();
		    	dA.title = json.get("articlePrefixName"+i).asText();			
		    	dA.firstName = json.get("articleFirstName"+i).asText();		
		    	dA.lastName = json.get("articleLastName"+i).asText();			
		    	dA.codeFromStock = json.get("articleStock"+i).asText(); 
		    
		    	dA.detail = procurementDetail;
		    	
		    	if(!editingMode){
		    		dA.save();
		    		
		    		dA.barCode="A";	
		    		String hex = Integer.toHexString((int)dA.id);
					if(hex.length()==1)
						hex="0000000"+hex;
					else if(hex.length()==2)
						hex="000000"+hex;
					else if(hex.length()==3)
						hex="00000"+hex;
					else if(hex.length()==4)
						hex="0000"+hex;
					else if(hex.length()==5)
						hex="000"+hex;
					else if(hex.length()==6)
						hex="00"+hex;
					else if(hex.length()==7)
						hex="0"+hex;
					
					dA.barCode=dA.barCode+hex;
		    		dA.update();
		    	}
		    	else dA.update();
		    	
		    	
	    	}
    	}
	
	    	
    	List<models.durableArticles.ProcurementDetail> procurementDetails = models.durableArticles.ProcurementDetail.find.where().eq("procurement", procurement).findList(); 
    	ObjectNode result = Json.newObject();
    	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
    	
    	int i=0;
    	int count=0;
    	for(models.durableArticles.ProcurementDetail p : procurementDetails){
    		
    		if(p.status != OrderDetailStatus.DELETE)
    		{
	    		FSN_Description newFsn = FSN_Description.find.byId(p.fsn.descriptionId);
	    		ObjectNode item = Json.newObject();
	    		item.put("id", p.id);
	    		if(p.fsn != null) item.put("fsn", p.fsn.descriptionId);
				else item.put("fsn", "null");
	    		item.put("description", p.description);
	    		item.put("quantity", p.quantity);
	    		item.put("classifier", "อัน");
	    		item.put("price", p.price);
	    		item.put("priceNoVat", p.priceNoVat);
	    		item.put("lifeTime", p.llifeTime);
	    		item.put("fileName", p.fsn.fileName);
	    		item.put("fileType", p.fsn.fileType);
	    		item.put("path", p.fsn.path);
	
	    		jsonArray.insert(i++, item);
    		}
    		else
    			count++;
    	}
    	result.put("type", "article");
    	result.put("length",(procurementDetails.size()-count));
	    result.put("data",jsonArray);
    	return ok(result);
    }
    
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result importsCancelOrder(){
    	RequestBody body = request().body();
    	JsonNode json = body.asJson();
    	System.out.println(json.get("id").toString());
    	String s = json.get("typeOfOrder").asText();
    	System.out.println(s);
    	models.durableArticles.Procurement articlesOrder = null; //declare value ให้เห็นค่าที่จะใช้ จะได้นำมาอยู่ใน if ได้
    	models.durableGoods.Procurement goodsOrder = null;
    	
    	String tab="";
    	if(s.equals("article")){
    		System.out.println("in articles");
    		articlesOrder = models.durableArticles.Procurement.find.byId(Long.parseLong(json.get("id").toString()));
    		
    		if(articlesOrder.status ==ImportStatus.INIT)
    		{
    		articlesOrder.status = ImportStatus.CANCEL;
	    		for(models.durableArticles.ProcurementDetail pd :articlesOrder.details)
				{
	    			pd.status= OrderDetailStatus.DELETE;
	    			for(DurableArticles d:pd.subDetails)
					{
						d.status = SuppliesStatus.DELETE;
						d.update();
					}		
					pd.update();
				}
    		}
    		
        	articlesOrder.update();
        	tab="1";
    	}else{
    		System.out.println("in good");
    		goodsOrder = models.durableGoods.Procurement.find.byId(Long.parseLong(json.get("id").toString()));
    		
    		if(goodsOrder.status ==ImportStatus.INIT)
    		{
    		goodsOrder.status = ImportStatus.CANCEL;
	    		for(models.durableGoods.ProcurementDetail pd :goodsOrder.details)
				{
	    			pd.status= OrderDetailStatus.DELETE;
	    			if(pd.typeOfDurableGoods==0)
					{
						MaterialCode mc=MaterialCode.find.byId(pd.code);
						
						System.out.println("Before");
						System.out.println(mc.remain);
						System.out.println(mc.pricePerEach);
						
						double sumPrice=mc.pricePerEach*mc.remain;
						sumPrice=sumPrice-(pd.quantity*pd.price);
						mc.remain=mc.remain-pd.quantity;
						if(mc.remain>0)
							mc.pricePerEach=sumPrice/mc.remain;
						else
							mc.pricePerEach=0;
						
						System.out.println("After");
						System.out.println(mc.remain);
						System.out.println(mc.pricePerEach);
						
						mc.update();
					}
					else
					{
						FSN_Description fsn = FSN_Description.find.byId(pd.code);
						
						System.out.println("Before");
						System.out.println(fsn.remain);
						System.out.println(fsn.pricePerEach);
						
						double sumPrice=fsn.pricePerEach*fsn.remain;
						sumPrice=sumPrice-(pd.quantity*pd.price);
						fsn.remain=fsn.remain-pd.quantity;
						
						if(fsn.remain>0)
							fsn.pricePerEach=sumPrice/fsn.remain;
						else
							fsn.pricePerEach=0;
						
						System.out.println("After");
						System.out.println(fsn.remain);
						System.out.println(fsn.pricePerEach);
						
						fsn.update();
					}
					
					for(DurableGoods d:pd.subDetails)
					{
						d.status = SuppliesStatus.DELETE;
						d.update();
					}		
					pd.update();
				}
			}
    		
    		goodsOrder.update();
    		tab="2";
    	}
        return redirect(routes.Import.importsOrder2(tab));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result createImportsOrderGoodsAdd() {
	    User user = User.find.where().eq("username", session().get("username")).findUnique();
	    models.durableGoods.Procurement order= new models.durableGoods.Procurement();
	    order.status = ImportStatus.INIT;
	    order.save();
	    return redirect(routes.Import.importsOrderGoodsAdd(order.id));
	} 

    @Security.Authenticated(Secured.class)
    public static Result importsOrderGoodsAdd(Long id) {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        models.durableGoods.Procurement order = models.durableGoods.Procurement.find.byId(id);
        List<Company> companies = Company.find.all();
        return ok(importsOrderGoodsAdd.render(order,user,companies));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result removeProcurement(){
    	DynamicForm form = Form.form().bindFromRequest();
    	String type = form.get("type");
    	String tab="";

    	if(type.equals("durableArticles"))
    	{
    		if(!form.get("durableArticlesProcurementTickList").equals(""))
    		{
    			String[] durableArticlesProcurementInList = form.get("durableArticlesProcurementTickList").split(",");
    			
    			int cantDelete=0;
    			for(int i=0;i<durableArticlesProcurementInList.length;i++)
    			{
    				models.durableArticles.Procurement p = models.durableArticles.Procurement.find.byId(Long.parseLong(durableArticlesProcurementInList[i]));
    				
    				if(p.status!=ImportStatus.UNCHANGE)
    				{
	    				p.status = ImportStatus.DELETE;
	    				File file = new File("./public/"+p.path);		//get file------------------------------------------------
	        			file.delete();									//delete file---------------------------------------------
	        			
	    				for(ProcurementDetail pd :p.details)
	    				{
	    					pd.status = OrderDetailStatus.DELETE;
	    					for(DurableArticles d:pd.subDetails)
	    					{
	    						d.status = SuppliesStatus.DELETE;
	    						d.update();
	    					}
	    					pd.update();
	    				}
	    				p.update();
    				}
    				else
    					cantDelete++;
    			}
    			flash("delete1","ลบรายการจัดซื้อและนำเข้าทั้งหมด " + (durableArticlesProcurementInList.length-cantDelete) +" รายการ ");
    			if(cantDelete!=0)
    				flash("delete2","ไม่สามารถลบรายการจัดซื้อและนำเข้าทั้งหมด " + cantDelete +" รายการ ");
    		}
    		else
    		{
    			flash("notSelect1","เลือกรายการจัดซื้อและนำเข้าที่ต้องการจะลบ");
    		}
    	}
    	else if(type.equals("goods"))
    	{
    		if(!form.get("goodsProcurementTickList").equals(""))
    		{
    			String[] goodsProcurementInList = form.get("goodsProcurementTickList").split(",");

    			int cantDelete=0;
    			for(int i=0;i<goodsProcurementInList.length;i++)
    			{
    				models.durableGoods.Procurement p = models.durableGoods.Procurement.find.byId(Long.parseLong(goodsProcurementInList[i]));
    				if(p.status!=ImportStatus.UNCHANGE)
    				{
	    				p.status = ImportStatus.DELETE;  
	    				
	    				File file = new File("./public/"+p.path);		//get file------------------------------------------------
	        			file.delete();									//delete file---------------------------------------------
	        			
	    				for(models.durableGoods.ProcurementDetail pd :p.details)
	    				{
	    					pd.status= OrderDetailStatus.DELETE;
	    					if(pd.typeOfDurableGoods==0)
	    					{
	    						MaterialCode mc=MaterialCode.find.byId(pd.code);
	    						
	    						double sumPrice=mc.pricePerEach*mc.remain;
	    						sumPrice=sumPrice-(pd.quantity*pd.price);
	    						mc.remain=mc.remain-pd.quantity;
	    						if(mc.remain>0)
	    							mc.pricePerEach=sumPrice/mc.remain;
	    						else
	    							mc.pricePerEach=0;
	    						
	    						mc.update();
	    					}
	    					else
	    					{
	    						FSN_Description fsn = FSN_Description.find.byId(pd.code);
	    						
	    						double sumPrice=fsn.pricePerEach*fsn.remain;
	    						sumPrice=sumPrice-(pd.quantity*pd.price);
	    						fsn.remain=fsn.remain-pd.quantity;
	    						
	    						if(fsn.remain>0)
	    							fsn.pricePerEach=sumPrice/fsn.remain;
	    						else
	    							fsn.pricePerEach=0;
	    						
	    						fsn.update();
	    					}
	    					
	    					for(DurableGoods d:pd.subDetails)
	    					{
	    						d.status = SuppliesStatus.DELETE;
	    						d.update();
	    					}		
	    					pd.update();
	    				}
	        			
	    				p.update();
    				}
    				else
    					cantDelete++;
    			}
    			flash("delete3","ลบรายการจัดซื้อและนำเข้าทั้งหมด " + (goodsProcurementInList.length-cantDelete) +" รายการ ");
    			if(cantDelete!=0)
    				flash("delete4","ไม่สามารถลบรายการจัดซื้อและนำเข้าทั้งหมด " + cantDelete +" รายการ ");
    		}
    		else
    		{
    			flash("notSelect2","เลือกรายการจัดซื้อและนำเข้าที่ต้องการจะลบ");
    		}
    	}
    	
		if(type.equals("durableGoods"))
		{
			tab="1";
		}
		else if(type.equals("goods"))
		{
			tab="2";
		}
    	
    	return redirect(routes.Import.importsOrder2(tab));
    }
    
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result removeProcurementDetail(){ //article
    	
    	RequestBody body = request().body();

    	JsonNode json = body.asJson();
    
    	ProcurementDetail pc;
    	models.durableArticles.Procurement procurement=null;
    	
    	
    	if(!json.get("parseData").asText().equals(""))
    	{
        	String[] procumentDetails=json.get("parseData").asText().split(",");    		
        	
        	for(int i=0;i<procumentDetails.length;i++)
        	{
        		pc = ProcurementDetail.find.byId(Long.parseLong(procumentDetails[i]));
        		if(pc.status!=OrderDetailStatus.UNCHANGE)
        		{
	        		procurement = models.durableArticles.Procurement.find.byId(pc.procurement.id);
	        		for(DurableArticles subDetail:pc.subDetails)
	        		{
	        			subDetail.status = SuppliesStatus.DELETE;
	        			subDetail.update();
	        		}
	        		pc.status = OrderDetailStatus.DELETE;
	        		pc.update();
        		}
        	}
    	}
    	/////////////////
    	
    	List<models.durableArticles.ProcurementDetail> procurementDetails = models.durableArticles.ProcurementDetail.find.where().eq("procurement", procurement).findList(); 
    	ObjectNode result = Json.newObject();
    	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
    	int i=0;
    	int count=0;
    	for(models.durableArticles.ProcurementDetail p : procurementDetails){
    		if(p.status!=OrderDetailStatus.DELETE)
    		{
	    		ObjectNode item = Json.newObject();
	    		item.put("id", p.id);
	    		if(p.fsn != null) item.put("fsn", p.fsn.descriptionId);
	    		else item.put("fsn", "null");
	    		item.put("description", p.description);
	    		item.put("quantity", p.quantity);
	    		item.put("classifier", "อัน");
	    		item.put("price", p.price);
	    		item.put("priceNoVat", p.priceNoVat);
	    		item.put("lifeTime", p.llifeTime);
	    		item.put("fileName", p.fsn.fileName);
	    		item.put("fileType", p.fsn.fileType);
	    		item.put("path", p.fsn.path);
	    		jsonArray.insert(i++, item);
    		}
    		else
    			count++;
    	}
    	result.put("type", "article");
    	result.put("length",(procurementDetails.size()-count));
	    result.put("data",jsonArray);
    	return ok(result);
    }
    
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result removeProcurementDetail2(){ //good
    	
    	RequestBody body = request().body();

    	JsonNode json = body.asJson();
    
    	models.durableGoods.ProcurementDetail pc;
    	models.durableGoods.Procurement procurement=null;
    	
    	
    	if(!json.get("parseData").asText().equals(""))
    	{
        	String[] procumentDetails=json.get("parseData").asText().split(",");    		
        	
        	for(int i=0;i<procumentDetails.length;i++)
        	{
        		pc = models.durableGoods.ProcurementDetail.find.byId(Long.parseLong(procumentDetails[i]));
        		if(pc.status!=OrderDetailStatus.UNCHANGE)
        		{
					if(pc.typeOfDurableGoods==0)//สิ้นเปลือง
					{
						MaterialCode mc=MaterialCode.find.byId(pc.code);
						
						double sumPrice=mc.pricePerEach*mc.remain;
						sumPrice=sumPrice-(pc.quantity*pc.price);
						mc.remain=mc.remain-pc.quantity;
						if(mc.remain>0)
							mc.pricePerEach=sumPrice/mc.remain;
						else
							mc.pricePerEach=0;
						
						mc.update();
					}
					else//คงทนถาวร
					{
						FSN_Description fsn = FSN_Description.find.byId(pc.code);
						
						double sumPrice=fsn.pricePerEach*fsn.remain;
						sumPrice=sumPrice-(pc.quantity*pc.price);
						fsn.remain=fsn.remain-pc.quantity;
						if(fsn.remain>0)
							fsn.pricePerEach=sumPrice/fsn.remain;
						else
							fsn.pricePerEach=0;
						
						fsn.update();
		
					}

	        		procurement = models.durableGoods.Procurement.find.byId(pc.procurement.id);
	        		for(DurableGoods subDetail:pc.subDetails)
	        		{
	        			subDetail.status = SuppliesStatus.DELETE;
	        			subDetail.update();
	        		}
	        		pc.status = OrderDetailStatus.DELETE;
	        		pc.update();
        		}
        	}
    	}
    	/////////////////
    	
    	List<models.durableGoods.ProcurementDetail> procurementDetails = models.durableGoods.ProcurementDetail.find.where().eq("procurement", procurement).findList(); 
    	ObjectNode result = Json.newObject();
    	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
    	int i=0;
    	int count=0;
    	for(models.durableGoods.ProcurementDetail p : procurementDetails){
    		
    		if(p.status!=OrderDetailStatus.DELETE)
    		{
	    		FSN_Description fsnCode=null;
	    		MaterialCode consumableGoodCode=null;
	    		
	    		if(p.typeOfDurableGoods==1)//is a durableGood
	    			fsnCode = FSN_Description.find.byId(p.code);
	    		else
	    			consumableGoodCode= MaterialCode.find.byId(p.code);
	    		
	    		ObjectNode item = Json.newObject();
	    		item.put("id", p.id);
	    		if(p.code!="") item.put("code", p.code);
	    		else item.put("code", p.code);
	    		item.put("description", p.description);
	    		item.put("quantity", p.quantity);
	    		item.put("classifier", "อัน");
	    		item.put("price", p.price);
	    		item.put("priceNoVat", p.priceNoVat);
	    		if(p.typeOfDurableGoods==1)//is a durableGood
	    		{
	    			item.put("fileName", fsnCode.fileName);
	    			item.put("fileType", fsnCode.fileType);
	    			item.put("path", fsnCode.path);
	    		}
	    		else
	    		{
	    			item.put("fileName", consumableGoodCode.fileName);
	    			item.put("fileType", consumableGoodCode.fileType);
	    			item.put("path", consumableGoodCode.path);
	    		}
	    		
	    		jsonArray.insert(i++, item);
    		}
    		else
    			count++;
    	}
    	result.put("type", "goods");
    	result.put("length",(procurementDetails.size()-count));
	    result.put("data",jsonArray);
    	return ok(result);
    }
    
    @Security.Authenticated(Secured.class)
    public static Result findNextMaterialNumber(String matKey){
        String desIdInput = matKey;
        System.out.println(matKey);
        List<MaterialCode> allMat = MaterialCode.find.where().ilike("code",desIdInput+"%").orderBy("code desc").findList();
        String lastMat = matKey + "000";
        if(allMat.size() > 0){
           lastMat = allMat.get(0).code; 
        }
        ObjectNode result = Json.newObject();
        JsonNode json;
        
            ObjectMapper mapper = new ObjectMapper();

        try{
            String jsonArray = mapper.writeValueAsString(lastMat);
            json = Json.parse(jsonArray);
            result.put("lastDes",json);
        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("stats","error1");
        }
        catch(JsonProcessingException e){
            result.put("message", e.getMessage());
            result.put("stats","error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("stats","error3");
        }

      return ok(result);
        
    }
    @Security.Authenticated(Secured.class)
    public static Result findNextFsnNumber(String fsnKey){
        String desIdInput = fsnKey;
        System.out.println(fsnKey);
        List<FSN_Description> allDes= FSN_Description.find.where().ilike("descriptionId",desIdInput+"%").orderBy("descriptionId desc").findList();
        String lastDes = fsnKey + "-0000";
        if(allDes.size() > 0){
           lastDes = allDes.get(0).descriptionId; 
        }
        ObjectNode result = Json.newObject();
        JsonNode json;
        
            ObjectMapper mapper = new ObjectMapper();

        try{
            String jsonArray = mapper.writeValueAsString(lastDes);
            json = Json.parse(jsonArray);
            result.put("lastDes",json);
        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("stats","error1");
        }
        catch(JsonProcessingException e){
            result.put("message", e.getMessage());
            result.put("stats","error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("stats","error3");
        }

      return ok(result);
        
    }
    
    @Security.Authenticated(Secured.class)
    public static Result findFsn(){
        List<FSN_Class> fsnClass;
        List<FSN_Group> fsnGroup;

        List<String> groupId = new ArrayList<String>();
        List<String> groupDes = new ArrayList<String>();
        List<String> classId = new ArrayList<String>();
        List<String> classDes = new ArrayList<String>();
        ObjectNode result = Json.newObject();
        JsonNode json;

        try{
            fsnClass = FSN_Class.find.all();
            fsnGroup = FSN_Group.find.all();

            for(FSN_Group fsnG : fsnGroup){ 
                groupId.add(fsnG.groupId);               
                groupDes.add(fsnG.groupDescription);
                
            } 
            for(FSN_Class fsnC : fsnClass){
                classId.add(fsnC.classId);
                classDes.add(fsnC.classDescription);
            }

            ObjectMapper mapper = new ObjectMapper();

            String jsonArray = mapper.writeValueAsString(groupId);
            json = Json.parse(jsonArray);
            result.put("groupId",json);

            jsonArray = mapper.writeValueAsString(groupDes);
            json = Json.parse(jsonArray);
            result.put("groupDes",json);

            jsonArray = mapper.writeValueAsString(classDes);
            json = Json.parse(jsonArray);
            result.put("classDes",json);

            jsonArray = mapper.writeValueAsString(classId);
            json = Json.parse(jsonArray);
            result.put("classId",json);

        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("stats","error1");
        }
        catch(JsonProcessingException e){
            result.put("message", e.getMessage());
            result.put("stats","error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("stats","error3");
        }

        return ok(result);
    }
    
    @Security.Authenticated(Secured.class)
    public static Result autoCompleteFsn(){
        ObjectNode result = Json.newObject();
        JsonNode json;

        List<String> fsnName = new ArrayList<String>();
        List<String> fsnCode = new ArrayList<String>();

        List<FSN_Description> fsnDes = FSN_Description.find.all();

        try{
            for(FSN_Description fd : fsnDes){
                fsnName.add(fd.descriptionDescription);
                fsnCode.add(fd.descriptionId);
            }
            ObjectMapper mapper = new ObjectMapper();

            String jsonArray = mapper.writeValueAsString(fsnName);
            json = Json.parse(jsonArray);
            result.put("fsnName",json);

            jsonArray = mapper.writeValueAsString(fsnCode);
            json = Json.parse(jsonArray);
            result.put("fsnCode",json);
        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("stats","error1");
        }
        catch(JsonProcessingException e){
            result.put("message", e.getMessage());
            result.put("stats","error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("stats","error3");
        }

        return ok(result);
    }
    
    @Security.Authenticated(Secured.class)
    public static Result autoCompleteGood(){
        ObjectNode result = Json.newObject();
        JsonNode json;
        String jsonArray;
        List<String> goodsCode = new ArrayList<String>();
        List<String> goodsName = new ArrayList<String>();

        List<String> goodsCodeFsn = new ArrayList<String>();
        List<String> goodsNameFsn = new ArrayList<String>();

        List<MaterialCode> materialCode = MaterialCode.find.all();
        List<FSN_Description> fsnDes = FSN_Description.find.all();

        try{
            for(MaterialCode mc : materialCode){
                goodsCode.add(mc.code);
                goodsName.add(mc.description);
            }
            for(FSN_Description fd : fsnDes){
                goodsCodeFsn.add(fd.descriptionId);
                goodsNameFsn.add(fd.descriptionDescription);
            }
            ObjectMapper mapper = new ObjectMapper();

            jsonArray = mapper.writeValueAsString(goodsCode);
            json = Json.parse(jsonArray);
            result.put("goodsCode",json);

            jsonArray = mapper.writeValueAsString(goodsName);
            json = Json.parse(jsonArray);
            result.put("goodsName",json);

            jsonArray = mapper.writeValueAsString(goodsCodeFsn);
            json = Json.parse(jsonArray);
            result.put("goodsCodeFsn",json);

            jsonArray = mapper.writeValueAsString(goodsNameFsn);
            json = Json.parse(jsonArray);
            result.put("goodsNameFsn",json);
        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("stats","error1");
        }
        catch(JsonProcessingException e){
            result.put("message", e.getMessage());
            result.put("stats","error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("stats","error3");
        }

        return ok(result);
    }
    @Security.Authenticated(Secured.class)
    public static Result autocompleteImportOrderCommitee(){
        List<User> allUser = User.find.all();
        List<String> namePrefix = new ArrayList<String>();        
        List<String> name = new ArrayList<String>();        
        List<String> lastname = new ArrayList<String>();
        List<String> position = new ArrayList<String>();
        List<String> code = new ArrayList<String>();
        List<String> codeName = new ArrayList<String>();
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode result = Json.newObject();
        JsonNode json;
    String jsonArray;
        try{

            for(User us : allUser){ 
                namePrefix.add(us.namePrefix);
                name.add(us.firstName);               
                lastname.add(us.lastName);
                position.add(us.position);
            } 

            jsonArray = mapper.writeValueAsString(name);
            json = Json.parse(jsonArray);
            result.put("name",json);

            jsonArray = mapper.writeValueAsString(lastname);
            json = Json.parse(jsonArray);
            result.put("lastname",json);

            jsonArray = mapper.writeValueAsString(position);
            json = Json.parse(jsonArray);
            result.put("position",json);

            jsonArray = mapper.writeValueAsString(namePrefix);
            json = Json.parse(jsonArray);
            result.put("namePrefix",json);

        }
        catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("stats","error1");
        }
        catch(JsonProcessingException e){
            result.put("message", e.getMessage());
            result.put("stats","error2");
        }
        catch(Exception e){
            result.put("message", e.getMessage());
            result.put("stats","error3");
        }
        return ok(result);
        
    }
}
