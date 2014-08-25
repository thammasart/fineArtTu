package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.*;
import models.ConsumableCode;

@Entity
public class DurableGoods extends Model{	// ครุภัณฑ์

	@Id
	public long id;
	public int remain; // จำนวนปัจจุบัน, ยอดคงเหลือ

	@ManyToOne //หมายเลขวัสดุ
	public ConsumableCode code;
	@ManyToOne
	public ProcurementDetail detail;


	@SuppressWarnings("unchecked")
	public static Finder<Long,DurableGoods> find = new Finder(Long.class,DurableGoods.class);
}
