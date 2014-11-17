package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.User;
import models.MaterialCode;
import models.type.*;
import models.User;

@Entity
@Table (name = "consumable_requisition_detail")
public class RequisitionDetail extends Model{

	@Id
	public long id;
	public int quantity; // จำนวน
	public Double price; // ราคาต๋อชิ้น
	public Double totlePrice; // ราคารวม
	public String description; // หมายเหตุการเบิก

	public int year; //ปีงบประมาณ
	public ExportStatus status; // สถานะ

	@ManyToOne
	public MaterialCode code; //รหัสัสด
	@ManyToOne
	public User withdrawer; // ผู้เบิก
	@ManyToOne
	public Requisition requisition;	// ใบเบิก

	public void updateDetail(){
		List<ProcurementDetail> importDetails = ProcurementDetail.find.where().eq("code",this.code.code).eq("procurement.status",ImportStatus.SUCCESS).eq("status",OrderDetailStatus.SUCCESS).le("procurement.addDate",this.requisition.approveDate).orderBy("procurement.addDate desc").findList();
		double totalImport = 0;
        for(ProcurementDetail detail : importDetails){
            totalImport += detail.quantity;
        }

		List<RequisitionDetail> exportDetails = RequisitionDetail.find.where().eq("code",this.code).eq("requisition.status",ExportStatus.SUCCESS).le("requisition.approveDate",requisition.approveDate).findList();
        double totalExport = 0;
        for(RequisitionDetail detail : exportDetails){
        	if(detail.requisition.equals(requisition)){
        		if(detail.id < this.id){
            		totalExport += detail.quantity;
            	}
            }
            else{
            	totalExport += detail.quantity;
            }
        }

        int remain = (int)(totalImport-totalExport);
        List<Double> priceList = new ArrayList<Double>();
        List<Integer> remainList = new ArrayList<Integer>();
        for(ProcurementDetail detail : importDetails){
       		if(remain > detail.quantity){
       			priceList.add(detail.price);
       			remainList.add(detail.quantity);
                remain -= detail.quantity;
            }
       		else{
       			priceList.add(detail.price);
       			remainList.add(remain);
                remain = 0;
                break;
            }
        }

        int quantity = this.quantity;
        double totlePrice = 0.00;
        while(quantity > 0 && !priceList.isEmpty() && !remainList.isEmpty()){
        	int index = remainList.size() -1;
        	remain = remainList.get(index);
        	if(quantity > remain){
        		totlePrice += priceList.get(index) * remain;
        		priceList.remove(index);
        		remainList.remove(index);
        		quantity -= remain;
        	}
        	else{
        		totlePrice += priceList.get(index) * quantity;
        		quantity = 0;
        	}
        }

        this.price = totlePrice/quantity;
        this.totlePrice = totlePrice;
        this.update();
	}

	public static void updateAfter(Date date, MaterialCode code){
		List<RequisitionDetail> exportDetails = RequisitionDetail.find.where().eq("code",code).eq("requisition.status",ExportStatus.SUCCESS).ge("requisition.approveDate",date).findList();
        for(RequisitionDetail detail : exportDetails){
        	detail.updateDetail();
        }
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,RequisitionDetail> find = new Finder(Long.class,RequisitionDetail.class);
}