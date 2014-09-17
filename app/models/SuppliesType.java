package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class SuppliesType extends Model{

	@Id
	public long id;
	public String name;
	
	@SuppressWarnings("unchecked")
	public static Finder<String,SuppliesType> find = new Finder(String.class,SuppliesType.class);
}
