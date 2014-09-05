import java.io.*;

public class ReplaceInFile {
	public static String PATH = "./app/views/";

	public void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				replaceFileNoContain(fileEntry,"sideMenu","headerTag\\(","headerTagFull\\(");
				replaceFileNoContain(fileEntry,"sideMenu","footer\\(","footerFull\\(");
				replaceFile(fileEntry, "--!>", "-->");
				/*System.out.println(fileEntry.getName());
				replaceFile(fileEntry, "=\"//", "=\"http:/");
				replaceFile(fileEntry, "//", "/");
				replaceFile(fileEntry, "/assets", "./assets");
				replaceFile(
						fileEntry,
						"<button class=\"btn btn-block btn-lg btn-success inputBox\"> Login </button>",
						"<a href=\"main.htm\" class=\"btn btn-block btn-lg btn-success inputBox\" > Login </a>");
				replaceFile(fileEntry, "href=\"/home\"", "href=\"main.htm\"");
				replaceFile(fileEntry, "href=\"/export\"",
						"href=\"export.htm\"");
				replaceFile(fileEntry, "href=\"/export/order\"",
						"href=\"exportOrder.htm\"");
				replaceFile(fileEntry, "href=\"/export/order/add\"",
						"href=\"exportOrderAdd.htm\"");
				replaceFile(fileEntry, "href=\"/export/transferOutSide\"",
						"href=\"exportTransferOutSide.htm\"");
				replaceFile(fileEntry, "href=\"/export/transferOutSide/add\"",
						"href=\"exportTransferOutSideAdd.htm\"");
				replaceFile(fileEntry, "href=\"/export/transferInside\"",
						"href=\"exportTransferInside.htm\"");
				replaceFile(fileEntry, "href=\"/export/transferInside/add\"",
						"href=\"exportTransferInsideAdd.htm\"");
				replaceFile(fileEntry, "href=\"/export/donate\"",
						"href=\"exportDonate.htm\"");
				replaceFile(fileEntry, "href=\"/export/donate/add\"",
						"href=\"exportDonateAdd.htm\"");
				replaceFile(fileEntry, "href=\"/export/sold\"",
						"href=\"exportSold.htm\"");
				replaceFile(fileEntry, "href=\"/export/sold/add\"",
						"href=\"exportSoldAdd.htm\"");
				replaceFile(fileEntry, "href=\"/export/other\"",
						"href=\"exportOther.htm\"");
				replaceFile(fileEntry, "href=\"/export/other/add\"",
						"href=\"exportOtherAdd.htm\"");
				replaceFile(fileEntry, "href=\"/import\"",
						"href=\"imports.htm\"");
				replaceFile(fileEntry, "href=\"/import/institute\"",
						"href=\"importsInstitute.htm\"");
				replaceFile(fileEntry, "href=\"/import/instituteAdd\"",
						"href=\"importsInstituteAdd.htm\"");
				replaceFile(fileEntry, "href=\"/import/material\"",
						"href=\"importsMaterial.htm\"");
				replaceFile(fileEntry,
						"href=\"/import/materialDurableArticlesAdd\"",
						"href=\"importsMaterialDurableArticlesAdd.htm\"");
				replaceFile(fileEntry,
						"href=\"/import/materialDurableGoodsAdd\"",
						"href=\"importsMaterialDurableGoodsAdd.htm\"");
				replaceFile(fileEntry,
						"href=\"/import/materialConsumableGoodsAdd\"",
						"href=\"importsMaterialConsumableGoodsAdd.htm\"");
				replaceFile(fileEntry, "href=\"/import/order\"",
						"href=\"importsOrder.htm\"");
				replaceFile(fileEntry,
						"href=\"/import/order/DurableArticlesAdd\"",
						"href=\"importsOrderDurableArticlesAdd.htm\"");
				replaceFile(fileEntry,
						"href=\"/import/order/DurableArticlesAdd/Material1\"",
						"href=\"importsOrderDurableArticlesAddMaterial1.htm\"");
				replaceFile(fileEntry,
						"href=\"/import/order/DurableArticlesAdd/Material2\"",
						"href=\"importsOrderDurableArticlesAddMaterial2.htm\"");
				replaceFile(fileEntry, "href=\"/import/order/GoodsAdd\"",
						"href=\"importsOrderGoodsAdd.htm\"");
				replaceFile(fileEntry,
						"href=\"/import/order/GoodsAdd/Material1\"",
						"href=\"importsOrderGoodsAddMaterial1.htm\"");
				replaceFile(fileEntry,
						"href=\"/import/order/GoodsAdd/Material2\"",
						"href=\"importsOrderGoodsAddMaterial2.htm\"");
				replaceFile(fileEntry, "href=\"/Admin\"", "href=\"admin.htm\"");
				replaceFile(fileEntry, "href=\"/Admin/addUser\"",
						"href=\"addUser.htm\"");
				replaceFile(fileEntry, "href=\"/Admin/manageRole\"",
						"href=\"manageRole.htm\"");
				replaceFile(fileEntry, "href=\"/graph\"", "href=\"graph.htm\"");
				replaceFile(fileEntry, "href=\"/report\"",
						"href=\"report.htm\"");
				replaceFile(fileEntry, "href=\"/report/RemainingMaterial\"",
						"href=\"reportRemainingMaterial.htm\"");
				replaceFile(fileEntry,
						"href=\"/report/RemainingMaterialConclusion\"",
						"href=\"reportRemainingMaterialConclusion.htm\"");
				replaceFile(fileEntry, "href=\"/report/DurableArticles\"",
						"href=\"reportDurableArticles.htm\"");
				replaceFile(fileEntry,
						"href=\"/report/ImportDurableArticles\"",
						"href=\"reportImportDurableArticles.htm\"");
				replaceFile(fileEntry,
						"href=\"/report/exportDurableArticles\"",
						"href=\"reportExportDurableArticles.htm\"");
				replaceFile(fileEntry,
						"href=\"/report/exchangeDurableArticles\"",
						"href=\"reportExchangeDurableArticles.htm\"");*/
				
			}
		}
	}

	public void replaceFile(File inFile, String text1, String text2) {
		try {

			File file = inFile;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			String line = "", oldtext = "";
			while ((line = reader.readLine()) != null) {
				oldtext += line + "\n";
			}
			reader.close();
			// replace a word in a file
			// String newtext = oldtext.replaceAll("drink", "Love");

			// To replace a line in a file

			String newtext = oldtext.replaceAll(text1, text2);
			String outFile = PATH + inFile.getName();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFile), "UTF-8"));
			try {
				out.write(newtext);
			} finally {
				out.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void replaceFileNoContain(File inFile, String fileContain, String text1,
			String text2) {
		try {
			File file = inFile;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			String line = "", oldtext = "";
			while ((line = reader.readLine()) != null) {
				oldtext += line + "\n";
			}
			reader.close();
			// replace a word in a file
			// String newtext = oldtext.replaceAll("drink", "Love");

			// To replace a line in a file
			if (!oldtext.contains(fileContain)) {
				String newtext = oldtext.replaceAll(text1, text2);
				String outFile = PATH 
						+ inFile.getName();
				Writer out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outFile), "UTF-8"));
				try {
					out.write(newtext);
				} finally {
					out.close();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String args[]) {
		ReplaceInFile x = new ReplaceInFile();
		File file = new File(ReplaceInFile.PATH);
		x.listFilesForFolder(file);
		
		System.out.println("finish");
	}
}
