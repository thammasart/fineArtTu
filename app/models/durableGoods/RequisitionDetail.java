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
@Table (name = "requisition_detail")
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
		List<ProcurementDetail> importDetails = ProcurementDetail.find.where().eq("code",this.code.code).eq("procurement.status",ImportStatus.SUCCESS).le("procurement.addDate",this.requisition.approveDate).orderBy("procurement.addDate desc").findList();
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
        List<ProcurementDetail> remainMaterial = new ArrayList<ProcurementDetail>();
        for(ProcurementDetail detail : importDetails){
       		if(remain > detail.quantity){
       			remainMaterial.add(0,detail);
                remain -= detail.quantity;
            }
       		else{
                detail.quantity = remain;
       			remainMaterial.add(0,detail);
                remain = 0;
                break;
            }
        }

        int quantity = this.quantity;
        double totlePrice = 0.00;
        while(quantity > 0 && !remainMaterial.isEmpty()){
        	remain = remainMaterial.get(0).quantity;
        	if(quantity > remain){
        		totlePrice += remainMaterial.get(0).price * remain;
        		remainMaterial.remove(0);
        		quantity -= remain;
        	}
        	else{
        		totlePrice += remainMaterial.get(0).price * quantity;
        		quantity = 0;
        	}
        }

        this.price = totlePrice/this.quantity;
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