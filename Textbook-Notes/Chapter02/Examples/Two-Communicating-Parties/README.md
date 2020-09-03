## Notes

A server here is created that makes use of connection-oriented service that is found in the socket library. 
This makes both code of the server and client to transfer data through a connection. 

The "AF_INET" and "SOCK_STREAM" makes it where TCP protocol is used for communication between the server and client. 
Now the use of the TCP protocol is completely hidden from the applications. 

Now this is a formal example of an application-level protocol where the client sends some data and the server will return a response. 
We can see in the code that the client send ***Hello World*** and then the server responds the same but with an asterisk at the end of it. 
