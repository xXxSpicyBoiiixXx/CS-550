# System Architecture

* Deciding on software componenets, their interaction, and their placement leads to an instance of a software architecture, also known as a system architecture. 

### Centralized Organization

* Server: A process implementing a specific service e.g. a file system or database service.

* Client: A process that requests a service from a server by sending it a request and subsequently waiting for the server's reply. 

* Request-Reply Behavior: Client-server interaction.

* Idempotent: An operation that can be repeated multiple times without harm. 

### Multitiered Architectures

* There are distinction into two separate machiens the client and server

1. Client machine containing only the programs implements the user-interface level 

2. Server machine containing the rest, the programs implementing the processing and data level

* In this organization everything is hanled by the server while the client is essentially no more than a dumb terminal, possibly with only a convenient graphical interface. 

* REMEMBER THE 3 LAYERS

1. User interface layer 

2. Processing layer 

3. Data layer
