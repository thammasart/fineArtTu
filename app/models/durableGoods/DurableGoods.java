package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.*;
import models.MaterialCode;

@Entity
@Table (name = "durable_goods")
public class DurableGoods extends Model{	// ครุภัณฑ์

	@Id
	public long id;
	//public String code;
	public String department;		//สาขา
	public String room;				//ห้อง
	public String floorLevel;		//ชั้น
	public String codes; 			//รหัส
	public String title;			//คำนำหน้าชื่อ
	public String firstName;		//ชื่อ
	public String lastName;			//สกุล
	public int remain; // จำนวนปัจจุบัน, ยอดคงเหลือ
    public int typeOfDurableGoods;//คงทนถาวร = 1 / false = 0 
    
    public SuppliesStatus status;
	
/*
	@ManyToOne //หมายเลขวัสดุ
	public MaterialCode code;
	*/
	@ManyToOne
	public ProcurementDetail detail;


	@SuppressWarnings("unchecked")
	public static Finder<Long,DurableGoods> find = new Finder(Long.class,DurableGoods.class);
}
