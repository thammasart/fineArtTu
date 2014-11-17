package models;

import models.durableGoods.ProcurementDetail;
import models.durableGoods.RequisitionDetail;
import models.type.ImportStatus;
import models.type.ExportStatus;
import models.type.OrderDetailStatus;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class MaterialCode extends Model{

	@Id
	public String code;
	@Column(nullable=false)
	public String description;

	//public String typeOfGood;
	public String classifier; 									//หน่วย
	public int minNumberToAlert;								//จำนวนขั้นต่ำสำหรับแจ้งเตือน
	//public int lifeOfGood;
	public String otherDetail;									//รายละเอียดอื่นๆ 
	
	public int remain = 0; // จำนวนปัจจุบัน, ยอดคงเหลือ
	public double pricePerEach;

	public String fileName;
	public String path;
	public String fileType;
	
	@ManyToOne
	public MaterialType materialType;

    public void updateRemain(){
        List<ProcurementDetail> importDetails = ProcurementDetail.find.where().eq("code",this.code).eq("procurement.status",ImportStatus.SUCCESS).eq("status",OrderDetailStatus.SUCCESS).orderBy("procurement.addDate desc").findList();
        double totalImport = 0;
        for(ProcurementDetail detail : importDetails){
            totalImport += detail.quantity;
        }

        List<RequisitionDetail> exportDetails = RequisitionDetail.find.where().eq("code",this).eq("requisition.status",ExportStatus.SUCCESS).findList();
        double totalExport = 0;
        for(RequisitionDetail detail : exportDetails){
            totalExport += detail.quantity;
        }

        this.remain = (int)(totalImport-totalExport);
        if(this.remain > 0){
            int remain = (int)(totalImport-totalExport);
            double totalPrice = 0;
            for(int i=importDetails.size()-1; i>=0 && remain > 0; i--){
                if(remain > importDetails.get(i).quantity){
                    totalPrice += importDetails.get(i).price * importDetails.get(i).quantity;
                    remain -= importDetails.get(i).quantity;
                }
                else{
                    totalPrice += importDetails.get(i).price * remain;
                    remain = 0;
                    break;
                }
            }
            this.pricePerEach = totalPrice/(totalImport-totalExport); // price per unit
        }
        else {
            this.pricePerEach  = 0.00;
        }
        this.update();
    }
        
    public List<Double> getRemaining(int year){
        List<ProcurementDetail> importDetails = ProcurementDetail.find.where().eq("code",this.code).eq("procurement.status",ImportStatus.SUCCESS).le("procurement.budgetYear",year).orderBy("procurement.addDate asc").findList();
        double totalImport = 0;

        for(ProcurementDetail detail : importDetails){
            totalImport += detail.quantity;
        }
        
        List<RequisitionDetail> exportDetails = RequisitionDetail.find.where().eq("code",this).eq("requisition.status",ExportStatus.SUCCESS).le("requisition.approveDate",new Date(year-2443,8,30)).findList();
        double totalExport = 0;
        for(RequisitionDetail detail : exportDetails){
            totalExport += detail.quantity;
        }

        List<Double> result = new ArrayList<Double>();
        result.add(totalImport-totalExport); // remaining
        int remain = (int)(totalImport-totalExport);
        if(remain>0){
            double totalPrice = 0;
            for(int i=importDetails.size()-1; i>=0 && remain > 0; i--){
                if(remain > importDetails.get(i).quantity){
                    totalPrice += importDetails.get(i).price * importDetails.get(i).quantity;
                    remain -= importDetails.get(i).quantity;
                }
                else{
                    totalPrice += importDetails.get(i).price * remain;
                    remain = 0;
                    break;
                }
            }
            result.add(totalPrice/(totalImport-totalExport)); // price per unit
        }else {
            result.add(0.0);
        }
      return result;
    }

	@SuppressWarnings("unchecked")
	public static Finder<String,MaterialCode> find = new Finder(String.class,MaterialCode.class);
}
