import sys
import os
import json
import shutil

def print_usage():
    print "WRONG USAGE: Directory path that contains documents, number of collection and desired output location must be provided."
    print "python 3_collection_builder.py [INPUT_DIRECTORY_PATH] [MIN_COLLECTION_SIZE] [OUTPUT_DIRECTORY_PATH]"
    print "Ex:    3_collection_builder.py govDatDocuments 100 govDatCollections"

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
# Read hosts_analysis.json which is the output of _2_host_analyzer.py
hosts_analysis = json.loads(open("hosts_analysis.json").read())

# #
# Create collection directories as subdirectories of output directory
for index,host_analysis in enumerate(hosts_analysis):
    if len(host_analysis['documents']) < int(sys.argv[2]):
        break
    os.mkdir(sys.argv[3] + "/" + str(index))

# #
# Loop for top hosts
for index,host_analysis in enumerate(hosts_analysis):
    # Do not get collection with size < MIN_COLLECTION_SIZE
    if len(host_analysis['documents']) < int(sys.argv[2]):
        break
    # Loop for every document belongs to this host
    for document_id in host_analysis['documents']:
        # Copy document to cluster directory that created wrt. its host 
        shutil.copy2(sys.argv[1] + "/" + document_id, sys.argv[3] + "/" + str(index) + "/" + document_id)
