package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;
import models.type.ImportStatus;

@Entity
@Table (name = "durable_articles_procurement")
public class Procurement extends Model{

	@Id
	public long id;
	public String title; 				// ชื่อรายการ
	public String contractNo; 			// เลขที่
	//public Date dateOfApproval; 		// วันที่อนุมัติในสัญญา
	public Date addDate = new Date(); 				// วันที่นำเข้า
	public Date checkDate = new Date(); 				// วันทีตรวจสอบ
	public String budgetType; 			// ประเภทงบประมาณ
	public int budgetYear; 				// ปีงบประมาณ
	//public String dealer; 			// ผู้ติดต่อ พนักงานขาย
	//public String telephoneNumber;	// เบอร์โทร พนักงานขาย
	public ImportStatus status;			//สถานะใบเบิก
	
	@ManyToOne
	public Company company; 			// บริษัทที่ทำการซื้อ 	

	@OneToMany
	public List<EO_Committee> eoCommittee = new ArrayList<EO_Committee>(); // คณะกรรมการเปิดซองสอบราคา
	@OneToMany
	public List<AI_Committee> aiCommittee = new ArrayList<AI_Committee>(); // คณะกรรมการตรวจรับ
	
	public String getAddDate(){
		return  addDate != null ? addDate.getDate() + "/" + (addDate.getMonth()+1) + "/" + (addDate.getYear()+1900):"";
	}
	
	public String toString(){
		String s = "";
		for(AI_Committee a : aiCommittee){
			s += "id = " + a.id + "\n";
			s += "firstName = " + a.committee.firstName + "\n";
			s += "lastName = " + a.committee.lastName + "\n";
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,Procurement> find = new Finder(Long.class,Procurement.class);
}
