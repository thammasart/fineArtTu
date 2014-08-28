package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_auction_detail")
public class AuctionDetail extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,AuctionDetail> find = new Finder(Long.class,AuctionDetail.class);
}