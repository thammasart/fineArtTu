package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.Company;
import models.User;

@Entity
@Table (name = "repairing_detail")
public class RepairingDetail extends Model{

	@Id
	public long id;
	public String description; // ลักษะณะการชำรุด
	public double price; // ราคาซ่อม

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการโอนย่าย
	@ManyToOne
	public Repairing repairing; // การส่งซ่อม

	@SuppressWarnings("unchecked")
	public static Finder<Long,RepairingDetail> find = new Finder(Long.class,RepairingDetail.class);
}