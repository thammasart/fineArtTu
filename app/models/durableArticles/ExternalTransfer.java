package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;

@Entity
@Table (name = "external_transfer")
public class ExternalTransfer extends Model{

	@Id
	public long id;
	public Date approveDate; // วันที่ทำการอนุมัติ

	@ManyToOne
	public Company company; // หน่ายงานที่รับจำหน่าย
	@OneToMany
	public List<ExternalTransfer_FF_Committee> ffCommittee = new ArrayList<ExternalTransfer_FF_Committee>(); // คณะกรรมการสอบข้อเท็จจริง
	@OneToMany
	public List<ExternalTransfer_D_Committee> dCommittee = new ArrayList<ExternalTransfer_D_Committee>(); // คณะกรรมการจำหน่าย

	@SuppressWarnings("unchecked")
	public static Finder<Long,ExternalTransfer> find = new Finder(Long.class,ExternalTransfer.class);
}