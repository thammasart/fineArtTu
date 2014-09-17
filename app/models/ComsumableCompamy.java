package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class ComsumableCompamy extends Model{

	@Id
	public long id;

	@ManyToOne
	public SuppliesType type;
	@ManyToOne
	public Company company;
	
	@SuppressWarnings("unchecked")
	public static Finder<String,ComsumableCompamy> find = new Finder(String.class,ComsumableCompamy.class);
}