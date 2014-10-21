package models;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class UserStatus extends Model{

	@Id
	public String name;
	public boolean module1;
	public boolean module2;
	public boolean module3;
	public boolean module4;
	public boolean module5;
	public boolean module6;

	@JsonBackReference
	@OneToMany(mappedBy="status")
	public List<User> numberOfUser = new ArrayList<User>();

	@SuppressWarnings("unchecked")
	public static Finder<String,UserStatus> find = new Finder(String.class,UserStatus.class);
}
