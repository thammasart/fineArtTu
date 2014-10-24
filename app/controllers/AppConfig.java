package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppConfig {
	public static int TAX = -1;
	public static void doConfig() {
		if (TAX == -1) {
			try{
				String tax = readFile("./conf/tax.ini", Charset.defaultCharset());
				TAX = Integer.parseInt(tax);
			}catch (Exception e){
			 
			}
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void setTax(int value) {
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
}
