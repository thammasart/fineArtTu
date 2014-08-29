package models.durableArticles;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "external_transfer_detail")
public class ExternalTransferDetail extends Model{

	@Id
	public long id;
	public String code; // รหัส
	public String annotation; //หมายเหตุ คำอธิบายประกอบ

	@ManyToOne
	public DurableArticles durableArticles; // ครุภัณฑ์ที่ต้องการโอนย่าย

	@ManyToOne
	public ExternalTransfer exTransfer; // การโอนย้าย ภายนอก

	@SuppressWarnings("unchecked")
	public static Finder<Long,ExternalTransferDetail> find = new Finder(Long.class,ExternalTransferDetail.class);
}