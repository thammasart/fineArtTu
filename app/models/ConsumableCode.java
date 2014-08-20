package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class ConsumableCode extends Model{

	@Id
	public long id;
	@Column(nullable=false)
	public long number;
	@Column(nullable=false)
	public int code;
	@Column(nullable=false)
	public String description;

	@ManyToOne
	public ConsumableType consumableType;

	@SuppressWarnings("unchecked")
	public static Finder<Long,ConsumableCode> find = new Finder(Long.class,ConsumableCode.class);
}