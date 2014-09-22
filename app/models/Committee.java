package models;

import play.db.ebean.*;
import javax.persistence.*;

import models.type.CommitteeType;

@Entity
@Table (name = "consumable_committee")
public class Committee extends Model{

	@Id
	public long id;
	public String identificationNo; // เลขประจำตัวประชาชน
	public String title; // คำหนำหน้าชื่อ
	public String firstName; // ชื่อ
	public String lastName; // นามสกุล
	public String position; // ตำแหน่ง

	@SuppressWarnings("unchecked")
	public static Finder<Long,Committee> find = new Finder(Long.class,Committee.class);
}