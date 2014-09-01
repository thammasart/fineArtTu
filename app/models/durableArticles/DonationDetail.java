package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;

@Entity
@Table (name = "durable_articles_donation_detail")
public class DonationDetail extends Model{

	@Id
	public long id;
	public String code; // รหัส
	public String annotation; //หมายเหตุ คำอธิบายประกอบ

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการบริจาค
	@ManyToOne
	public Donation donation; // การโอนย้าย

	@SuppressWarnings("unchecked")
	public static Finder<Long,DonationDetail> find = new Finder(Long.class,DonationDetail.class);
}