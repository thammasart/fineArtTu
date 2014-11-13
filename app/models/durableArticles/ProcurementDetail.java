package models.durableArticles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import play.db.ebean.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import models.fsnNumber.FSN_Description;
import models.type.OrderDetailStatus;

@Entity
@Table (name = "durable_articles_procurement_detail")
public class ProcurementDetail extends Model{

	@Id
	public long id;
	public String description; // ชื่อ - รายละเอียด
	public double price; // ราคราต่อหน่วย
	public double priceNoVat; // ราคาไม่รวมภาษี
	
	public double depreciationPrice=0.0; //มูลค่าสินทรัพย์
	public double depreciationOfYear=0.0; //ค่าเสื่อมประจำปี
	
	public int quantity; // จำนวน
	//public String classifier; // หน่วย, ลักษณนาม
	public double llifeTime;// อายุการใช้งาน
	public double alertTime;// เวลาแจ้งเตือน
	public String seller; // ยี่ห้อ
	public String phone; // ยี่ห้อ
	public String brand; // ยี่ห้อ
	public String serialNumber; //หมายเลขเครื่อง
	public OrderDetailStatus status;
	
	
	
	public void getDepreciationOfYear(double value)
	{
		//System.out.println(value);
		depreciationOfYear=value;
		this.update();
	}
	
	public double getSumablePrice()
	{
		return this.price*this.quantity;
	}
	

	public double getAnnualDepricate(){	////////////////////////ค่าเสื่อมประจำปี
		return getSumablePrice()/this.llifeTime;
	}
	
	public List<Double> getTotalDepricate(int year){
			////////////////////////////////////////////////////////////////////////
		 	Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                //24/08/2557
	        //int year = localCalendar.get(Calendar.YEAR);
	        /////////////////////////////////////////////////////////////////////////
	        int addDateDay = this.procurement.getDay();
	        int addDateMonth = this.procurement.getMonth();
	        int addDateYear = this.procurement.getYear();
	        
	        int beginYear=addDateYear;
	        if(addDateDay>15 && addDateMonth>=9)
	        	beginYear++;
		
	        double deprecateOfYear = 0;
	        double deprecatePrice = getSumablePrice();
	        double totalDeprecate = 0;
	        
	        if(this.llifeTime<=year-beginYear)		//กรณีเกินlife span
	        {
	        	deprecateOfYear=getAnnualDepricate();		//ค่าเสื่อมประจำปี
	        	deprecatePrice=1.0;							//มูลค่าทรัพย์เป็น 1
	        	totalDeprecate=getSumablePrice()-1;			//ค่าเสื่อมสะสม คือราคาทั้งหมด-1
	        }
	        else if(year<beginYear)
	        {
	        	deprecateOfYear=-1;							//ไม่คำนวณ
	        	deprecatePrice=-1;							//ไม่คำนวณ
	        	totalDeprecate=-1;							//ไม่คำนวณ
	        }
	        else
	        {
		        for(int i=0;i<=year-beginYear;i++)
		        {
		        	if(i==0){								//ปีแรก
		        		if(addDateDay>15)
		        			addDateMonth++;
		        		
		        		int countMonth=0;
		        		
		        		if(addDateMonth<=9)
		        			countMonth=9-addDateMonth+1;
		        		else
		        		{
		        			countMonth=9-addDateMonth+13;
		        		}

		        		deprecateOfYear = getAnnualDepricate()*(countMonth/12.0); 	//ans
		        		deprecatePrice=deprecatePrice-deprecateOfYear;				//ans
		        		totalDeprecate=totalDeprecate+deprecateOfYear;				//ans
		        		
		        		if(deprecatePrice<=1.0)
		        		{
		        			deprecatePrice=1.0;
		        			totalDeprecate=getSumablePrice()-1;
		        			break;
		        		}
		        	}
		        	else{									//ปีอื่นๆ
		        		deprecateOfYear = getAnnualDepricate();
		        		deprecatePrice=deprecatePrice-deprecateOfYear;
		        		totalDeprecate=totalDeprecate+deprecateOfYear;
		        		
		        		if(deprecatePrice<=1.0)
		        		{
		        			deprecatePrice=1.0;
		        			totalDeprecate=getSumablePrice()-1;
		        			break;
		        		}
		        	}
		        }
	        }
	        
	        List<Double> returnValue = new ArrayList<Double>();
	        returnValue.add(deprecateOfYear);
	        returnValue.add(deprecatePrice);
	        returnValue.add(totalDeprecate);
		
		return returnValue;
	}
	
	
	public double getTotalDepreciationPrice()	//return ค่าเสื่อมราคาสะสม
	{
		return this.getSumablePrice()-this.depreciationPrice;
	}
	
	@JsonBackReference
	@OneToMany(mappedBy="detail")
	public List<DurableArticles> subDetails = new ArrayList<DurableArticles>();

	@ManyToOne
	public FSN_Description fsn; // หมายเลขครุภัณฑ์
	
	@ManyToOne
	public Procurement procurement; // การจัดซื้อ
	
	

	@SuppressWarnings("unchecked")
	public static Finder<Long,ProcurementDetail> find = new Finder(Long.class,ProcurementDetail.class);
	
}
