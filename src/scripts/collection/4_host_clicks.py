import sys
import os
import re
import json
from datetime import datetime

def print_usage():
    print "WRONG USAGE: Directory path that contains query logs must be provided."
    print "python 4_host_clicks.py [QUERY_LOGS_PATH] [MAX_NUMBER_OF_QUERY] [MIN_COLLECTION_SIZE]"
    print "Ex:    4_host_clicks.py data/aol 50 50"

if len(sys.argv) < 4:
    print_usage()
    sys.exit()

# #
# Read hosts_analysis.json which is the output of _2_host_analyzer.py
hosts_analysis = json.loads(open("hosts_analysis.json").read())

# #
# Collect the click logs assosiated with hosts.
host_query_dict = []
# Structure of array elements
# {
#     "collection_id" : collection_id,
#     "host" : host,
#     "hit_queries": [
#             {
#                 "query": query,
#                 "clicked_url": url
#             },
#             .
#             .
#     ]
# }
for i in range(len(hosts_analysis)):
    if len(hosts_analysis[i]['documents']) < int(sys.argv[3]):
        break
    collection_dict = {}
    collection_dict['collection_id'] = i
    collection_dict['host'] = hosts_analysis[i]['host']
    collection_dict['hit_queries'] = []
    host_query_dict.append(collection_dict)

for filename in os.listdir(sys.argv[1]):
    # start_time = datetime.now()

    aol_log = open(sys.argv[1] + "/" + filename)
    aol_log_read = aol_log.readlines()
    aol_log.close()

    for line in aol_log_read:
        
        line_regexed = re.match(".*?\t(.*?)\t.*?\t.*\t(.*)$",line)
        # Click data available?
        if line_regexed.group(2) == "":
            continue
        for index,host_analysis in enumerate(hosts_analysis):
            # No click data neccessary for collections below threshold
            if len(hosts_analysis[index]['documents']) < int(sys.argv[3]):
                break
            # Enough click data for host.
            if len(host_query_dict[index]['hit_queries']) >= int(sys.argv[2]):
                continue

            exist = False
            try:
                exist = "." + host_analysis['host'] in line_regexed.group(2)
            except UnicodeDecodeError as ude: 
                print "Error in line: " + line
                break
            # Click data assosiated with one of the hosts
            if (exist):
                query_dict = {}
                query_dict['query'] = line_regexed.group(1)
                query_dict['clicked_url'] = line_regexed.group(2)
                
                host_query_dict[index]['hit_queries'].append(query_dict)
                # Click data already assosiated with one of the hosts, no need for furthere host control
                break
    # end_time = datetime.now()
    # print "Started at: ", start_time.strftime("%H:%M:%S")
    # print "Ended at:   ", end_time.strftime("%H:%M:%S")

queries_json_file = open("queries_analysis.json","w")
json.dump(host_query_dict, queries_json_file)
queries_json_file.close()