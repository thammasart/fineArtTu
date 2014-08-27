package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "durable_articles_external_transfer_detail")
public class ExternalTransferDetail extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,ExternalTransferDetail> find = new Finder(Long.class,ExternalTransferDetail.class);
}