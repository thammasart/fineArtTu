package models.durableGoods;

import java.util.ArrayList;
import java.util.List;

import play.db.ebean.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import models.MaterialCode;


@Entity
@Table (name = "durable_goods_procurement_detail")
public class ProcurementDetail extends Model{

	@Id
	public long id;
	public String code;
	public String description; // ชื่อ - รายละเอียด
	public double price; // ราคราต่อหน่วย
	public double priceNoVat; // ราคาต่อหน่วยไม่รวมภาษี
	public int quantity; // จำนวน
	//public String classifier; // หน่วย, ลักษณนาม
	public String seller; // ยี่ห้อ
	public String phone; // ยี่ห้อ
	public String brand; // ยี่ห้อ
	public String serialNumber; //หมายเลขเครื่อง
	public String partOfPic; // รูปภาพ
	public int typeOfDurableGoods;//คงทนถาวร = 1 / false = 0 
	
	
	
	@JsonBackReference
	@OneToMany(mappedBy="detail")
	public List<DurableGoods> subDetails = new ArrayList<DurableGoods>();
/*
	@ManyToOne
	public MaterialCode code; // หมายเลขวัสดุ
*/
	@ManyToOne
	public Procurement procurement; // การจัดซื้อ

	@SuppressWarnings("unchecked")
	public static Finder<Long,ProcurementDetail> find = new Finder(Long.class,ProcurementDetail.class);
}
