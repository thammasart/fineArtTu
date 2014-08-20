package models;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class Procurement extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,Procurement> find = new Finder(Long.class,Procurement.class);
}