import os
import sys


predictions = {}
for i in range(1001):
	if i == 0:
		continue
	predictions[i] = []


for result_file_name in os.listdir("/home/arcelik/workspace/resource-selection-for-distributed-ir/data/liblinear/results/"):

	result_file = open("/home/arcelik/workspace/resource-selection-for-distributed-ir/data/liblinear/results/" + result_file_name)
	result_file_read = result_file.readlines()
	result_file.close()

	positive_index = 1;	
	for index,line in enumerate(result_file_read):
		if index == 0:
			line_replaced = line.replace("\n","")
			line_splitted = line_replaced.split(" ")
			if len(line_splitted) < 3:
				break
			if line_splitted[1] == "1":
				positive_index = 1
			else:
				positive_index = 2
			continue
		else:
			line_replaced = line.replace("\n","")
			line_splitted = line_replaced.split(" ")
			score = float(line_splitted[positive_index])
			if score == 0.5:
				score = 0
			predictions[index].append((result_file_name,score))

for i in range(51):
	if i == 0:
		continue
	else:
		predictions[i].sort(key=lambda tup: tup[1])
		print predictions[i]		
