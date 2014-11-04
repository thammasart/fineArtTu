package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.Committee;

@Entity
@Table (name = "ex_transfer_ff_committee")
public class ExternalTransfer_FF_Committee extends Model{

	@Id
	public long id;
	public String employeesType; // ประเภทกรรมการ - ประเภทบุคลากร
	public String committeePosition; // ตำแหน่งในคณกรรมการ

	@ManyToOne
	public Committee committee;	// กรรมการ
	@ManyToOne
	public ExternalTransfer exTransfer; // การโอนย้ายข้ามหน่ายงาน
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,ExternalTransfer_FF_Committee> find = new Finder(Long.class,ExternalTransfer_FF_Committee.class);
}