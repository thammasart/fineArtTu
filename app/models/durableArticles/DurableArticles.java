package models.durableArticles;

import java.util.ArrayList;
import java.util.List;

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

	@SuppressWarnings("unchecked")
	public static Finder<Long,DurableArticles> find = new Finder(Long.class,DurableArticles.class);
}