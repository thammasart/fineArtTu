import org.junit.*;
import java.util.Date;
import java.util.Calendar;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

import models.ConsumableCode;
import models.ConsumableType;

import java.util.List;
import java.util.ArrayList;

public class ConsumableCodeTest extends WithApplication {
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@Test
	public void createConsumableTypeNotNull() {
		assertNotNull(new ConsumableType());
	}

	@Test
	public void createConsumableTypeWithParameter() {
		ConsumableType type = new ConsumableType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";

		assertEquals("วัสดุสำนักงาน", type.typeName);
		assertEquals("ว.สนง.", type.acronym);
	}

	@Test
	public void createConsumableTypeAndSaveComplete() {
		ConsumableType type = new ConsumableType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";
		type.save();

		assertEquals(1, ConsumableType.find.findRowCount());

		ConsumableType type2 = new ConsumableType();
		type2.typeName = "วัสดุไฟฟ้าและวิทยุ";
		type2.acronym = "ว.ฟฟ.";
		type2.save();

		assertEquals(2, ConsumableType.find.findRowCount());
		assertEquals(type, ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique());
		assertEquals(type2, ConsumableType.find.where().eq("acronym","ว.ฟฟ.").findUnique());
	}

	@Test
	public void createConsumableCodeNotNull() {
		assertNotNull(new ConsumableCode());
	}

	@Test
	public void createConsumableCodeWithParameter() {
		ConsumableType type = new ConsumableType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";

		ConsumableCode code = new ConsumableCode();
		code.code = "1001";
		code.description = "กรรไกร 6 นิ้ว";
		code.consumableType = type;

		assertEquals("1001", code.code);
		assertEquals("กรรไกร 6 นิ้ว", code.description);
		assertEquals(type, code.consumableType);
	}

	@Test
	public void createConsumableCodeAndSaveComplete() {
		ConsumableType type = new ConsumableType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";
	 	type.save();

		ConsumableCode code = new ConsumableCode();
		code.code = "1001";
		code.description = "กรรไกร 6 นิ้ว";
		code.consumableType = ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique();
		code.save();

		assertEquals(1, ConsumableCode.find.findRowCount());

		ConsumableCode code2 = new ConsumableCode();
		code2.code = "1002";
		code2.description = "กระดาษกาว 2 หน้า-บาง- 1 นิ้วครึ่ง";
		code2.consumableType = ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique();
		code2.save();

		assertEquals(2, ConsumableCode.find.findRowCount());
		assertEquals(code, ConsumableCode.find.byId("1001"));
		assertEquals(code2, ConsumableCode.find.byId("1002"));
	}

	@Test
	public void saveFSNClassConnextToFSNGroup(){

		ConsumableType type = new ConsumableType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";
	 	type.save();

		ConsumableCode code = new ConsumableCode();
		code.code = "1001";
		code.description = "กรรไกร 6 นิ้ว";
		code.consumableType = ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique();
		code.save();

		assertEquals(1, ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique().codeInType.size());
		assertEquals(code, ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique().codeInType.get(0));

		ConsumableCode code2 = new ConsumableCode();
		code2.code = "1002";
		code2.description = "กระดาษกาว 2 หน้า-บาง- 1 นิ้วครึ่ง";
		code2.consumableType = ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique();
		code2.save();

		assertEquals(2, ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique().codeInType.size());
		assertEquals(code2, ConsumableType.find.where().eq("acronym","ว.สนง.").findUnique().codeInType.get(1));

	}
}