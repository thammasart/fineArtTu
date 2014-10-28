package models;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class MaterialType extends Model{

	@Id
	public String preCodeId;
	@Column(nullable=false)
	public String typeName;
	@Column(nullable=false)
	public String acronym;

	@JsonBackReference
	@OneToMany(mappedBy="materialType")
	public List<MaterialCode> codeInType = new ArrayList<MaterialCode>();

	@SuppressWarnings("unchecked")
	public static Finder<String,MaterialType> find = new Finder(String.class,MaterialType.class);
}
