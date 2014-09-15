package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.fsnNumber.FSN_Type;

@Entity
public class Company extends Model{	// ครุภัณฑ์

	@Id
	public long id;
	public String typeEntrepreneur;          	//ประเภทผู้ประกอบการ
	public String typedealer;           	 	//ประเภทผู้ค้า
	public String nameEntrepreneur; 		 	//ชื่อสถานประกอบการ
	public String nameDealer; 				 	//ชื่อผู้ค้า
	public String payCodition;				 	//เงื่อนไขในการชำระเงิน
	public int payPeriod; 					 	//ระยะเวลาในการชำระเงิน
	public int sendPeriod; 					 	//ระยะเวลาในการจัดส่ง
	public String durableArticlesType; 		 	//ประเภทครุภัณฑ์
	public String durableGoodsType; 		 	//ประเภทวัสดุคงทนถาวร
	public String consumableGoodsType;       	//ประเภทวัสดุสิ้นเปลือง
	public String otherDetail;				//รายละเอียดอื่นๆ

	@ManyToOne
	public Address address;// ที่อยู่

	@SuppressWarnings("unchecked")
	public static Finder<Long,Company> find = new Finder(Long.class,Company.class);
}