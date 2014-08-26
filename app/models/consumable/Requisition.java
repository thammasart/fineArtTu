package models.consumable;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "consumable_requisition")
public class Requisition extends Model{

	@Id
	public long id;

	@SuppressWarnings("unchecked")
	public static Finder<Long,Requisition> find = new Finder(Long.class,Requisition.class);
}