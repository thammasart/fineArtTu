package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class ConsumableType extends Model{

	@Id
	public long id;
	@Column(nullable=false)
	public String typeName;
	@Column(nullable=false)
	public String acronym;

	@SuppressWarnings("unchecked")
	public static Finder<Long,ConsumableType> find = new Finder(Long.class,ConsumableType.class);
}