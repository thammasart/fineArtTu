package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class FSN_Group extends Model{

	@Id
	@Column(length=2)
	public String id;
	@Column(nullable=false)
	public String description;

	@OneToMany
	public List<FSN_Class> groupClassList = new ArrayList<FSN_Class>(); // คณะกรรมการตรวจรับ

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Group> find = new Finder(String.class,FSN_Group.class);
}