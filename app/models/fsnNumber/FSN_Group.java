package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class FSN_Group extends Model{

	@Id
	@Column(length=2)
	public String groupId;
	@Column(nullable=false)
	public String groupDescription;

	@JsonBackReference
	@OneToMany(mappedBy="group")
	public List<FSN_Class> classInGroup = new ArrayList<FSN_Class>();

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Group> find = new Finder(String.class,FSN_Group.class);
}
