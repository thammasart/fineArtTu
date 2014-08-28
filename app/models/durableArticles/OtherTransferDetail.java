package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;

import models.*;

@Entity
@Table (name = "other_transfer_detail")
public class OtherTransferDetail extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,OtherTransferDetail> find = new Finder(Long.class,OtherTransferDetail.class);
}