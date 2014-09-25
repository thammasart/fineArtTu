package models;

import org.junit.*;
import java.util.Date;
import java.util.Calendar;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

import models.MaterialCode;
import models.MaterialType;
import models.consumable.*;

import java.util.List;
import java.util.ArrayList;

public class ConsumableTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));

		MaterialType type = new MaterialType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";
	 	type.save();

		MaterialCode code = new MaterialCode();
		code.code = "1001";
		code.description = "กรรไกร 6 นิ้ว";
		code.materialType = MaterialType.find.where().eq("acronym","ว.สนง.").findUnique();
		code.save();

		MaterialCode code2 = new MaterialCode();
		code2.code = "1002";
		code2.description = "กระดาษกาว 2 หน้า-บาง- 1 นิ้วครึ่ง";
		code2.materialType = MaterialType.find.where().eq("acronym","ว.สนง.").findUnique();
		code2.save();
	}

	@Test
	public void createConsumableNotNull() {
		assertNotNull(new Consumable());
	}

	@Test
	public void createConsumableWithParameter() {
		Consumable consumable = new Consumable();
		consumable.code = MaterialCode.find.byId("1001");
		consumable.classifier = "เล่ม";
		consumable.brand = "";
		consumable.details = "กรรไกร 6 นิ้ว";

		assertEquals(MaterialCode.find.byId("1001"), consumable.code);
		assertEquals("เล่ม", consumable.classifier);
		assertEquals("", consumable.brand);
		assertEquals("กรรไกร 6 นิ้ว", consumable.details);
	}

	@Test
	public void createConsumableAndSaveComplete() {
		Consumable consumable = new Consumable();
		consumable.code = MaterialCode.find.byId("1001");
		consumable.classifier = "เล่ม";
		consumable.brand = "";
		consumable.details = "กรรไกร 6 นิ้ว";
		consumable.save();

		assertEquals(consumable, Consumable.find.byId(1L));
	}

	@Test
	public void createProcurementNotNull() {
		assertNotNull(new Procurement());
	}

	@Test
	public void createProcurementWithParameter() {
		Procurement procurement = new Procurement();
		procurement.title = "จัดซื้อเครื่องปรับอากาศ";
		procurement.contractNo = "1/57";
		procurement.dateOfApproval = new Date();
		procurement.addDate  = new Date();
		procurement.checkDate = new Date();
		procurement.budgetType = "พิเศษ";
		procurement.budgetYear = 2557;
		procurement.dealer = "test" ;
		procurement.telephoneNumber = "0999999999";

		assertEquals("จัดซื้อเครื่องปรับอากาศ", procurement.title);
		assertEquals("1/57", procurement.contractNo);
		assertEquals(new Date(), procurement.dateOfApproval);
		assertEquals(new Date(), procurement.addDate);
		assertEquals(new Date(), procurement.checkDate);
	    assertEquals("พิเศษ",procurement.budgetType);
		assertEquals(2557,procurement.budgetYear);
		assertEquals("test",procurement.dealer);
		assertEquals("0999999999",procurement.telephoneNumber);
	}

	@Test
	public void createProcurementAndSaveComplete() {
		Procurement procurement = new Procurement();
		procurement.title = "จัดซื้อเครื่องปรับอากาศ"; // ชื่อ เรื่อง
		procurement.contractNo = "1/57"; // สัญญาเลขที่
		procurement.dateOfApproval = new Date(); // วันที่อนุมัติในสัญญา
		procurement.addDate  = new Date(); // วันที่นำเข้า
		procurement.checkDate = new Date(); // วันทีตรวจสอบ
		procurement.budgetType = "พิเศษ"; // ประเภทงบประมาณ
		procurement.budgetYear = 2557; // ปีงบประมาณ
		procurement.dealer = "test" ;
		procurement.telephoneNumber = "0999999999";
		procurement.save();

		assertEquals(procurement, Procurement.find.byId(1L));
	}

}