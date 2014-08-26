package models.durableGoods;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;

@Entity
@Table (name = "durable_goods_committee")
public class Committee extends Model{

	@Id
	public long id;

	public CommitteeType type;

	@ManyToOne
	public Procurement procurement;
}