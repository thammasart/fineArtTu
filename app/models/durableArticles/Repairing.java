package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;
import models.User;
import models.type.ExportStatus;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "durable_articles_repairing")
public class Repairing extends Model{

	@Id
	public long id;
	public String title; // เรื่อง
	public String number; // เลขที่
	public Date dateOfSentToRepair;
	public Date dateOfReceiveFromRepair;
	public double repairCosts; // ราคาไซ่อม
	public ExportStatus status;
	
	@ManyToOne
	public Company company; // ร้านซ่อม
	@ManyToOne
	public User approver; // ผู้อนุมัติ

	@JsonBackReference
	@OneToMany(mappedBy="repairing")
	public List<RepairingDetail> detail = new ArrayList<RepairingDetail>();

	public void setDateOfSentToRepair(String date){
        String[] sList = date.split("/");
        if(sList.length == 3){
            int d = Integer.parseInt(sList[0]);
            int m = Integer.parseInt(sList[1]);
            int y = Integer.parseInt(sList[2]);
            this.dateOfSentToRepair = new Date(y-2443,m-1,d);
        }
        else{
        	this.dateOfSentToRepair = null;
        }
	}

	public String getDateOfSentToRepair(){
		if(dateOfSentToRepair == null){
			return " -- ไม่ระบุ -- ";
		}
		else{
			String result = ""+dateOfSentToRepair.getDate();
			if(result.length() == 1){
				result = "0" + result;
			}
			switch (dateOfSentToRepair.getMonth()) {
	            case 0:  result += "/01/";break;//" มกราคม ";break;
	            case 1:  result += "/02/";break;//" กุมภาพันธ์ ";break;
	            case 2:  result += "/03/";break;//" มีนาคม ";break;
	            case 3:  result += "/04/";break;//" เมษายน ";break;
	            case 4:  result += "/05/";break;//" พฤษภาคม ";break;
	            case 5:  result += "/06/";break;//" มิถุนายน ";break;
	            case 6:  result += "/07/";break;//" กรกฎาคม ";break;
	            case 7:  result += "/08/";break;//" สิงหาคม ";break;
	            case 8:  result += "/09/";break;//" กันยายน ";break;
	            case 9:  result += "/10/";break;//" ตุลาคม ";break;
	            case 10: result += "/11/";break;//" พฤษจิกายน ";break;
	            case 11: result += "/12/";break;//" ธันวาคม ";break;
	            default: result += "Invalid month";break;
	        }
        	result += (dateOfSentToRepair.getYear() + 2443);
			return result;
		}
	}

	public void setDateOfReceiveFromRepair(String date){
        String[] sList = date.split("/");
        if(sList.length == 3){
            int d = Integer.parseInt(sList[0]);
            int m = Integer.parseInt(sList[1]);
            int y = Integer.parseInt(sList[2]);
            this.dateOfReceiveFromRepair = new Date(y-2443,m-1,d);
        }
        else{
        	this.dateOfReceiveFromRepair = null;
        }
	}

	public String getDateOfReceiveFromRepair(){
		if(dateOfReceiveFromRepair == null){
			return " -- ไม่ระบุ -- ";
		}
		else{
			String result = ""+dateOfReceiveFromRepair.getDate();
			if(result.length() == 1){
				result = "0" + result;
			}
			switch (dateOfReceiveFromRepair.getMonth()) {
	            case 0:  result += "/01/";break;//" มกราคม ";break;
	            case 1:  result += "/02/";break;//" กุมภาพันธ์ ";break;
	            case 2:  result += "/03/";break;//" มีนาคม ";break;
	            case 3:  result += "/04/";break;//" เมษายน ";break;
	            case 4:  result += "/05/";break;//" พฤษภาคม ";break;
	            case 5:  result += "/06/";break;//" มิถุนายน ";break;
	            case 6:  result += "/07/";break;//" กรกฎาคม ";break;
	            case 7:  result += "/08/";break;//" สิงหาคม ";break;
	            case 8:  result += "/09/";break;//" กันยายน ";break;
	            case 9:  result += "/10/";break;//" ตุลาคม ";break;
	            case 10: result += "/11/";break;//" พฤษจิกายน ";break;
	            case 11: result += "/12/";break;//" ธันวาคม ";break;
	            default: result += "Invalid month";break;
	        }
        	result += (dateOfReceiveFromRepair.getYear() + 2443);
			return result;
		}
	}

	public String getCompanyToString(){
		String result = "";
		if(company != null){
			result += company.nameEntrepreneur;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,Repairing> find = new Finder(Long.class,Repairing.class);
}