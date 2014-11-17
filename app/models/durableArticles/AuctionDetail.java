package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "auction_detail")
public class AuctionDetail extends Model{

	@Id
	public long id;

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการจำหน่าย

 	@ManyToOne
	public Auction auction;	// ใบเบิก

	public double getRemainPrice(){
		int date = auction.approveDate.getDate();
		int month = auction.approveDate.getMonth()+1;
		int year = auction.approveDate.getYear() + 2443;	
		double remainPrice = durableArticles.getDepreciationPrice(date,month,year);
		return remainPrice;
	}

	public String getRemainPriceTotring(){
		double result = this.getRemainPrice();
		return String.format("%.2f", result);
	}
	public String getRemainLifetimeTotring(){
		int date = auction.approveDate.getDate();
		int month = auction.approveDate.getMonth()+1;
		int year = auction.approveDate.getYear() + 2443;
		return this.durableArticles.getRemainLifetimeToString(date,month,year);
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,AuctionDetail> find = new Finder(Long.class,AuctionDetail.class);
}