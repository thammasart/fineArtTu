package models.consumable;

import play.db.ebean.*;
import javax.persistence.*;

import models.ConsumableCode;
import models.Company;


@Entity
@Table (name = "consumable")
public class Consumable extends Model{ // วัสดุสิ้นเปลือง

	@Id
	public long id;
//	public String groupClass; // ประเภท
//	public String type; // ประเภทย่อย
//	public String name; // ชื่อ


	public int balance; // จำนวนปัจจุบัน, ยอดคงเหลือ
	public String classifier; // หน่วย, ลักษณนาม
	public String brand; // ยี่ห้อ

	public String details; // รายละเอียด
	public String partOfPic; // รูปภาพ

	@ManyToOne
	public Company company;

	@SuppressWarnings("unchecked")
	public static Finder<Long,Consumable> find = new Finder(Long.class,Consumable.class);
}
