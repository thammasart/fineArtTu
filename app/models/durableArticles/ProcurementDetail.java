package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.fsnNumber.FSN_Description;

@Entity
@Table (name = "durable_articles_procurement_detail")
public class ProcurementDetail extends Model{

	@Id
	public long id;
	public String description; // ชื่อ - รายละเอียด
	public double priceNoVat; // ราคาไม่รวมภาษี
	public double price; // ราคราต่อหน่วย
	public int quantity; // จำนวน
	public String classifier; // หน่วย, ลักษณนาม
	public double llifeTime;// อายุการใช้งาน
	public double alertTime;// เวลาแจ้งเตือน
	public String brand; // ยี่ห้อ
	public String serialNumber; //หมายเลขเครื่อง
	public String partOfPic; // รูปภาพ

	@ManyToOne
	public FSN_Description fsn; // หมายเลขครุภัณฑ์
	@ManyToOne
	public Procurement procurement; // การจัดซื้อ

	@SuppressWarnings("unchecked")
	public static Finder<Long,ProcurementDetail> find = new Finder(Long.class,ProcurementDetail.class);
}