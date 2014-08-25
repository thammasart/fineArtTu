package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import models.fsnNumber.FSN_Description;

@Entity
@Table (name = "durable_goods_procurement_detail")
public class ProcurementDetail extends Model{

	@Id
	public long id;

	public String code; // รหัสวัสดุ - ครุภัณฑ์
	public String description; // ชื่อ - รายละเอียด
	public int quantity; // จำนวน
	public String classifier; // หน่วย, ลักษณนาม
	public double priceNoVat; // ราคาไม่รวมภาษี
	public double price; // ราคราต่อหน่วย
	public String brand; // ยี่ห้อ
	public String partOfPic; // รูปภาพ

	@ManyToOne
	public FSN_Description fsn; // หมายเลขครุภัณฑ์
	@ManyToOne
	public Procurement procurement; // การจัดซื้อ

	@SuppressWarnings("unchecked")
	public static Finder<Long,ProcurementDetail> find = new Finder(Long.class,ProcurementDetail.class);
}
