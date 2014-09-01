package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.Company;
import models.User;

@Entity
@Table (name = "durable_articles_repairing_detail")
public class RepairingDetail extends Model{

	@Id
	public long id;
	public String descliption; // ลักษะณะการชำรุด
	public double price; // ราคาซ่อม

	@ManyToOne
	public Company company; // ร้านซ่อม
	@ManyToOne
	public User user; // `ผู้ส่งซ่อม 

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการโอนย่าย
	@ManyToOne
	public Repairing พepairing; // การส่งซ่อม

	@SuppressWarnings("unchecked")
	public static Finder<Long,RepairingDetail> find = new Finder(Long.class,RepairingDetail.class);
}