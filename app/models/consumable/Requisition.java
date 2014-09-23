package models.consumable;

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
@Table (name = "consumable_requisition")
public class Requisition extends Model{

	@Id
	public long id;
	public String title; // ชื่อ เรื่อง
	public String number; //เลขที่
	public Date approveDate; //วันที่ทำการอนุมัติ
	public ExportStatus status; //สถานะใบเบิก

	@JsonBackReference
	@OneToMany(mappedBy="requisition")
	public List<RequisitionDetail> detils = new ArrayList<RequisitionDetail>();

	@ManyToOne
	public User user; // ผู้จ่าย
	@ManyToOne
	public User approver; // ผู้อนุมัติ

	public String getApproveDate(){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(approveDate);
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,Requisition> find = new Finder(Long.class,Requisition.class);
}