package TrainerGenerator;
import org.apache.lucene.search.TopDocs;

import Searcher.SearchFiles;
import Utils.Enums;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Arrays;

class Trainer {

    public static void main(String[] args) {

        Path path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, 
        		Enums.PROC_GOV2_DATA_DIR, Enums.SAMPLED_COLLECTIONS_JSON);
        String sampledCollectionPath = path.toString();
        ArrayList<String> sampledCollectionIds = null;

        try (BufferedReader br = new BufferedReader(new FileReader(sampledCollectionPath))) {
            String line = br.readLine();
            sampledCollectionIds = new ArrayList<String>(Arrays.asList(line.split(", ")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sampled documents index path
        path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, 
        		Enums.INDEX_DIR, Enums.SAMPLE_DOCS_DIR);
        String sampledDocumentsIndexPath = path.toString();
        // Concatenated sampled documents index path
        path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, 
        		Enums.INDEX_DIR, Enums.SAMPLE_CONCAT_DIR);
        String concatenatedSampledDocumentsIndexPath = path.toString();

        for (int i = 0; i < sampledCollectionIds.size(); i++) {
            path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, 
            		Enums.PROC_AOL_DIR, Enums.TRAINING_QUERIES_DIR, sampledCollectionIds.get(i));
            String trainingQueriesPath = path.toString();

            int K = 100000000;

            try {
                SearchFiles searchFileSampleDocs = new SearchFiles();
                searchFileSampleDocs.search(sampledDocumentsIndexPath, trainingQueriesPath, K);
                ArrayList<TopDocs> topDocsListSampledDocs = searchFileSampleDocs.getTopDocsList();

                SearchFiles searchFilesConcatenatedSampleDocs = new SearchFiles();
                searchFilesConcatenatedSampleDocs.search(concatenatedSampledDocumentsIndexPath, trainingQueriesPath, K);
                ArrayList<TopDocs> topDocsListConcatenatedSampleDocs = searchFilesConcatenatedSampleDocs.getTopDocsList();


                for (int j = 0; j < topDocsListSampledDocs.size(); j++) {
                	TopDocs topDocsSampledDocs = topDocsListSampledDocs.get(j);
                    TopDocs topDocsConcatenatedSampledDocs = topDocsListConcatenatedSampleDocs.get(j);

                    String[] sampledDocsNames = searchFileSampleDocs.getNames(topDocsListSampledDocs.get(j));
                    String[] concatenatedSampledDocsNames = searchFilesConcatenatedSampleDocs.getNames(topDocsListConcatenatedSampleDocs.get(j));
                	
                    CollectionScorer collectionScorer = new CollectionScorer(sampledCollectionIds, topDocsSampledDocs, topDocsConcatenatedSampledDocs,sampledDocsNames, concatenatedSampledDocsNames);
                    collectionScorer.scoreCollections();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // TODO: remove below break.
            break;
        }
    }
}
