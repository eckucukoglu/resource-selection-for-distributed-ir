package demo;

public class LuceneDemo {
	
	/**
	 * Main function of project.
	 *  
	 * @param args
	 */
	public static void main(String[] args) {
		String ifInput[] = {"-docs", "C:\\Users\\EmreCan\\git\\resource-selection-for-distributed-ir\\data\\test"};
		String sfInput[] = {};
		
		IndexFiles indexFile = new IndexFiles(ifInput);
		
		try {
			SearchFiles searchFile = new SearchFiles(sfInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return;
	}
	
}