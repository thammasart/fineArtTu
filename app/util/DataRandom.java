package util;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;


import models.Address;
import models.Company;
import models.MaterialCode;
import models.MaterialType;
import models.durableArticles.DurableArticles;
import models.durableGoods.DurableGoods;
import models.durableGoods.ProcurementDetail;
import models.fsnNumber.FSN_Class;
import models.fsnNumber.FSN_Description;
import models.fsnNumber.FSN_Type;
import models.type.ImportStatus;
import models.type.SuppliesStatus;

public class DataRandom {
	final static String[] budgetType = {"งบพิเศษ","งบคลัง","งบกองทุนค่าธรรมเนียม","อื่นๆ"};
	final static ImportStatus[] importStatus = {ImportStatus.CANCEL,ImportStatus.INIT,ImportStatus.SUCCESS,ImportStatus.DELETE};
	final static String[] classifier = {"อัน","ชิ้น","ตัว","ตู้","หลัง"};
	final static String[] department = {"การละคอน","พัสตราภรณ์","อุตสาหกรรม","พัสดุ","เลขานุการ"};
	final static String[] typeEntrepreneur = {"นิติบุคคล","บุคคลธรรมดา"};
	final static String[] typeDealer1 = {"ห้างหุ้นส่วนสามัญนิติบุคคล","ห้างห้นส่วนจำกัด","บริษัทมหาชนจำกัด","กิจการร่วมค้า","มูลนิธิ","สหกรณ์","องค์กรธุรกิจจัดตั้งหรือจดทะเบียนภายใต้กฎหมายเฉพาะ","กิจการค้าร่วม","หน่วยงานภาครัฐที่เบิกเงินผ่านระบบ"};
	final static String[] typeDealer2 = {"คณะบุคคล","กิจการร้านค้าเจ้าของคนเดียวหรือบุคคลธรรมดา","ห้างหุ้นส่วนสามัญ"};
	private static Random rand = new Random();
	private static int contractNo1 = 1;
	private static int contractNo2 = 1;
	
	public static String toStandardYear(String date){
		String trueDate = "";
		String[] d = date.split("/");
		int year = Integer.parseInt(d[d.length-1]) - 543;
		d[d.length-1] = year + "";
		for(int i=0; i<d.length; i++){
			trueDate += d[i];
			if(i<d.length-1) trueDate += "/";
		}
		return trueDate;
	}
	
	public static String getNextFsnDescriptionDetail(String typeId){
		FSN_Type type = FSN_Type.find.byId(typeId);
		int i=FSN_Description.find.where().eq("typ", type).findRowCount() + 1;
		return String.format("-%04d", i);
	}
	
	public static FSN_Type getRandomFsnType(){
		FSN_Class groupClass = FSN_Class.find.all().get(rand.nextInt(FSN_Class.find.findRowCount()));
		FSN_Type fsnType = new FSN_Type();
		int i = FSN_Type.find.where().eq("groupClass", groupClass).findRowCount() + 1;
		fsnType.typeId = groupClass.classId + String.format("-%03d", i);
		fsnType.typeDescription = randomString();
		fsnType.groupClass = groupClass;
		return fsnType;
	}
	
	public static FSN_Description getRandomFsnDescription(){
		FSN_Type type = FSN_Type.find.all().get(rand.nextInt(FSN_Type.find.findRowCount()));
		FSN_Description fsnDescription = new FSN_Description();
		int i = FSN_Description.find.where().eq("typ", type).findRowCount() + 1;
		fsnDescription.descriptionId = type.typeId + String.format("-%04d", i);
		fsnDescription.descriptionDescription = randomString();
		fsnDescription.classifier = classifier[rand.nextInt(classifier.length)];
		fsnDescription.typ = type;
		return fsnDescription;
	}
	
	public static MaterialCode getRandomDurableMaterialCode(){
		MaterialCode m = new MaterialCode();
		while(true){
			m.code = "10" + String.format("%03d", rand.nextInt(100));
			if(MaterialCode.find.byId(m.code) == null){
				break;
			}
		}
		m.description = randomString(10);
		m.classifier = classifier[rand.nextInt(classifier.length)];
		m.minNumberToAlert = rand.nextInt(10);
		m.materialType = MaterialType.find.all().get(rand.nextInt(MaterialType.find.findRowCount()));
		return m;
	}
	
	public static Address getRandomAddress(){
		Address a = new Address();
		a.buildingNo = randomString(12);
		a.village = randomString(12);
		a.alley = randomString(12);
		a.road = randomString(12);
		a.parish = randomString(12);
		a.district = randomString(12);
		a.province = randomString(12);
		a.telephoneNumber = randomString(12);
		a.fax = randomString(12);
		a.postCode = randomString(12);
		a.email = randomString(12);
		return a;
	}

	public static Company getRandomCompany(){
		Company c = new Company();
		int typeEn = rand.nextInt(typeEntrepreneur.length);
		c.typeEntrepreneur = typeEntrepreneur[typeEn];
		if(typeEn == 0){
			c.typedealer = typeDealer1[rand.nextInt(typeDealer1.length)];
		}else if(typeEn == 1){
			c.typedealer = typeDealer2[rand.nextInt(typeDealer2.length)];
		}
		c.nameEntrepreneur = randomString(10);
		c.nameDealer = randomString(10);
		c.payCodition = randomString(12);
		c.payPeriod = rand.nextInt(20);
		c.sendPeriod = rand.nextInt(100);
		c.durableArticlesType = randomString(32);
		c.durableGoodsType = randomString(32);
		c.consumableGoodsType = randomString(32);
		c.otherDetail = "";
		c.address = getRandomAddress();
		c.address.save();
		return c;
	}
	
	public static models.durableArticles.Procurement getRandomArticleProcurement(){
		models.durableArticles.Procurement p = new models.durableArticles.Procurement();
		p.title = randomString(15); 				
		p.contractNo = contractNo1++ + "/57"; 			
		p.addDate = new Date(); 	
		p.checkDate = new Date(); 
		p.budgetType = budgetType[rand.nextInt(budgetType.length)];
		p.institute = randomString();
		p.budgetYear = 2557;
		p.status = importStatus[rand.nextInt(importStatus.length)];
		p.company = getRandomCompany();
		p.company.save();
		return p;
	}
	
	public static models.durableArticles.ProcurementDetail getRandomArticleProcurementDetail(models.durableArticles.Procurement p){
		models.durableArticles.ProcurementDetail pd = new models.durableArticles.ProcurementDetail();
		pd.description = randomString();
		pd.quantity = rand.nextInt(100);
		pd.price = Math.abs(rand.nextDouble() * 1000);
		pd.priceNoVat = Math.abs(rand.nextDouble()- (pd.price*0.07));
		pd.alertTime = Math.abs(rand.nextDouble()*10);
		pd.llifeTime = Math.abs(rand.nextDouble()*10);
		pd.seller = randomString();
		pd.phone = String.format("%010d",rand.nextLong());
		pd.brand = randomString();
		pd.serialNumber = randomString(10);
		pd.fsn = FSN_Description.find.all().get(rand.nextInt(FSN_Description.find.findRowCount()));
		pd.procurement = p;
		return pd;
	}
	
	public static models.durableArticles.DurableArticles getRandomDurableArticles(models.durableArticles.ProcurementDetail pd){
		DurableArticles d = new DurableArticles();
		d.department = department[rand.nextInt(department.length)];
		d.room = randomString(4);
		d.floorLevel = randomString(2);
		int i = DurableArticles.find.where().eq("detail.fsn", pd.fsn).findRowCount() + 1;
		d.code = pd.fsn.descriptionId + String.format("-%03d", i);
		d.codeFromStock = String.format("%05d",rand.nextInt(99999));
		d.title = randomString();
		d.firstName = randomString();
		d.lastName = randomString();
		d.status = SuppliesStatus.NORMAL;
		d.detail = pd;
		
		return d;
	}
	
	public static models.durableGoods.Procurement getRandomGoodsProcurement(){
		models.durableGoods.Procurement p = new models.durableGoods.Procurement();
		p.title = randomString(20);
		p.contractNo = contractNo2++ + "/57";
		p.budgetType = budgetType[rand.nextInt(budgetType.length)];
		p.budgetYear = 2557;
		p.status = importStatus[rand.nextInt(importStatus.length)];
		p.company = getRandomCompany();
		p.company.save();
		return p;
	}
	
	public static models.durableGoods.ProcurementDetail getRandomGoodsProcurementDetail(models.durableGoods.Procurement p){
		models.durableGoods.ProcurementDetail pd = new ProcurementDetail();
		pd.description = randomString(20);
		pd.price = Math.abs(rand.nextDouble() * 1000);
		pd.priceNoVat = Math.abs(rand.nextDouble()- (pd.price*0.07));
		pd.quantity = rand.nextInt(100);
		pd.seller = randomString(12);
		pd.phone = randomString(10);
		pd.brand = randomString(5);
		pd.serialNumber = randomString(12);
		pd.code = MaterialCode.find.all().get(rand.nextInt(MaterialCode.find.findRowCount())).code;
		pd.procurement = p; 
		return pd;
	}
	
	public static models.durableGoods.DurableGoods getRandomDurableGoods(models.durableGoods.ProcurementDetail pd){
		DurableGoods d = new DurableGoods();
		d.department = department[rand.nextInt(department.length)];
		d.room = randomString(4);
		d.floorLevel = randomString(2);
		d.code = pd.code;
		d.codes = pd.code;
		d.title = randomString();
		d.firstName = randomString();
		d.lastName = randomString();
		d.remain = rand.nextInt(100);
		d.detail = pd;
		return d;
	}
	
	public static String randomString(){
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
	
	public static String randomString(int n){
		SecureRandom random = new SecureRandom();
		String s = new BigInteger(130, random).toString(32);
		s = s.length() > n ? s.substring(0, n) : s;
		return s;
	}
	
}
