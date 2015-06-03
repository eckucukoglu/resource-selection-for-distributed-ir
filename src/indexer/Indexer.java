package indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;

public class Indexer {
	
	/**
	* Main function of project.
	*
	* @param args
	*/
	public static void main(String[] args) {
		
		Path path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", "govDatSamples");
		String docsPath = path.toString();
		path = Paths.get(System.getProperty("user.dir"), "data", "index");
		String indexPath = path.toString();
		
		boolean isUpdate = false;
		
		IndexFiles indexFile = new IndexFiles(docsPath, indexPath, isUpdate);
		
		try {
			SearchFiles searchFile = new SearchFiles(sfInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//		try {
//			String path = new File("").getAbsolutePath();
//			path = path.concat("/data/processed-gov2/govDatSamples/GX000-00-0758811-3");
//			
//			InputStream stream = new FileInputStream(new File(path));
//			JTidyHTMLHandler handler = new JTidyHTMLHandler();
//			Document myDoc = handler.getDocument(stream);
//			System.out.println(myDoc.getField("contents"));
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
}