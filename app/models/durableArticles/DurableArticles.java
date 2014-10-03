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

	public double getRemainingLifetime(){

		Date now = new Date();
		double lifeTime = detail.llifeTime;
		Date addDate = detail.procurement.addDate;
		double y  = addDate.getYear() - addDate.getYear();
		double m  = addDate.getMonth() - addDate.getMonth();
		if(addDate.getDate() > 15){
			m++;
		}
		if(addDate.getDate() > 15){
			m--;
		}
		double result = lifeTime - (y + (m/12));

		System.out.println("code : "+ code + "\t\tadd date :"+ addDate + "\t\tlifeTime :" + lifeTime + "\t\tresult :" + result);

		return result;
	}
	public double getRemainingPrice(){
		return 1.23456789;	
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,DurableArticles> find = new Finder(Long.class,DurableArticles.class);
}