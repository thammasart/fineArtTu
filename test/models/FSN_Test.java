import org.junit.*;
import java.util.Date;
import java.util.Calendar;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

import models.fsnNumber.*;
import models.*;

import java.util.List;
import java.util.ArrayList;

public class FSN_Test extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@Test
	public void createFSNGroupNotNull() {
		assertNotNull(new FSN_Group());
	}

	@Test
	public void createFSNGroupWithParameter() {
		FSN_Group group = new FSN_Group();
		group.id = "10";
		group.description = "อาวุธ";

		assertEquals("10", group.id);
		assertEquals("อาวุธ", group.description);
	}

	@Test
	public void createFSNGroupAndSaveComplete() {
		FSN_Group group = new FSN_Group();
		group.id = "10";
		group.description = "อาวุธ";
		group.save();

		assertEquals(1, FSN_Group.find.findRowCount());
		assertEquals(group, FSN_Group.find.byId("10"));

		FSN_Group group2 = new FSN_Group();
		group2.id = "75";
		group2.description = "พัสดุและเครื่องใช้สำนักงาน";
		group2.save(); 

		assertEquals(2, FSN_Group.find.findRowCount());
		assertEquals(group, FSN_Group.find.byId("10"));
		assertEquals(group2, FSN_Group.find.byId("75"));
	}

	@Test
	public void createFSNClassNotNull() {
		assertNotNull(new FSN_Group());
	}

	@Test
	public void createFSNClassWithParameter() {
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = group;

		assertEquals("7510", groupClass.id);
		assertEquals("พัสดุสำนักงาน", groupClass.description);
		assertEquals(group, groupClass.group);
	}

	@Test
	public void createFSNClassAndSaveComplete() {
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";
		group.save();

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = FSN_Group.find.byId("75");
		groupClass.save();

		assertEquals(1, FSN_Class.find.findRowCount());

		FSN_Class groupClass2 = new FSN_Class();
		groupClass2.id = "7520";
		groupClass2.description = "เครื่องใช้และเครื่องประกอบสำนักงาน";
		groupClass2.group = FSN_Group.find.byId("75");
		groupClass2.save();

		assertEquals(2, FSN_Class.find.findRowCount());
		assertEquals(groupClass, FSN_Class.find.byId("7510"));
		assertEquals(groupClass2, FSN_Class.find.byId("7520"));
	}

	@Test
	public void saveFSNClassConnextToFSNGroup(){
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";
		group.save();

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = FSN_Group.find.byId("75");
		groupClass.save();

		assertEquals(1, FSN_Group.find.byId("75").classInGroup.size());
		assertEquals(groupClass, FSN_Group.find.byId("75").classInGroup.get(0));

		FSN_Class groupClass2 = new FSN_Class();
		groupClass2.id = "7520";
		groupClass2.description = "เครื่องใช้และเครื่องประกอบสำนักงาน";
		groupClass2.group = FSN_Group.find.byId("75");
		groupClass2.save();

		assertEquals(2, FSN_Group.find.byId("75").classInGroup.size());
		assertEquals(groupClass2, FSN_Group.find.byId("75").classInGroup.get(1));
	}

	@Test
	public void createFSNTypeNotNull() {
		assertNotNull(new FSN_Type());
	}

	@Test
	public void createFSNTypeWithParameter() {
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = group;

		FSN_Type type = new FSN_Type();
		type.id = "7510001";
		type.description = "แฟ้มหรือปกเก็บกระดาษ";
		type.groupClass = groupClass;

		assertEquals("7510001", type.id);
		assertEquals("แฟ้มหรือปกเก็บกระดาษ", type.description);
		assertEquals(groupClass, type.groupClass);
	}

	@Test
	public void createFSNTypeAndSaveComplete() {
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";
		group.save();

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = FSN_Group.find.byId("75");
		groupClass.save();

		FSN_Type type = new FSN_Type();
		type.id = "7510001";
		type.description = "แฟ้มหรือปกเก็บกระดาษ";
		type.groupClass = FSN_Class.find.byId("7510");
		type.save();

		assertEquals(1, FSN_Type.find.findRowCount());

		FSN_Type type2 = new FSN_Type();
		type2.id = "7510002";
		type2.description = "ปากกา";
		type2.groupClass = FSN_Class.find.byId("7510");
		type2.save();

		assertEquals(2, FSN_Type.find.findRowCount());
		assertEquals(type, FSN_Type.find.byId("7510001"));
		assertEquals(type2, FSN_Type.find.byId("7510002"));
	}

	public void saveFSNTypeConnextToFSNGroup(){
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";
		group.save();

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = FSN_Group.find.byId("75");
		groupClass.save();

		FSN_Type type = new FSN_Type();
		type.id = "7510001";
		type.description = "แฟ้มหรือปกเก็บกระดาษ";
		type.groupClass = FSN_Class.find.byId("7510");
		type.save();

		assertEquals(1, FSN_Class.find.byId("7510").typeInClass.size());
		assertEquals(type, FSN_Class.find.byId("7510").typeInClass.get(0));

		FSN_Type type2 = new FSN_Type();
		type2.id = "7510002";
		type2.description = "ปากกา";
		type2.groupClass = FSN_Class.find.byId("7510");
		type2.save();

		assertEquals(2, FSN_Class.find.byId("7510").typeInClass.size());
		assertEquals(type2, FSN_Class.find.byId("7510").typeInClass.get(1));
	}

	@Test
	public void createFSNDescriptionNotNull() {
		assertNotNull(new FSN_Description());
	}

	@Test
	public void createFSNDescriptionWithParameter() {
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = group;

		FSN_Type type = new FSN_Type();
		type.id = "7510002";
		type.description = "ปากกา";
		type.groupClass = groupClass;

		FSN_Description des = new FSN_Description();
		des.id = "75100020001";
		des.description = "ปากกาแดง";
		des.type = type;

		assertEquals("75100020001", des.id);
		assertEquals("ปากกาแดง", des.description);
		assertEquals(type, des.type);
	}

	@Test
	public void createFSNDescriptionAndSaveComplete() {
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";
		group.save();

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = FSN_Group.find.byId("75");
		groupClass.save();

		FSN_Type type = new FSN_Type();
		type.id = "7510002";
		type.description = "ปากกา";
		type.groupClass = FSN_Class.find.byId("7510");
		type.save();

		FSN_Description des = new FSN_Description();
		des.id = "75100020001";
		des.description = "ปากกาแดง";
		des.type = FSN_Type.find.byId("7510002");
		des.save();

		assertEquals(1, FSN_Description.find.findRowCount());

		FSN_Description des2 = new FSN_Description();
		des2.id = "75100020002";
		des2.description = "ปากกาน้ำเงิน";
		des2.type = FSN_Type.find.byId("7510002");
		des2.save();

		assertEquals(2, FSN_Description.find.findRowCount());
		assertEquals(des, FSN_Description.find.byId("75100020001"));
		assertEquals(des2, FSN_Description.find.byId("75100020002"));
	}

	public void saveFSNDescriptionConnextToFSNGroup(){
		FSN_Group group = new FSN_Group();
		group.id = "75";
		group.description = "พัสดุและเครื่องใช้สำนักงาน";
		group.save();

		FSN_Class groupClass = new FSN_Class();
		groupClass.id = "7510";
		groupClass.description = "พัสดุสำนักงาน";
		groupClass.group = FSN_Group.find.byId("75");
		groupClass.save();

		FSN_Type type2 = new FSN_Type();
		type2.id = "7510002";
		type2.description = "ปากกา";
		type2.groupClass = FSN_Class.find.byId("7510");
		type2.save();

		FSN_Description des = new FSN_Description();
		des.id = "75100020001";
		des.description = "ปากกาแดง";
		des.type = FSN_Type.find.byId("7510002");
		des.save();

		assertEquals(1, FSN_Type.find.byId("7510002").desInType.size());
		assertEquals(des, FSN_Type.find.byId("7510002").desInType.get(0));

		FSN_Description des2 = new FSN_Description();
		des2.id = "75100020002";
		des2.description = "ปากกาน้ำเงิน";
		des2.type = FSN_Type.find.byId("7510002");
		des2.save();

		assertEquals(2, FSN_Type.find.byId("7510002").desInType.size());
		assertEquals(des2, FSN_Type.find.byId("7510002").desInType.get(2));
	}
}