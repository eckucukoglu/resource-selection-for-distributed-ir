import os
import sys
import re

def print_usage():
    print "WRONG USAGE: Directory path that contains training models and Directory path that prediction results saved must be provided."
    print "python trainer.py [TEST_DIRECTORY_PATH] [MODEL_DIRECTORY_PATH] [OUTPUT_DIRECTORY_PATH]"
    print "Ex:    trainer.py ../../../data/testFiles/ ../../../data/models/ ../../../data/perdictResults/"
	# python predicter.py /home/arcelik/workspace/resource-selection-for-distributed-ir/data/liblinear/testFiles/ /home/arcelik/workspace/resource-selection-for-distributed-ir/data/liblinear/models/ /home/arcelik/workspace/resource-selection-for-distributed-ir/data/liblinear/results/

if len(sys.argv) < 4:
    print_usage()
    sys.exit()


os.chdir(sys.argv[2])
os.system("ls")


for training_file in os.listdir(sys.argv[2]):
	collection_id = re.findall("(.*)\.",training_file)[0]
	cmd = 'liblinear-predict -b 1 '
	cmd = cmd + sys.argv[1] + "/" + collection_id + " " #1st argument: Test file for collection
	cmd = cmd + sys.argv[2] + "/" + training_file + " " #2nd argument: Model file for collection
	cmd = cmd + sys.argv[3] + "/" + collection_id + " " #3nd argument: Result file to be generated
	os.system(cmd)