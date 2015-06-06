import sys
import json
import random

def print_usage():
    print "WRONG USAGE: Directory path that contains collections, sample percentage and desired output location must be provided."
    print "python 6_create_training_and_test_queries.py [NO_OF_TRAINING_DATA] [NO_OF_TEST_DATA] [OUTPUT_DIRECTORY_PATH]"
    print "Ex:    6_create_training_and_test_queries.py 40 10 ../../../data/processed-aol"

if len(sys.argv) < 4:
    print_usage()
    sys.exit()

click_datas = json.loads(open("queries_analysis.json").read())

i = 0
for index, click_data in enumerate(click_datas):
	if len(click_data['hit_queries']) < 50:
		continue
	
	training_queries = click_data['hit_queries'][:40]
	test_queries = click_data['hit_queries'][40:50]
	
	training_queries_file = open(sys.argv[3] + "/" + "training-queries" + "/" + str(click_data['collection_id']),'w')
	for training_query in training_queries:
		training_queries_file.write(training_query['query'])
		training_queries_file.write("\n")
	training_queries_file.close()
	
	test_queries_file = open(sys.argv[3] + "/" + "test-queries" + "/" + str(click_data['collection_id']),'w')
	for test_query in test_queries:
		test_queries_file.write(test_query['query'])
		test_queries_file.write("\n")
	test_queries_file.close()

	i = i + 1
	if i == 100:
		break