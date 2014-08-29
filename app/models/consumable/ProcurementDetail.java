package models.consumable;

import play.db.ebean.*;
import javax.persistence.*;

import models.ConsumableCode;

@Entity
@Table (name = "consumable_procurement_detail")
public class ProcurementDetail extends Model{

	@Id
	public long id;
	public double price; // ราคราต่อหน่วย
	public double priceNoVat; // ราคาต่อหน่วยไม่รวมภาษี
	public int quantity; // จำนวน
	public String brand; // ยี่ห้อ
	public String partOfPic; // รูปภาพ

	@ManyToOne
	public ConsumableCode code; // หมายเลขวัสดุ
	@ManyToOne
	public Procurement procurement; // การจัดซื้อ

	@SuppressWarnings("unchecked")
	public static Finder<Long,ProcurementDetail> find = new Finder(Long.class,ProcurementDetail.class);
}
