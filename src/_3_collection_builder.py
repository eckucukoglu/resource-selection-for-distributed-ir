import sys
import os
import json
import shutil

def print_usage():
    print "WRONG USAGE: Directory path that contains documents, number of collection and desired output location must be provided."
    print "python _3_collection_builder.py [INPUT_DIRECTORY_PATH] [NO_OF_COLLECTION_TO_BE_CREATED] [OUTPUT_DIRECTORY_PATH]"
    print "Ex:    _3_collection_builder.py govDatDocuments 100 govDatCollections"

if len(sys.argv) < 3:
    print_usage()
    sys.exit()

# #
# Create output directory 
if os.path.exists(sys.argv[3]):
    print "cannot create output directory '" + sys.argv[3] + "': File exists"
    sys.exit()
os.mkdir(sys.argv[3])

# #
# Create collection directories as subdirectories of output directory
for i in range(100):
    os.mkdir(sys.argv[3] + "/" + str(i))

# #
# Read hosts_analysis.json which is the output of _2_host_analyzer.py
# Get first NO_OF_COLLECTION_TO_BE_CREATED hosts
hosts_analysis = json.loads(open("hosts_analysis.json").read())[:int(sys.argv[2])]

# #
# Loop for top hosts
for index,host_analysis in enumerate(hosts_analysis):
    # Loop for every document belongs to this host
    for document_id in host_analysis['documents']:
        # Copy document to cluster directory that created wrt. its host 
        shutil.copy2(sys.argv[1] + "/" + document_id, sys.argv[3] + "/" + str(index) + "/" + document_id)
