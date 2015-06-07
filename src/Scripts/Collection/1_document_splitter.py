import sys
import os
import re

def print_usage():
    print "WRONG USAGE: Directory path that contains input collections and desired output location must be provided."
    print "python 1_document_splitter.py [INPUT_DIRECTORY_PATH] [OUTPUT_DIRECTORY_PATH]"
    print "Ex:    1_document_splitter.py data/gov2 govDatDocuments"

if len(sys.argv) < 3:
    print_usage()
    sys.exit()

# #
# Create output directory 
if os.path.exists(sys.argv[2]):
    print "cannot create output directory '" + sys.argv[2] + "': Directory exists"
    sys.exit()
os.mkdir(sys.argv[2])

# #
# Read all the collections from given input directory
for filename in os.listdir(sys.argv[1]):
    
    input_collection = open(sys.argv[1] + "/" + filename)
    input_collection_read = input_collection.read()
    input_collection.close()
    
    # Regex for extracting document from collection => "<DOC>.*?</DOC>[^\<]" #TODO: [^\<] part in regex may be unnecessary
    documents = re.findall("<DOC>.*?</DOC>[^\<]",input_collection_read,re.S)
    
    # Create a file for each document
    for document in documents:

        # Regex for extracting document id  from document => "<DOCNO>(.*?)<\/DOCNO>"
        document_id = re.findall("<DOCNO>(.*?)</DOCNO>",document,re.S)[0]

        # Save created json in 'doc_id' file
        document_file = open(sys.argv[2] + "/" + document_id,"w")
        document_file.write(document)
        document_file.close()
    break
