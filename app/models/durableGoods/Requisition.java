package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;

import models.*;

@Entity
@Table (name = "durable_goods_requisition")
public class Requisition extends Model{

	@Id
	public long id;
	public Date approveDate; // วันที่ทำการอนุมัติ

	@ManyToOne
	public User user; // ผู้จ่าย
	@ManyToOne
	public User approver; // ผู้อนุมัติ

	@SuppressWarnings("unchecked")
	public static Finder<Long,Requisition> find = new Finder(Long.class,Requisition.class);
}