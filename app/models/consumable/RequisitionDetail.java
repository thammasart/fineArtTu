package models.consumable;

import play.db.ebean.*;
import javax.persistence.*;

import models.User;
import models.ConsumableCode;

@Entity
@Table (name = "consumable_requisition_detail")
public class RequisitionDetail extends Model{

	@Id
	public long id;
	public ConsumableCode code;
	public int quantity;
	public String description;

	@ManyToOne
	public User user;
	@ManyToOne
	public Requisition requisition;

	@SuppressWarnings("unchecked")
	public static Finder<Long,RequisitionDetail> find = new Finder(Long.class,RequisitionDetail.class);
}