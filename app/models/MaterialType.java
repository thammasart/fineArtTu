package models;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class MaterialType extends Model{

	@Id
	public long id;
	@Column(nullable=false)
	public String typeName;
	@Column(nullable=false)
	public String acronym;

	@OneToMany(mappedBy="materialType")
	public List<MaterialCode> codeInType = new ArrayList<MaterialCode>();

	@SuppressWarnings("unchecked")
	public static Finder<Long,MaterialType> find = new Finder(Long.class,MaterialType.class);
}