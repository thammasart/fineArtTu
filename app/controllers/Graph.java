package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.RequestBody;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;
import models.durableArticles.Auction;
import models.durableArticles.AuctionDetail;
import models.durableArticles.Borrow;
import models.durableArticles.BorrowDetail;
import models.durableArticles.Donation;
import models.durableArticles.DonationDetail;
import models.durableArticles.DurableArticles;
import models.durableArticles.InternalTransfer;
import models.durableArticles.InternalTransferDetail;
import models.durableArticles.OtherTransfer;
import models.durableArticles.OtherTransferDetail;
import models.durableArticles.Repairing;
import models.durableArticles.RepairingDetail;
import models.durableGoods.DurableGoods;
import models.consumable.Requisition;
import models.consumable.RequisitionDetail;
import models.fsnNumber.FSN_Description;
import models.type.ExportStatus;
import models.type.ImportStatus;

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
	private static String[] department = {"สาขาวิชาการละคอน","สาขาวิชาศิลปะการออกแบบพัสตราภรณ์","สาขาวิชาศิลปะการออกแบบอุตสาหกรรม","สำนักงานเลขานุการ"};

    public static Result index() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        return ok(graph.render(user));
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
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
      String request = json.get("request").asText();
      if(request.equals("search")){
    	  
    	  String query = json.get("query").asText();
    	  result = getSearchResult(query);
    	  
      }else{
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
		  }else if(mode.equals("borrow")){
			  result = getBorrow(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
		  }else if(mode.equals("remain")){
			  result = getRemain(relation,row,column,page,lastClickedRow,lastClickedColumn,selectedName);
		  }
      }
	  
      return ok(result);
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public static Result getDescription() {
    	RequestBody body = request().body();
        JsonNode json = body.asJson();
        
        String className = json.get("className").asText();
        String[] ids = className.equals("remain") ? json.get("ids").asText().split("/"):json.get("ids").asText().split(",");
        String result = "no data";
        
        if(className.equals("durableArticle")){
        	result = getArticleHTML(ids);
        }else if(className.equals("materialCode")){
        	result = getGoodsHTML(ids);
        }else if(className.equals("procurementDurableArticle")){
        	result = getProcurementArticleHTML(ids);
        }else if(className.equals("procurementMaterialCode")){
        	result = getProcurementMaterialHTML(ids);
        }else if(className.equals("requisition")){
        	result = getRequisitionHTML(ids);
        }else if(className.equals("internalTransfer")){
        	result = getInternalTransferHTML(ids);
        }else if(className.equals("auction")){
        	result = getAuctionHTML(ids);
        }else if(className.equals("donate")){
        	result = getDonationHTML(ids);
        }else if(className.equals("otherTransfer")){
        	result = getOtherTransferHTML(ids);
        }else if(className.equals("repair")){
        	result = getRepairHTML(ids);
        }else if(className.equals("borrow")){
        	result = getBorrowHTML(ids);
        }else if(className.equals("remain")){
        	if(ids.length < 2){
        		if(json.get("ids").asText().charAt(0) == '/'){
        			result = getRemainHTML(new String[0],ids[0].split(","));
        		}else{
        			result = getRemainHTML(ids[0].split(","),new String[0]);
        		}
        	}else{
        		result = getRemainHTML(ids[0].split(","),ids[1].split(","));
        	}
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
    
    private static ArrayNode getHeader(ArrayList<String> columns){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	ArrayNode thead = JsonNodeFactory.instance.arrayNode();
    	thead.add("type");
    	for(String column : columns){
    		ObjectNode annotation = Json.newObject();
    		annotation.put("role", "annotation");
    		thead.add(column);
    		thead.add(annotation);
    	}
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
    
    private static ArrayNode getDetailHeader(ArrayList<String> columns){
    	
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	ArrayNode thead = JsonNodeFactory.instance.arrayNode();
    	thead.add("type");
    	for(String column : columns){
    		thead.add(column);
    	}
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
    			result = getTableBalanceArticle(ps, selectedName, col);
    		}else if(lCol == 3 || lCol == 4){
    			List<models.durableGoods.Procurement> ps = models.durableGoods.Procurement.find.where().between("addDate", d.get(0), d.get(1)).eq("status", ImportStatus.SUCCESS).findList();
    			result = getTableBalanceGoods(ps, selectedName);
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
    			result = getTableProcurementArticle(ps,selectedName);
    		}else if(lCol == 3 || lCol == 4){
    			List<models.durableGoods.Procurement> ps = models.durableGoods.Procurement.find.where().between("addDate", d.get(0), d.get(1)).eq("status", ImportStatus.SUCCESS).findList();
    			result = getTableProcurementGoods(ps,selectedName);
    		}
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
			List<Requisition> rs = Requisition.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
			result = getDetailRequisitionMap(rs);
    	}else{
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(lRow);
    		}else if(relation.equals("month")){
    			d = getMonthDate(lRow);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(lRow);
    		}
			List<Requisition> rs = Requisition.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
			result = getTableRequisition( rs , selectedName );
    	}
    	return result;
    }
    
    private static ArrayNode getTransfer(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayList<String> columns = new ArrayList<String>();
    	columns.add("โอนย้ายภายใน");
    	columns.add("จำหน่าย");
    	columns.add("บริจาค");
    	columns.add("อื่นๆ");
    	ArrayNode result = getHeader(columns);
    	if(row == -1 && col == -1){
    		if(relation.equals("year")){
    			for(int i=3; i>=0; i--){
    				int year = Calendar.getInstance().get(Calendar.YEAR);
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				
    				List<Date> date = getYearDate((3-i));
    				ArrayList<Integer> d = getSumTransfer(date.get(0), date.get(1)); 
    				year = year-i;
    				tr.add(""+year);
    				tr.add(d.get(0));
    				tr.add(d.get(0));
    				tr.add(d.get(1));
    				tr.add(d.get(1));
    				tr.add(d.get(2));
    				tr.add(d.get(2));
    				tr.add(d.get(3));
    				tr.add(d.get(3));
    				result.add(tr);
    			}
    		}else{
    			int num = 12;
    			if(relation.equals("quarter")) num = 4;
    			for(int i=0; i<num; i++){
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				ArrayList<Integer> d;
    				if(relation.equals("month")){
    					List<Date> date = getMonthDate(i);
        				d = getSumTransfer(date.get(0), date.get(1));
    					tr.add(new SimpleDateFormat("MMM",new Locale("th", "th")).format(cal.getTime()));
    				}else{
    					List<Date> date = getQuarterDate(i);
        				d = getSumTransfer(date.get(0), date.get(1));
    					tr.add("Q"+(i+1));
    				}
    				tr.add(d.get(0));
    				tr.add(d.get(0));
    				tr.add(d.get(1));
    				tr.add(d.get(1));
    				tr.add(d.get(2));
    				tr.add(d.get(2));
    				tr.add(d.get(3));
    				tr.add(d.get(3));
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
			
    		if(col == 1 || col == 2){
    			List<InternalTransfer> rs = InternalTransfer.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList(); 
    			result = getDetailInternalTranfersMap(rs);
    		}else if(col == 3 || col == 4){
    			List<Auction> rs = Auction.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    			result = getDetailAuctionTranfersMap(rs);
    		}else if(col == 5 || col == 6){
    			List<Donation> rs = Donation.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    			result = getDetailDonationTranfersMap(rs);
    		}else if(col == 7 || col == 8){
    			List<OtherTransfer> rs = OtherTransfer.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    			result = getDetailOtherTranfersMap(rs);
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
    			List<InternalTransfer> rs = InternalTransfer.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList(); 
    			result = getTableInternalTranfers(rs, selectedName);
    		}else if(lCol == 3 || lCol == 4){
    			List<Auction> rs = Auction.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    			result = getTableAuctionTranfers(rs, selectedName);
    		}else if(lCol == 5 || lCol == 6){
    			List<Donation> rs = Donation.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    			result = getTableDonationTranfers(rs, selectedName);
    		}else if(lCol == 7 || lCol == 8){
    			List<OtherTransfer> rs = OtherTransfer.find.where().between("approveDate", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    			result = gettableOtherTranfers(rs, selectedName);
    		}    		
    	}
    	return result;
    }
    
	private static ArrayNode getRepairing(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayList<String> columns = new ArrayList<String>();
    	columns.add("ซ่อมแล้ว");
    	columns.add("กำลังซ่อม");
    	ArrayNode result = getHeader(columns);
    	if(row == -1 && col == -1){
    		if(relation.equals("year")){
    			for(int i=3; i>=0; i--){
    				int year = Calendar.getInstance().get(Calendar.YEAR);
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				
    				List<Date> date = getYearDate((3-i));
    				ArrayList<Integer> d = getSumRepair(date.get(0), date.get(1)); 
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
    				ArrayList<Integer> d;
    				if(relation.equals("month")){
    					List<Date> date = getMonthDate(i);
        				d = getSumRepair(date.get(0), date.get(1));
    					tr.add(new SimpleDateFormat("MMM",new Locale("th", "th")).format(cal.getTime()));
    				}else{
    					List<Date> date = getQuarterDate(i);
        				d = getSumRepair(date.get(0), date.get(1));
    					tr.add("Q"+(i+1));
    				}
    				tr.add(d.get(0));
    				tr.add(d.get(0));
    				tr.add(d.get(1));
    				tr.add(d.get(1));
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
    		List<Repairing> rs = null;
    		if(col == 1 || col == 2){
    			rs = Repairing.find.where().between("dateOfReceiveFromRepair", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    		}else if(col == 3 || col == 4){
    			rs = Repairing.find.where().between("dateOfSentToRepair", d.get(0), d.get(1)).eq("status", ExportStatus.REPAIRING).findList();
    		}
    		result = getDetailRepairingMap(rs);
    	}else{
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(lRow);
    		}else if(relation.equals("month")){
    			d = getMonthDate(lRow);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(lRow);
    		}
    		
    		List<Repairing> rs = null;
    		if(lCol == 1 || lCol == 2){
    			rs = Repairing.find.where().between("dateOfReceiveFromRepair", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    		}else if(lCol == 3 || lCol == 4){
    			rs = Repairing.find.where().between("dateOfSentToRepair", d.get(0), d.get(1)).eq("status", ExportStatus.REPAIRING).findList();
    		}
    		result = getTableRepairing(rs, selectedName);
    	}
    	return result;
    }
    

	private static ArrayNode getBorrow(String relation, int row, int col, int page, int lRow, int lCol, String selectedName) {
		ArrayList<String> columns = new ArrayList<String>();
    	columns.add("รับคืนเรียบร้อย");
    	columns.add("ถูกยืม");
    	ArrayNode result = getHeader(columns);
    	if(row == -1 && col == -1){
    		if(relation.equals("year")){
    			for(int i=3; i>=0; i--){
    				int year = Calendar.getInstance().get(Calendar.YEAR);
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				
    				List<Date> date = getYearDate((3-i));
    				ArrayList<Integer> d = getSumBorrow(date.get(0), date.get(1)); 
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
    				ArrayList<Integer> d;
    				if(relation.equals("month")){
    					List<Date> date = getMonthDate(i);
        				d = getSumRepair(date.get(0), date.get(1));
    					tr.add(new SimpleDateFormat("MMM",new Locale("th", "th")).format(cal.getTime()));
    				}else{
    					List<Date> date = getQuarterDate(i);
        				d = getSumRepair(date.get(0), date.get(1));
    					tr.add("Q"+(i+1));
    				}
    				tr.add(d.get(0));
    				tr.add(d.get(0));
    				tr.add(d.get(1));
    				tr.add(d.get(1));
    				result.add(tr);
    			}
    		}
    	}else if(page != 2){
    		//TODO borrow map
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(row);
    		}else if(relation.equals("month")){
    			d = getMonthDate(row);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(row);
    		}
    		List<models.durableArticles.Borrow> bs = null;
    		if(col == 1 || col == 2){
    			bs = models.durableArticles.Borrow.find.where().between("dateOfEndBorrow", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    		}else if(col == 3 || col == 4){
    			bs = models.durableArticles.Borrow.find.where().between("dateOfStartBorrow", d.get(0), d.get(1)).eq("status", ExportStatus.BORROW).findList();
    		}
    		result = getDetailBorrowMap(bs);
    	}else{
    		//TODO borrow table
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(lRow);
    		}else if(relation.equals("month")){
    			d = getMonthDate(lRow);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(lRow);
    		}
    		
    		List<models.durableArticles.Borrow> bs = null;
    		if(lCol == 1 || lCol == 2){
    			bs = models.durableArticles.Borrow.find.where().between("dateOfEndBorrow", d.get(0), d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    		}else if(lCol == 3 || lCol == 4){
    			bs = models.durableArticles.Borrow.find.where().between("dateOfStartBorrow", d.get(0), d.get(1)).eq("status", ExportStatus.BORROW).findList();
    		}
    		result = getTableBorrow(bs, selectedName);
    	}
    	return result;
	}

	private static ArrayNode getRemain(String relation,int row,int col, int page, int lRow, int lCol, String selectedName){
    	ArrayNode result = getDetailHeader();
    	if(row == -1 && col == -1){
    		if(relation.equals("year")){
    			for(int i=3; i>=0; i--){
    				int year = Calendar.getInstance().get(Calendar.YEAR);
    				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    				
    				List<Date> date = getYearDate((3-i));
    				int d = getSumRemain( date.get(1)); 
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
        				d = getSumRemain(date.get(1));
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
    		List<models.durableGoods.ProcurementDetail> ps =  models.durableGoods.ProcurementDetail.find.where().le("procurement.addDate", d.get(1)).eq("procurement.status", ImportStatus.SUCCESS).eq("typeOfDurableGoods", 0).findList();
    		List<Requisition> rs = Requisition.find.where().le("approveDate", d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
    		//[unchange]rs.addAll(models.durableGoods.DurableGoods.find.where().le("detail.procurement.addDate", d.get(0)).eq("detail.procurement.status", ImportStatus.UNCHANGE).eq("typeOfDurableGoods", 0).findList());
			result = getDetailRemainMap(ps,rs);
    	}else{
    		List<Date> d = null;
    		if(relation.equals("year")){
    			d = getYearDate(lRow);
    		}else if(relation.equals("month")){
    			d = getMonthDate(lRow);
    		}else if(relation.equals("quarter")){
    			d = getQuarterDate(lRow);
    		}
    		List<models.durableGoods.ProcurementDetail> ps =  models.durableGoods.ProcurementDetail.find.where().le("procurement.addDate", d.get(1)).eq("procurement.status", ImportStatus.SUCCESS).eq("typeOfDurableGoods", 0).findList();
    		List<Requisition> rs = Requisition.find.where().le("approveDate", d.get(1)).eq("status", ExportStatus.SUCCESS).findList();
			result = getTableRemain( ps, rs , selectedName );
    	}
    	return result;
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
		return Requisition.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList().size();
	}

	private static ArrayList<Integer> getSumTransfer(Date startDate, Date endDate){
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(models.durableArticles.InternalTransfer.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList().size());
		result.add(models.durableArticles.Auction.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList().size());
		result.add(models.durableArticles.Donation.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList().size());
		result.add(models.durableArticles.OtherTransfer.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList().size());
		return result;
	}
	
	private static ArrayList<Integer> getSumRepair(Date startDate, Date endDate){
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(models.durableArticles.Repairing.find.where().between("dateOfReceiveFromRepair", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList().size());
		result.add(models.durableArticles.Repairing.find.where().between("dateOfSentToRepair", startDate, endDate).eq("status", ExportStatus.REPAIRING).findList().size());
		return result;
	}

	private static ArrayList<Integer> getSumBorrow(Date startDate, Date endDate) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(models.durableArticles.Borrow.find.where().between("dateOfEndBorrow", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList().size());
		result.add(models.durableArticles.Borrow.find.where().between("dateOfStartBorrow", startDate, endDate).eq("status", ExportStatus.BORROW).findList().size());
		return result;
	}

	private static int getSumRemain(Date endDate) {
		int remainGoods = models.durableGoods.DurableGoods.find.where().le("detail.procurement.addDate", endDate).eq("detail.procurement.status", ImportStatus.SUCCESS).eq("typeOfDurableGoods", 0).findList().size();
		//[unchange]remainGoods += models.durableGoods.DurableGoods.find.where().le("detail.procurement.addDate", endDate).eq("detail.procurement.status", ImportStatus.UNCHANGE).eq("typeOfDurableGoods", 0).findList().size();
		List<RequisitionDetail> rs = RequisitionDetail.find.where().le("requisition.approveDate", endDate).eq("requisition.status", ExportStatus.SUCCESS).findList();
		for(RequisitionDetail r : rs){
			remainGoods -= r.quantity;
		}
		return remainGoods;
	}

	private static ArrayNode getDetailBalanceMapArticle(List<models.durableArticles.Procurement> ps){
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("ละคอน");
		columns.add("พัสตราภรณ์");
		columns.add("อุตสาหกรรม");
		columns.add("อื่นๆ");
    	ArrayNode result = ps.size() > 0 ? getDetailHeader(columns) : JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Double[]> listResult = new HashMap<String,Double[]>();
		for(models.durableArticles.Procurement p : ps){
			for(models.durableArticles.ProcurementDetail pd : p.details){
				String key = pd.fsn.typ.groupClass.group.groupDescription;
				for(DurableArticles d : pd.subDetails){
					Double[] value = listResult.get(key);
					if(d.department != null){
						if(d.department.equals("สาขาวิชาการละคอน")){
							if(value == null){
								listResult.put(key, new Double[] {pd.price, 0.0, 0.0, 0.0});
								/*if(pd.quantity * pd.price != 0){
									listResult.put(key, new Double[] {pd.quantity * pd.price, 0.0, 0.0, 0.0});
								}else{
									listResult.put(key, new Double[] {pd.price, 0.0, 0.0, 0.0});
								}*/
							}else{
								value = listResult.get(key);
								value[0] = listResult.get(key)[0] + pd.price;
								listResult.put(key, value);
							}
						}else if(d.department.equals("สาขาวิชาศิลปะการออกแบบพัสตราภรณ์")){
							if(value == null){
								listResult.put(key, new Double[] {0.0, pd.price, 0.0, 0.0});
								/*if(pd.quantity * pd.price != 0){
									listResult.put(key, new Double[] {0.0, pd.quantity * pd.price, 0.0, 0.0});
								}else{
									listResult.put(key, new Double[] {0.0, pd.price, 0.0, 0.0});
								}*/
							}else{
								value = listResult.get(key);
								value[1] = listResult.get(key)[1] + pd.price;
								listResult.put(key, value);
							}
						}else if(d.department.equals("สาขาวิชาศิลปะการออกแบบอุตสาหกรรม")){
							if(value == null){
								listResult.put(key, new Double[] {0.0, 0.0, pd.price, 0.0});
								/*if(pd.quantity * pd.price != 0){
									listResult.put(key, new Double[] {0.0, 0.0, pd.quantity * pd.price, 0.0});
								}else{
									listResult.put(key, new Double[] {0.0, 0.0, pd.price, 0.0});
								}*/
							}else{
								value = listResult.get(key);
								value[2] = listResult.get(key)[2] + pd.price;
								listResult.put(key, value);
							}
						}else{
							if(value == null){
								listResult.put(key, new Double[] {0.0, 0.0, 0.0, pd.price});
								/*if(pd.quantity * pd.price != 0){
									listResult.put(key, new Double[] {0.0, 0.0, 0.0, pd.quantity * pd.price});
								}else{
									listResult.put(key, new Double[] {0.0, 0.0, 0.0, pd.price});
								}*/
							}else{
								value = listResult.get(key);
								value[3] = listResult.get(key)[3] + pd.price;
								listResult.put(key, value);
							}
						}
						
					}
				}
			}
		}
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add(key);
			tr.add(listResult.get(key)[0]);
			tr.add(listResult.get(key)[1]);
			tr.add(listResult.get(key)[2]);
			tr.add(listResult.get(key)[3]);
			//tr.add(listResult.get(key));
			//tr.add(colors[i++]);
			//tr.add(key);
			result.add(tr);
		}
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
    }
    
    private static ArrayNode getDetailBalanceMapGoods(List<models.durableGoods.Procurement> ps){
    	ArrayNode result = getDetailHeader();
    	HashMap<String,Double> listResult = new HashMap<String,Double>();
		for(models.durableGoods.Procurement p : ps){
			for(models.durableGoods.ProcurementDetail pd : p.details){
				String key = "";
				Double value = null;
				if(pd.typeOfDurableGoods == 1){
					FSN_Description fsn = FSN_Description.find.byId(pd.code);
					key = fsn.typ.groupClass.group.groupDescription;
					value = listResult.get(key);
				}else{
					MaterialCode c = MaterialCode.find.byId(pd.code);
					key = c.materialType.typeName;
					value = listResult.get(key);
				}
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
    	return result;
    }
    
    private static ArrayNode getDetailProcurementMapArticle(List<models.durableArticles.Procurement> ps){
    	ArrayNode result = getDetailHeader();
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
			tr.add(Integer.valueOf(listResult.get(key)));
			tr.add(colors[i++]);
			tr.add(key);
			result.add(tr);
		}
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
    }
    
    private static ArrayNode getDetailProcurementMapGoods(List<models.durableGoods.Procurement> ps){
    	ArrayNode result = getDetailHeader();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
		for(models.durableGoods.Procurement p : ps){
			for(models.durableGoods.ProcurementDetail pd : p.details){
				String key = "";
				Integer value = null;
				if(pd.typeOfDurableGoods == 1){
					FSN_Description fsn = FSN_Description.find.byId(pd.code);
					key = fsn.typ.groupClass.group.groupDescription;
					value = listResult.get(key);
				}else{
					MaterialCode c = MaterialCode.find.byId(pd.code);
					key = c.materialType.typeName;
					value = listResult.get(key);
				}
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
    	return result;
    }
    
    private static ArrayNode getDetailRequisitionMap(List<Requisition> rs){
    	ArrayNode result = getDetailHeader();
    	HashMap<String, Integer> listResult = new HashMap<String,Integer>();
    	for(Requisition r : rs){
    		for(RequisitionDetail rd : r.details){
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
    }
    
    private static ArrayNode getDetailInternalTranfersMap(List<InternalTransfer> rs) {
		ArrayNode result = getDetailHeader();
    	HashMap<String, Integer> listResult = new HashMap<String,Integer>();
    	for(InternalTransfer r : rs){
    		for(InternalTransferDetail rd : r.detail){
    			String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer value = listResult.get(key);
				if(value == null){
					listResult.put(key, 1);
				}else{
					listResult.put(key, listResult.get(key) + 1);
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
	}
    
    private static ArrayNode getDetailAuctionTranfersMap(List<Auction> rs) {
		ArrayNode result = getDetailHeader();
		HashMap<String, Integer> listResult = new HashMap<String,Integer>();
		for(Auction r : rs){
			for(AuctionDetail rd : r.detail){
				String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				Integer value = listResult.get(key);
				if(value == null){
					listResult.put(key, 1);
				}else{
					listResult.put(key, listResult.get(key) + 1);
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
	}

	private static ArrayNode getDetailDonationTranfersMap(List<Donation> rs) {
		ArrayNode result = getDetailHeader();
		HashMap<String, Integer> listResult = new HashMap<String,Integer>();
		for(Donation r : rs){
			for(DonationDetail rd : r.detail){
				String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				Integer value = listResult.get(key);
				if(value == null){
					listResult.put(key, 1);
				}else{
					listResult.put(key, listResult.get(key) + 1);
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
	}

	private static ArrayNode getDetailOtherTranfersMap(List<OtherTransfer> rs) {
		ArrayNode result = getDetailHeader();
		HashMap<String, Integer> listResult = new HashMap<String,Integer>();
		for(OtherTransfer r : rs){
			for(OtherTransferDetail rd : r.detail){
				String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				Integer value = listResult.get(key);
				if(value == null){
					listResult.put(key, 1);
				}else{
					listResult.put(key, listResult.get(key) + 1);
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
	}

	/*private static ArrayNode getDetailTransferMap(Date startDate, Date endDate) {
    	ArrayList<String> columnName = new ArrayList<String>();
    	columnName.add("โอนย้ายภายใน");
    	columnName.add("จำหน่าย");
    	columnName.add("บริจาค");
    	columnName.add("อื่นๆ");
    	ArrayNode result = getDetailHeader(columnName);
    	HashMap<String, Integer[]> listResult = new HashMap<String, Integer[]>();
    	List<InternalTransfer> internals = models.durableArticles.InternalTransfer.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    	List<Auction> auctions = models.durableArticles.Auction.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    	List<Donation> donations = models.durableArticles.Donation.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    	List<OtherTransfer> others = models.durableArticles.OtherTransfer.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    	
    	List<String> list = getTransferKey(internals, auctions, donations, others);
    	listResult = getMapTransfer(internals, auctions, donations, others, list);
    	
    	for(String key : listResult.keySet()){
    		ArrayNode tr = JsonNodeFactory.instance.arrayNode();
    		tr.add(key);
    		tr.add(listResult.get(key)[0]);
    		tr.add(listResult.get(key)[1]);
    		tr.add(listResult.get(key)[2]);
    		tr.add(listResult.get(key)[3]);
    		result.add(tr);
    	}
    	
		return result;
	}*/
    
	private static ArrayNode getDetailRepairingMap(List<Repairing> rs) {
		ArrayNode result =  getDetailHeader();
    	HashMap<String, Integer> listResult = new HashMap<String,Integer>();
    	for(Repairing r : rs){
    		for(RepairingDetail rd : r.detail){
    			String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer value = listResult.get(key);
				if(value == null){
					listResult.put(key, 1);
				}else{
					listResult.put(key, listResult.get(key) + 1);
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
	}

	private static ArrayNode getDetailBorrowMap(List<models.durableArticles.Borrow> bs) {
		ArrayNode result = getDetailHeader();
    	HashMap<String, Integer> listResult = new HashMap<String,Integer>();
    	for(models.durableArticles.Borrow b : bs){
    		for(models.durableArticles.BorrowDetail bd : b.detail){
    			String key = bd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer value = listResult.get(key);
				if(value == null){
					listResult.put(key, 1);
				}else{
					listResult.put(key, listResult.get(key) + 1);
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
	}

	private static ArrayNode getDetailRemainMap(List<models.durableGoods.ProcurementDetail> ps, List<Requisition> rs) {
		ArrayNode result = ps.size() > 0 ? getDetailHeader() : JsonNodeFactory.instance.arrayNode();
    	HashMap<String, Integer> listResult = new HashMap<String,Integer>();
		for(models.durableGoods.ProcurementDetail pd : ps){
			String key = "";
			Integer value = null;
			if(pd.typeOfDurableGoods == 1){
				FSN_Description fsn = FSN_Description.find.byId(pd.code);
				key = fsn.typ.groupClass.group.groupDescription;
				value = listResult.get(key);
			}else{
				MaterialCode c = MaterialCode.find.byId(pd.code);
				key = c.materialType.typeName;
				value = listResult.get(key);
			}
			if(value == null){
				if(pd.quantity != 0){
					listResult.put(key, pd.quantity);
				}
			}else{
				listResult.put(key, listResult.get(key) + pd.quantity);
			}
		}
    	for(Requisition r : rs){
    		for(RequisitionDetail rd : r.details){
    			String key = rd.code.materialType.typeName;
    			Integer value = listResult.get(key);
				if(value == null){
					if(rd.quantity != 0){
						listResult.put(key, 0-rd.quantity);
					}
				}else{
					listResult.put(key, listResult.get(key) - rd.quantity);
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
		if(result.size() <= 1){
			result.add(getEmptyMapDetail());
		}
		return result;
	}

	private static ArrayNode getEmptyMapDetail(){
		ArrayNode tr = JsonNodeFactory.instance.arrayNode();
		tr.add("ไม่มีข้อมูล");
		tr.add(0);
		tr.add(colors[0]);
		tr.add("ไม่มีข้อมูล");
		return tr;
	}

	private static ArrayNode getTableBalanceArticle(List<models.durableArticles.Procurement> ps, String selectedName, int col){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Double> listResult = new HashMap<String,Double>();
    	HashMap<String,String> ids = new HashMap<String,String>();
		for(models.durableArticles.Procurement p : ps){
			for(models.durableArticles.ProcurementDetail pd : p.details){
				for(DurableArticles d : pd.subDetails){
					if(department[col-1].equals(d.department)){
						if(pd.fsn.typ.groupClass.group.groupDescription.equals(selectedName)){
							String key = pd.fsn.descriptionDescription;
							Double value = listResult.get(key);
							if(value == null){
								if(pd.quantity * pd.price != 0){
									listResult.put(key, pd.price);
									ids.put(key,""+d.id);
								}
							}else{
								listResult.put(key, listResult.get(key) + pd.price);
								ids.put(key,ids.get(key)+","+d.id);
							}
						}
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
			tr.add(getDescriptionButton("durableArticle", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
		return result;
    }
    
    private static ArrayNode getTableBalanceGoods(List<models.durableGoods.Procurement> ps, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Double> listResult = new HashMap<String,Double>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	for(models.durableGoods.Procurement p : ps){
    		for(models.durableGoods.ProcurementDetail pd : p.details){
    			for(DurableGoods d : pd.subDetails){
    				String key = "";
    				Double value = null;
    				if(pd.typeOfDurableGoods == 1){
    					FSN_Description fsn = FSN_Description.find.byId(pd.code);
    					if(fsn.typ.groupClass.group.groupDescription.equals(selectedName)){
    						key = fsn.descriptionDescription;
							value = listResult.get(key);
    					}else{
    						continue;
    					}
    				}else{
    					MaterialCode c = MaterialCode.find.byId(pd.code);
    					if(c.materialType.typeName.equals(selectedName)){
    						key = c.description;
    						value = listResult.get(key);
    					}else{
    						continue;
    					}
    				}
					if(value == null){
						if(pd.quantity * pd.price != 0){
							listResult.put(key, pd.price);
							ids.put(key,"" + d.id);
						}
					}else{
						listResult.put(key, listResult.get(key) + pd.price);
						ids.put(key,ids.get(key) + "," + d.id);
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
			tr.add(getDescriptionButton("materialCode", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
    	return result;
    }
    
    private static ArrayNode getTableProcurementArticle(List<models.durableArticles.Procurement> ps, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
		for(models.durableArticles.Procurement p : ps){
			for(models.durableArticles.ProcurementDetail pd : p.details){
				if(pd.fsn.typ.groupClass.group.groupDescription.equals(selectedName)){
					String key = pd.fsn.descriptionDescription;
					Integer value = listResult.get(key);
					if(value == null){
						if(pd.quantity != 0){
							listResult.put(key, pd.quantity);
							ids.put(key,""+pd.id);
						}
					}else{
						listResult.put(key, listResult.get(key) + pd.quantity);
						ids.put(key,ids.get(key)+","+pd.id);
					}
				}
			}
		}
		int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("procurementDurableArticle", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
		return result;
    }
    
    private static ArrayNode getTableProcurementGoods(List<models.durableGoods.Procurement> ps, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	for(models.durableGoods.Procurement p : ps){
    		for(models.durableGoods.ProcurementDetail pd : p.details){
    			String key = "";
				Integer value = null;
				if(pd.typeOfDurableGoods == 1){
					FSN_Description fsn = FSN_Description.find.byId(pd.code);
					if(fsn.typ.groupClass.group.groupDescription.equals(selectedName)){
						key = fsn.descriptionDescription;
						value = listResult.get(key);
					}else{
						continue;
					}
				}else{
					MaterialCode c = MaterialCode.find.byId(pd.code);
					if(c.materialType.typeName.equals(selectedName)){
						key = c.description;
						value = listResult.get(key);
					}else{
						continue;
					}
				}
				if(value == null){
					if(pd.quantity != 0){
						listResult.put(key, pd.quantity);
						ids.put(key,"" + pd.id);
					}
				}else{
					listResult.put(key, listResult.get(key) + pd.quantity);
					ids.put(key,ids.get(key) + "," + pd.id);
				}
    		}
    		
    	}
    	int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("procurementMaterialCode", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
    	return result;
    }
    
    private static ArrayNode getTableRequisition(List<Requisition> rs, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	for(Requisition r : rs){
    		for(RequisitionDetail rd : r.details){
    			MaterialCode c = rd.code;
    			if(c.materialType.typeName.equals(selectedName)){
    				String key = c.description;
					Integer value = listResult.get(key);
					if(value == null){
						if(rd.quantity  != 0){
							listResult.put(key, rd.quantity);
							ids.put(key,"" + rd.id);
						}
					}else{
						listResult.put(key, listResult.get(key) + rd.quantity);
						ids.put(key,ids.get(key) + "," + rd.id);
					}
				}
    		}
    		
    	}
    	int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("requisition", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
    	return result;
    }
    
    /*private static ArrayNode getTableTransfer(Date startDate, Date endDate, int col, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	
    	if(col == 1){ // โอนย้ายภายใน
    		List<InternalTransfer> internals = models.durableArticles.InternalTransfer.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    		for(InternalTransfer internal : internals){
    			for(InternalTransferDetail internalDetail : internal.detail){
    				String key = internalDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    				if(key.equals(selectedName)){
    					key = internalDetail.durableArticles.detail.fsn.descriptionDescription;
    					Integer value = listResult.get(key);
    					if(value == null){
    						listResult.put(key, 1);
    						ids.put(key,"" + internalDetail.id);
    					}else{
    						listResult.put(key, value + 1);
    						ids.put(key,ids.get(key) + "," + internalDetail.id );
    					}
    				}
    			}
    		}
    		
    	}else if(col == 2){ // จำหน่าย
    		List<Auction> auctions = models.durableArticles.Auction.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    		for(Auction auction	: auctions){
    			for(AuctionDetail auctionDetail : auction.detail){
    				String key = auctionDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    				if(key.equals(selectedName)){
    					key = auctionDetail.durableArticles.detail.fsn.descriptionDescription;
    					Integer value = listResult.get(key);
    					if(value == null){
    						listResult.put(key, 1);
    						ids.put(key,"" + auctionDetail.id );
    					}else{
    						listResult.put(key, value + 1);
    						ids.put(key,ids.get(key) + "," + auctionDetail.id );
    					}
    				}
    			}
    		}
    	}else if(col == 3){ // บริจาค
    		List<Donation> donations = models.durableArticles.Donation.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    		for(Donation donation : donations){
    			for(DonationDetail donationDetail : donation.detail){
    				String key = donationDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    				if(key.equals(selectedName)){
    					key = donationDetail.durableArticles.detail.fsn.descriptionDescription;
    					Integer value = listResult.get(key);
    					if(value == null){
    						listResult.put(key, 1);
    						ids.put(key,"" + donationDetail.id );
    					}else{
    						listResult.put(key, value + 1);
    						ids.put(key,ids.get(key) + "," + donationDetail.id );
    					}
    				}
    			}
    		}
    	}else if(col == 4){ // อื่นๆ
    		List<OtherTransfer> others = models.durableArticles.OtherTransfer.find.where().between("approveDate", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    		for(OtherTransfer other : others){
    			for(OtherTransferDetail otherDetail : other.detail){
    				String key = otherDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    				if(key.equals(selectedName)){
    					key = otherDetail.durableArticles.detail.fsn.descriptionDescription;
    					Integer value = listResult.get(key);
    					if(value == null){
    						listResult.put(key, 1);
    						ids.put(key,"" + otherDetail.id );
    					}else{
    						listResult.put(key, value + 1);
    						ids.put(key,ids.get(key) + "," + otherDetail.id );
    					}
    				}
    			}
    		}
    	}
    	int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(ids.get(key));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
    	return result;
    }*/
    

    private static ArrayNode getTableInternalTranfers(List<InternalTransfer> rs, String selectedName) {
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	for(InternalTransfer internal : rs){
			for(InternalTransferDetail internalDetail : internal.detail){
				String key = internalDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				if(key.equals(selectedName)){
					key = internalDetail.durableArticles.detail.fsn.descriptionDescription;
					Integer value = listResult.get(key);
					if(value == null){
						listResult.put(key, 1);
						ids.put(key,"" + internalDetail.id);
					}else{
						listResult.put(key, value + 1);
						ids.put(key,ids.get(key) + "," + internalDetail.id );
					}
				}
			}
		}
    	int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("internalTransfer", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
    	return result;
	}

	private static ArrayNode getTableAuctionTranfers(List<Auction> rs, String selectedName) {
		ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	for(Auction auction : rs){
			for(AuctionDetail auctionDetail : auction.detail){
				String key = auctionDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				if(key.equals(selectedName)){
					key = auctionDetail.durableArticles.detail.fsn.descriptionDescription;
					Integer value = listResult.get(key);
					if(value == null){
						listResult.put(key, 1);
						ids.put(key,"" + auctionDetail.id );
					}else{
						listResult.put(key, value + 1);
						ids.put(key,ids.get(key) + "," + auctionDetail.id );
					}
				}
			}
		}
		int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("auction", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
		return result;
	}

	private static ArrayNode getTableDonationTranfers(List<Donation> rs, String selectedName) {
		ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	for(Donation donation : rs){
			for(DonationDetail donationDetail : donation.detail){
				String key = donationDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				if(key.equals(selectedName)){
					key = donationDetail.durableArticles.detail.fsn.descriptionDescription;
					Integer value = listResult.get(key);
					if(value == null){
						listResult.put(key, 1);
						ids.put(key,"" + donationDetail.id );
					}else{
						listResult.put(key, value + 1);
						ids.put(key,ids.get(key) + "," + donationDetail.id );
					}
				}
			}
		}
    	int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("donate", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
		return result;
	}

	private static ArrayNode gettableOtherTranfers(List<OtherTransfer> rs, String selectedName) {
		ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	for(OtherTransfer other : rs){
			for(OtherTransferDetail otherDetail : other.detail){
				String key = otherDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				if(key.equals(selectedName)){
					key = otherDetail.durableArticles.detail.fsn.descriptionDescription;
					Integer value = listResult.get(key);
					if(value == null){
						listResult.put(key, 1);
						ids.put(key,"" + otherDetail.id );
					}else{
						listResult.put(key, value + 1);
						ids.put(key,ids.get(key) + "," + otherDetail.id );
					}
				}
			}
		}
    	int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("otherTransfer", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
		return result;
	}

	private static ArrayNode getTableRepairing(List<Repairing> rs, String selectedName) {
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	
		for(Repairing r : rs){
			for(RepairingDetail rd : r.detail){
				String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				if(key.equals(selectedName)){
					key = rd.durableArticles.detail.fsn.descriptionDescription;
					Integer value = listResult.get(key);
					if(value == null){
						listResult.put(key, 1);
						ids.put(key,"" + rd.id);
					}else{
						listResult.put(key, value + 1);
						ids.put(key,ids.get(key) + "," + rd.id );
						//cost.put(key, rd.price);
					}
				}
			}
		}
		int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("repair", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
		return result;
	}
	
	private static ArrayNode getTableBorrow(List<models.durableArticles.Borrow> bs, String selectedName) {
		ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
		for(models.durableArticles.Borrow b : bs){
			for(models.durableArticles.BorrowDetail bd : b.detail){
				String key = bd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
				if(key.equals(selectedName)){
					key = bd.durableArticles.detail.fsn.descriptionDescription;
					Integer value = listResult.get(key);
					if(value == null){
						listResult.put(key, 1);
						ids.put(key,"" + bd.id);
					}else{
						listResult.put(key, value + 1);
						ids.put(key,ids.get(key) + "," + bd.id );
						//cost.put(key, rd.price);
					}
				}
			}
		}
		int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			tr.add(getDescriptionButton("borrow", ids.get(key)));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
		return result;
	}

	private static ArrayNode getTableRemain(List<models.durableGoods.ProcurementDetail> ps, List<Requisition> rs, String selectedName) {
		// TODO Auto-generated method stub
		ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String[]> ids = new HashMap<String,String[]>();
    	for(models.durableGoods.ProcurementDetail pd : ps){
			for(DurableGoods d : pd.subDetails){
				String key = "";
				Integer value = null;
				if(pd.typeOfDurableGoods == 1){
					FSN_Description fsn = FSN_Description.find.byId(pd.code);
					if(fsn.typ.groupClass.group.groupDescription.equals(selectedName)){
						key = fsn.descriptionDescription;
						value = listResult.get(key);
					}
				}else{
					MaterialCode c = MaterialCode.find.byId(pd.code);
					if(c.materialType.typeName.equals(selectedName)){
						key = c.description;
						value = listResult.get(key);
					}
				}
				if(value == null){
					if(pd.quantity != 0){
						listResult.put(key, 1);
						ids.put(key,new String[] {"" + d.id, ""}); // {durableGoods.id , requisitionDetail.id}
					}
				}else{
					listResult.put(key, listResult.get(key) + 1);
					String [] s = ids.get(key);
					s[0] += ","+d.id;
					ids.put(key,s);
				}
			}
		}
    	for(Requisition r : rs){
    		for(RequisitionDetail rd : r.details){
    			MaterialCode c = rd.code;
    			if(c.materialType.typeName.equals(selectedName)){
    				String key = c.description;
					Integer value = listResult.get(key);
					if(value == null){
						if(rd.quantity  != 0){
							listResult.put(key, 0 - rd.quantity);
							ids.put(key,new String[] {"", "" + rd.id}); // {durableGoods.id , requisitionDetail.id}
						}
					}else{
						listResult.put(key, listResult.get(key) - rd.quantity);
						String [] s = ids.get(key);
						if(s[1].equals("")){
							s[1] += "" + rd.id;
						}else{
							s[1] += "," + rd.id;
						}
						ids.put(key, s);
					}
				}
    		}
    		
    	}
    	//TODO
    	int i=1;
		for (String key : listResult.keySet()) {
				ArrayNode tr = JsonNodeFactory.instance.arrayNode();
				tr.add("" + i++);
				tr.add(key);
				tr.add(String.format("%d",listResult.get(key)));
				tr.add(getDescriptionButton("remain", ids.get(key)[0]+"/"+ids.get(key)[1]));
				//tr.add(descriptionBtn);
				result.add(tr);
		}
    	return result;
	}

	private static String getArticleHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(String id: ids){
			DurableArticles d = DurableArticles.find.byId(Long.valueOf(id));
			if(d!=null){
				result += "<div class=\"well\">";
				result += getDetailLabel("id", String.valueOf(d.id));
				result += getDetailLabel("code", d.code);
				result += getDetailLabel("ชื่อครุภัณฑ์", d.detail.fsn.descriptionDescription);
				result += getDetailLabel("คำนำหน้า", d.title);
				result += getDetailLabel("firstName", d.firstName);
				result += getDetailLabel("lastName", d.lastName);
				result += getDetailLabel("department", d.department);
				result += getDetailLabel("floorLevel", d.floorLevel);
				result += getDetailLabel("room", d.room);
				
				String expandable = "";
				expandable += getDetailLabel("price", String.valueOf(d.detail.price));
				expandable += getDetailLabel("จำนวนนำเข้า", String.valueOf(d.detail.quantity));
				expandable += getDetailLabel("brand", d.detail.brand);
				expandable += getDetailLabel("phone", d.detail.phone);
				expandable += getDetailLabel("pic","<img style=\"width:80px;\" src=\"/assets/"+ d.detail.fsn.path + "\">");
				expandable += getDetailLabel("contractNo", d.detail.procurement.contractNo);
				expandable += getDetailLabel("budgetType", d.detail.procurement.budgetType);
				expandable += getDetailLabel("addDate", d.detail.procurement.getAddDate());
				expandable += getDetailLabel("checkDate", d.detail.procurement.getCheckDate());
				
				result += getExpandableHTML("รายละเอียดใบรายการ", expandable);
				result += "</div>";
			}
		}
		result += "</div>";
		return result;
	}
	
	private static String getGoodsHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(String id: ids){
			DurableGoods d = DurableGoods.find.byId(Long.valueOf(id));
			MaterialCode m = null;
			FSN_Description fsn = null;
			if(d!=null){
				if(d.typeOfDurableGoods == 0){
					m = MaterialCode.find.byId(d.detail.code);
				}else{
					fsn = FSN_Description.find.byId(d.detail.code);
				}
				
				result += "<div class=\"well\">";
				result += getDetailLabel("code", d.codes);
				result += getDetailLabel("ชื่อวัสดุ", d.detail.description);
				result += getDetailLabel("คำนำหน้า", d.title);
				result += getDetailLabel("firstName", d.firstName);
				result += getDetailLabel("lastName", d.lastName);
				result += getDetailLabel("department", d.department);
				result += getDetailLabel("floorLevel", d.floorLevel);
				result += getDetailLabel("room", d.room);
				String expandable = "";
				expandable += getDetailLabel("price", String.valueOf(d.detail.price));
				expandable += getDetailLabel("จำนวนนำเข้า", String.valueOf(d.detail.quantity));
				expandable += getDetailLabel("brand", d.detail.brand);
				expandable += getDetailLabel("phone", d.detail.phone);
				String path = d.typeOfDurableGoods == 0 ? m.path: fsn.path;
				expandable += getDetailLabel("pic","<img style=\"width:80px;\" src=\"/assets/" + path + "\">");
				expandable += getDetailLabel("fsn", d.detail.code);
				expandable += getDetailLabel("contractNo", d.detail.procurement.contractNo);
				expandable += getDetailLabel("budgetType", d.detail.procurement.budgetType);
				expandable += getDetailLabel("addDate", d.detail.procurement.getAddDate());
				expandable += getDetailLabel("checkDate", d.detail.procurement.getCheckDate());
				result += getExpandableHTML("รายละเอียดใบรายการ", expandable);
				result += "</div>";
			}
		}
		result += "</div>";
		return result;
	}

	private static String getProcurementArticleHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(String id: ids){
			models.durableArticles.ProcurementDetail pd = models.durableArticles.ProcurementDetail.find.byId(Long.valueOf(id));
			if(pd!=null){
				result += "<div class=\"well\">";
				result += getDetailLabel("description", pd.description);
				result += getDetailLabel("alertTime", String.valueOf(pd.alertTime));
				result += getDetailLabel("brand", pd.brand);
				result += getDetailLabel("price", String.valueOf(pd.price));
				result += getDetailLabel("phone", pd.phone);
				result += getDetailLabel("quantity", String.valueOf(pd.quantity));
				result += getDetailLabel("seller", pd.seller);
				result += getDetailLabel("serialNumber", pd.serialNumber);
				result += getDetailLabel("priceNoVat", String.valueOf(pd.priceNoVat));
				result += getDetailLabel("pic", "<img style=\"width:80px;\" src=\"/assets/"+ pd.fsn.path + "\">");
				
				String expandable = "";
				expandable += getDetailLabel("pic","<img style=\"width:80px;\" src=\"/assets/"+ pd.fsn.path + "\">");
				expandable += getDetailLabel("contractNo", pd.procurement.contractNo);
				expandable += getDetailLabel("budgetType", pd.procurement.budgetType);
				expandable += getDetailLabel("addDate", pd.procurement.getAddDate());
				expandable += getDetailLabel("checkDate", pd.procurement.getCheckDate());
				result += getExpandableHTML("รายละเอียดใบรายการ", expandable);
				
				expandable = "";
				for(models.durableArticles.EO_Committee eo : pd.procurement.eoCommittee){
					if(eo.committee != null){
						expandable += getDetailCommitteeLabel(eo.committeePosition, String.format("%s %s %s", eo.committee.namePrefix, eo.committee.firstName, eo.committee.lastName));
					}
				}
				result += getExpandableHTML("คณะกรรมการเปิดซอง", expandable);
				
				expandable = "";
				for(models.durableArticles.AI_Committee ai : pd.procurement.aiCommittee){
					if(ai.committee != null){
						expandable += getDetailCommitteeLabel(ai.committeePosition, String.format("%s %s %s", ai.committee.namePrefix, ai.committee.firstName, ai.committee.lastName));
					}
				}
				result += getExpandableHTML("คณะกรรมการตรวจรับ", expandable);
				
				
				result += "</div>";
			}
		}
		result += "</div>";
		return result;
	}

	private static String getProcurementMaterialHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(String id: ids){
			models.durableGoods.ProcurementDetail pd = models.durableGoods.ProcurementDetail.find.byId(Long.valueOf(id));
			MaterialCode m = null;
			FSN_Description fsn = null;
			if(pd!=null){
				if(pd.typeOfDurableGoods == 0){
					m = MaterialCode.find.byId(pd.code);
				}else{
					fsn = FSN_Description.find.byId(pd.code);
				}

				result += "<div class=\"well\">";
				result += getDetailLabel("description", pd.description);
				result += getDetailLabel("brand", pd.brand);
				result += getDetailLabel("price", String.valueOf(pd.price));
				result += getDetailLabel("phone", pd.phone);
				result += getDetailLabel("quantity", String.valueOf(pd.quantity));
				result += getDetailLabel("seller", pd.seller);
				result += getDetailLabel("serialNumber", pd.serialNumber);
				result += getDetailLabel("priceNoVat", String.valueOf(pd.priceNoVat));
				
				String expandable = "";
				String path = pd.typeOfDurableGoods == 0 ? m.path: fsn.path;
				expandable += getDetailLabel("pic", "<img style=\"w);dth:80px;\" src=\"/assets/" + path + "\">");
				expandable += getDetailLabel("contractNo", pd.procurement.contractNo);
				expandable += getDetailLabel("budgetType", pd.procurement.budgetType);
				expandable += getDetailLabel("addDate", pd.procurement.getAddDate());
				expandable += getDetailLabel("checkDate", pd.procurement.getCheckDate());
				result += getExpandableHTML("รายละเอียดใบรายการ", expandable);
				
				expandable = "";
				for(models.durableGoods.AI_Committee ai : pd.procurement.aiCommittee){
					if(ai.committee!=null){
						expandable += getDetailCommitteeLabel(ai.committeePosition, String.format("%s %s %s", ai.committee.namePrefix, ai.committee.firstName, ai.committee.lastName));
					}
				}
				result += getExpandableHTML("คณะกรรมการตรวจรับ", expandable);
				
				result += "</div>";
			}
		}
		result += "</div>";
		return result;
	}

	private static String getRequisitionHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(String id: ids){
			RequisitionDetail rd = RequisitionDetail.find.byId(Long.valueOf(id));
			result += "<div class=\"well\">";
			
			result += getDetailLabel("id", String.valueOf(rd.id));
			result += getDetailLabel("รายการ/เรื่อง", rd.requisition.title);
			result += getDetailLabel("หมายเลขใบรายการ", rd.requisition.number);
			result += getDetailLabel("ชื่อพัสดุ", rd.description);
			result += getDetailLabel("จำนวน", String.valueOf(rd.quantity));
			result += getDetailLabel("วันที่อนุมัติ", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(rd.requisition.approveDate));
			result += getDetailLabel("ผู้อนุมัติ", String.format("%s %s %s", rd.requisition.approver.namePrefix, rd.requisition.approver.firstName, rd.requisition.approver.lastName ));
			result += getDetailLabel("ผู้เบิก", String.format("%s %s %s",  rd.requisition.user.namePrefix, rd.requisition.user.firstName, rd.requisition.user.lastName));
			result += "</div>";
		}
		result += "</div>";
		return result;
	}

	private static String getInternalTransferHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(int i=0; i<ids.length; i++){
			String id = ids[i];
			String detailsCodes = "";
			InternalTransferDetail inDetail = InternalTransferDetail.find.byId(Long.valueOf(id));
			InternalTransfer in = inDetail.internalTransfer; 
			result += "<div class=\"well\">";
			
			result += getDetailLabel("id", String.valueOf(inDetail.id));
			result += getDetailLabel("รายการ/เรื่อง", inDetail.internalTransfer.title);
			result += getDetailLabel("หมายเลขใบรายการ", inDetail.internalTransfer.number);
			result += getDetailLabel("วันที่อนุมัติ", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(inDetail.internalTransfer.approveDate));
			//TODO ผู้รับผิดชอบ
			result += getDetailLabel("ผู้อนุมัติ", "pending");//String.format("%s %s %s", inDetail.internalTransfer.approver.namePrefix, inDetail.internalTransfer.approver.firstName, inDetail.internalTransfer.approver.lastName ));
			result += getDetailLabel("ผู้รับผิดชอบ", "pending");//String.format("%s %s %s", inDetail.internalTransfer., inDetail.internalTransfer.approver.firstName, inDetail.internalTransfer.approver.lastName ));
			for(; i<ids.length; i++){
				id = ids[i];
				InternalTransferDetail newDetail = InternalTransferDetail.find.byId(Long.valueOf(id));
				if(newDetail.internalTransfer.equals(in)){
					detailsCodes += getDetailLabel("หมายเลขพัสดุ", newDetail.durableArticles.code);
					detailsCodes += getDetailLabel("ย้ายไปสาขา", newDetail.newDepartment,"margin-left:2%");
					detailsCodes += getDetailLabel("ย้ายไปห้อง", newDetail.newRoom, "margin-left:2%");
					detailsCodes += getDetailLabel("ย้ายไปชั้น", newDetail.newFloorLevel, "margin-left:2%");
					/*detailsCodes += getDetailLabel("ย้ายสาขาไปยัง", newDetail.durableArticles.department+"->"+newDetail.department,"margin-left:3%");
					detailsCodes += getDetailLabel("ย้ายห้องไปยัง", newDetail.durableArticles.room+"->"+newDetail.room, "margin-left:3%");
					detailsCodes += getDetailLabel("ย้ายชั้นไปยัง", newDetail.durableArticles.floorLevel+"->"+newDetail.floorLevel, "margin-left:3%");*/
				}else{
					i--;
					break;
				}
			}
			
			result += getExpandableHTML("รายการโอนย้าย", detailsCodes);
			result += "</div>";
		}
		result += "</div>";
		return result;
	}

	private static String getAuctionHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		Auction a = null;
		for(int i=0;i<ids.length;i++){
			String id = ids[i];
			String expandable = "";
			String detailsCodes = ""; 
			AuctionDetail ad = AuctionDetail.find.byId(Long.valueOf(id));
			a = ad.auction;
			result += "<div class=\"well\">";
			
			result += getDetailLabel("id", String.valueOf(ad.id));
			result += getDetailLabel("รายการ/เรื่อง", ad.auction.title);
			result += getDetailLabel("หมายเลขใบรายการ", ad.auction.contractNo);
			result += getDetailLabel("ราคารวม", String.valueOf(ad.auction.totalPrice));
			//result += getDetailLabel("หมายเลขใบรายการ", ad.auction.dCommittee.);
			result += getDetailLabel("วันที่อนุมัติ", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(ad.auction.approveDate));
			//TODO ผู้รับผิดชอบ
			result += getDetailLabel("ผู้อนุมัติ", "pending");//String.format("%s %s %s", inDetail.internalTransfer.approver.namePrefix, inDetail.internalTransfer.approver.firstName, inDetail.internalTransfer.approver.lastName ));
			result += getDetailLabel("ผู้รับผิดชอบ", "pending");//String.format("%s %s %s", inDetail.internalTransfer., inDetail.internalTransfer.approver.firstName, inDetail.internalTransfer.approver.lastName ));
			expandable = "";
			expandable += getDetailLabel("ชื่อสถานประกอบการ", "pending", "width:18%;");
			result += getExpandableHTML("สถานที่จำหน่าย", expandable);
			//TODO committee
			result += getExpandableHTML("คณะกรรมการสอบข้อเท็จจรืง", "pending");
			result += getExpandableHTML("คณะกรรมการปรเมินราคากลาง", "pending");
			result += getExpandableHTML("คณะกรรมการจำหน่าย", "pending");
			for(;i<ids.length;i++){
				id = ids[i];
				AuctionDetail newAd = AuctionDetail.find.byId(Long.valueOf(id));
				if(newAd.auction.equals(a)){
					detailsCodes += getDetailLabel("หมายเลขพัสดุ", newAd.durableArticles.code);
				}else{
					i--;
					break;
				}
			}
			result += getExpandableHTML("รายการจำหน่าย", detailsCodes); 
			result += "</div>";
		}
				
		result += "</div>";
		return result;
	}

	private static String getDonationHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		Donation d = null;
		for(int i=0;i<ids.length;i++){
			String id = ids[i];
			String expandable = "";
			String detailsCodes = ""; 
			DonationDetail ad = DonationDetail.find.byId(Long.valueOf(id));
			d = ad.donation;
			result += "<div class=\"well\">";
			
			result += getDetailLabel("id", String.valueOf(ad.id));
			result += getDetailLabel("รายการ/เรื่อง", ad.donation.title);
			result += getDetailLabel("หมายเลขใบรายการ", ad.donation.contractNo);
			//result += getDetailLabel("หมายเลขใบรายการ", ad.donation.dCommittee.);
			result += getDetailLabel("วันที่อนุมัติ", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(ad.donation.approveDate));
			//TODO ผู้รับผิดชอบ
			result += getDetailLabel("ผู้อนุมัติ", "pending");//String.format("%s %s %s", inDetail.internalTransfer.approver.namePrefix, inDetail.internalTransfer.approver.firstName, inDetail.internalTransfer.approver.lastName ));
			result += getDetailLabel("ผู้รับผิดชอบ", "pending");//String.format("%s %s %s", inDetail.internalTransfer., inDetail.internalTransfer.approver.firstName, inDetail.internalTransfer.approver.lastName ));
			expandable = "";
			expandable += getDetailLabel("ชื่อสถานประกอบการ", "pending", "width:18%;");
			result += getExpandableHTML("สถานที่จำหน่าย", expandable);
			//TODO committee
			result += getExpandableHTML("คณะกรรมการสอบข้อเท็จจรืง", "pending");
			result += getExpandableHTML("คณะกรรมการจำหน่าย", "pending");
			for(;i<ids.length;i++){
				id = ids[i];
				DonationDetail newAd = DonationDetail.find.byId(Long.valueOf(id));
				if(newAd.donation.equals(d)){
					detailsCodes += getDetailLabel("หมายเลขพัสดุ", newAd.durableArticles.code);
				}else{
					i--;
					break;
				}
			}
			result += getExpandableHTML("รายการจำหน่าย", detailsCodes); 
			result += "</div>";
		}
				
		result += "</div>";
		return result;
	}

	private static String getOtherTransferHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(int i=0; i<ids.length; i++){
			String id = ids[i];
			String detailsCodes = "";
			OtherTransferDetail inDetail = OtherTransferDetail.find.byId(Long.valueOf(id));
			OtherTransfer in = inDetail.otherTransfer; 
			result += "<div class=\"well\">";
			
			result += getDetailLabel("id", String.valueOf(inDetail.id));
			result += getDetailLabel("รายการ/เรื่อง", inDetail.otherTransfer.title);
			result += getDetailLabel("หมายเลขใบรายการ", inDetail.otherTransfer.number);
			result += getDetailLabel("สาเหตุการโอนย้าย", inDetail.otherTransfer.description);
			result += getDetailLabel("วันที่อนุมัติ", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(inDetail.otherTransfer.approveDate));
			//TODO ผู้รับผิดชอบ
			result += getDetailLabel("ผู้อนุมัติ", "pending");//String.format("%s %s %s", inDetail.otherTransfer.approver.namePrefix, inDetail.otherTransfer.approver.firstName, inDetail.otherTransfer.approver.lastName ));
			result += getDetailLabel("ผู้รับผิดชอบ", "pending");//String.format("%s %s %s", inDetail.otherTransfer., inDetail.otherTransfer.approver.firstName, inDetail.otherTransfer.approver.lastName ));
			result += getExpandableHTML("คณะกรรมการสอบข้อเท็จจรืง", "pending");
			result += getExpandableHTML("คณะกรรมการจำหน่าย", "pending");
			for(; i<ids.length; i++){
				id = ids[i];
				OtherTransferDetail newDetail = OtherTransferDetail.find.byId(Long.valueOf(id));
				if(newDetail.otherTransfer.equals(in)){
					detailsCodes += getDetailLabel("หมายเลขพัสดุ", newDetail.durableArticles.code);
				}else{
					i--;
					break;
				}
			}
			
			result += getExpandableHTML("รายการโอนย้าย", detailsCodes);
			result += "</div>";
		}
		result += "</div>";
		return result;
	}

	private static String getRepairHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(int i=0; i<ids.length; i++){
			String id = ids[i];
			String detailsCodes = "";
			RepairingDetail rd = RepairingDetail.find.byId(Long.valueOf(id));
			Repairing r = rd.repairing; 
			result += "<div class=\"well\">";
			
			result += getDetailLabel("id", String.valueOf(rd.id));
			result += getDetailLabel("รายการ/เรื่อง", rd.repairing.title);
			result += getDetailLabel("หมายเลขใบรายการ", rd.repairing.number);
			result += getDetailLabel("ร้านค้าที่ส่งซ่อม", r.company.nameEntrepreneur);
			result += getDetailLabel("วันที่ส่งซ่อม", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(rd.repairing.dateOfSentToRepair));
			if(rd.repairing.dateOfReceiveFromRepair != null){
				result += getDetailLabel("วันที่รับคืน", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(rd.repairing.dateOfReceiveFromRepair));
			}
			result += getDetailLabel("ผู้อนุมัติ",String.format("%s %s %s", rd.repairing.approver.namePrefix, rd.repairing.approver.firstName, rd.repairing.approver.lastName ));
			detailsCodes += getDetailLabel("ราคาส่งซ่อม", String.valueOf(r.repairCosts));
			for(; i<ids.length; i++){
				id = ids[i];
				RepairingDetail newDetail = RepairingDetail.find.byId(Long.valueOf(id));
				if(newDetail.repairing.equals(r)){
					detailsCodes += getDetailLabel("หมายเลขพัสดุ", newDetail.durableArticles.code);
					detailsCodes += getDetailLabel("ลักษณะการชำรุด", newDetail.description);
				}else{
					i--;
					break;
				}
			}
			
			result += getExpandableHTML("รายการส่งซ่อม", detailsCodes);
			result += "</div>";
		}
		result += "</div>";
		return result;
	}
	
	private static String getBorrowHTML(String[] ids) {
		String result = "<div>";
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(int i=0; i<ids.length; i++){
			String id = ids[i];
			String detailsCodes = "";
			BorrowDetail bd = BorrowDetail.find.byId(Long.valueOf(id));
			Borrow b = bd.borrow; 
			result += "<div class=\"well\">";
			
			result += getDetailLabel("id", String.valueOf(bd.id));
			result += getDetailLabel("รายการ/เรื่อง", bd.borrow.title);
			result += getDetailLabel("หมายเลขใบรายการ", bd.borrow.number);
			result += getDetailLabel("ผู้ยืม", String.format("%s %s %s", b.user.namePrefix, b.user.firstName, b.user.lastName));
			result += getDetailLabel("ผู้อนุมัติ", String.format("%s %s %s", b.approver.namePrefix, b.approver.firstName, b.approver.lastName ));
			result += getDetailLabel("วันที่ยืม", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(b.dateOfStartBorrow));
			if(b.dateOfEndBorrow != null){
				result += getDetailLabel("วันที่รับคืน", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(b.dateOfEndBorrow));
			}
			//TODO ผู้รับผิดชอบ
			for(; i<ids.length; i++){
				id = ids[i];
				BorrowDetail newDetail = BorrowDetail.find.byId(Long.valueOf(id));
				if(newDetail.borrow.equals(b)){
					detailsCodes += getDetailLabel("หมายเลขพัสดุ", newDetail.durableArticles.code);
					detailsCodes += getDetailLabel("รายละเอียดเพิ่มเติม", "pending");//newDetail.description);
				}else{
					i--;
					break;
				}
			}
			
			result += getExpandableHTML("รายการส่งซ่อม", detailsCodes);
			result += "</div>";
		}
		result += "</div>";
		return result;
	}

	private static String getRemainHTML(String[] ids1, String[] ids2) {
		// TODO Auto-generated method stub
		String result = "<div>";
		String name = null;
		Date date = null;
		int quantity = 0;
		int numRequisition = 0;
		result += "<button class=\"graphBack btn btn-danger btn-s\" onclick=\"backToTable()\">ย้อนกลับ</button>";
		for(String id : ids1){
			models.durableGoods.DurableGoods d = models.durableGoods.DurableGoods.find.byId(Long.valueOf(id));
			MaterialCode m = MaterialCode.find.byId(d.detail.code);
			name = m.description;
			if(date == null){
				date = d.detail.procurement.addDate;
				cal.setTime(d.detail.procurement.addDate);
			}else{
				Calendar c = Calendar.getInstance();
				c.setTime(d.detail.procurement.addDate);
				if(cal.before(c)){
					date = d.detail.procurement.addDate;
				}
			}
			quantity += 1;
		}
		result += "<div class=\"well\">";
		result += getDetailLabel("ชื่อวัสดุ", name);
		result += getDetailLabel("วันที่นำเข้าล่าสุด", new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(date));
		result += getDetailLabel("จำนวนนำเข้าทั้งหมด", String.valueOf(quantity));
		
		String expandable = "";
		HashMap<String, Integer> withdrawer = new HashMap<String, Integer>();
		for(int i=0; i<ids2.length; i++){
			String id = ids2[i];
			String detailsCodes = "";
			RequisitionDetail r = RequisitionDetail.find.byId(Long.valueOf(id));
			if(r.withdrawer!=null){
				String key = String.format("%s %s %s", r.withdrawer.namePrefix, r.withdrawer.firstName, r.withdrawer.lastName );
				Integer value = withdrawer.get(key);
				if(value == null){
					withdrawer.put(key, r.quantity);
				}else{
					withdrawer.put(key, withdrawer.get(key) + r.quantity);
				}
			}
			numRequisition += r.quantity;
		}
		for(String key : withdrawer.keySet()){
			expandable += getDetailLabel("ผู้เบิก", key + " เบิกจำนวน " + String.valueOf(withdrawer.get(key)));
		}
		result += getDetailLabel("จำนวนที่เหลือ", String.valueOf(quantity - numRequisition));
		result += getExpandableHTML("รายการผู้เบิก", expandable);
		result += "</div>";
		result += "</div>";
		return result;
	}
    
    /*private static ArrayNode getTableRepairing(Date startDate, Date endDate, int col, String selectedName){
    	ArrayNode result = JsonNodeFactory.instance.arrayNode();
    	HashMap<String,Integer> listResult = new HashMap<String,Integer>();
    	HashMap<String,String> ids = new HashMap<String,String>();
    	//HashMap<String,Double> cost = new HashMap<String,Double>();
    	
    	if(col == 1){ // ซ่อมแล้ว
    		List<Repairing> repaireds = Repairing.find.where().between("dateOfReceiveFromRepair", startDate, endDate).eq("status", ExportStatus.SUCCESS).findList();
    		for(Repairing r : repaireds){
    			for(RepairingDetail rd : r.detail){
    				String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    				if(key.equals(selectedName)){
    					key = rd.durableArticles.detail.fsn.descriptionDescription;
    					Integer value = listResult.get(key);
    					if(value == null){
    						listResult.put(key, 1);
    						ids.put(key,"" + rd.id);
    					}else{
    						listResult.put(key, value + 1);
    						ids.put(key,ids.get(key) + "," + rd.id );
    						//cost.put(key, rd.price);
    					}
    				}
    			}
    		}
    	}else if(col == 2){ // กำลังซ่อม
    		List<Repairing> repairings = Repairing.find.where().between("dateOfReceiveFromRepair", startDate, endDate).eq("status", ExportStatus.REPAIRING).findList();
    		for(Repairing r : repairings){
    			for(RepairingDetail rd : r.detail){
    				String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    				if(key.equals(selectedName)){
    					key = rd.durableArticles.detail.fsn.descriptionDescription;
    					Integer value = listResult.get(key);
    					if(value == null){
    						listResult.put(key, 1);
    						ids.put(key,"" + rd.id);
    					}else{
    						listResult.put(key, value + 1);
    						ids.put(key,ids.get(key) + "," + rd.id );
    					}
    				}
    			}
    		}
    	}
    	int i=1;
		for (String key : listResult.keySet()) {
			ArrayNode tr = JsonNodeFactory.instance.arrayNode();
			tr.add("" + i++);
			tr.add(key);
			tr.add(String.format("%d",listResult.get(key)));
			if(col == 1){
				tr.add(arg0)
			}
			tr.add(ids.get(key));
			//tr.add(descriptionBtn);
			result.add(tr);
		}
    	return result;
    }*/
    
    private static String getExpandableHTML(String header,String content){
		String result = "";
		result += "<div class=\"myBtn\">"+header+"&nbsp<span class=\"glyphicon glyphicon-chevron-right\"></span></div>";
		result += "<div class='collapse'>";
			result += content;
		result += "</div>";
		return result;
	}
    
    private static String getDetailLabel(String label, String content){
    	return "<div class=\"form-inline\">" +
    			"<div class=\"form-group detailLabel\" align=\"right\">"+ label + "&nbsp:</div>" +
    			"<div class=\"form-group detailContent\">" + content + "</div></div>";
    }
    
    private static String getDetailLabel(String label, String content,String style){
    	return "<div class=\"form-inline\">" +
    			"<div class=\"form-group detailLabel committee\" style=\"" +style + "\" align=\"right\">"+ label + "&nbsp:</div>" +
    			"<div class=\"form-group detailContent\">" + content + "</div></div>";
    }
    
    private static String getDetailCommitteeLabel(String label, String content){
    	return "<div class=\"form-inline\">" +
    			"<div class=\"form-group detailLabel committee\" align=\"right\">"+ label + "&nbsp:</div>" +
    			"<div class=\"form-group detailContent\">" + content + "</div></div>";
    }
    
	private static String getDescriptionButton(String className,String ids){
		return "<button class='btn btn-xs btn-info' onclick='getDescription(\""+ className +"\", \""+ ids + "\")' >รายละเอียด</button>";
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
	
	private static ArrayNode getSearchResult(String query){
		ArrayNode result = JsonNodeFactory.instance.arrayNode();
		List<User> q1 = util.SearchQuery.getUsers(query);
		List<Company> q2 = util.SearchQuery.getCompanies(query);
		List<models.durableArticles.Procurement> q3 = util.SearchQuery.getDurableArticleProcurement(query);
		List<models.durableGoods.Procurement> q4 = util.SearchQuery.getDurableGoodsProcurement(query);
		List<Requisition> q5 = util.SearchQuery.getRequisition(query);
		List<InternalTransfer> q6 = util.SearchQuery.getInternalTransfer(query);
		List<Auction> q7 = util.SearchQuery.getAuction(query);
		List<Donation> q8 = util.SearchQuery.getDonations(query);
		List<OtherTransfer> q9 = util.SearchQuery.getOtherTransfer(query);
		List<Repairing> q10 = util.SearchQuery.getRepair(query);
		List<Borrow> q11 = util.SearchQuery.getBorrow(query);
		List<MaterialCode> q12 = util.SearchQuery.getMaterialCodes(query);
		String[] names = {"ผู้ใช้","สถานประกอบการ","นำเข้าครุภัณฑ์","นำเข้าวัสดุ","เบิกจ่ายวัสดุ","โอนย้ายภายใน","จำหน่าย","บริจาค","โอนย้ายอื่นๆ","ส่งซ่อม","ขอยืม","คงเหลือ"};
		String ids = "";
		int i=1;
		if(q1.size()>0){
			for(User u : q1){
				if(ids.equals("")){
					ids += u.username;
				}else{
					ids += ","+u.username;
				}
			}
			result.add(getSearchTableRow(i++, names[0], q1.size(), "user", ids));
		}
		if(q2.size()>0){
			for(Company c : q2){
				if(ids.equals("")){
					ids += c.id;
				}else{
					ids += "," + c.id;
				} 
			}
			result.add(getSearchTableRow(i++, names[1], q2.size(), "company", ids));
		}
		if(q3.size()>0){
			result.add(getSearchTableRow(i++, names[2], q3.size(), "procurementDurableArticle", ids));
		}
		if(q4.size()>0){
			result.add(getSearchTableRow(i++, names[3], q4.size(), "procurementMaterialCode", ids));
		}
		if(q5.size()>0){
			result.add(getSearchTableRow(i++, names[4], q5.size(), "requisition", ids));
		}
		if(q6.size()>0){
			result.add(getSearchTableRow(i++, names[5], q6.size(), "internalTransfer", ids));
		}
		if(q7.size()>0){
			result.add(getSearchTableRow(i++, names[6], q7.size(), "auction", ids));
		}
		if(q8.size()>0){
			result.add(getSearchTableRow(i++, names[7], q8.size(), "donate", ids));
		}
		if(q9.size()>0){
			result.add(getSearchTableRow(i++, names[8], q9.size(), "otherTransfer", ids));
		}
		if(q10.size()>0){
			result.add(getSearchTableRow(i++, names[9], q10.size(), "repair", ids));
		}
		if(q11.size()>0){
			result.add(getSearchTableRow(i++, names[10], q11.size(), "borrow", ids));
		}
		/*if(q12.size()>0){
			result.add(getSearchTableRow(i++, names[11], q12.size(), "remain", ids));
		}*/
		return result;
	}

	private static ArrayNode getSearchTableRow(int index, String name, int size, String className, String ids ){
		ArrayNode tr = JsonNodeFactory.instance.arrayNode();
		tr.add(index+"");
		tr.add(name);
		tr.add(size + "");
		tr.add(getDescriptionButton(className, ids));
		return tr;
	}
	
	/*private static List<String> getTransferKey(List<otherTransfer> internals, List<Auction> auctions, List<Donation> donations, List<OtherTransfer> others){
    	List<String> list = new ArrayList<String>();
    	for(InternalTransfer internal : internals){
    		for(InternalTransferDetail internalDetail : internal.detail){
    			String key = internalDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			if(!list.contains(key)){
    				list.add(key);
    			}
    		}
    	}
    	
    	for(Auction auction : auctions){
    		for(AuctionDetail auctionDetail : auction.detail){
    			String key = auctionDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			if(!list.contains(key)){
    				list.add(key);
    			}
    		}
    	}
    	
    	for(Donation donation : donations){
    		for(DonationDetail donationDetail : donation.detail){
    			String key = donationDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			if(!list.contains(key)){
    				list.add(key);
    			}
    		}
    	}
    	
    	for(OtherTransfer other : others){
    		for(OtherTransferDetail otherDetail : other.detail){
    			String key = otherDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			if(!list.contains(key)){
    				list.add(key);
    			}
    		}
    	}
    	
    	return list;
    }*/
    
    /*private static HashMap<String, Integer[]> getMapTransfer(List<InternalTransfer> internals, List<Auction> auctions, List<Donation> donations, List<OtherTransfer> others, List<String> list){
    	HashMap<String , Integer[]> map = new HashMap<String, Integer[]>();
    	for(InternalTransfer internal : internals){
    		for(InternalTransferDetail internalDetail : internal.detail){
    			String key = internalDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer[] value = map.get(key);
    			if(value == null){
    				Integer[] newValue = {1,0,0,0};
    				map.put(key, newValue);
    			}else{
    				value[0] += 1;
    				map.put(key, value);
    			}
    		}
    	}
    	
    	for(Auction auction : auctions){
    		for(AuctionDetail auctionDetail : auction.detail){
    			String key = auctionDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer[] value = map.get(key);
    			if(value == null){
    				Integer[] newValue = {0,1,0,0};
    				map.put(key, newValue);
    			}else{
    				value[1] += 1;
    				map.put(key, value);
    			}
    		}
    	}
    	
    	for(Donation donation : donations){
    		for(DonationDetail donationDetail : donation.detail){
    			String key = donationDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer[] value = map.get(key);
    			if(value == null){
    				Integer[] newValue = {0,0,1,0};
    				map.put(key, newValue);
    			}else{
    				value[2] += 1;
    				map.put(key, value);
    			}
    		}
    	}
    	
    	for(OtherTransfer other : others){
    		for(OtherTransferDetail otherDetail : other.detail){
    			String key = otherDetail.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer[] value = map.get(key);
    			if(value == null){
    				Integer[] newValue = {0,0,0,1};
    				map.put(key, newValue);
    			}else{
    				value[3] += 1;
    				map.put(key, value);
    			}
    		}
    	}
    	return map;
    }*/
    
    /*private static HashMap<String, Integer[]> getMapRepairing(List<Repairing> repaireds, List<Repairing> repairings, List<String> list) {
    	HashMap<String , Integer[]> map = new HashMap<String, Integer[]>();
    	for(Repairing r : repaireds){
    		for(RepairingDetail rd : r.detail){
    			String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer[] value = map.get(key);
    			if(value == null){
    				Integer[] newValue = {1,0};
    				map.put(key, newValue);
    			}else{
    				value[0] += 1;
    				map.put(key, value);
    			}
    		}
    	}
    	for(Repairing r : repairings){
    		for(RepairingDetail rd : r.detail){
    			String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			Integer[] value = map.get(key);
    			if(value == null){
    				Integer[] newValue = {0,1};
    				map.put(key, newValue);
    			}else{
    				value[1] += 1;
    				map.put(key, value);
    			}
    		}
    	}
		return map;
	}*/

	/*private static List<String> getRepairKey(List<Repairing> repaireds, List<Repairing> repairings){
    	List<String> list = new ArrayList<String>();
    	for(Repairing r : repaireds){
    		for(RepairingDetail rd : r.detail){
    			String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			if(!list.contains(key)){
    				list.add(key);
    			}
    		}
    	}
    	
    	for(Repairing r : repairings){
    		for(RepairingDetail rd : r.detail){
    			String key = rd.durableArticles.detail.fsn.typ.groupClass.group.groupDescription;
    			if(!list.contains(key)){
    				list.add(key);
    			}
    		}
    	}
    	return list;
    }*/
    
}
