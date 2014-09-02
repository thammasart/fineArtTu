package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class FSN_Type extends Model{

	@Id
	@Column(length=7)
	public String id;
	@Column(nullable=false)
	public String description;

	@OneToMany(mappedBy="type")
	public List<FSN_Description> desInType = new ArrayList<FSN_Description>();
	@ManyToOne
	public FSN_Class groupClass;

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Type> find = new Finder(String.class,FSN_Type.class);
}