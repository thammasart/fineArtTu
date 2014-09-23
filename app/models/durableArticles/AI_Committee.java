package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;

@Entity
@Table (name = "durable_articles_ai_committee")
public class AI_Committee extends Model{

	@Id
	public long id;
	public String employeesType; // ประเภทกรรมการ - ประเภทบุคลากร
	public String committeePosition; // ตำแหน่งในคณกรรมการ


	@ManyToOne
	public Committee committee;	// กรรมการ
	@ManyToOne
	public Procurement procurement;	// การจัดซื้อ
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,AI_Committee> find = new Finder(Long.class,AI_Committee.class);
}