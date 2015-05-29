import sys
import os
import shutil
import random

def print_usage():
    print "WRONG USAGE: Directory path that contains collections, sample percentage and desired output location must be provided."
    print "python 4_collection_sampler.py [INPUT_DIRECTORY_PATH] [PERCENTAGE_OF_COLLECTIONS_TO_BE_SAMPLED] [OUTPUT_DIRECTORY_PATH]"
    print "Ex:    4_collection_sampler.py govDatCollections 5 govDatSamples"

if len(sys.argv) < 3:
    print_usage()
    sys.exit()

# #
# Create output directory 
if os.path.exists(sys.argv[3]):
    print "cannot create output directory '" + sys.argv[3] + "': Directory exists"
    sys.exit()
os.mkdir(sys.argv[3])

# #
# Get collections created by _3_collection_builder.py
for collection_id in os.listdir(sys.argv[1]):
	# Get the documents in collection
	documents_in_collection = os.listdir(sys.argv[1] + "/" + collection_id)
	# Decide how many documents to be sampled
	number_of_documents_to_be_sampled = len(documents_in_collection) / int(sys.argv[2])
	# Pick random documents
	documents_to_be_sampled = random.sample(documents_in_collection,number_of_documents_to_be_sampled)
	# Copy randomly selected documents to output location
	for document_id in documents_to_be_sampled:
		shutil.copy2(sys.argv[1] + "/" + collection_id + "/" + document_id, sys.argv[3] + "/" + document_id + "-" + collection_id)
