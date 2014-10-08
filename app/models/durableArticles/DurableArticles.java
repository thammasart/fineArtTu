package models.durableArticles;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

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

	@ManyToOne
	public ProcurementDetail detail;

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