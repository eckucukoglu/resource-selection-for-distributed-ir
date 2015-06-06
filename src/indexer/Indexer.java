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
		
		// Document collection directory path
		Path path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", "govDatSamples");
		String docsPath = path.toString();
		// Index files directory path
		path = Paths.get(System.getProperty("user.dir"), "data", "index");
		String indexPath = path.toString();
		// Query list file path
		path = Paths.get(System.getProperty("user.dir"), "data", "queries", "0");
		String queryListPath = path.toString();
		// Top-K documents retrieved
		int K = 50;
		
		IndexFiles indexFile = new IndexFiles(docsPath, indexPath);
		
		try {
			SearchFiles searchFile = new SearchFiles(indexPath, queryListPath, K);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			
//		try {
//			path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", "govDatSamples", "GX000-00-0758811-3");
//			String tmp = path.toString();
//			
//			InputStream stream = new FileInputStream(new File(tmp));
//			JTidyHTMLHandler handler = new JTidyHTMLHandler();
//			Document myDoc = handler.getDocument(stream);
//			
//			System.out.println(myDoc.getField("contents"));
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}