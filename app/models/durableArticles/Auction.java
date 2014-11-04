package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import models.Company;
import models.type.ExportStatus;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "durable_articles_auction")
public class Auction extends Model{ // จำหน่าย หรือ การขายทอดตลาด

	@Id
	public long id;
	public String title; // ชื่อ เรื่อง
	public String contractNo; // สัญญาเลขที่
	public double totalPrice; // ราคารวม
	public Date approveDate; // วันที่ทำการอนุมัติ
	public String soldDestination; // บริษัท-ร้านค้า ที่จำหน่วยไป
	public String buildingNo; // เลขที่
	public String village; // หมู่
	public String alley; // ซอย
	public String road; // ถนน
	public String parish; // ตำบล
	public String district; // อำเภอ
	public String province; // จังหวัด
	public String telephoneNumber; // เบอร์โทร
	public String fax; // Fax
	public String postCode; // รหัสไปรษณีย์
	public String email; //e-mail

	public ExportStatus status; //สถานะใบโอน

	@ManyToOne
	public Company company; // ร้านค้าที่รับจำหน่าย

	@JsonBackReference
	@OneToMany(mappedBy="auction")
	public List<AuctionDetail> detail = new ArrayList<AuctionDetail>();

	@JsonBackReference
	@OneToMany(mappedBy="auction")
	public List<Auction_FF_Committee> ffCommittee = new ArrayList<Auction_FF_Committee>(); // คณะกรรมการสอบข้อเท็จจริง
	@JsonBackReference
	@OneToMany(mappedBy="auction")
	public List<Auction_D_Committee> dCommittee = new ArrayList<Auction_D_Committee>(); // คณะกรรมการจำหน่าย
	@JsonBackReference
	@OneToMany(mappedBy="auction")
	public List<Auction_E_Committee> eCommittee = new ArrayList<Auction_E_Committee>(); // คณะกรรมการประเมิณราคากลาง

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
	        }
        	result += (approveDate.getYear() + 2443);
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
	public static Finder<Long,Auction> find = new Finder(Long.class,Auction.class);
}