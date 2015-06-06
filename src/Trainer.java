import indexer.SearchFiles;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Arrays;

class Trainer {

    /**
     * Main function for trainer.
     *
     * @param args
     */
    public static void main(String[] args) {

        Path path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", "sampled_collections.json");
        String sampledCollectionPath = path.toString();
        ArrayList<String> sampledCollections = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(sampledCollectionPath))) {
            String line = br.readLine();
            line = line.replace("[","");
            line = line.replace("]","");
            sampledCollections = new ArrayList<String>(Arrays.asList(line.split(", ")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sampled documents index path
        path = Paths.get(System.getProperty("user.dir"), "data", "index", "govDatSamples");
        String sampledDocumentsIndexPath = path.toString();
        // Concatenated sampled documents index path
        path = Paths.get(System.getProperty("user.dir"), "data", "index", "govDatSamplesConcatenated");
        String concatenatedSampledDocumentsIndexPath = path.toString();

        for (int i = 0; i < sampledCollections.size(); i++) {
            path = Paths.get(System.getProperty("user.dir"), "data", "processed-aol", "training-queries",sampledCollections.get(i));
            String trainingQueriesPath = path.toString();

            int K = 100000000;

            try {
                SearchFiles searchFile = new SearchFiles();
                searchFile.search(sampledDocumentsIndexPath, trainingQueriesPath, K);
                ArrayList<TopDocs> topDocs = searchFile.getTopDocsList();

                for (int j = 0; j < topDocs.size(); j++) {
                    CollectionScorer collectionScorer = new CollectionScorer(sampledCollections, topDocs.get(j), searchFile.getNames(topDocs.get(j)));
                    collectionScorer.scoreCollections();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
    }
}
