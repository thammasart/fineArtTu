package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import models.User;
import models.MaterialCode;

@Entity
@Table (name = "order_goods_detail")
public class OrderGoodsDetail extends Model{

	@Id
	public long id;
	public String description; // หมายเหตุการเบิก
	public String department; //สาขา
	public String room; //ห้อง
	public String floorLevel; //ชั้น
	public String codes; //รหัส
	public String title; //คำนำหน้าชื่อ
	public String firstName; //ชื่อ
	public String lastName; 
	public String position;

	@ManyToOne
	public DurableGoods goods; // 	วัสดุที่ต้องการจำหน่าย
	@ManyToOne
	public User withdrawer; // ผู้เบิก
	@ManyToOne
	public OrderGoods order; // ใบเบิก

	@SuppressWarnings("unchecked")
	public static Finder<Long,OrderGoodsDetail> find = new Finder(Long.class,OrderGoodsDetail.class);
}
