import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;

import controllers.AppConfig;
import models.*;
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
		}
	}
}