package models;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class ConsumableType extends Model{

	@Id
	public long id;
	@Column(nullable=false)
	public String typeName;
	@Column(nullable=false)
	public String acronym;

	@OneToMany(mappedBy="consumableType")
	public List<ConsumableCode> codeInType = new ArrayList<ConsumableCode>();

	@SuppressWarnings("unchecked")
	public static Finder<Long,ConsumableType> find = new Finder(Long.class,ConsumableType.class);
}