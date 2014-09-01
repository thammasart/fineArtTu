import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;
import models.*;
import java.util.*;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
		if(User.find.findRowCount() == 0) {
			Ebean.save((List) Yaml.load("user.yml"));
			Ebean.save((List) Yaml.load("consumableCode.yml"));
			Ebean.save((List) Yaml.load("fsn.yml"));
		}
	}
}