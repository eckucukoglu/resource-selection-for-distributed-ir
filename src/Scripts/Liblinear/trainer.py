import os
import sys

def print_usage():
    print "WRONG USAGE: Directory path that contains training files and Directory path that .model files saved must be provided."
    print "python trainer.py [INPUT_DIRECTORY_PATH] [OUTPUT_DIRECTORY_PATH]"
    print "Ex:    trainer.py ../../../data/liblinear/trainingFiles/ ../../../data/liblinear/models/"
    # python trainer.py /home/arcelik/workspace/resource-selection-for-distributed-ir/data/liblinear/trainingFiles/ /home/arcelik/workspace/resource-selection-for-distributed-ir/data/liblinear/models/


if len(sys.argv) < 3:
    print_usage()
    sys.exit()


os.chdir(sys.argv[2])

for training_file in os.listdir(sys.argv[1]):
	cmd = 'liblinear-train -s 0 ' + sys.argv[1] + "/" + training_file
	# cmd = 'liblinear-train -s 6 ' + sys.argv[1] + "/" + training_file
	os.system(cmd)
