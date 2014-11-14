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

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการยืม
	@ManyToOne
	public Borrow borrow; // การยืม

	@SuppressWarnings("unchecked")
	public static Finder<Long,BorrowDetail> find = new Finder(Long.class,BorrowDetail.class);
}