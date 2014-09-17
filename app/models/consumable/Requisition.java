package models.consumable;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;

import models.User;
import models.type.ExportStatus;

@Entity
@Table (name = "consumable_requisition")
public class Requisition extends Model{

	@Id
	public long id;
	public String title; // ชื่อ เรื่อง
	public String number; //เลขที่
	public Date approveDate; //วันที่ทำการอนุมัติ
	public ExportStatus status; //สถานะใบเบิก

	@ManyToOne
	public User user; // ผู้จ่าย
	@ManyToOne
	public User approver; // ผู้อนุมัติ

	@SuppressWarnings("unchecked")
	public static Finder<Long,Requisition> find = new Finder(Long.class,Requisition.class);
}