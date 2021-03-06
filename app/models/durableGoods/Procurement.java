package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import models.*;
import models.durableArticles.DurableArticles;
import models.durableGoods.DurableGoods;
import models.durableGoods.ProcurementDetail;
import models.durableGoods.AI_Committee;
import models.type.ImportStatus;
import models.type.SuppliesStatus;

import models.Committee;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "durable_goods_procurement")
public class Procurement extends Model{

	@Id
	public long id;
	public String title; 				// ชื่อรายการ
	public String contractNo; 			// เลขที่
	//public Date dateOfApproval; 		// วันที่อนุมัติในสัญญา
	public Date addDate = new Date(); 				// วันที่นำเข้า
	public Date checkDate = new Date(); 				// วันทีตรวจสอบ
	public String budgetType; 			// ประเภทงบประมาณ
	//public String institute;
	public int budgetYear; 				// ปีงบประมาณ
	//public String dealer; 			// ผู้ติดต่อ พนักงานขาย
	//public String telephoneNumber;	// เบอร์โทร พนักงานขาย
	public ImportStatus status;			//สถานะใบเบิก

	public String fileName;
	public String fileType;
	public String path;
	
	public String barCode;
	
	@ManyToOne
	public Company company; 				// บริษัทที่ทำการซื้อ

	@JsonBackReference
	@OneToMany(mappedBy="procurement")
	public List<ProcurementDetail> details = new ArrayList<ProcurementDetail>();
	@JsonBackReference
	@OneToMany(mappedBy="procurement")
	public List<AI_Committee> aiCommittee = new ArrayList<AI_Committee>(); // คณะกรรมการตรวจรับ

	public String getAddDate(){
		return  new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(addDate);
	}
	public String getCheckDate(){
		return  new SimpleDateFormat("dd/MM/yyyy", new Locale("th","th")).format(checkDate);
	}
	
	public int getUNCHANGE()
	{
		int canChangeOrderDetail=1;
		
		for(ProcurementDetail pd:this.details)
		{
			for(DurableGoods d:pd.subDetails)
			{
				if(d.status!=SuppliesStatus.NORMAL && d.status != SuppliesStatus.DELETE)
	    		{
	    			if(d.status!=null)
	    			{
	    			canChangeOrderDetail=0;break;
	    			}
	    		}
			}
		}
		
		return canChangeOrderDetail;
	}
	
	public String toString()
	{
		String s="";
		
		for(AI_Committee a: aiCommittee)
		{
			System.out.println("AIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII_COMITTTEEE");
			System.out.println(a.committee.namePrefix);
			System.out.println(a.committee.firstName);
			System.out.println(a.committee.lastName);
			
			System.out.println(a.committee.position);
			System.out.println(a.employeesType);
			System.out.println(a.committeePosition);

			
		}
		
		
		for(ProcurementDetail detail:this.details)
		{
			System.out.println("----------------------------------------ProcumentDetail-----------------------------------------------");
			System.out.println(detail.id);
			System.out.println(detail.description);
			System.out.println(detail.price);
			System.out.println(detail.priceNoVat);
			System.out.println(detail.quantity);
			System.out.println(detail.seller);
			System.out.println(detail.phone);
			System.out.println(detail.brand);
			System.out.println(detail.serialNumber);
			
		
			for(DurableGoods subDetail:detail.subDetails)
			{
				System.out.println("----------------------------------------SubDetail-------------------------------------------------");
				System.out.println(subDetail.id);
				System.out.println(subDetail.department);
				System.out.println(subDetail.room);
				System.out.println(subDetail.floorLevel);
				System.out.println(subDetail.codes);
				System.out.println(subDetail.title);
				System.out.println(subDetail.firstName);
				System.out.println(subDetail.lastName);
				System.out.println("----------------------------------------End------------------------------------------------------");
			}
	
		}
		
		return s;
	}

	@Override
   	public void save(){
		super.save();
		if(this.status == ImportStatus.SUCCESS && this.addDate != null){
			for(ProcurementDetail detail : details){
				MaterialCode material = MaterialCode.find.byId(detail.code);
				if(material != null){
					RequisitionDetail.updateAfter(this.addDate, material);
				}
			}
		}
	}

	@Override
   	public void update(){
		super.update();
		if(this.status == ImportStatus.SUCCESS && this.addDate != null){
			for(ProcurementDetail detail : details){
				MaterialCode material = MaterialCode.find.byId(detail.code);
				if(material != null){
					RequisitionDetail.updateAfter(this.addDate, material);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Finder<Long,Procurement> find = new Finder(Long.class,Procurement.class);
}
