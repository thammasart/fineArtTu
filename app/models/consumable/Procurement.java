package models.consumable;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;

@Entity
@Table (name = "consumable_procurement")
public class Procurement extends Model{

	@Id
	public long id;
	public String name; // ชื่อ เรื่อง
	public String contractNo; // สัญญาเลขที่
	public Date dateOfApproval; // วันที่อนุมัติในสัญญา
	public Date addDate; // วันที่นำเข้า
	public Date checkDate; // วันทีตรวจสอบ
	public String budgetType; // ประเภทงบประมาณ
	public int budgetYear; // ปีงบประมาณ
	public String dealer; // ผู้ติดต่อ พนักงานขาย
	public String telephoneNumber;// เบอร์โทร พนักงานขาย

	@ManyToOne
	public Company company; // บริษัทที่ทำการซื้อ

	@OneToMany
	public List<EO_Committee> eoCommittee = new ArrayList<EO_Committee>(); // คณะกรรมการตรวจรับ

	@SuppressWarnings("unchecked")
	public static Finder<Long,Procurement> find = new Finder(Long.class,Procurement.class);
}
