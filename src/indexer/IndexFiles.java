package indexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class IndexFiles {
	
	IndexFiles (String docsPath, String indexPath) {
		
		final Path docDir = Paths.get(docsPath);
		
		if (!Files.isReadable(docDir)) {
			System.out.println("Document directory '" + docDir.toAbsolutePath() +
					"' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		
		Date start = new Date();
		try {
			
			Directory dir = FSDirectory.open(Paths.get(indexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			
			iwc.setOpenMode(OpenMode.CREATE);
			
			// Optional: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer.  But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			// iwc.setRAMBufferSizeMB(256.0);
			
			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);
			
			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here.  This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);
			
			writer.close();
			
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");
			
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() +
			"\n with message: " + e.getMessage());
		}
	}
	
	static void indexDocs (final IndexWriter writer, Path path) throws IOException {
		
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				try {
					indexDoc(writer, file);
				} catch (IOException ignore) {
					// don't index files that can't be read.
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	static void indexDoc (IndexWriter writer, Path file) throws IOException {
		try (InputStream stream = Files.newInputStream(file)) {

			Document doc = new Document();
			
			// Add the path of the file as a field named "path".  Use a
			// field that is indexed (i.e. searchable), but don't tokenize
			// the field into separate words and don't index term frequency
			// or positional information:
			Field pathField = new StringField("name", file.getFileName().toString(), Field.Store.YES);
			doc.add(pathField);
					
			// Add the contents of the file to a field named "contents".  Specify a Reader,
			// so that the text of the file is tokenized and indexed, but not stored.
			// Note that FileReader expects the file to be in UTF-8 encoding.
			// If that's not the case searching for special characters will fail.
			doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
			
			System.out.println("adding " + file.getFileName());
			writer.addDocument(doc);
		}
	}
}
