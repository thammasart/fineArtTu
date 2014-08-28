package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;

@Entity
@Table (name = "durable_articles_auction")
public class Auction extends Model{ // จำหน่าย หรือ การขายทอดตลาด

	@Id
	public long id;
	public double totalPrice; // ราคารวม
	public Date approveDate; // วันที่ทำการอนุมัติ

	@ManyToOne
	public Company company; // ร้านค้าที่รับจำหน่าย
	@OneToMany
	public List<Auction_FF_Committee> ffCommittee = new ArrayList<Auction_FF_Committee>(); // คณะกรรมการสอบข้อเท็จจริง
	@OneToMany
	public List<Auction_D_Committee> dCommittee = new ArrayList<Auction_D_Committee>(); // คณะกรรมการจำหน่าย
	@OneToMany
	public List<Auction_E_Committee> eCommittee = new ArrayList<Auction_E_Committee>(); // คณะกรรมการประเมิณราคากลาง

	@SuppressWarnings("unchecked")
	public static Finder<Long,Auction> find = new Finder(Long.class,Auction.class);
}