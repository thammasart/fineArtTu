package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;

import models.User;

@Entity
@Table (name = "internal_transfer")
public class InternalTransfer extends Model{

	@Id
	public long id;
	public Date approveDate; // วันที่ทำการอนุมัติ

	@ManyToOne
	public User approver; // ผู้อนุมัติ
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,InternalTransfer> find = new Finder(Long.class,InternalTransfer.class);
}