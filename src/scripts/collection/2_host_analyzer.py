import sys
import os
import re
import json

def print_usage():
    print "WRONG USAGE: Directory path that contains documents must be provided."
    print "python 2_url_analyzer.py [INPUT_DIRECTORY_PATH]"
    print "Ex:    2_url_analyzer.py govDatDocuments"

if len(sys.argv) < 2:
    print_usage()
    sys.exit()

# #
# Read all the collections from given input directory
# Extract their <DOCNO> as document_id & the first line after <DOCHDR> as document_url
document_id_url_pairs = []
for filename in os.listdir(sys.argv[1]):
    
    document_file = open(sys.argv[1] + "/" + filename)
    document_file_read = document_file.read()
    document_file.close()
    
    # Regex for extracting document id  from document => "<DOCNO>(.*?)<\/DOCNO>"
    # Regex for extracting document url from document => "<DOCHDR>\n.*?([\w]*?\.gov)"
    document_id_url_pairs.append((re.findall("<DOCNO>(.*?)<\/DOCNO>",document_file_read)[0],re.findall("<DOCHDR>\n.*?([\w]*?\.gov)",document_file_read)[0]))

# #
# Collect the host names from document_urls and push them to array.
# Structure of array elements:
# {
#     "host": host_name,
#     "total": number_of_documents_belongs_to_this_host,
#     "documents": [
#       doc_id_1,
#       doc_id_5,
#       doc_id_1800
#     ]
# }
url_dicts = []
for document_id_url_pair in document_id_url_pairs:

    # Regex for extracting host from url => "<DOCHDR>\n.*?([\w]*?\.gov)"
    url_stripped = re.match("(.*.gov\/)|(.*.gov)",document_id_url_pair[1]).group(0)

    existed_item = filter(lambda item: item['host'] == url_stripped, url_dicts)

    if existed_item:
        existed_item[0]['total'] = existed_item[0]['total'] + 1
        existed_item[0]['documents'].append(document_id_url_pair[0])
    else:
        url_dict = {}
        url_dict['host'] = url_stripped
        url_dict['total'] = 1
        url_dict['documents'] = []
        url_dict['documents'].append(document_id_url_pair[0])
        url_dicts.append(url_dict)

# #
# Sort the array wrt. total number of documents
url_dicts_sorted = sorted(url_dicts, key=lambda x: x['total'],reverse=True)

# #
# Save created json in 'host_analysis.json' file
hosts_json_file = open("hosts_analysis.json","w")
json.dump(url_dicts_sorted, hosts_json_file)
hosts_json_file.close()
