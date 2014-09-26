package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;

@Entity
@Table (name = "committee")
public class Committee extends Model{

	@Id
	public String identificationNo; // เลขประจำตัวประชาชน
	public String title; // คำหนำหน้าชื่อ
	public String firstName; // ชื่อ
	public String lastName; // นามสกุล
	public String position; // ตำแหน่ง

	@SuppressWarnings("unchecked")
	public static Finder<String,Committee> find = new Finder(String.class,Committee.class);
}