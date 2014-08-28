package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;

@Entity
@Table (name = "other_transfer_ff_committee")
public class OtherTransfer_FF_Committee extends Model{

	@Id
	public long id;

	@ManyToOne
	public Committee committee;	// กรรมการ
	@ManyToOne
	public OtherTransfer otherTransfer; // การโอนย้ายข้ามหน่ายงาน
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,OtherTransfer_FF_Committee> find = new Finder(Long.class,OtherTransfer_FF_Committee.class);
}