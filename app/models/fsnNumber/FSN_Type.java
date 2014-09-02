package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class FSN_Type extends Model{

	@Id
	public String id;
	@Column(nullable=false)
	public String description;

	@ManyToOne
	public FSN_Class groupClass;

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Type> find = new Finder(String.class,FSN_Type.class);
}