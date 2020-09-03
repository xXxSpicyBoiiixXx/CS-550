## Notes

A server here is created that makes use of connection-oriented service that is found in the socket library. 
This makes both code of the server and client to transfer data through a connection. 

The "AF_INET" and "SOCK_STREAM" makes it where TCP protocol is used for communication between the server and client. 
Now the use of the TCP protocol is completely hidden from the applications. 

Now this is a formal example of an application-level protocol where the client sends some data and the server will return a response. 
We can see in the code that the client send ***Hello World*** and then the server responds the same but with an asterisk at the end of it. 

Some other things to note about some main function available in this interface....

* socket(): To create an object representing the connection
* accept(): A blocking call to wait for incoming connection requests. Once there is a connection, the call will then return a new socket for a separate connection
* connect(): Sets up a connection to a specified party
* close(): Close an open connection
* send(): Send data over a connection
* recv(): Recieve data over a connection
