package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_internal_transfer")
public class InternalTransfer extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,InternalTransfer> find = new Finder(Long.class,InternalTransfer.class);
}