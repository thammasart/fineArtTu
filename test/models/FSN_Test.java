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
	/*	group =  FSN_Group.find.byId("75");
		List<FSN_Class> l = group.groupClassList;
		int size = l.size();
//		assertEquals(1, FSN_Group.find.byId("75").groupClassList.size());*/


		FSN_Class groupClass2 = new FSN_Class();
		groupClass2.id = "7520";
		groupClass2.description = "เครื่องใช้และเครื่องประกอบสำนักงาน";
		groupClass2.group = FSN_Group.find.byId("75");
		groupClass2.save();

		assertEquals(2, FSN_Class.find.findRowCount());
		assertEquals(groupClass, FSN_Class.find.byId("7510"));
		assertEquals(groupClass2, FSN_Class.find.byId("7520"));
	}
}