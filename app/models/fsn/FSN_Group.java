package models.fsn;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class FSN_Group extends Model{

	@Id
	public String groupId;
	@Column(nullable=false)
	public String groupDescription;

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Group> find = new Finder(String.class,FSN_Group.class);
}