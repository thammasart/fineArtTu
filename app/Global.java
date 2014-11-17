import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;

import controllers.AppConfig;
import models.*;
import models.type.*;

import models.durableGoods.*;


import java.util.*;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
		AppConfig.doConfig();
		if(User.find.findRowCount() == 0) {
			Ebean.save((List) Yaml.load("user.yml"));
			Ebean.save((List) Yaml.load("materialCode.yml"));
			Ebean.save((List) Yaml.load("fsn.yml"));
			Ebean.save((List) Yaml.load("company.yml"));
			Ebean.save((List) Yaml.load("export/requisition.yml"));
			Ebean.save((List) Yaml.load("export/auction.yml"));
			Ebean.save((List) Yaml.load("export/donation.yml"));
			Ebean.save((List) Yaml.load("export/otherTransfer.yml"));
			Ebean.save((List) Yaml.load("export/internalTransfer.yml"));
			Ebean.save((List) Yaml.load("export/repairing.yml"));
			Ebean.save((List) Yaml.load("export/borrow.yml"));

			cratedurableGoods();
			updateMaterialCode();
		}
	}

	private void cratedurableGoods(){
		models.durableGoods.Procurement order = new models.durableGoods.Procurement();
		order.title = "test01";
		order.contractNo = "test01";
		order.budgetType = "พิเศษ";
		order.budgetYear = 57;
		order.status = ImportStatus.SUCCESS;
		order.company = Company.find.all().get(0);
		order.save();

		models.durableGoods.ProcurementDetail detail = new models.durableGoods.ProcurementDetail();
	 	detail.code = "7125-001-0001";
	 	detail.description = "วัสดุ คงทนถาวร ทดสอบ";
	 	detail.price = 2140.00;
	 	detail.priceNoVat = 2000.00;
	 	detail.quantity = 10;
	 	detail.remain = 10;
	 	detail.seller = "ไม่ระบุ"; 
	 	detail.phone = "ไม่ระบุ";
	 	detail.brand = "ไม่ระบุ";
	 	detail.serialNumber = "ไม่ระบุ";
	 	detail.typeOfDurableGoods = 1;
	 	detail.procurement = order;
	 	detail.save();

	 	for(int i=1; i<11; i++){
		 	DurableGoods goods = new DurableGoods();
		 	goods.department = "ห้องพัสดุ"; //สาขา
			goods.room = "555"; //ห้อง
			goods.floorLevel = "5"; //ชั้น
			goods.codes = detail.code+"("+i+"/"+detail.quantity+")"; //รหัส
			goods.title = "นาย"; //คำนำหน้าชื่อ
			goods.firstName = "สรณัฐ"; //ชื่อ
			goods.lastName = "ชัยเพ็ชร์"; //สกุล
			goods.status = SuppliesStatus.NORMAL;
			goods.detail = detail;
			goods.save();
		} 
	}

	private void updateMaterialCode(){
		for(MaterialCode code : MaterialCode.find.all()){
			code.updateRemain();
		}
	}
}