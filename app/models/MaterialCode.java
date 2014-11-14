package models;

import models.durableGoods.ProcurementDetail;
import models.durableGoods.RequisitionDetail;
import models.type.ImportStatus;
import models.type.ExportStatus;
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
        
        public List<Double> getRemaining(int year){
            List<ProcurementDetail> importDetails = ProcurementDetail.find.where().eq("code",this.code).eq("procurement.status",ImportStatus.SUCCESS).le("procurement.budgetYear",year).findList();
            double totalImport = 0;
            double totalPrice = 0;
            for(ProcurementDetail detail : importDetails){
                totalImport += detail.quantity;
                totalPrice += detail.price * detail.quantity;
            }
            
            List<RequisitionDetail> exportDetails = RequisitionDetail.find.where().eq("code",this).eq("requisition.status",ExportStatus.SUCCESS).le("requisition.approveDate",new Date(year-2443,8,30)).findList();
            double totalExport = 0;
            for(RequisitionDetail detail : exportDetails){
                totalExport += detail.quantity;
            }

            List<Double> result = new ArrayList<Double>();
            result.add(totalImport-totalExport); // remaining
            if(totalImport>0){
                result.add(totalPrice/totalImport); // price per unit
            }else {
                result.add(0.0);
            }
          return result;
        }
	@SuppressWarnings("unchecked")
	public static Finder<String,MaterialCode> find = new Finder(String.class,MaterialCode.class);
}
