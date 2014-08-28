package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_repairing_detail")
public class RepairingDetail extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,RepairingDetail> find = new Finder(Long.class,RepairingDetail.class);
}