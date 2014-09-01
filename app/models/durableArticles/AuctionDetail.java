package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "auction_detail")
public class AuctionDetail extends Model{

	@Id
	public long id;
 	public String code; //รหัส
 	public double price; //มูลค่าคงเหลือ หลังหักค่าเสื่อม

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการจำหน่าย

 	@ManyToOne
	public Auction auction;	// ใบเบิก

	@SuppressWarnings("unchecked")
	public static Finder<Long,AuctionDetail> find = new Finder(Long.class,AuctionDetail.class);
}