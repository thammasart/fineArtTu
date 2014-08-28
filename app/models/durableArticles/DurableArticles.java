package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.*;
import models.fsnNumber.FSN_Description;

@Entity
@Table (name = "durable_articles")
public class DurableArticles extends Model{	// ครุภัณฑ์

	@Id
	public long id;
	public String code; // รหัส
	public String codeFromStock; //รหัสจากคลัง
	public SuppliesStatus status; // สถานะ

	@ManyToOne
	public ProcurementDetail detail;

	@SuppressWarnings("unchecked")
	public static Finder<Long,DurableArticles> find = new Finder(Long.class,DurableArticles.class);
}