package models.durableArticles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.TimeZone;

import play.db.ebean.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import models.type.*;
import models.fsnNumber.FSN_Description;

@Entity
@Table (name = "durable_articles")
public class DurableArticles extends Model{	// ครุภัณฑ์

	@Id
	public long id;
	public String department;		//สาขา
	public String room;				//ห้อง
	public String floorLevel;		//ชั้น
	public String code; 			//รหัส
	public String title;			//คำนำหน้าชื่อ
	public String firstName;		//ชื่อ
	public String lastName;			//สกุล
	public String codeFromStock; 	//รหัสจากคลัง
	public SuppliesStatus status; 	// สถานะ
	
	public String barCode; 			//รหัสบาร์โค๊ด

	@ManyToOne
	public ProcurementDetail detail;

	
	public double getDepreciationPrice()
	{
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        int day = localCalendar.get(Calendar.DATE);
        int month = localCalendar.get(Calendar.MONTH) + 1;
        int year = localCalendar.get(Calendar.YEAR);
        
		return getDepreciationPrice( day, month, year);
	}

	/*int day,int month,int year*/
	public double getDepreciationPrice(int day, int month, int year)
	{
        int addDateDay = this.detail.procurement.getDay();
        int addDateMonth = this.detail.procurement.getMonth();
        int addDateYear = this.detail.procurement.getYear();
		
		// System.out.println("Welcome To Hell");
		// System.out.println("Item   :" + addDateDay + "/" + addDateMonth + "/" + addDateYear);
		// System.out.println("Current:" + day + "/" + month + "/" + year);
		// System.out.println(this.detail.price+"  "+this.detail.llifeTime);
		
		double depreciationPrice = this.detail.price;
		double depreciationOfYear = this.detail.price / this.detail.llifeTime; 
		double depreciationOfMonth = depreciationOfYear / 12;
		
		for(int i =0;i<year-addDateYear;i++){
			if(depreciationPrice-depreciationOfYear>1.0){
				depreciationPrice=depreciationPrice-depreciationOfYear;
				// System.out.println("reduce:"+i+":"+depreciationOfYear);
			}
			else break;
		}
		
		if(day>15){
			month++;
		}
		if(addDateDay>15){
			addDateMonth++;	
		}

		for(int i =0;i<month-addDateMonth;i++){
			if( depreciationPrice - depreciationOfMonth > 1.0){
				depreciationPrice = depreciationPrice - depreciationOfMonth;
				// System.out.println("reduce:"+i+":"+depreciationOfMonth);
			}
			else{
				depreciationPrice = 1.0;
				break;
			}
		}
		return depreciationPrice;
	}
	
	
	
	
	public int getRemainMonthLifetime(){
		Date now = new Date();
		Date addDate = new Date();
		int lifeTime = 0;
		if(detail != null){
			lifeTime = (int)detail.llifeTime;
			addDate = detail.procurement.addDate;
		}
		int m  = (addDate.getYear() + lifeTime) - now.getYear();
		m = m*12;
		m = m + (addDate.getMonth() - now.getMonth());
		if(addDate.getDate() > 15){
			m++;
		}
		else if(addDate.getDate() < 15){
			m--;
		}
		return m;
	}

	public String getRemainLifetimeToString(){
		int m = this.getRemainMonthLifetime();
		if(m < 0)	m = 0;
		
		int y = m/12;
		m = m%12;

		String result = "" + y +" ปี " + m + " เดือน";
		return result;
	}

	public double getRemaining(){
		int lifeTime = 1;
		double price = 0;
		if(detail != null){
			lifeTime = (int)detail.llifeTime;
			price = (int)detail.price;
		}
		int m = (lifeTime*12) - this.getRemainMonthLifetime();
		double result = price - ((price/(lifeTime*12)) * m ); 
		return result;	
	}

	public String getRemainingPriceToString(){
		double result = this.getRemaining(); 
		return String.format("%1$,.2f", result);	
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,DurableArticles> find = new Finder(Long.class,DurableArticles.class);
}
