Sprint 0: Project Set Up
Stories :
1. Springboot application setup
2. Git operations validation setup (commit, push)
3. CI/CD Setup
4. Code Quality setup 
5. Logging Mechanism
6. Testing framework

Epic 1. Record Processing

Stories: 

1. File Validation

Description:
Read new files from new folder and check the format of name of the file. If format of file name matches with the required 
pattern, then move the file into processing folder, else if incorrect format found then move to garbage folder.

Acceptance Criteria: 
> Files with correct name formats must be available in processing folder.
> Files with incorrect name formats must be available in garbage folder.

2. File Processing

Description:
Read new files from processing folder every two hours.The processing must be on the basis of files naming convention, where xxxx
denotes a integer and files having smallest number present in it's name must be processed first. 
If an issue occurs with record then file shall be sent to error folder with same file name with error appended, for example transactions0123-error.csv for file
transactions0123.csv consisting of the error description, else call kafka publisher and move the record to done folder.

Acceptance Criteria:
> Read files from processing folder and process them record by record
> Processing file must be able to call kafka publisher (subject to availability)
> Successful processed records pushed to done folder with correct naming convention
> Erroneous processed records pushed to done folder with correct naming convention

4. Kafka POC
Understanding how kafka works
Setting up kafka on local and publishig and reading from kafka from java code

5. Kafka Publisher

Description:
All processing shall be posted on kafka topic, one topic for transaction and another topic for other entities.Kafka Implementation shall
include Kafka POC completion.

Acceptance Criteria:
> Kafka topics setup
> Integration of kafka with the application
> Create publisher and publish to kafka topic

