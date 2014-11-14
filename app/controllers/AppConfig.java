package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.RequestBody;
import play.data.*;
import play.libs.Json;
import views.html.*;
import models.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.User;

import play.mvc.Security;

public class AppConfig extends Controller {
	public static double TAX = -1;
	public static void doConfig() {
		if (TAX == -1) {
			try{
				String tax = readFile("./conf/tax.ini", Charset.defaultCharset());
				TAX = Double.parseDouble(tax);
			}catch (Exception e){
			 
			}
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void setTax(double value) {
		File file = new File("./conf/tax.ini");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file), 1024);
			out.write(""+value);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Security.Authenticated(Secured.class)
	@BodyParser.Of(BodyParser.Json.class)
	public static Result changeTax(){
		User user = User.find.where().eq("username", session().get("username")).findUnique();
		if(user==null){
			return redirect(routes.Application.index());
		}
		if(!user.isPermit(6)){
			return ok(permissionDenied.render());
		}else{
			RequestBody body = request().body();
	    	JsonNode json = body.asJson();
	    	ObjectNode result = new Json().newObject();
	    	try{
	    		double tax = json.get("tax").asDouble();
	    		setTax(tax);
	    		TAX = tax;
	    	}catch(NumberFormatException nfe){  
	    		result.put("status", "formatError");
	    		return ok(result);
	    	} 
	    	
	    	result.put("status", "success");
			return ok(result);
		}
	}
}
