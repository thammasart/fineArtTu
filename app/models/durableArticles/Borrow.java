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
@Table (name = "borrow")
public class Borrow extends Model{

	@Id
	public long id;
	public String title; // เรื่อง
	public String number; // เลขที่
	public Date dateOfStartBorrow;
	public Date dateOfEndBorrow;
	public String description; // หมายเหตุการยืม
	public ExportStatus status;
	
	@ManyToOne
	public User user; // ผู้ยืม
	@ManyToOne
	public User approver; // ผู้อนุมัติ

	@JsonBackReference
	@OneToMany(mappedBy="borrow")
	public List<BorrowDetail> detail = new ArrayList<BorrowDetail>();

	public void setDateOfStartBorrow(String date){
        String[] sList = date.split("/");
        if(sList.length == 3){
            int d = Integer.parseInt(sList[0]);
            int m = Integer.parseInt(sList[1]);
            int y = Integer.parseInt(sList[2]);
            this.dateOfStartBorrow = new Date(y-2443,m-1,d);
        }
	}

	public String getDateOfStartBorrow(){
		if(this.dateOfStartBorrow == null){
			return " -- ไม่ระบุ -- ";
		}
		else{
			String result = ""+this.dateOfStartBorrow.getDate();
			if(result.length() == 1){
				result = "0" + result;
			}
			switch (dateOfStartBorrow.getMonth()) {
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
        	result += (dateOfStartBorrow.getYear() + 2443);
			return result;
		}
	}

	public void setDateOfEndBorrow(String date){
        String[] sList = date.split("/");
        if(sList.length == 3){
            int d = Integer.parseInt(sList[0]);
            int m = Integer.parseInt(sList[1]);
            int y = Integer.parseInt(sList[2]);
            this.dateOfEndBorrow = new Date(y-2443,m-1,d);
        }
	}

	public String getDateOfEndBorrow(){
		if(this.dateOfEndBorrow == null){
			return " -- ไม่ระบุ -- ";
		}
		else{
			String result = ""+this.dateOfEndBorrow.getDate();
			if(result.length() == 1){
				result = "0" + result;
			}
			switch (dateOfEndBorrow.getMonth()) {
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
        	result += (dateOfEndBorrow.getYear() + 2443);
			return result;
		}
	}

	public String getWithdrawerToString(){
		String result = "";
		if(user != null){
			result += user.firstName + "  " + user.lastName;
		}
		return result;
	}

	public String getBarcode(){
		return String.format("E06%06X", this.id);
	} 

	@SuppressWarnings("unchecked")
	public static Finder<Long,Borrow> find = new Finder(Long.class,Borrow.class);
}
