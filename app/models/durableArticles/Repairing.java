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
	public Date dateOfResiveFromRepair;
	public ExportStatus status;
	
	@ManyToOne
	public Company company; // ร้านซ่อม
	@ManyToOne
	public User approver; // ผู้อนุมัติ

	@JsonBackReference
	@OneToMany(mappedBy="repairing")
	public List<RepairingDetail> detail = new ArrayList<RepairingDetail>();

	@SuppressWarnings("unchecked")
	public static Finder<Long,Repairing> find = new Finder(Long.class,Repairing.class);
}