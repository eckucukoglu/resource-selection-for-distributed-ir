package Utils;

/**
 * Common constants for whole project.
 */
public class Enums {
	
	/** Search will be executed on field of documents. **/
	public static final String FIELD_CONTENT = "contents";
	/** Document names are stored in the field **/
	public static final String FIELD_NAME = "name";
	
	/** Total collection count **/
	public static final int TOTAL_COLLECTION_COUNT = 129;
	/** Main data directory **/
	public static final String DATA_DIR = "data";
	/** Pre-processed gov2 data directory **/
	public static final String PROC_GOV2_DATA_DIR = "processed-gov2";
	/** Gov2 data collection directory **/
	public static final String GOV2_COLLECTION_DIR = "govDatCollections";
	/** Text extracted documents directory **/
	public static final String EXT_COLLECTION_DIR = "govDatCollectionsOnlyText";
	/** Generated sample document's directory **/
	public static final String SAMPLE_DOCS_DIR = "govDatSamples";
	/** Main index directory  **/
	public static final String INDEX_DIR = "index";
	/** Liblinear related files directory **/
    public static final String LIBLINEAR_DIR = "liblinear";
	
	/** Sampled collections collection id list **/
	public static final String SAMPLED_COLLECTIONS_JSON = "sampled_collections.json";
	
	/** Concatenated document samples are in this directory **/
	public static final String SAMPLE_CONCAT_DIR = "govDatSamplesConcatenated";
	
	/** Pre-processed AOL queries' main directory **/
	public static final String PROC_AOL_DIR = "processed-aol";

    /** Training queries are listed in this directory **/
    public static final String TRAINING_QUERIES_DIR = "training-queries";

    /** Training queries are listed in this directory **/
    public static final String TESTING_QUERIES_DIR = "test-queries";

    /** Liblinear training files path * n training files, 1 per collection **/
    public static final String TRAINING_FILES_DIR = "trainingFiles";

    /** Liblinear training files path * n training files, 1 per collection **/
    public static final String TESTING_FILES_DIR = "testFiles";
}
