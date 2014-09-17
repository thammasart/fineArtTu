package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;
import models.type.ExportStatus;

@Entity
@Table (name = "other_transfer")
public class OtherTransfer extends Model{

	@Id
	public long id;
	public Date approveDate; // วันที่ทำการอนุมัติ
	public String description; // สาเหตุ-รายละเอียดการโอน การโอน
	public ExportStatus status; //สถานะใบโอน

	@OneToMany
	public List<OtherTransfer_FF_Committee> ffCommittee = new ArrayList<OtherTransfer_FF_Committee>(); // คณะกรรมการสอบข้อเท็จจริง
	@OneToMany
	public List<OtherTransfer_D_Committee> dCommittee = new ArrayList<OtherTransfer_D_Committee>(); // คณะกรรมการจำหน่าย

	@SuppressWarnings("unchecked")
	public static Finder<Long,OtherTransfer> find = new Finder(Long.class,OtherTransfer.class);
}