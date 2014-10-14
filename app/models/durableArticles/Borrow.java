package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;
import models.User;
import models.type.ExportStatus;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "durable_articles_borrow")
public class Borrow extends Model{

	@Id
	public long id;
	public String title; // เรื่อง
	public String number; // เลขที่
	public Date startBorrow;
	public Date endBorrow;
	public ExportStatus status;
	
	@ManyToOne
	public User user; // ร้านซ่อม
	@ManyToOne
	public User approver; // ผู้อนุมัติ

	@JsonBackReference
	@OneToMany(mappedBy="borrow")
	public List<BorrowDetail> detail = new ArrayList<BorrowDetail>();

	@SuppressWarnings("unchecked")
	public static Finder<Long,Borrow> find = new Finder(Long.class,Borrow.class);
}