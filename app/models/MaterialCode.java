package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class MaterialCode extends Model{

	@Id
	public String code;
	@Column(nullable=false)
	public String description;

	@ManyToOne
	public MaterialType materialType;

	@SuppressWarnings("unchecked")
	public static Finder<String,MaterialCode> find = new Finder(String.class,MaterialCode.class);
}