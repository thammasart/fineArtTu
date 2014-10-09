package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "internal_transfer_detail")
public class InternalTransferDetail extends Model{

	@Id
	public long id;
	public String code; // รหัส
	public String department; //สาขา
	public String room;	//ห้อง
	public String floorLevel; //ชั้น

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการโอนย่าย

	@ManyToOne
	public InternalTransfer internalTransfer; // การโอนย้ายภายใน

	@SuppressWarnings("unchecked")
	public static Finder<Long,InternalTransferDetail> find = new Finder(Long.class,InternalTransferDetail.class);
}