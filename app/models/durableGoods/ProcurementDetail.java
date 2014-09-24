package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import models.MaterialCode;

@Entity
@Table (name = "durable_goods_procurement_detail")
public class ProcurementDetail extends Model{

	@Id
	public long id;
	public String description; // ชื่อ - รายละเอียด
	public double price; // ราคราต่อหน่วย
	public double priceNoVat; // ราคาต่อหน่วยไม่รวมภาษี
	public int quantity; // จำนวน
	public String classifier; // หน่วย, ลักษณนาม
	public String brand; // ยี่ห้อ
	public String serialNumber; //หมายเลขเครื่อง
	public String partOfPic; // รูปภาพ

	@ManyToOne
	public MaterialCode code; // หมายเลขวัสดุ
	@ManyToOne
	public Procurement procurement; // การจัดซื้อ

	@SuppressWarnings("unchecked")
	public static Finder<Long,ProcurementDetail> find = new Finder(Long.class,ProcurementDetail.class);
}
