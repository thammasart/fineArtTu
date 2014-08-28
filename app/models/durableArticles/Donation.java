package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_donation")
public class Donation extends Model{ // บริจาค

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,Donation> find = new Finder(Long.class,Donation.class);
}