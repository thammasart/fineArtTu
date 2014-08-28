package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "internal_transfer_detail")
public class InternalTransferDetail extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,InternalTransferDetail> find = new Finder(Long.class,InternalTransferDetail.class);
}