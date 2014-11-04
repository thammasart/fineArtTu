package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;
import models.User;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "durable_articles_eo_committee")
public class EO_Committee extends Model{

	@Id
	public long id;
	public String employeesType; // ประเภทกรรมการ - ประเภทบุคลากร
	public String committeePosition; // ตำแหน่งในคณกรรมการ

	@JsonBackReference
	@ManyToOne
	public User committee;	// กรรมการ
	@JsonBackReference
	@ManyToOne
	public Procurement procurement;	// การจัดซื้อ
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,EO_Committee> find = new Finder(Long.class,EO_Committee.class);
}