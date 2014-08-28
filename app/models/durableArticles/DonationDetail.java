package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;

@Entity
@Table (name = "durable_articles_donation_detail")
public class DonationDetail extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,DonationDetail> find = new Finder(Long.class,DonationDetail.class);
}