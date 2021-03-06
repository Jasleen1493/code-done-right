Credit card transaction processor

1. Create a spring boot application
2. You will get a directory with following folders
    1. new, processing, done, error, garbage
    2. new will have csv files with following naming patterns
        1. transactionXXXX.csv and referenceXXXX.csv, XXXX denotes an integer
3. The application will read from data from above csv files
    1. It will be credit card transactions data having data for following entities
        1. Cardholders, Cards, Transaction, Merchants
    2. Transaction data will be present in transactionXXXX.csv file
    3. Data for other entities will be present in referenceXXXX.csv file
    4. There could be multiple files so file with XXXX should be processed first
    5. transactionXXXX.csv and referenceXXXX.csv can be processed in parallel
    6. Transaction data files could be very big, data for rest of entities will be very small
4. This process will run every two hours 
    1. Process will not run unless the previous run have been successful.
5. Error handling
    1. Do not process any file that does not correspond to naming convention
        1. Move them to garbage folder
    2. New files will be received in new folder
    3. Move the file to processing folder before start processing
    4. If you get any schema error do not process the record and put it in an error file with same name as original file with error appended to it
        1. e.g. transactions0123.csv will have transactions0123-error.csv
    5. Move a processed file to done folder
6. Publish the processes records into a separate Kafka topic
    1. One topic for transactions
    2. One topic for other entities
7. Code quality
    1. 100% unit test code coverage for business logic
    2. Integration test cases
    3. Should not be able to push code if coverage is less than 100%
        1. Lines and branch
    4. Github message should be like 
        1. CCT-XX: <commit message>
        2. Should not be able to push code if the condition is not satisfied
8. Logging
    1. Every processed records should be logged in log file
    2. Every processing of record should have a unique id across different runs of application
    3. Except for customer name and address
    4. Card no and expiry date
    5. Merchant name and address
9. Use something like GitHub actions for CI/CD
10. Containerise the application
11. Data Model

	customer
		id (PK)
		name
		account_no

	credit_card
		card_no (PK)
		card_type (FKey ref_card.card_type)
		expiry_date
		customer_id (FKey customer.id)

	ref_card
		id (PK)
		card_type

	transaction
		id (PK)
		card_no (FKey credit_card.card_no)
		merchant_id (FKey merchant.id)
		transaction_type (FKey transaction_type.id)

	ref_transaction_type
		id (PK)
		transaction_type

	merchant
		id (PK)
		account_no
		name

	Note: Customer and merchant have a many to many relationship with address table
	
	
Credit card transaction handler

1. Create a spring boot application
2. Listen to kafka topics as created by credit card transaction processor
    1. Topic listener for transactions
    2. Topic listener for other entities
3. Read every record in topic and persist it in databse of your choice
    1. Please take meaningful defaults for column sizes for various fields
4. Create tables
    1. Use any database scm tool to do it, flyway, liquibase etc
4. Create following rest apis 
    1. Fetch transactions by credit card type
    	1. Sortable by date of trx, customer country, merchant country
	2. should be able to fetch trx pagewise, should be able to specify page size as well
    2. Fetch transaction for a date range, last month, current month
    	1. Sortable by date of trx, customer country, merchant country
	2. should be able to fetch trx pagewise, should be able to specify page size as well
    3. Fetch transaction for a customer or merchant for a date range, last month, current month
    	1. Sortable by date of trx, customer country, merchant country
	2. should be able to fetch trx pagewise, should be able to specify page size as well
    4. Default page size is 20 and page no 1
6. Input handling
    1. Validate input fields, choose meaningful formats default values
    2. Handle sql injection issues
6. Output handling
    1. Client must never receive an exception
    2. If no record exist for a search criteria then "No data exists for your search criteria" should be returned along with an empty object.
7. Code quality
    1. 100% unit test code coverage for business logic
    2. Integration test cases
    3. Should not be able to push code if coverage is less than 100%
        1. Lines and branch
    4. Github message should be like 
        1. CCT-XX: <commit message>
        2. Should not be able to push code if the condition is not satisfied
8. Logging
    1. Every processed records should be logged in log file
    2. Every processing of record should have a unique id across different runs of application
    3. Except for customer name and address
    4. Card no and expiry date
    5. Merchant name and address
9. Use something like GitHub actions for CI/CD
10. Containerise the application
    1. Can use something like docker compose now that you have two applications for running multiple containers on local
    2. Optional: It would help if you know docker swarm and run on vms
    3. Deploy on minikube on Local
11. Data Model

	customer
		id (PK)
		name
		account_no

	credit_card
		card_no (PK)
		card_type (FKey ref_card.card_type)
		expiry_date
		customer_id (FKey customer.id)

	ref_card
		id (PK)
		card_type

	transaction
		id (PK)
		card_no (FKey credit_card.card_no)
		merchant_id (FKey merchant.id)
		transaction_type (FKey transaction_type.id)

	ref_transaction_type
		id (PK)
		transaction_type

	merchant
		id (PK)
		account_no
		name

	Note: Customer and merchant have a many to many relationship with address table

