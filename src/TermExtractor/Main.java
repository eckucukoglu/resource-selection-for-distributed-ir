package TermExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static final int TOTAL_COLLECTION_COUNT = 129;

	public static void main(String[] args) {
		
		for (int i = 0; i < TOTAL_COLLECTION_COUNT; ++i) {
			Path path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", 
					"govDatCollectionsOnlyText", Integer.toString(i));
			String dirPath = path.toString();
			File file = new File(dirPath);
			file.mkdir();
			path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", "govDatCollections", Integer.toString(i));
			dirPath = path.toString();
			extractDocs(dirPath, i);
			
			System.out.println("Collection " + i + " finished.");
		}
	}
	
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
				System.out.println("StringIndexOutOfBoundsException: " + file.getName() + "\tcol: " + colId);
			}
		}
	}
	
	public static void saveDoc (String content, int colId, String fileName) {
		Path path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", 
				"govDatCollectionsOnlyText", Integer.toString(colId),  fileName);
		String filePath = path.toString();
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filePath, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(content);
		writer.close();
	}
}
