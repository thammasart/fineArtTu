package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.RequestBody;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import javax.persistence.ManyToOne;

import models.durableArticles.DurableArticles;
import models.durableArticles.ProcurementDetail;

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
    public static Result importsInstituteAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsInstituteAdd.render(user));
    }


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



        return redirect(routes.Import.importsInstitute());
    }
    

    //----------------------------------------------------------------------------------------------------


    @Security.Authenticated(Secured.class)
        public static Result importsMaterial() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<FSN_Description> fsns = FSN_Description.find.all();                    //ครุภัณฑ์
        List<MaterialCode> goodsCode = MaterialCode.find.all();                            //วัสดุ
        return ok(importsMaterial.render(fsns,goodsCode,user));
    }

    @Security.Authenticated(Secured.class)
        public static Result importsMaterialDurableArticlesAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<FSN_Type> groupType = FSN_Type.find.all(); 
        return ok(importsMaterialDurableArticlesAdd.render(groupType,user));
    }

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

        System.out.println( gId +"\n"+ gD +"\n"+ cId +"\n"+ cD +"\n"+ tId +"\n"+ tD +"\n"+ fsnId +"\n"+ fsnDes );

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

        return redirect(routes.Import.importsMaterial());
    }   


    //----------------------------------------------------------------------------------------------------
    @Security.Authenticated(Secured.class)
        public static Result importsMaterialDurableGoodsAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialDurableGoodsAdd.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsMaterialConsumableGoodsAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsMaterialConsumableGoodsAdd.render(user));
    }

    public static Result saveNewMaterialDurableGoods(){

        DynamicForm form = Form.form().bindFromRequest();

        Form<MaterialCode> newCodeForm = Form.form(MaterialCode.class).bindFromRequest();
        MaterialCode newCode = newCodeForm.get();


        if(form.get("chosenType").equals("1"))
        {
            newCode.typeOfGood = "วัสดุคงทนถาวร";
            newCode.minNumberToAlert =0;
        }
        else
        {
            newCode.typeOfGood = "วัสดุสิ้นเปลือง";
        }

        newCode.materialType = MaterialType.find.byId(form.get("chosen"));   //connect link

        newCode.save();

        return redirect(routes.Import.importsMaterial());
    }

    //----------------------------------------------------------------------------------------------------
    @Security.Authenticated(Secured.class)
        public static Result importsOrder() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<models.durableArticles.Procurement> aProcurement = models.durableArticles.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
        //List<models.durableArticles.Procurement> aProcurement = models.durableArticles.Procurement.find.all();
        List<models.durableGoods.Procurement> gProcurement = models.durableGoods.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
        
        
        return ok(importsOrder.render(aProcurement,gProcurement,user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderDurableArticlesAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        models.durableArticles.Procurement order= new models.durableArticles.Procurement();
        order.status = ImportStatus.INIT;
        order.save();
        return ok(importsOrderDurableArticlesAdd.render(order,user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderDurableArticlesAddMaterial1() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderDurableArticlesAddMaterial1.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderDurableArticlesAddMaterial2() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderDurableArticlesAddMaterial2.render(user));
    }
    

    public static Result saveNewArticlesOrder(){
    	DynamicForm form = Form.form().bindFromRequest();
    	System.out.println(Form.form(models.durableArticles.Procurement.class).bindFromRequest());
    	
    	//models.durableArticles.Procurement articlesOrder = Form.form(models.durableArticles.Procurement.class).bindFromRequest().get();
    	
    	models.durableArticles.Procurement articlesOrder = models.durableArticles.Procurement.find.byId(Long.parseLong(form.get("id")));
    	try {
    		Date date;
	        if(!form.get("addDate_p").equals("")) {
				date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(form.get("addDate_p"));
				articlesOrder.addDate = date;
	        }
	        if(!form.get("checkDate_p").equals("")){
	        	date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(form.get("checkDate_p"));
	        	articlesOrder.checkDate = date;
	        }
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	System.out.println(articlesOrder.checkDate.toLocaleString());
        
        articlesOrder.status = ImportStatus.SUCCESS;        
        articlesOrder.save();
        return redirect(routes.Import.importsOrder());
    }
    
    public static Result saveNewGoodsOrder(){
    	DynamicForm form = Form.form().bindFromRequest();
    	System.out.println(Form.form(models.durableGoods.Procurement.class).bindFromRequest());
    	
    	System.out.println(Long.parseLong(form.get("id")));
    	models.durableGoods.Procurement goodsOrder = models.durableGoods.Procurement.find.byId(Long.parseLong(form.get("id")));
    	
    	try {
    		Date date;
	        if(!form.get("addDate_p").equals("")) {
				date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(form.get("addDate_p"));
				goodsOrder.addDate = date;
	        }
	        if(!form.get("checkDate_p").equals("")){
	        	date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(form.get("checkDate_p"));
	        	goodsOrder.checkDate = date;
	        }
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	System.out.println(goodsOrder.checkDate.toLocaleString());
        
    	goodsOrder.status = ImportStatus.SUCCESS;        
    	goodsOrder.save();
        return redirect(routes.Import.importsOrder());
    }
    
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveNewGoodsOrderDetail(){
    	RequestBody body = request().body();
    	System.out.println(body);
    	JsonNode json = body.asJson();
    	models.durableGoods.Procurement procurement = models.durableGoods.Procurement.find.byId(Long.parseLong(json.get("procurementId").asText()));
    	models.durableGoods.ProcurementDetail procurementDetail = new models.durableGoods.ProcurementDetail();
    	DurableGoods durableGoods = new DurableGoods();
    	
    	procurementDetail.description = json.get("description").asText();
    	procurementDetail.priceNoVat = Double.parseDouble(json.get("priceNoVat").asText());
    	procurementDetail.price = Double.parseDouble(json.get("price").asText());
    	procurementDetail.quantity = Integer.parseInt(json.get("quantity").asText());
    	//procurementDetail.classifier = json.get("classifier").asText();
    	procurementDetail.brand = json.get("brand").asText();
    	procurementDetail.serialNumber = json.get("serialNumber").asText();
    	//procurementDetail.partOfPic = json.get("serialNumber").asText();
    	procurementDetail.procurement = procurement;
    	procurementDetail.save();
    	
    	List<models.durableGoods.ProcurementDetail> procurementDetails = models.durableGoods.ProcurementDetail.find.where().eq("procurement", procurement).findList(); 
    	ObjectNode result = Json.newObject();
    	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
    	int i=0;
    	
    	for(models.durableGoods.ProcurementDetail p : procurementDetails){
    		ObjectNode item = Json.newObject();
    		item.put("fsn", "fsnCode");
    		item.put("description", p.description);
    		item.put("quantity", p.quantity);
    		item.put("classifier", "อัน");
    		item.put("price", p.price);
    		item.put("priceNoVat", p.priceNoVat);
    		jsonArray.insert(i++, item);
    	}
    	result.put("type", "goods");
    	result.put("length",procurementDetails.size());
	    result.put("data",jsonArray);
    
    	return ok(result);
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveNewArticlesOrderDetail(){
    	// TODO : save detail
    	RequestBody body = request().body();
    	System.out.println(body);
    	JsonNode json = body.asJson();
    	models.durableArticles.Procurement procurement = models.durableArticles.Procurement.find.byId(Long.parseLong(json.get("procurementId").asText()));
    	models.durableArticles.ProcurementDetail procurementDetail = new models.durableArticles.ProcurementDetail();
    	DurableArticles durableArticles = new DurableArticles();
    	
    	procurementDetail.description = json.get("description").asText();
    	procurementDetail.priceNoVat = Double.parseDouble(json.get("priceNoVat").asText());
    	procurementDetail.price = Double.parseDouble(json.get("price").asText());
    	procurementDetail.quantity = Integer.parseInt(json.get("quantity").asText());
    	//procurementDetail.classifier = json.get("classifier").asText();
    	procurementDetail.llifeTime = Double.parseDouble(json.get("llifeTime").asText());
    	procurementDetail.alertTime = Double.parseDouble(json.get("alertTime").asText());
    	procurementDetail.brand = json.get("brand").asText();
    	procurementDetail.serialNumber = json.get("serialNumber").asText();
    	//procurementDetail.partOfPic = json.get("serialNumber").asText();
    	procurementDetail.procurement = procurement;
    	procurementDetail.save();
    	
    	
    	/*durableArticles.code = json.get("code").asText();
    	durableArticles.codeFromStock = json.get("codeFromStock").asText();
    	durableArticles.status = SuppliesStatus.NORMAL;
    	durableArticles.detail = procurementDetail;*/
    	
    	List<models.durableArticles.ProcurementDetail> procurementDetails = models.durableArticles.ProcurementDetail.find.where().eq("procurement", procurement).findList(); 
    	ObjectNode result = Json.newObject();
    	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
    	int i=0;
    	for(models.durableArticles.ProcurementDetail p : procurementDetails){
    		ObjectNode item = Json.newObject();
    		item.put("fsn", "fsnCode");
    		item.put("description", p.description);
    		item.put("quantity", p.quantity);
    		item.put("classifier", "อัน");
    		item.put("price", p.price);
    		item.put("priceNoVat", p.priceNoVat);
    		item.put("lifeTime", p.llifeTime);
    		jsonArray.insert(i++, item);
    	}
    	result.put("type", "article");
    	result.put("length",procurementDetails.size());
	    result.put("data",jsonArray);
    	return ok(result);
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result importsCancelOrder(){
    	RequestBody body = request().body();
    	JsonNode json = body.asJson();
    	System.out.println(json.get("id").toString());
    	String s = json.get("typeOfOrder").asText();
    	System.out.println(s);
    	models.durableArticles.Procurement articlesOrder = null;
    	models.durableGoods.Procurement goodsOrder = null;
    	
    	if(s.equals("article")){
    		System.out.println("in articles");
    		articlesOrder = models.durableArticles.Procurement.find.byId(Long.parseLong(json.get("id").toString()));
        	articlesOrder.status = ImportStatus.CANCEL;
        	articlesOrder.update();
    	}else{
    		System.out.println("in good");
    		goodsOrder = models.durableGoods.Procurement.find.byId(Long.parseLong(json.get("id").toString()));
    		goodsOrder.status = ImportStatus.CANCEL;
    		goodsOrder.update();
    	}
        return redirect(routes.Import.importsOrder());
    }
    
    

    @Security.Authenticated(Secured.class)
        public static Result importsOrderGoodsAdd() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        models.durableGoods.Procurement order= new models.durableGoods.Procurement();
        order.status = ImportStatus.INIT;
        order.save();
        return ok(importsOrderGoodsAdd.render(order,user));
    }
    
    @Security.Authenticated(Secured.class)
        public static Result importsOrderGoodsAddMaterial1() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderGoodsAddMaterial1.render(user));
    }
    @Security.Authenticated(Secured.class)
        public static Result importsOrderGoodsAddMaterial2() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(importsOrderGoodsAddMaterial2.render(user));
    }

    
    @Security.Authenticated(Secured.class)
    public static Result findNextFsnNumber(){
//        String desIdInput = "";
//        String desIdOutput = "";
//        FSN_Description lastDes = FSN_Description.find.where().ilike("descriptionId",desIdInput+"%").orderBy("descriptionId desc").findList().get(0);
//        
//        ObjectNode result = Json.newObject();
//        JsonNode json;
//        
//            ObjectMapper mapper = new ObjectMapper();
//
//            String jsonArray = mapper.writeValueAsString(groupId);
//            json = Json.parse(jsonArray);
//            result.put("groupId",json);
//
        return TODO;
//        
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
    
}
