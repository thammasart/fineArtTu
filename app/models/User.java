package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.fsnNumber.*;

@Entity
public class User extends Model{

	@Id
	public String username;
	@Column(nullable=false)
	public String password;

	public String departure;
	public String position;
	public String firstName;
	public String lastName;

	@ManyToOne
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
		System.out.println("\nจำนวน ผู้ใช้ :" + User.find.findRowCount());
		System.out.println("จำนวน รหัสวัสดุ :" + MaterialCode.find.findRowCount());
		System.out.println("จำนวน fsn group :" + FSN_Group.find.findRowCount());
		System.out.println("จำนวน fsn class :" + FSN_Class.find.findRowCount());
		System.out.println("จำนวน fsn type :" + FSN_Type.find.findRowCount());
		System.out.println("จำนวน fsn Description :" + FSN_Description.find.findRowCount());
		System.out.println("จำนวน article procurement :" + models.durableArticles.Procurement.find.findRowCount());
		System.out.println("จำนวน article procurement detail :" + models.durableArticles.ProcurementDetail.find.findRowCount());
		System.out.println("จำนวน goods procurement :" + models.durableGoods.Procurement.find.findRowCount());
		System.out.println("จำนวน goods procurement detail :" + models.durableGoods.ProcurementDetail.find.findRowCount());
		System.out.println("จำนวน durable article :" + models.durableArticles.DurableArticles.find.findRowCount());
		System.out.println("จำนวน durable article :" + models.durableGoods.DurableGoods.find.findRowCount());

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
