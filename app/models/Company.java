package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.fsnNumber.FSN_Type;

@Entity
public class Company extends Model{	// ครุภัณฑ์

	@Id
	public long id;
	public String name; // ชื่อ
	public String dealer; // ผู้ติดต่อ
	public String telephoneNumber;// เบอร์โทร
	public int payPeriod; // ระยะเวลาในการชำระเงิน
	public int sendPeriod; // ระยะเวลาในการจัดส่ง
	public String durableType; // ประเภทครุภัณฑ์
	public String consumableType; // ประเภทวัสดุ

	@OneToMany
	public Address address;// ที่อยู่

	@SuppressWarnings("unchecked")
	public static Finder<Long,Company> find = new Finder(Long.class,Company.class);
}