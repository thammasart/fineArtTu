package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class FSN_Description extends Model{

	@Id
	@Column(length=13)
	public String descriptionId;
	@Column(nullable=false)
	public String descriptionDescription;

	public String classifier; 									//หน่วย
	public String otherDetail;									//รายละเอียดอื่นๆ 
	
	public int remain = 0; //ของวัสดุคงทนถาวรเท่านั้น
	public double pricePerEach;
	
	public String fileName;
	public String path;
	public String fileType;
	

	@ManyToOne
	public FSN_Type typ;

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Description> find = new Finder(String.class,FSN_Description.class);
}