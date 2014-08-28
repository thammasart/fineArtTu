package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_Auction")
public class Auction extends Model{ // การขายทอดตลาด

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,Donation> find = new Finder(Long.class,Donation.class);
}