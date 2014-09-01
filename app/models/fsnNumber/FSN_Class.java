package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class FSN_Class extends Model{

	@Id
	@Column(length=4)
	public String id;
	@Column(nullable=false)
	public String description;

	@ManyToOne
	public FSN_Group group;

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Class> find = new Finder(String.class,FSN_Class.class);
}