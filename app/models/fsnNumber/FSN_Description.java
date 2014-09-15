package models.fsnNumber;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class FSN_Description extends Model{

	@Id
	@Column(length=11)
	public String descriptionId;
	@Column(nullable=false)
	public String descriptionDescription;

	@ManyToOne
	public FSN_Type type;

	@SuppressWarnings("unchecked")
	public static Finder<String,FSN_Description> find = new Finder(String.class,FSN_Description.class);
}