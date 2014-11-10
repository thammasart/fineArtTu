package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;

import models.type.ExportStatus;

@Entity
@Table (name = "internal_transfer_detail")
public class InternalTransferDetail extends Model{

	@Id
	public long id;
	public String code; // รหัส

	public String firstDepartment; //สาขาเก่า
	public String firstRoom; //ห้องก่า
	public String firstFloorLevel; //ชั้นเก่า
	public String firstFirstName; // ชื่อผู้ดูแลเก่า
	public String firstLastName; // นามสกุลผู้ดูแลเก่า

	public String newDepartment; //สาขาใหม่
	public String newRoom;	//ห้องใหม่
	public String newFloorLevel; //ชั้นใหม่
	public String newFirstName; // ชื่อผู้ดูแลใหม่
	public String newLastName; // นามสกุลผู้ดูแลใหม่
	public String newPosition; // ตำแหน่งผู้ดูแลใหม่

	public Date approveDate;
	public ExportStatus status;

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการโอนย่าย

	@ManyToOne
	public InternalTransfer internalTransfer; // การโอนย้ายภายใน

	@SuppressWarnings("unchecked")
	public static Finder<Long,InternalTransferDetail> find = new Finder(Long.class,InternalTransferDetail.class);
}