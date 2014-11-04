package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;
import models.User;

@Entity
@Table (name = "durable__goods_eo_committee")
public class AI_Committee extends Model{

	@Id
	public long id;
	public String employeesType; // ประเภทกรรมการ - ประเภทบุคลากร
	public String committeePosition; // ตำแหน่งในคณกรรมการ

	@ManyToOne
	public User committee;	// กรรมการ
	@ManyToOne
	public Procurement procurement;	// การจัดซื้อ
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,AI_Committee> find = new Finder(Long.class,AI_Committee.class);
}