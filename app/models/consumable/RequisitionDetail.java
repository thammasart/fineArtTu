package models.consumable;

import play.db.ebean.*;
import javax.persistence.*;

import models.User;
import models.MaterialCode;

import models.User;

@Entity
@Table (name = "consumable_requisition_detail")
public class RequisitionDetail extends Model{

	@Id
	public long id;
	public int quantity; // จำนวน
	public String description; // หมายเหตุการเบิก

	@ManyToOne
	public MaterialCode code; //รหัสัสด
	@ManyToOne
	public User withdrawer; // ผู้เบิก
	@ManyToOne
	public Requisition requisition;	// ใบเบิก

	@SuppressWarnings("unchecked")
	public static Finder<Long,RequisitionDetail> find = new Finder(Long.class,RequisitionDetail.class);
}