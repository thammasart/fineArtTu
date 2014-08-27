package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_other_external")
public class OtherTransfer extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,OtherTransfer> find = new Finder(Long.class,OtherTransfer.class);
}