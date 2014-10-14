package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.Company;
import models.User;

@Entity
@Table (name = "durable_articles_borrow_detail")
public class BorrowDetail extends Model{

	@Id
	public long id;
	public String description; // ลักษะณะการชำรุด

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการโอนย่าย
	@ManyToOne
	public Borrow borrow; // การส่งซ่อม

	@SuppressWarnings("unchecked")
	public static Finder<Long,BorrowDetail> find = new Finder(Long.class,BorrowDetail.class);
}