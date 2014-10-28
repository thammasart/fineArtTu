package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.RequestBody;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;
import models.type.ExportStatus;
import models.type.ImportStatus;

import java.text.DecimalFormat;
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
	
	private static Calendar cal = Calendar.getInstance();
	private static String colors[] = {"#7B9CDE","#EA8871","#FFC266","#70C074","#C266C2",
	             "#898BCD","#66C2DD","#EB8FAD","#A3CC66","#D48282",
	             "#83A1BF","#C28FC2","#7ACCC2","#CCCC70","#A385E0",
	             "#F0AB66","#B96A6A","#84BEA1","#99ACCA","#898BCD"};
	private static String descriptionBtn = "<button class='btn btn-xs btn-info' onclick='desc()' > รายละเอียด</button>";

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
      String selectedName = json.get("selectedName").asText(); 
      
      JsonNode clickedItem = json.get("clickedItem");
      int row = clickedItem.get("row").asInt();
      int column = clickedItem.get("column").asInt();
      
      JsonNode last = json.get("lastSelected");
      int lastClickedRow = last.get("row").asInt();
      int lastClickedColumn = last.get("column").asInt();
      
      
      ArrayNode result=null;
      
      System.out.println(String.format("%d %d / %d %d / %d", row,column,lastClickedRow, lastClickedColumn,page));
      
	  if(mode.equals("balance")){
		  result = getBalance(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }else if(mode.equals("procurement")){
		  result = getProcurement(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }else if(mode.equals("requisition")){
		  result = getRequisition(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }else if(mode.equals("transfer")){
		  result = getTransfer(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }else if(mode.equals("repairing")){
		  result = getRepairing(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }else if(mode.equals("remain")){
		  result = getRemain(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }else if(mode.equals("sold")){
		  result = getSold(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }else if(mode.equals("donate")){
		  result = getDonate(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }else if(mode.equals("other")){
		  result = getOther(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
	  }
	  
      return ok(result);
    }
    
    private static ArrayNode getHeader(){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	ArrayNode thead = JsonNodeFactory.instance.arrayNode();
    	ObjectNode annotation1 = Json.newObject();
    	annotation1.put("role", "annotation");
    	ObjectNode annotation2 = Json.newObject();
    	annotation2.put("role", "annotation");
    	thead.add("type");
    	thead.add("ครุภัณฑ์");
    	thead.add(annotation1);
    	thead.add("วัสดุ");
    	thead.add(annotation2);
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
    
    private static ArrayNode getBalance(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = null;
    	//System.out.println("\n\n\n\n"+row +"  "+col +"\n\n\n\n");
    	if(row == -1 && col == -1){
    		result = getHeader();
    		if(relation.equals("year")){
    			for(int i=3; i>=0; i--){
    				int year = Calendar.getInstance().get(Calendar.YEAR);
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				
    				List<Date> date = getYearDate((3-i));
    				List<Double> d = getSumBalance(date.get(0), date.get(1)); 
    				year = year-i;
    				DecimalFormat df = new DecimalFormat("#.00"); 
    				tr.add(""+year);
    				tr.add(Double.valueOf(String.format("%.2f",d.get(0))));
    				tr.add(Double.valueOf(String.format("%.2f",d.get(0))));
    				tr.add(Double.valueOf(String.format("%.2f",d.get(1))));
    				tr.add(Double.valueOf(String.format("%.2f",d.get(1))));
    				result.add(tr);
    			}
    		}else{
    			int num = 12;
    			if(relation.equals("quarter")) num = 4;
    			for(int i=0; i<num; i++){
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				List<Double> d;
    				if(relation.equals("month")){
    					List<Date> date = getMonthDate(i);
        				d = getSumBalance(date.get(0), date.get(1));
    					tr.add(new SimpleDateFormat("MMM",new Locale("th", "th")).format(cal.getTime()));
    				}else{
    					List<Date> date = getQuarterDate(i);
        				d = getSumBalance(date.get(0), date.get(1));
    					tr.add("Q"+(i+1));
    				}
    				tr.add(Double.valueOf(String.format("%.2f",d.get(0))));
    				tr.add(Double.valueOf(String.format("%.2f",d.get(0))));
    				tr.add(Double.valueOf(String.format("%.2f",d.get(1))));
    				tr.add(Double.valueOf(String.format("%.2f",d.get(1))));
    				result.add(tr);
    			}
    		}
    	}else if(page!=2){
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(row);
    		}else if(relation.equals("month")){
    			d = getMonthDate(row);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(row);
    		}
    		if(col == 1 || col == 2){
    			List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", d.get(0), d.get(1)).eq("status", ImportStatus.SUCCESS).findList();
    			result = getDetailBalanceMapArticle(ps);
    		}else if(col == 3 || col == 4){
    			List<models.durableGoods.Procurement> ps = models.durableGoods.Procurement.find.where().between("addDate", d.get(0), d.get(1)).eq("status", ImportStatus.SUCCESS).findList();
    			result = getDetailBalanceMapGoods(ps);
    		}
    	}else{
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(lRow);
    		}else if(relation.equals("month")){
    			d = getMonthDate(lRow);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(lRow);
    		}
    		if(lCol == 1 || lCol == 2){
    			List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", d.get(0), d.get(1)).eq("status", ImportStatus.SUCCESS).findList();
    			result = getTableBalanceArticle(ps,selectedName);
    		}else if(lCol == 3 || lCol == 4){
    			List<models.durableGoods.Procurement> ps = models.durableGoods.Procurement.find.where().between("addDate", d.get(0), d.get(1)).eq("status", ImportStatus.SUCCESS).findList();
    			result = getTableBalanceGoods(ps,selectedName);
    		}
    	}
    	return result;
    }
    
    private static ArrayNode getProcurement(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		result = getHeader();
    		if(relation.equals("year")){
    			for(int i=3; i>=0; i--){
    				int year = Calendar.getInstance().get(Calendar.YEAR);
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				
    				List<Date> date = getYearDate((3-i));
    				List<Integer> d = getSumProcurement(date.get(0), date.get(1)); 
    				year = year-i;
    				tr.add(""+year);
    				tr.add(d.get(0));
    				tr.add(d.get(0));
    				tr.add(d.get(1));
    				tr.add(d.get(1));
    				result.add(tr);
    			}
    		}else{
    			int num = 12;
    			if(relation.equals("quarter")) num = 4;
    			for(int i=0; i<num; i++){
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				List<Integer> d;
    				if(relation.equals("month")){
    					List<Date> date = getMonthDate(i);
        				d = getSumProcurement(date.get(0), date.get(1));
    					tr.add(new SimpleDateFormat("MMM",new Locale("th", "th")).format(cal.getTime()));
    				}else{
    					List<Date> date = getQuarterDate(i);
        				d = getSumProcurement(date.get(0), date.get(1));
    					tr.add("Q"+(i+1));
    				}
    				tr.add(d.get(0));
    				tr.add(d.get(0));
    				tr.add(d.get(1));
    				tr.add(d.get(1));
    				result.add(tr);
    			}
    		}
    	}else if(page!=2){
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(row);
    		}else if(relation.equals("month")){
    			d = getMonthDate(row);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(row);
    		}
    		if(col == 1 || col == 2){
    			List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", d.get(0), d.get(1)).eq("status", ImportStatus.SUCCESS).findList();
    			result = getDetailProcurementMapArticle(ps);
    		}else if(col == 3 || col == 4){
    			List<models.durableGoods.Procurement> ps = models.durableGoods.Procurement.find.where().between("addDate", d.get(0), d.get(1)).eq("status", ImportStatus.SUCCESS).findList();
    			result = getDetailProcurementMapGoods(ps);
    		}
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getRequisition(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = getDetailHeader();
    	if(row == -1 && col == -1){
    		if(relation.equals("year")){
    			for(int i=3; i>=0; i--){
    				int year = Calendar.getInstance().get(Calendar.YEAR);
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				
    				List<Date> date = getYearDate((3-i));
    				int d = getSumRequisition(date.get(0), date.get(1)); 
    				year = year-i;
    				tr.add(""+year);
    				tr.add(d);
    				tr.add(colors[3-i]);
    				tr.add(d);
    				result.add(tr);
    			}
    		}else{
    			int num = 12;
    			if(relation.equals("quarter")) num = 4;
    			for(int i=0; i<num; i++){
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				int d;
    				if(relation.equals("month")){
    					List<Date> date = getMonthDate(i);
        				d = getSumRequisition(date.get(0), date.get(1));
    					tr.add(new SimpleDateFormat("MMM",new Locale("th", "th")).format(cal.getTime()));
    				}else{
    					List<Date> date = getQuarterDate(i);
        				d = getSumRequisition(date.get(0), date.get(1));
    					tr.add("Q"+(i+1));
    				}
    				tr.add(d);
    				tr.add(colors[i]);
    				tr.add(d);
    				result.add(tr);
    			}
    		}
    	}else if(page != 2){
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(row);
    		}else if(relation.equals("month")){
    			d = getMonthDate(row);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(row);
    		}
			List<models.consumable.Requisition> rs = models.consumable.Requisition.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
			result = getDetailRequisitionMap(rs);
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getTransfer(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else if(page != 2){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getRepairing(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else if(page != 2){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getSold(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else if(page != 2){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getDonate(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else if(page != 2){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getOther(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else if(page !=2){
    		
    	}else{
    		
    	}
    	return result;
    }
    
    private static ArrayNode getRemain(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	if(row == -1 && col == -1){
    		
    	}else if(page != 2){
    		
    	}else{
    		
    	}
    	return result;
    }

    private static ArrayNode getDetailBalanceMapArticle(List<models.durableArticles.Procurement> ps){
    	ArrayNode result = ps.size() > 0 ? getDetailHeader() : JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Double> listResult = new HashMap<String,Double>();
		for(models.durableArticles.Procurement p : ps){
			for(models.durableArticles.ProcurementDetail pd : p.details){
				String key = pd.fsn.typ.groupClass.group.groupDescription;
				Double value = listResult.get(key);
				if(value == null){
					if(pd.quantity * pd.price != 0){
						listResult.put(key, pd.quantity * pd.price);
					}else{
						listResult.put(key, pd.price);
					}
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
		return result;
    }
    
    private static ArrayNode getDetailBalanceMapGoods(List<models.durableGoods.Procurement> ps){
    	ArrayNode result = ps.size() > 0 ? getDetailHeader() : JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Double> listResult = new HashMap<String,Double>();
		for(models.durableGoods.Procurement p : ps){
			for(models.durableGoods.ProcurementDetail pd : p.details){
				MaterialCode c = MaterialCode.find.byId(pd.code);
				String key = c.materialType.typeName;
				Double value = listResult.get(key);
				if(value == null){
					if(pd.quantity * pd.price != 0){
						listResult.put(key, pd.quantity * pd.price);
					}else{
						listResult.put(key, pd.price);
					}
				}else{
					listResult.put(key, listResult.get(key) + (pd.price * pd.quantity));
				}
			}
		}
		int i=0;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add(key);
			tr.add(Double.valueOf(String.format("%.2f",listResult.get(key))));
			tr.add(colors[i++]);
			tr.add(key);
			result.add(tr);
		}
    	return result;
    }
    
    private static ArrayNode getDetailProcurementMapArticle(List<models.durableArticles.Procurement> ps){
    	ArrayNode result = ps.size() > 0 ? getDetailHeader() : JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
		for(models.durableArticles.Procurement p : ps){
			for(models.durableArticles.ProcurementDetail pd : p.details){
				String key = pd.fsn.typ.groupClass.group.groupDescription;
				Integer value = listResult.get(key);
				if(value == null){
					if(pd.quantity != 0){
						listResult.put(key, pd.quantity);
					}
				}else{
					listResult.put(key, listResult.get(key) + (pd.quantity));
				}
			}
		}
		int i=0;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add(key);
			tr.add(Double.valueOf(String.format("%.2f",listResult.get(key))));
			tr.add(colors[i++]);
			tr.add(key);
			result.add(tr);
		}
		return result;
    }
    
    private static ArrayNode getDetailProcurementMapGoods(List<models.durableGoods.Procurement> ps){
    	ArrayNode result = ps.size() > 0 ? getDetailHeader() : JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
		for(models.durableGoods.Procurement p : ps){
			for(models.durableGoods.ProcurementDetail pd : p.details){
				MaterialCode c = MaterialCode.find.byId(pd.code);
				String key = c.materialType.typeName;
				Integer value = listResult.get(key);
				if(value == null){
					if(pd.quantity != 0){
						listResult.put(key, pd.quantity);
					}
				}else{
					listResult.put(key, listResult.get(key) + pd.quantity);
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
    	return result;
    }
    
    private static ArrayNode getDetailRequisitionMap(List<models.consumable.Requisition> rs){
    	ArrayNode result = rs.size() > 0 ? getDetailHeader() : JsonNodeFactory.instance.arrayNode();
    	HashMap<String, Integer> listResult = new HashMap<String,Integer>();
    	for(models.consumable.Requisition r : rs){
    		for(models.consumable.RequisitionDetail rd : r.detils){
    			String key = rd.code.materialType.typeName;
    			Integer value = listResult.get(key);
				if(value == null){
					if(rd.quantity != 0){
						listResult.put(key, rd.quantity);
					}
				}else{
					listResult.put(key, listResult.get(key) + rd.quantity);
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
		return result;
    }
    
    private static List<Date> getYearDate(int row){
    	int year = Calendar.getInstance().get(Calendar.YEAR)-(3-row);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, 1);
		cal.set(Calendar.DAY_OF_WEEK, 1);
		
		Date startDate = cal.getTime();
		
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 11); // 11 = december
		cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
		
		Date endDate = cal.getTime();
		
		List<Date> d = new ArrayList<Date>();
		d.add(startDate);
		d.add(endDate);
		return d;
    }
    
    private static List<Date> getQuarterDate(int row){
    	int year = Calendar.getInstance().get(Calendar.YEAR);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, row*3);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date startDate = cal.getTime();
		
		cal.set(Calendar.MONTH, row*3+2);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = cal.getTime();
		
		List<Date> d = new ArrayList<Date>();
		d.add(startDate);
		d.add(endDate);
		return d;
    } 
    
    private static List<Date> getMonthDate(int row){
    	int year = Calendar.getInstance().get(Calendar.YEAR);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, row);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date startDate = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = cal.getTime();
		
		List<Date> d = new ArrayList<Date>();
		d.add(startDate);
		d.add(endDate);
		return d;
    }
    
    private static List<Double> getSumBalance(Date startDate,Date endDate){
    	List<Double> d = new ArrayList<Double>();
    	List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", startDate, endDate).eq("status", ImportStatus.SUCCESS).findList();
		double sum = 0;
		for(models.durableArticles.Procurement p : ps){
			for(models.durableArticles.ProcurementDetail pd : p.details){
				sum+= pd.price * pd.quantity;
			}
		}
		d.add(Double.valueOf(String.format("%.2f",sum)));
		List<models.durableGoods.Procurement> pgs = models.durableGoods.Procurement.find.where().between("addDate", startDate, endDate).eq("status", ImportStatus.SUCCESS).findList();
		double sum2 = 0;
		for(models.durableGoods.Procurement p : pgs){
			for(models.durableGoods.ProcurementDetail pd : p.details){
				sum2+= pd.price * pd.quantity;
			}
		}
		d.add(Double.valueOf(String.format("%.2f",sum2)));
		return d;
    }
    
    private static List<Integer> getSumProcurement(Date startDate, Date endDate){
    	List<Integer> d = new ArrayList<Integer>();
    	List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().between("addDate", startDate, endDate).eq("status", ImportStatus.SUCCESS).findList();
		List<models.durableGoods.Procurement> pgs = models.durableGoods.Procurement.find.where().between("addDate", startDate, endDate).eq("status", ImportStatus.SUCCESS).findList();
		d.add(ps.size());
		d.add(pgs.size());
		return d;
    }
    
    private static int getSumRequisition(Date startDate, Date endDate){
    	return models.consumable.Requisition.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList().size();
    }
    
    private static ArrayNode getTableBalanceArticle(List<models.durableArticles.Procurement> ps,String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Double> listResult = new HashMap<String,Double>();
		for(models.durableArticles.Procurement p : ps){
			for(models.durableArticles.ProcurementDetail pd : p.details){
				if(pd.fsn.typ.groupClass.group.groupDescription.equals(selectedName)){
					String key = pd.fsn.descriptionDescription;
					Double value = listResult.get(key);
					if(value == null){
						if(pd.quantity * pd.price != 0){
							listResult.put(key, pd.quantity * pd.price);
						}
					}else{
						listResult.put(key, listResult.get(key) + (pd.quantity * pd.price));
					}
				}
			}
		}
		int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%.2f",listResult.get(key)));
			tr.add(descriptionBtn);
			result.add(tr);
		}
		return result;
    }
    
    private static ArrayNode getTableBalanceGoods(List<models.durableGoods.Procurement> ps,String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Double> listResult = new HashMap<String,Double>();
    	for(models.durableGoods.Procurement p : ps){
    		for(models.durableGoods.ProcurementDetail pd : p.details){
    			MaterialCode c = MaterialCode.find.byId(pd.code);
    			if(c.materialType.typeName.equals(selectedName)){
    				String key = c.description;
					Double value = listResult.get(key);
					System.out.println(String.format("%s\t%.2f\n", key,value));
					if(value == null){
						if(pd.quantity * pd.price != 0){
							listResult.put(key, pd.quantity * pd.price);
						}
					}else{
						listResult.put(key, listResult.get(key) + (pd.quantity * pd.price));
					}
				}
    		}
    		
    	}
    	int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%.2f",listResult.get(key)));
			tr.add(descriptionBtn);
			result.add(tr);
		}
    	return result;
    }
}
