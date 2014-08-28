package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;

@Entity
@Table (name = "donation_d_committee")
public class Donation_D_Committee extends Model{

	@Id
	public long id;

	@ManyToOne
	public Committee committee;	// กรรมการ
	@ManyToOne
	public Donation donation; // การบริจาค
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,Donation_D_Committee> find = new Finder(Long.class,Donation_D_Committee.class);
}