package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;

@Entity
@Table (name = "auction_e_committee")
public class Auction_E_Committee extends Model{

	@Id
	public long id;

	@ManyToOne
	public Committee committee;	// กรรมการ
	@ManyToOne
	public Auction auction;	// การจำหน่าย
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,Auction_E_Committee> find = new Finder(Long.class,Auction_E_Committee.class);
}