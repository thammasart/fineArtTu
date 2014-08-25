package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;

@Entity
public class Committee extends Model{

	@Id
	public long id;

	public CommitteeType type;

	@OneToMany
	public Procurement procurement;
}