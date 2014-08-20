package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class FSN_Class extends Model{

	@Id
	@Column(length=2)
	public String groupClassId;
	@Column(nullable=false)
	public String groupClassDescription;

	@ManyToOne
	public FSN_Group group;

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Class> find = new Finder(String.class,FSN_Class.class);
}