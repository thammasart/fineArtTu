package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;
import models.User;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "other_transfer_d_committee")
public class OtherTransfer_D_Committee extends Model{

	@Id
	public long id;
	public String employeesType; // ประเภทกรรมการ - ประเภทบุคลากร
	public String committeePosition; // ตำแหน่งในคณกรรมการ

	@ManyToOne
	public User user;	// กรรมการ
	
	@JsonBackReference
	@ManyToOne
	public OtherTransfer otherTransfer; // การโอนย้ายข้ามหน่ายงาน
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,OtherTransfer_D_Committee> find = new Finder(Long.class,OtherTransfer_D_Committee.class);
}