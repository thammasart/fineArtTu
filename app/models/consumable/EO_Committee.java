package models.consumable;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;

@Entity
@Table (name = "consumable_eo_committee")
public class EO_Committee extends Model{

	@Id
	public long id;

	@ManyToOne
	public Committee committee;	// กรรมการ
	@ManyToOne
	public Procurement procurement;	// การจัดซื้อ
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,EO_Committee> find = new Finder(Long.class,EO_Committee.class);
}