import org.junit.*;
import java.util.Date;
import java.util.Calendar;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

import models.MaterialCode;
import models.MaterialType;

import java.util.List;
import java.util.ArrayList;

public class MaterialCodeTest extends WithApplication {
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@Test
	public void createMaterialTypeNotNull() {
		assertNotNull(new MaterialType());
	}

	@Test
	public void createMaterialTypeWithParameter() {
		MaterialType type = new MaterialType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";

		assertEquals("วัสดุสำนักงาน", type.typeName);
		assertEquals("ว.สนง.", type.acronym);
	}

	@Test
	public void createMaterialTypeAndSaveComplete() {
		MaterialType type = new MaterialType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";
		type.save();

		assertEquals(1, MaterialType.find.findRowCount());

		MaterialType type2 = new MaterialType();
		type2.typeName = "วัสดุไฟฟ้าและวิทยุ";
		type2.acronym = "ว.ฟฟ.";
		type2.save();

		assertEquals(2, MaterialType.find.findRowCount());
		assertEquals(type, MaterialType.find.where().eq("acronym","ว.สนง.").findUnique());
		assertEquals(type2, MaterialType.find.where().eq("acronym","ว.ฟฟ.").findUnique());
	}

	@Test
	public void createMaterialCodeNotNull() {
		assertNotNull(new MaterialCode());
	}

	@Test
	public void createMaterialCodeWithParameter() {
		MaterialType type = new MaterialType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";

		MaterialCode code = new MaterialCode();
		code.code = "1001";
		code.description = "กรรไกร 6 นิ้ว";
		code.materialType = type;

		assertEquals("1001", code.code);
		assertEquals("กรรไกร 6 นิ้ว", code.description);
		assertEquals(type, code.materialType);
	}

	@Test
	public void createMaterialCodeAndSaveComplete() {
		MaterialType type = new MaterialType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";
	 	type.save();

		MaterialCode code = new MaterialCode();
		code.code = "1001";
		code.description = "กรรไกร 6 นิ้ว";
		code.materialType = MaterialType.find.where().eq("acronym","ว.สนง.").findUnique();
		code.save();

		assertEquals(1, MaterialCode.find.findRowCount());

		MaterialCode code2 = new MaterialCode();
		code2.code = "1002";
		code2.description = "กระดาษกาว 2 หน้า-บาง- 1 นิ้วครึ่ง";
		code2.materialType = MaterialType.find.where().eq("acronym","ว.สนง.").findUnique();
		code2.save();

		assertEquals(2, MaterialCode.find.findRowCount());
		assertEquals(code, MaterialCode.find.byId("1001"));
		assertEquals(code2, MaterialCode.find.byId("1002"));
	}

	@Test
	public void saveFSNClassConnextToFSNGroup(){

		MaterialType type = new MaterialType();
		type.typeName = "วัสดุสำนักงาน";
		type.acronym = "ว.สนง.";
	 	type.save();

		MaterialCode code = new MaterialCode();
		code.code = "1001";
		code.description = "กรรไกร 6 นิ้ว";
		code.materialType = MaterialType.find.where().eq("acronym","ว.สนง.").findUnique();
		code.save();

		assertEquals(1, MaterialType.find.where().eq("acronym","ว.สนง.").findUnique().codeInType.size());
		assertEquals(code, MaterialType.find.where().eq("acronym","ว.สนง.").findUnique().codeInType.get(0));

		MaterialCode code2 = new MaterialCode();
		code2.code = "1002";
		code2.description = "กระดาษกาว 2 หน้า-บาง- 1 นิ้วครึ่ง";
		code2.materialType = MaterialType.find.where().eq("acronym","ว.สนง.").findUnique();
		code2.save();

		assertEquals(2, MaterialType.find.where().eq("acronym","ว.สนง.").findUnique().codeInType.size());
		assertEquals(code2, MaterialType.find.where().eq("acronym","ว.สนง.").findUnique().codeInType.get(1));

	}
}