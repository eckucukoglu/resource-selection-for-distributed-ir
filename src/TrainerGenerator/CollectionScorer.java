package TrainerGenerator;
import org.apache.lucene.search.TopDocs;

import java.util.ArrayList;

public class CollectionScorer {

    private ArrayList<String> sampledCollectionIds;
    private TopDocs topDocs;
    private ArrayList<Integer> topDocsCollectionIds;
    
    private ArrayList<Float> reddeTopScoresWRTTopTen;
    private ArrayList<Float> reddeTopScoresWRTTopHundred;
    private ArrayList<Float> GAVGScores;

    public CollectionScorer(ArrayList<String> sampledCollectionIds, TopDocs topDocs, String[] names) {
        this.sampledCollectionIds = sampledCollectionIds;
        this.topDocs = topDocs;
        this.topDocsCollectionIds = new ArrayList<Integer>();
        for (int i = 0; i < names.length; i++) {
            String docName = names[i];
            Integer collectionId = Integer.parseInt(docName.substring(docName.lastIndexOf("-") + 1));
            this.topDocsCollectionIds.add(collectionId);
        }
        
        this.reddeTopScoresWRTTopTen = new ArrayList<Float>();
        this.reddeTopScoresWRTTopHundred = new ArrayList<Float>();
        this.GAVGScores = new ArrayList<Float>();
    }

    public void scoreCollections() {
        calculateReddeTopScores();
        calculateGAVGScores();
    }

//    private void calculateReddeScores() {
//        for (int i = 0; i < sampledCollections.size(); i++) {
//            reddeScores.add(0);
//            for (int j = 0; j < topDocsCollections.size(); j++){
//                if (topDocsCollections.get(j) == i) {
//                    reddeScores.set(i,reddeScores.get(i) + 1);
//                }
//            }
//        }
//    }

    private void calculateReddeTopScores() {
        for (int i = 0; i < sampledCollectionIds.size(); i++) {
            reddeTopScoresWRTTopTen.add(new Float(0));
            reddeTopScoresWRTTopHundred.add(new Float(0));

            for (int j = 0; j < topDocsCollectionIds.size(); j++) {
                if (j > 100)
                    break;
                
                if (topDocsCollectionIds.get(j) == i) {
                    if ( j < 10 ) {
                        reddeTopScoresWRTTopTen.set(i, 
                        		reddeTopScoresWRTTopTen.get(i) + (topDocs.scoreDocs[j].score * new Float(0.05)));
                    }
                    reddeTopScoresWRTTopHundred.set(i, 
                			reddeTopScoresWRTTopHundred.get(i) + (topDocs.scoreDocs[j].score * new Float(0.05)));
                }
            }
        }
    }

    private void calculateGAVGScores() {
        for (int i = 0; i < sampledCollectionIds.size(); i++) {
        	int noOfRetrievedDocForCollection = 0;
            
        	GAVGScores.add(new Float(0));
            
            for (int j = 0; j < topDocsCollectionIds.size(); j++){
                if (topDocsCollectionIds.get(j) == i) {
                    noOfRetrievedDocForCollection++;
                    
                    if (noOfRetrievedDocForCollection == 1) {
                        GAVGScores.set(i, topDocs.scoreDocs[j].score);
                    } else {
                        GAVGScores.set(i, GAVGScores.get(i) * topDocs.scoreDocs[j].score);
                        System.out.println("Fatih");
                    }
                }
            }
            
            if (noOfRetrievedDocForCollection != 0) {
                GAVGScores.set(i, new Float(Math.pow(GAVGScores.get(i), 
                		new Float(1) / new Float(noOfRetrievedDocForCollection))));

            }
        }
    }

}
