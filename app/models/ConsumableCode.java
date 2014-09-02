package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class ConsumableCode extends Model{

	@Id
	public String code;
	@Column(nullable=false)
	public String description;

	@ManyToOne
	public ConsumableType consumableType;

	@SuppressWarnings("unchecked")
	public static Finder<String,ConsumableCode> find = new Finder(String.class,ConsumableCode.class);
}