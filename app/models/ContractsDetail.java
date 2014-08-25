package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.fsnNumber.FSN_Description;

@Entity
public class ContractsDetail extends Model{

	@Id
	public long id;

	public String code; // รหัสวัสดุ - ครุภัณฑ์
	public String description; // ชื่อ - รายละเอียด
	public int quantity; // จำนวน
	public String classifier; // หน่วย, ลักษณนาม
	public double priceNoVat; // ราคาไม่รวมภาษี
	public double price; // ราคราต่อหน่วย
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
	public static Finder<Long,Procurement> find = new Finder(Long.class,Procurement.class);
}