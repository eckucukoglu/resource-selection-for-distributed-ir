package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.lucene.document.Document;

public class LuceneDemo {
	
	/**
	 * Main function of project.
	 *  
	 * @param args
	 */
	public static void main(String[] args) {
		String ifInput[] = {"-docs", "C:\\Users\\EmreCan\\git\\resource-selection-for-distributed-ir\\data\\test"};
		String sfInput[] = {};
		
//		IndexFiles indexFile = new IndexFiles(ifInput);
//		
//		try {
//			SearchFiles searchFile = new SearchFiles(sfInput);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			String path = new File("").getAbsolutePath();
			path = path.concat("/data/processed-gov2/govDatSamples/GX000-00-0758811-3");
			
			InputStream stream = new FileInputStream(new File(path));
			JTidyHTMLHandler handler = new JTidyHTMLHandler();
			Document myDoc = handler.getDocument(stream);
			System.out.println(myDoc.getField("contents"));
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return;
	}
	
}