package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;
import models.type.ExportStatus;

@Entity
@Table (name = "durable_articles_donation")
public class Donation extends Model{ // บริจาค

	@Id
	public long id;
	public String title; // ชื่อ เรื่อง
	public String contractNo; // สัญญาเลขที่
	public Date approveDate; // วันที่ทำการอนุมัติ
	public ExportStatus status; //สถานะใบโอน

	@ManyToOne
	public Company company; // หน่ายงานที่รับจำหน่าย
	@OneToMany
	public List<Donation_FF_Committee> ffCommittee = new ArrayList<Donation_FF_Committee>(); // คณะกรรมการสอบข้อเท็จจริง
	@OneToMany
	public List<Donation_D_Committee> dCommittee = new ArrayList<Donation_D_Committee>(); // คณะกรรมการจำหน่าย

	@SuppressWarnings("unchecked")
	public static Finder<Long,Donation> find = new Finder(Long.class,Donation.class);
}