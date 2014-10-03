package models.durableArticles;

import play.data.Form;
import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;
import models.durableArticles.*;
import models.fsnNumber.FSN_Description;
import models.type.ImportStatus;
import models.type.SuppliesStatus;

import models.Committee;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
	public String institute;
	public int budgetYear; 				// ปีงบประมาณ
	//public String dealer; 			// ผู้ติดต่อ พนักงานขาย
	//public String telephoneNumber;	// เบอร์โทร พนักงานขาย
	public ImportStatus status;			//สถานะใบเบิก
	
	@ManyToOne
	public Company company; 			// บริษัทที่ทำการซื้อ 	
	
	@JsonBackReference
	@OneToMany(mappedBy="procurement")
	public List<ProcurementDetail> details = new ArrayList<ProcurementDetail>();
	

	@OneToMany(mappedBy="procurement")
	public List<EO_Committee> eoCommittee = new ArrayList<EO_Committee>(); // คณะกรรมการเปิดซองสอบราคา
	@OneToMany(mappedBy="procurement")
	public List<AI_Committee> aiCommittee = new ArrayList<AI_Committee>(); // คณะกรรมการตรวจรับ
	
	public String getAddDate(){
		return  addDate != null ? addDate.getDate() + "/" + (addDate.getMonth()+1) + "/" + (addDate.getYear()+1900):"";
	}
	public String getCheckDate(){
		return  checkDate != null ? checkDate.getDate() + "/" + (checkDate.getMonth()+1) + "/" + (checkDate.getYear()+1900):"";
	}
	
	
	
	public String toString()
	{
		String s="";
		/*
		List<models.durableArticles.ProcurementDetail> details = models.durableArticles.ProcurementDetail.find.where().eq("procurement", this).findList();
		for(ProcurementDetail detail :details)
		{
			System.out.println(detail.quantity); 
			for(DurableArticles subDetail:detail.subDetails)
			{
				System.out.println(subDetail.id);
			}
			
		}
		*/
		
		List<DurableArticles> dList = DurableArticles.find.all();
		System.out.println("testStatus");
		for(DurableArticles d:dList)
		{
			if(d.detail.procurement.status==ImportStatus.SUCCESS)
			{
				System.out.print(d.detail.procurement.id+": ");
				System.out.print(d.detail.id+": ");
				System.out.println(d.detail.procurement.status);
			}

		}
		
		
		/*
		for(AI_Committee a: aiCommittee)
		{
			System.out.println("AIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII_COMITTTEEE");
			System.out.println(a.committee.title);
			System.out.println(a.committee.firstName);
			System.out.println(a.committee.lastName);
			
			System.out.println(a.committee.identificationNo);
			System.out.println(a.committee.position);
			System.out.println(a.employeesType);
			System.out.println(a.committeePosition);

		}
		
		
		for(EO_Committee a: eoCommittee)
		{
			System.out.println("EOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO_COMITTTEEE");
			System.out.println(a.committee.title);
			System.out.println(a.committee.firstName);
			System.out.println(a.committee.lastName);
			
			System.out.println(a.committee.identificationNo);
			System.out.println(a.committee.position);
			System.out.println(a.employeesType);
			System.out.println(a.committeePosition);

			
		}
		
		/*
		List<models.durableArticles.ProcurementDetail> details = models.durableArticles.ProcurementDetail.find.where().eq("procurement", this).findList();
		for(int i=0;i<details.size();i++)
		{
			System.out.println(details.get(i).description);
			
			
		}
		*/
		
		/*
		for(ProcurementDetail detail:this.details)
		{
			System.out.println("----------------------------------------ProcumentDetail-----------------------------------------------");
			System.out.println(detail.id);
			System.out.println(detail.description);
			System.out.println(detail.price);
			System.out.println(detail.priceNoVat);
			System.out.println(detail.quantity);
			System.out.println(detail.llifeTime);
			System.out.println(detail.alertTime);
			System.out.println(detail.seller);
			System.out.println(detail.phone);
			System.out.println(detail.brand);
			System.out.println(detail.serialNumber);
			
			for(DurableArticles subDetail:detail.subDetails)
			{
				System.out.println("----------------------------------------SubDetail-------------------------------------------------");
				System.out.println(subDetail.id);
				System.out.println(subDetail.department);
				System.out.println(subDetail.room);
				System.out.println(subDetail.floorLevel);
				System.out.println(subDetail.code);
				System.out.println(subDetail.title);
				System.out.println(subDetail.firstName);
				System.out.println(subDetail.lastName);
				System.out.println(subDetail.codeFromStock);
				System.out.println("----------------------------------------End------------------------------------------------------");
			}
		}
		*/
		return s;
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,Procurement> find = new Finder(Long.class,Procurement.class);
}
