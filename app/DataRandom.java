import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;

import models.durableArticles.DurableArticles;
import models.fsnNumber.FSN_Class;
import models.fsnNumber.FSN_Description;
import models.fsnNumber.FSN_Type;
import models.type.ImportStatus;

public class DataRandom {
	final static String[] budgetType = {"งบพิเศษ","งบคลัง","งบกองทุนค่าธรรมเนียม","อื่นๆ"};
	final static ImportStatus[] importStatus = {ImportStatus.CANCEL,ImportStatus.INIT,ImportStatus.SUCCESS,ImportStatus.DELETE};
	final static String[] classifier = {"อัน","ชิ้น","ตัว","ตู้","หลัง"};
	final static String[] department = {"การละคอน","พัสตราภรณ์","อุตสาหกรรม","พัสดุ","เลขานุการ"};
	private static Random rand = new Random();
	private static int contractNo = 1;
	
	public static String getNextFsnDescriptionDetail(String typeId){
		FSN_Type type = FSN_Type.find.byId(typeId);
		int i=1;
		while(true){
			if(FSN_Description.find.byId(type.typeId + String.format("-%04d", i)) == null){
				break;
			}
			i++;
		}
		return String.format("%04d", i);
	}
	
	public static FSN_Type getRandomFsnType(){
		FSN_Class groupClass = FSN_Class.find.all().get(rand.nextInt(FSN_Class.find.findRowCount()));
		FSN_Type fsnType = new FSN_Type();
		int i = 1;
		while(true){
			if(FSN_Type.find.byId(groupClass.classId + String.format("-%03d", i)) == null){
				fsnType.typeId = groupClass.classId + String.format("-%03d", i);
				break;
			}
			i++;
		}
		fsnType.typeDescription = randomString();
		fsnType.groupClass = groupClass;
		return fsnType;
	}
	
	public static FSN_Description getRandomFsnDescription(){
		FSN_Type type = FSN_Type.find.all().get(rand.nextInt(FSN_Type.find.findRowCount()));
		FSN_Description fsnDescription = new FSN_Description();
		int i = 1;
		while(true){
			if(FSN_Description.find.byId(type.typeId + String.format("-%04d", i)) == null){
				fsnDescription.descriptionId = type.typeId + String.format("-%03d", i);
				break;
			}
			i++;
		}
		fsnDescription.descriptionDescription = randomString();
		fsnDescription.classifier = classifier[rand.nextInt(classifier.length)];
		fsnDescription.typ = type;
		return fsnDescription;
	}
	
	public static models.durableArticles.Procurement getRandomProcurement(){
		models.durableArticles.Procurement p = new models.durableArticles.Procurement();
		p.title = randomString(15); 				
		p.contractNo = contractNo++ + "/57"; 			
		p.addDate = new Date(); 	
		p.checkDate = new Date(); 
		p.budgetType = budgetType[rand.nextInt(budgetType.length)];
		p.institute = randomString();
		p.budgetYear = 2557;
		p.status = importStatus[rand.nextInt(importStatus.length)];			
		return p;
	}
	
	public static models.durableArticles.ProcurementDetail getRandomProcurementDetail(){
		models.durableArticles.ProcurementDetail pd = new models.durableArticles.ProcurementDetail();
		pd.description = randomString();
		pd.price = rand.nextDouble();
		pd.quantity = rand.nextInt(100);
		pd.priceNoVat = rand.nextDouble();
		pd.alertTime = rand.nextDouble();
		pd.llifeTime = rand.nextDouble();
		pd.seller = randomString();
		pd.phone = String.format("%010d",rand.nextLong());
		pd.brand = randomString();
		pd.serialNumber = randomString(10);
		pd.fsn = FSN_Description.find.all().get(rand.nextInt(FSN_Description.find.findRowCount()));
		return pd;
	}
	
	public static models.durableArticles.DurableArticles getRanDurableArticles(FSN_Description fsn){
		int i=1;
		DurableArticles d = new DurableArticles();
		d.department = department[rand.nextInt(department.length)];
		d.room = randomString(4);
		d.floorLevel = randomString(2);
		
		while(true){
			if(DurableArticles.find.where().eq("code", fsn.descriptionId + String.format("-%03d", i)) == null){
				d.code = fsn.descriptionId + String.format("-%03d", i);
				break;
			}
			i++;
		}
		return new DurableArticles();
	}
	
	public static String randomString(){
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
	
	public static String randomString(int n){
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(n);
	}
	
}
