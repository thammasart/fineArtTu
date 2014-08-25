package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.*;

@Entity
public class DurableGoods extends Model{	// ครุภัณฑ์

	@Id
	public long id;
	public String groupClass; // ประเภท
	public String type; // ประเภทย่อย
	public String name; // ชื่อ
	public String budgetType; // ประเภทงบประมาณ
	public int budgetYear; // ปีงบประมาณ
	public double llifeTime;// อายุการใช้งาน
	public double priceNoVat; // ราคาไม่รวมภาษี
	public double price; // ราคารวมภาษี
	public int balance; // จำนวนปัจจุบัน, ยอดคงเหลือ
	public String classifier; // หน่วย, ลักษณนาม
	public String brand; // ยี่ห้อ
	public String serialNumber; //หมายเลขเครื่อง
	public String dealer; // ผู้ติดต่อ
	public String telephoneNumber;// เบอร์โทร
	public String details; // รายละเอียด
	public String partOfPic; // รูปภาพ
	public SuppliesStatus status; // สถานะ

	@OneToMany //หมายเลขวัสดุ
	public ConsumableCode code;
	@OneToMany
	public Company company;


	@SuppressWarnings("unchecked")
	public static Finder<Long,DurableGoods> find = new Finder(Long.class,DurableGoods.class);
}