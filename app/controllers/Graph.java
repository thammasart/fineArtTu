package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.RequestBody;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Graph extends Controller {
	
	private static String colors[] = {"#7B9CDE","#EA8871","#FFC266","#70C074","#C266C2",
	             "#898BCD","#66C2DD","#EB8FAD","#A3CC66","#D48282",
	             "#83A1BF","#C28FC2","#7ACCC2","#CCCC70","#A385E0",
	             "#F0AB66","#B96A6A","#84BEA1","#99ACCA","#898BCD"};

    public static Result index() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(graph.render(user));
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getData() {
      RequestBody body = request().body();
      JsonNode json = body.asJson();
      
      String relation = json.get("relation").asText();
      String mode = json.get("mode").asText();
      int page = json.get("page").asInt();
      
      JsonNode clickedItem = json.get("clickedItem");
      int row = clickedItem.get("row").asInt();
      int column = clickedItem.get("column").asInt();
      
      ArrayNode result=null;
      
	  if(mode.equals("balance")){
		  result = getBalance(relation,row,column);
	  }else if(mode.equals("procurement")){
		  result = getProcurement(relation,row,column);
	  }else if(mode.equals("requisition")){
		  result = getRequisition(relation,row,column);
	  }else if(mode.equals("transfer")){
		  result = getTransfer(relation,row,column);
	  }else if(mode.equals("repairing")){
		  result = getRepairing(relation,row,column);
	  }else if(mode.equals("remain")){
		  result = getRemain(relation,row,column);
	  }else if(mode.equals("sold")){
		  result = getSold(relation,row,column);
	  }else if(mode.equals("donate")){
		  result = getDonate(relation,row,column);
	  }else if(mode.equals("other")){
		  result = getOther(relation,row,column);
	  }
	  
      return ok(result);
    }
    
    private static ArrayNode getHeader(){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	ArrayNode thead = JsonNodeFactory.instance.arrayNode();
    	thead.add("type");
    	thead.add("ครุภัณฑ์");
    	thead.add("วัสดุ");
    	result.add(thead);
    	return result;
    }
    
    private static ArrayNode getDetailHeader(){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	ArrayNode thead = JsonNodeFactory.instance.arrayNode();
    	ObjectNode style = Json.newObject();
    	style.put("role", "style");
    	ObjectNode annotation = Json.newObject();
    	annotation.put("role", "annotation");
    	thead.add("name");
    	thead.add("value");
    	thead.add(style);
    	thead.add(annotation);
    	result.add(thead);
    	return result;
    }
    
    private static ArrayNode getBalance(String relation,int row,int col){
    	ArrayNode result = null;
    	Calendar cal = Calendar.getInstance();
    	//System.out.println("\n\n\n\n"+row +"  "+col +"\n\n\n\n");
    	if(row == -1 && col == -1){
    		result = getHeader();
    		if(relation.equals("year")){
    			for(int i=3; i>=0; i--){
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				int year = Calendar.getInstance().get(Calendar.YEAR)-i;
    				cal.set(Calendar.YEAR, year);
    				cal.set(Calendar.WEEK_OF_YEAR, 1);
    				cal.set(Calendar.DAY_OF_WEEK, 1);
    				
    				Date startDate = cal.getTime();
    				
    				cal.set(Calendar.YEAR, year);
    				cal.set(Calendar.MONTH, 11); // 11 = december
    				cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
    				
    				Date endDate = cal.getTime();
    				tr.add(""+year);
    				List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", startDate, endDate).findList();
    				double sum = 0;
    				for(models.durableArticles.Procurement p : ps){
    					for(models.durableArticles.ProcurementDetail pd : p.details){
    						sum+= pd.price * pd.quantity;
    					}
    				}
    				tr.add(sum);
    				
    				List<models.durableGoods.Procurement> pgs = models.durableGoods.Procurement.find.where().between("addDate", startDate, endDate).findList();
    				double sum2 = 0;
    				for(models.durableGoods.Procurement p : pgs){
    					for(models.durableGoods.ProcurementDetail pd : p.details){
    						sum2+= pd.price * pd.quantity;
    					}
    				}
    				tr.add(sum2);
    				result.add(tr);
    			}
    		}else if(relation.equals("month")){
    			int year = Calendar.getInstance().get(Calendar.YEAR);
    			cal.set(Calendar.YEAR, year);
    			for(int i=0; i<12; i++){
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				cal.set(Calendar.MONTH, i);
    				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
    				Date startDate = cal.getTime();
    				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    				Date endDate = cal.getTime();
    				tr.add(new SimpleDateFormat("MMM",new Locale("th", "th")).format(cal.getTime()));
    				List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", startDate, endDate).findList();
    				double sum = 0;
    				for(models.durableArticles.Procurement p : ps){
    					for(models.durableArticles.ProcurementDetail pd : p.details){
    						sum+= pd.price * pd.quantity;
    					}
    				}
    				tr.add(sum);
    				List<models.durableGoods.Procurement> pgs = models.durableGoods.Procurement.find.where().between("addDate", startDate, endDate).findList();
    				double sum2 = 0;
    				for(models.durableGoods.Procurement p : pgs){
    					for(models.durableGoods.ProcurementDetail pd : p.details){
    						sum2+= pd.price * pd.quantity;
    					}
    				}
    				tr.add(sum2);
    				result.add(tr);
    			}
    		}else if(relation.equals("quarter")){
    			int year = Calendar.getInstance().get(Calendar.YEAR);
    			cal.set(Calendar.YEAR, year);
    			for(int i=0; i<4; i++){
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				cal.set(Calendar.MONTH, i*3);
    				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
    				Date startDate = cal.getTime();
    				
    				cal.set(Calendar.MONTH, i*3+2);
    				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    				Date endDate = cal.getTime();
    				tr.add("Q"+(i+1));
    				List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", startDate, endDate).findList();
    				double sum = 0;
    				for(models.durableArticles.Procurement p : ps){
    					for(models.durableArticles.ProcurementDetail pd : p.details){
    						sum+= pd.price * pd.quantity;
    					}
    				}
    				tr.add(sum);
    				List<models.durableGoods.Procurement> pgs = models.durableGoods.Procurement.find.where().between("addDate", startDate, endDate).findList();
    				double sum2 = 0;
    				for(models.durableGoods.Procurement p : pgs){
    					for(models.durableGoods.ProcurementDetail pd : p.details){
    						sum2+= pd.price * pd.quantity;
    					}
    				}
    				tr.add(sum2);
    				result.add(tr);
    			}
    		}
    	}else{
    		result = getDetailHeader();
    		if(col == 1){
    			if(relation.equals("year")){
    				int year = Calendar.getInstance().get(Calendar.YEAR)-(3-row);
    				cal.set(Calendar.YEAR, year);
    				cal.set(Calendar.WEEK_OF_YEAR, 1);
    				cal.set(Calendar.DAY_OF_WEEK, 1);
    				
    				Date startDate = cal.getTime();
    				
    				cal.set(Calendar.YEAR, year);
    				cal.set(Calendar.MONTH, 11); // 11 = december
    				cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
    				
    				Date endDate = cal.getTime();
    				HashMap<String,Double> listResult = new HashMap<String,Double>();
    				List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", startDate, endDate).findList();
    				for(models.durableArticles.Procurement p : ps){
    					for(models.durableArticles.ProcurementDetail pd : p.details){
    						String key = pd.fsn.typ.groupClass.group.groupDescription;
    						Double value = listResult.get(key);
    						if(value == null){
    							listResult.put(key, pd.price * pd.quantity);
    						}else{
    							listResult.put(key, listResult.get(key) + (pd.price * pd.quantity));
    						}
    					}
    				}
    				int i=0;
    				for (String key : listResult.keySet()) {
    					ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    					tr.add(key);
    					tr.add(listResult.get(key));
    					tr.add(colors[i++]);
    					tr.add(key);
    					result.add(tr);
    				}
    			}else if(relation.equals("month")){
    				
    			}else if(relation.equals("quarter")){
    				
    			}
    		}else if(col == 2){
    			if(relation.equals("year")){
    				int year = Calendar.getInstance().get(Calendar.YEAR)-(3-row);
    				System.out.println(year);
    				cal.set(Calendar.YEAR, year);
    				cal.set(Calendar.WEEK_OF_YEAR, 1);
    				cal.set(Calendar.DAY_OF_WEEK, 1);
    				
    				Date startDate = cal.getTime();
    				
    				cal.set(Calendar.YEAR, year);
    				cal.set(Calendar.MONTH, 11); // 11 = december
    				cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
    				
    				Date endDate = cal.getTime();
    				HashMap<String,Double> listResult = new HashMap<String,Double>();
    				List<models.durableGoods.Procurement> pgs = models.durableGoods.Procurement.find.where().between("addDate", startDate, endDate).findList();
    				for(models.durableGoods.Procurement p : pgs){
    					for(models.durableGoods.ProcurementDetail pd : p.details){
    						String key = pd.code.description;
    						Double value = listResult.get(key);
    						if(value == null){
    							if(pd.price * pd.quantity != 0)
    								listResult.put(key, pd.price * pd.quantity);
    						}else{
    							listResult.put(key, listResult.get(key) + (pd.price * pd.quantity));
    						}
    					}
    				}
    				int i=0;
    				for (String key : listResult.keySet()) {
    					ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    					tr.add(key);
    					tr.add(listResult.get(key));
    					tr.add(colors[i++]);
    					tr.add(key);
    					result.add(tr);
    				}
    			}else if(relation.equals("month")){
    				
    			}else if(relation.equals("quarter")){
    				
    			}
    		}
    	}
    	return result;
    }
    
    private static ArrayNode getProcurement(String relation,int row,int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getRequisition(String relation,int row,int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else{
    		
    	}    	
    	return result;
    }
    
    private static ArrayNode getTransfer(String relation,int row,int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getRepairing(String relation,int row,int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getSold(String relation,int row,int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getDonate(String relation,int row,int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getOther(String relation,int row,int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getRemain(String relation,int row,int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else{
    		
    	}
    	return result;
    }

}
