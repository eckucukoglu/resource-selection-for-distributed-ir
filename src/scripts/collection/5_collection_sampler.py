import sys
import os
import shutil
import random
import json

def print_usage():
    print "WRONG USAGE: Directory path that contains collections, sample percentage and desired output location must be provided."
    print "python 5_collection_sampler.py [INPUT_DIRECTORY_PATH] [PERCENTAGE_OF_COLLECTIONS_TO_BE_SAMPLED] [OUTPUT_DIRECTORY_PATH] [CONCATENATED_OUTPUT_DIRECTORY_PATH]"
    print "Ex:    5_collection_sampler.py ../../../data/gov2/govDatCollections/ 5 ../../../data/gov2/govDatSamples ../../../data/gov2/govDatSamplesConcatenated"

if len(sys.argv) < 5:
    print_usage()
    sys.exit()

# #
# Create output directories
if os.path.exists(sys.argv[3]):
    print "cannot create output directory '" + sys.argv[3] + "': Directory exists"
    sys.exit()
os.mkdir(sys.argv[3])
if os.path.exists(sys.argv[4]):
    print "cannot create output directory '" + sys.argv[4] + "': Directory exists"
    sys.exit()
os.mkdir(sys.argv[4])

# #
# Read queries_analysis.json which is the output of 4_host_clicks.py
queries_analysis = json.loads(open("queries_analysis.json").read())

sampled_collections = []
i = 0
for query_analysis in queries_analysis:
	if len(query_analysis['hit_queries']) < 50:
		print "Collection " + str(query_analysis['collection_id']) + " won't be sampled, not enough click data available"
		# os.rmdir(sys.argv[1] + "/" + str(query_analysis['collection_id']))
		# print "Collection " + str(query_analysis['collection_id']) + " removed from collections file"
		continue
	sampled_collections.append(query_analysis['collection_id'])

	# Get the documents in collection
	documents_in_collection = os.listdir(sys.argv[1] + "/" + str(query_analysis['collection_id']))
	# Decide how many documents to be sampled
	number_of_documents_to_be_sampled = (len(documents_in_collection) * int(sys.argv[2])) / 100
	# Pick random documents
	documents_to_be_sampled = random.sample(documents_in_collection,number_of_documents_to_be_sampled)
	# Copy randomly selected documents to output location
	concatenated_documents = ""
	for document_id in documents_to_be_sampled:
		shutil.copy2(sys.argv[1] + "/" + str(query_analysis['collection_id']) + "/" + document_id, sys.argv[3] + "/" + document_id + "-" + str(query_analysis['collection_id']))

		to_be_concatenated_doc = open(sys.argv[1] + "/" + str(query_analysis['collection_id']) + "/" + document_id)
		to_be_concatenated_doc_read = to_be_concatenated_doc.read()
		to_be_concatenated_doc.close()

		concatenated_documents = concatenated_documents + to_be_concatenated_doc_read

	concatenated_documents_file = open(sys.argv[4] + "/" + str(query_analysis['collection_id']),'w')
	concatenated_documents_file.write(concatenated_documents)
	concatenated_documents_file.close()


	i = i + 1
	if i == 100:
		break

sampled_collections_file = open("sampled_collections.json","w")
json.dump(sampled_collections, sampled_collections_file)
sampled_collections_file.close()