package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.User;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "auction_ff_committee")
public class Auction_FF_Committee extends Model{

	@Id
	public long id;
	public String employeesType; // ประเภทกรรมการ - ประเภทบุคลากร
	public String committeePosition; // ตำแหน่งในคณกรรมการ

	@ManyToOne
	public User user;	// กรรมการ
	
	@JsonBackReference
	@ManyToOne
	public Auction auction;	// การจำหน่าย
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,Auction_FF_Committee> find = new Finder(Long.class,Auction_FF_Committee.class);
}