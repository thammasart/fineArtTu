package models.durableGoods;

import java.util.ArrayList;
import java.util.List;

import play.db.ebean.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import models.MaterialCode;
import models.type.OrderDetailStatus;
import models.type.ImportStatus;
import models.type.SuppliesStatus;

import models.durableArticles.DurableArticles;
import models.fsnNumber.FSN_Description;


@Entity
@Table (name = "durable_goods_procurement_detail")
public class ProcurementDetail extends Model{

	@Id
	public long id;
	public String code;
	public String description; // ชื่อ - รายละเอียด
	public double price; // ราคราต่อหน่วย
	public double priceNoVat; // ราคาต่อหน่วยไม่รวมภาษี
	public int quantity; // จำนวน
	public int remain; // จำนวนคงเหลือของใบรายการ
	
	//public String classifier; // หน่วย, ลักษณนาม
	public String seller; // ยี่ห้อ
	public String phone; // ยี่ห้อ
	public String brand; // ยี่ห้อ
	public String serialNumber; //หมายเลขเครื่อง
	public String partOfPic; // รูปภาพ
	public int typeOfDurableGoods;//คงทนถาวร = 1 / false = 0 
	
	public OrderDetailStatus status;
	
	@JsonBackReference
	@OneToMany(mappedBy="detail")
	public List<DurableGoods> subDetails = new ArrayList<DurableGoods>();
/*
	@ManyToOne
	public MaterialCode code; // หมายเลขวัสดุ
*/
	@ManyToOne
	public Procurement procurement; // การจัดซื้อ

	@SuppressWarnings("unchecked")
	public static Finder<Long,ProcurementDetail> find = new Finder(Long.class,ProcurementDetail.class);
        
    public String getClassifier(){
        FSN_Description  fsn = FSN_Description.find.byId(this.code);           
        return fsn.classifier;
    }
    public String getGroupeType(){
        FSN_Description  fsn = FSN_Description.find.byId(this.code);           
        return fsn.typ.groupClass.group.groupDescription;
    }
    
    public int getUNCHANGE()
	{
		int canChangeOrderDetail=1;
		
		for(DurableGoods d: subDetails)
		{
			if(d.status!=SuppliesStatus.NORMAL && d.status != SuppliesStatus.DELETE)
    		{
    			if(d.status!=null)
    			{
    				canChangeOrderDetail=0;break;
    			}
    		}
		}
		
		return canChangeOrderDetail;
	}

    @Override
   	public void save(){
		super.save();
		MaterialCode material = MaterialCode.find.byId(this.code);
		if(this.procurement != null && material != null){
			if(this.procurement.status == ImportStatus.SUCCESS && this.procurement.addDate != null){
				RequisitionDetail.updateAfter(this.procurement.addDate, material);
			}
		}
	}

	@Override
   	public void update(){
		super.update();
		MaterialCode material = MaterialCode.find.byId(this.code);
		if(this.procurement != null && material != null){
			if(this.procurement.status == ImportStatus.SUCCESS && this.procurement.addDate != null){
				RequisitionDetail.updateAfter(this.procurement.addDate, material);
			}
		}
	}
}
