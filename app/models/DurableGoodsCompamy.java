package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class DurableGoodsCompamy extends Model{

	@Id
	public long id;

	@ManyToOne
	public SuppliesType type;
	@ManyToOne
	public Company company;
	
	@SuppressWarnings("unchecked")
	public static Finder<String,DurableGoodsCompamy> find = new Finder(String.class,DurableGoodsCompamy.class);
}