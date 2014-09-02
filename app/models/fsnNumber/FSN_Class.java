package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class FSN_Class extends Model{

	@Id
	@Column(length=4)
	public String id;
	@Column(nullable=false)
	public String description;

	@OneToMany(mappedBy="groupClass")
	public List<FSN_Type> typeInClass = new ArrayList<FSN_Type>();
	@ManyToOne
	public FSN_Group group;

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Class> find = new Finder(String.class,FSN_Class.class);
}