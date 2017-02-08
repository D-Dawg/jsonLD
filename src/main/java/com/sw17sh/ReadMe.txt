
How to run the project.

1. Run Server by running the main() of JsonLDServer
2. Run Client by running the main() of JsonLDClient
______________________


Notes:

Client and Server containing more documentation in form of javadoc
_____________________________


Structure of project:

- client
    contains implementation of client
    JsonLDClient contains main to run it
- json
    since it is a prototypical implementation,
    some of these files are used for communication,
    or to replace some values inside them
    --------
    - EventDB.json ia the exemplary DataBase of our server containing all Events (copy of same event different name)
    - SearchActionSpecification.json is used by server as the initial specification of the communication
    - SearchActionRequest.json is used by client, it sends back as a request with values adjusted
    - SearActionResponse.json is used by the server to send back the results of the search in DB containing a list of matching events, containing each a potential BuyAction
    - BuyActionRequest.json is used by client to make the Buy request, values here are used as in file (client is not yet implemented to adjust these values)
    - BuyActionResponse.json is used by server to send back the confirmation
    - Ticket.json is used by server to attach to successful BuyAction
- model
    Jackson is used for mapping json to java classes.
    Folder containing all models used to map json communication
- server
    contains implementation of server
    JsonLDServer contains main to run it
- util
    Helper class, with methods for client and server

