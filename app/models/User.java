package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.UserStatus;

@Entity
public class User extends Model{

	@Id
	public String username;
	@Column(nullable=false)
	public String password;
	public UserStatus status;

	public static User authenticate(String username, String password) {

	/*
        System.out.println("จำนวน ผู้ใช้ :" + User.find.findRowCount());
		System.out.println("จำนวน รหัสวัสดุ :" + ConsumableCode.find.findRowCount());
		for(ConsumableCode code : ConsumableCode.find.all()){
			System.out.println( code.id + "\t" + code.number + "\t" + code.code + " \t" + code.consumableType.acronym + " \t" + code.description );
		}
        System.out.println("\nจำนวน ประเภทวัสดุ :" + ConsumableType.find.findRowCount());
        for(ConsumableType type : ConsumableType.find.all()){
			System.out.println( type.id + "\t" + type.acronym + "\t" + type.typeName );
		}
	*/

        User user = find.byId(username);
		if(user == null){
            return null;
        }
        else if(!username.equals(user.username) || !password.equals(user.password)){
            return null;
        }

        return user;
	}

	@SuppressWarnings("unchecked")
	public static Finder<String,User> find = new Finder(String.class,User.class);
}
