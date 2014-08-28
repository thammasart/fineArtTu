package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_repair")
public class Repair extends Model{

	@Id
	public long id;
	

	@SuppressWarnings("unchecked")
	public static Finder<Long,Repair> find = new Finder(Long.class,Repair.class);
}