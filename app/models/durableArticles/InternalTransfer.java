package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import models.User;
import models.type.ExportStatus;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "internal_transfer")
public class InternalTransfer extends Model{

	@Id
	public long id;
	public String title; // ชื่อ เรื่อง
	public String number; //เลขที่
	public Date approveDate; // วันที่ทำการอนุมัติ
	public ExportStatus status; //สถานะใบโอน

	@ManyToOne
	public User approver; // ผู้อนุมัต

	@JsonBackReference
	@OneToMany(mappedBy="internalTransfer")
	public List<InternalTransferDetail> detail = new ArrayList<InternalTransferDetail>();


	public void setApproveDate(String date){
		String[] sList = date.split("/");
		if(sList.length == 3){
			int d = Integer.parseInt(sList[0]);
			int m = Integer.parseInt(sList[1]);
			int y = Integer.parseInt(sList[2]);
			this.approveDate = new Date(y-2443,m-1,d);
		}
		else{
			this.approveDate = null;
		}
	}

	public String getApproveDate(){
		if(approveDate == null){
			return " -- ไม่ระบุ -- ";
		}
		else{
			String result = ""+approveDate.getDate();
			switch (approveDate.getMonth()) {
	            case 0:  result += " มกราคม ";break;
	            case 1:  result += " กุมภาพันธ์ ";break;
	            case 2:  result += " มีนาคม ";break;
	            case 3:  result += " เมษายน ";break;
	            case 4:  result += " พฤษภาคม ";break;
	            case 5:  result += " มิถุนายน ";break;
	            case 6:  result += " กรกฎาคม ";break;
	            case 7:  result += " สิงหาคม ";break;
	            case 8:  result += " กันยายน ";break;
	            case 9:  result += " ตุลาคม ";break;
	            case 10: result += " พฤษจิกายน ";break;
	            case 11: result += " ธันวาคม ";break;
	            default: result += "Invalid month";break;
	        }
        	result += (approveDate.getYear() + 2443);
			return result;
		}
	}
		
	@SuppressWarnings("unchecked")
	public static Finder<Long,InternalTransfer> find = new Finder(Long.class,InternalTransfer.class);
}