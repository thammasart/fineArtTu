package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;

@Entity
@Table (name = "auction_d_committee")
public class Auction_D_Committee extends Model{

	@Id
	public long id;

	@ManyToOne
	public Committee committee;	// กรรมการ
	@ManyToOne
	public Auction auction;	// การจำหน่าย
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,Auction_D_Committee> find = new Finder(Long.class,Auction_D_Committee.class);
}