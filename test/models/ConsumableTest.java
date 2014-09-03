import org.junit.*;
import java.util.Date;
import java.util.Calendar;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

import models.MaterialCode;
import models.MaterialType;
import models.consumable.Consumable;

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
}