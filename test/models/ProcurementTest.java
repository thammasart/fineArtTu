package models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.durableArticles.AI_Committee;
import models.durableArticles.EO_Committee;
import models.durableArticles.Procurement;
import models.durableArticles.ProcurementDetail;

import org.junit.*;
import static org.junit.Assert.*;

public class ProcurementTest {
	models.durableArticles.Procurement pa;
	models.durableGoods.Procurement pg;
	AI_Committee ai1;
	AI_Committee ai2;
	EO_Committee eo1;
	EO_Committee eo2;
	models.durableArticles.ProcurementDetail pad;
	models.durableGoods.ProcurementDetail pgd;
	@Before
	public void setUp(){
		// Article
		pa = new Procurement();
		pa.title = "1";
		pa.addDate = new Date();
		pa.checkDate = new Date();
		pa.budgetYear = 57;
		pa.contractNo = "1/57";
		
		Committee c1 = new Committee();
		c1.firstName = "firstname";
		c1.lastName = "lastname";
		c1.position = "none";
		c1.title = "none";
		
		Committee c2 = new Committee();
		c2.firstName = "firstname2";
		c2.lastName = "lastname2";
		c2.position = "none2";
		c2.title = "none2";
		
		Committee c3 = new Committee();
		c3.firstName = "firstname3";
		c3.lastName = "lastname3";
		c3.position = "none3";
		c3.title = "none3";
		
		Committee c4 = new Committee();
		c4.firstName = "firstname4";
		c4.lastName = "lastname4";
		c4.position = "none4";
		c4.title = "none4";
		
		ai1 = new AI_Committee();
		ai1.committee = c1;
		ai1.procurement = pa;
		
		ai2 = new AI_Committee();
		ai2.committee = c2;
		ai2.procurement = pa;
		
		List<AI_Committee> ais = new ArrayList<AI_Committee>();
		ais.add(ai1);
		ais.add(ai2);
		
		
		eo1 = new EO_Committee();
		eo1.committee = c3;
		eo1.procurement = pa;
		
		eo2 = new EO_Committee();
		eo2.committee = c4;
		eo2.procurement = pa;
		
		List<EO_Committee> eos = new ArrayList<EO_Committee>();
		eos.add(eo1);
		eos.add(eo2);
		
		pa.aiCommittee = ais;
		pa.eoCommittee = eos;
		
		// Goods
		pg = new models.durableGoods.Procurement();
		pg.title = "1";
		
		pad = new ProcurementDetail();
		pgd = new models.durableGoods.ProcurementDetail();
	}
	
	@Test
	public void testSetUp(){
		assertNotNull(pa);
		assertNotNull(pg);
	}
	
	@Test
	public void testCommittee(){
		assertNotNull(pa.aiCommittee);
		assertNotNull(pa.eoCommittee);
		assertEquals(2, pa.aiCommittee.size());
		assertEquals(2, pa.eoCommittee.size());
		assertEquals(ai1, pa.aiCommittee.get(0));
		assertEquals(ai2, pa.aiCommittee.get(1));
		assertEquals(eo1, pa.eoCommittee.get(0));
		assertEquals(eo2, pa.eoCommittee.get(1));
		assertNotEquals(ai2, pa.aiCommittee.get(0));
		assertNotEquals(ai1, pa.aiCommittee.get(1));
		assertNotEquals(eo2, pa.eoCommittee.get(0));
		assertNotEquals(eo1, pa.eoCommittee.get(1));
		assertEquals("firstname3", pa.eoCommittee.get(0).committee.firstName);
		assertEquals("lastname3", pa.eoCommittee.get(0).committee.lastName);
	}
	
	@Test
	public void testProcurementDetial(){
		assertNotNull(pad);
		assertNotNull(pgd);
	}
}
