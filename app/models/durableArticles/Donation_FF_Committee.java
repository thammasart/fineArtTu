package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;

@Entity
@Table (name = "donation_ff_committee")
public class Donation_FF_Committee extends Model{

	@Id
	public long id;

	@ManyToOne
	public Committee committee;	// กรรมการ
	@ManyToOne
	public Donation donation; // การบริจาค
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,Donation_FF_Committee> find = new Finder(Long.class,Donation_FF_Committee.class);
}