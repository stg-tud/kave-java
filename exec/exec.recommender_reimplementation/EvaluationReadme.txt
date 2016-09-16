Query Building for Raychev

Needs JavaToCSharpUtils folder in QUERY_FOLDER_PATH
1. Building Queries from CompletionEvents
	RaychevRunner.queryBuilderFromCompletionEvents QUERY_FOLDER_PATH points to Event Zips
2. Building Queries from Contexts with Random Holes
	RaychevRunner.queryBuilderWithRandomHoles
Queries are in the Format of Query_ClassName with java file and CompletionEvent as json
Java File needs to be fixed so that it is compilable

Evaluation Information:

Raychev needs Linux 64bit System

EvaluationRecommender can be extended to add additional Recommender
MeasureCalculator can be extended to add additional Measures (only supports one Expected Method at the moment)

Recommender and Measures are initialized in the constructor

Recommender Options are in the recommender class that extends EvaluationRecommender
Additional settings for PBN are in PBNMinerModule

Analysis:
	PBN and Heinemann built in Automatic Evaluation 
	Analysis Path needs to point to Zip Files with Contexts
	Additional parameter: Statement Limit filters all contexts where the number of statements exceeds the limit  
	
	Note that all contexts are transformed with the JavaTransformationVisitor before analysing
	
	Raychev:
		Build sentences with RaychevRunner.sentenceBuilder; FOLDERPATH is path to Context Zip Files
		Output is train_all file that contains sentence data for Raychev 

		Build model with SLANG
		put train_all file into bigdata folder
		run in shell:
			- train_bigram.sh all
		 	- train_ngram.sh all
		
Evaluation:
	Supported measures: F1 and MRR (can be changed in constructor of Evaluation class)
	
	Query Path needs to point to Queries - Format is Completion Events as json Files
	As additonal preparation for Raychev put Query folders that include compilable java file and completion event as json into the folder SLANG/tests/src/com/example/fill
	Change SUDO_PASSWORD in RaychevRecommender class
	When evaluating with Raychev the query folder path needs to be inside the SLANG folder, i.e. SLANG/tests/src/com/example/fill
	
Results and Logs (if activated by LOGGING constant) are saved in RESULT_PATH

Folder path to SLANG is home/Documents/SLANG

