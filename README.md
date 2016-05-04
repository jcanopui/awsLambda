Zurich Amazon
---------------
---------------

Set of different Amazon Lambda Services to register and publish push messages through Amazon


- lambda_register: lambda service to register token against platform and topic

- lambda_process: lambda service to process csv files containing tokenid and message and store the information on DynamoDB

- lambda_confirm: lambda service to confirm that user has seen the message

- lamba_publish: lamba service to send a notification. This lambda is executed form a DynamoDb stream event.

- lambda_receive: lambda service to receive message and topic or target