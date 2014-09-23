package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class MaterialCode extends Model{

	@Id
	public String code;
	@Column(nullable=false)
	public String description;

	public String typeOfGood;
	public String classifier; 									//หน่วย
	public int minNumberToAlert;								//จำนวนขั้นต่ำสำหรับแจ้งเตือน
	public int lifeOfGood;
	public String otherDetail;									//รายละเอียดอื่นๆ 
	

	@ManyToOne
	public MaterialType materialType;

	@SuppressWarnings("unchecked")
	public static Finder<String,MaterialCode> find = new Finder(String.class,MaterialCode.class);
}