package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.User;

@Entity
@Table (name = "auction_ff_committee")
public class Auction_FF_Committee extends Model{

	@Id
	public long id;

	@ManyToOne
	public User user;	// กรรมการ
	@ManyToOne
	public Auction auction;	// การจำหน่าย
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,Auction_FF_Committee> find = new Finder(Long.class,Auction_FF_Committee.class);
}