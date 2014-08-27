package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_external_external")
public class ExternalTransfer extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,ExternalTransfer> find = new Finder(Long.class,ExternalTransfer.class);
}