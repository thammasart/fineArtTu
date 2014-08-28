package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.*;

@Entity
@Table (name = "durable_goods_procurement")
public class Procurement extends Model{

	@Id
	public long id;
	public String contractNo; // สัญญาเลขที่
	public String budgetType; // ประเภทงบประมาณ
	public int budgetYear; // ปีงบประมาณ
	public Date dateOfApproval; // วันที่อนุมัติในสัญญา
	public String dealer; // ผู้ติดต่อ พนักงานขาย
	public String telephoneNumber;// เบอร์โทร พนักงานขาย

	@ManyToOne
	public Company company; // บริษัทที่ทำการซื้อ
	@OneToMany
	public List<EO_Committee> eoCommittee = new ArrayList<EO_Committee>(); // คณะกรรมการตรวจรับ

	@SuppressWarnings("unchecked")
	public static Finder<Long,Procurement> find = new Finder(Long.class,Procurement.class);
}