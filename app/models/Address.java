package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class Address extends Model{

	@Id
	public long id;
	public String buildingNo; 				//เลขที่
	public String village;					//หมู่
	public String alley; 					//ซอย
	public String road; 					//ถนน
	public String parish; 					//ตำบล /แขวง
	public String district; 				//อำเภอ /เขต
	public String province; 				//จังหวัด
	public String telephoneNumber;			//เบอร์โทร
	public String fax;						//Fax
	public String postCode; 				//รหัสไปรษณีย์
	public String email; 					//email
	


	@SuppressWarnings("unchecked")
	public static Finder<Long,Company> find = new Finder(Long.class,Company.class);
}