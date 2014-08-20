package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class Address extends Model{

	@Id
	public long id;
	public String buildingNo; // อาคาร เลขที่ หมู่บ้าน
	public String alley; // ซอย
	public String road; // ถนน
	public String parish; // แขวง / ตำบล
	public String district; // เขต / อำเภอ
	public String province; // จังหวัด
	public String postCode; // รหัสไปรษณีย์


	@SuppressWarnings("unchecked")
	public static Finder<Long,Company> find = new Finder(Long.class,Company.class);
}