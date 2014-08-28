package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import models.Company;

@Entity
@Table (name = "durable_articles_repairing")
public class Repairing extends Model{

	@Id
	public long id;
	

	@SuppressWarnings("unchecked")
	public static Finder<Long,Repairing> find = new Finder(Long.class,Repairing.class);
}