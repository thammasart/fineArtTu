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

        double totlePrice = 0.00;
		int totleMonth = 1;
		int remainMonth = 1;
		if(detail != null){
			totlePrice = detail.price;
			totleMonth = this.getTotalLlifeTimeInMonth();
			remainMonth = this.getRemainInMonth(day, month, year);
		}
		double result = (totlePrice/totleMonth) * remainMonth;
		if(result <= 0){
			result = 1.00;
		} 
		return result;
	}
	
	public double getRemaining(){
		double totlePrice = 0.00;
		int totleMonth = 1;
		int remainMonth = 1;
		if(detail != null){
			totlePrice = detail.price;
			totleMonth = this.getTotalLlifeTimeInMonth();
			remainMonth = this.getRemainInMonth();
		}
		double result = (totlePrice/totleMonth) * remainMonth;
		if(result <= 0){
			result = 1.00;
		} 
		return result;	
	}

	public String getRemainingPriceToString(){
		double result = this.getRemaining(); 
		return String.format("%1$,.2f", result);	
	}

	public int getTotalLlifeTimeInMonth(){
		if(detail != null)
			return (int) (this.detail.llifeTime * 12.0);
		else 
			return 0;
	}

	public String getRemainLifetimeToString(){
		int month = this.getRemainInMonth();
		int year = month/12;
		month = month%12;
		String result = "" + year +" ปี " + month + " เดือน";
		return result;
	}

	public String getRemainLifetimeToString(int date, int month, int year){
		int remainMonth = this.getRemainInMonth(date, month, year);
		int remainYear = remainMonth/12;
		remainMonth = remainMonth%12;
		String result = "" + remainYear +" ปี " + remainMonth + " เดือน";
		return result;
	}

	public int getRemainInMonth(){
		Date today = new Date();
		int date = today.getDate();
		int month = today.getMonth()+1;
		int year = today.getYear()+2443;
        return getRemainInMonth(date, month, year);
	}

	public int getRemainInMonth(int date, int month, int year){
		int addDateDay = this.detail.procurement.getDay();
        int addDateMonth = this.detail.procurement.getMonth();
        int addDateYear = this.detail.procurement.getYear();
        if(addDateDay > 15){
        	addDateMonth++;
        }
        if(date > 15){
        	month++;
        }
        int remainMonth = this.getTotalLlifeTimeInMonth() - (((year-addDateYear)*12) + (month-addDateMonth));
        if(remainMonth < 0)	remainMonth = 0;
        return remainMonth;
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,DurableArticles> find = new Finder(Long.class,DurableArticles.class);
}
