package TermExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import Utils.Enums;

/**
 * Text contents of the HTML documents 
 * are extracted and stored with
 * the same name as original documents.
 */
public class Extractor {

	public static void main(String[] args) {
		
		Date start = new Date();
		for (int i = 0; i < Enums.TOTAL_COLLECTION_COUNT; ++i) {
			Path path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, 
					Enums.PROC_GOV2_DATA_DIR, Enums.EXT_COLLECTION_DIR, Integer.toString(i));
			String dirPath = path.toString();
			File file = new File(dirPath);
			file.mkdir();
			path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, 
					Enums.PROC_GOV2_DATA_DIR, Enums.GOV2_COLLECTION_DIR, Integer.toString(i));
			dirPath = path.toString();
			extractDocs(dirPath, i);
		}
		Date end = new Date();
		System.out.println(end.getTime() - start.getTime() + " total milliseconds");
	}
	
	/**
	 * Documents in the given directory are extracted
	 * and stored in the corresponding collection directory.
	 * 
	 * @param dirPath documents' directory's path
	 * @param colId collection id
	 */
	public static void extractDocs (String dirPath, int colId) {
		File folder = new File(dirPath);
		File[] docs = folder.listFiles();
		
		for (File file: docs) {
			try {
				JTidyHTMLHandler handler = new JTidyHTMLHandler();
				String docText = handler.getDocument(new FileInputStream(file));
				saveDoc(docText, colId, file.getName());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("StringIndexOutOfBoundsException: " + 
						file.getName() + "\tcol: " + colId);
				saveDoc("", colId, file.getName());
			}
		}
	}
	
	/**
	 * Saves the given content in the file.
	 * 
	 * @param content content of the file.
	 * @param colId collection id.
	 * @param fileName file name
	 */
	public static void saveDoc (String content, int colId, String fileName) {
		Path path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, Enums.PROC_GOV2_DATA_DIR, 
				Enums.EXT_COLLECTION_DIR, Integer.toString(colId),  fileName);
		String filePath = path.toString();
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filePath, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		writer.println(content);
		writer.close();
	}
}
