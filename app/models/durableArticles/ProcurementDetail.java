package models.durableArticles;

import java.util.ArrayList;
import java.util.List;

import play.db.ebean.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import models.fsnNumber.FSN_Description;

@Entity
@Table (name = "durable_articles_procurement_detail")
public class ProcurementDetail extends Model{

	@Id
	public long id;
	public String description; // ชื่อ - รายละเอียด
	public double price; // ราคราต่อหน่วย
	public double priceNoVat; // ราคาไม่รวมภาษี
	public int quantity; // จำนวน
	//public String classifier; // หน่วย, ลักษณนาม
	public double llifeTime;// อายุการใช้งาน
	public double alertTime;// เวลาแจ้งเตือน
	public String seller; // ยี่ห้อ
	public String phone; // ยี่ห้อ
	public String brand; // ยี่ห้อ
	public String serialNumber; //หมายเลขเครื่อง
	public String partOfPic; // รูปภาพ
	
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