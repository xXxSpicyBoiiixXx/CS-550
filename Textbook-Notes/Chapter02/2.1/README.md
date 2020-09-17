# Notes

The organization of distirbuted systems is mostly about the software componenets that consititute the system. 

The goal of distributed system is to separate applications from underlying paltforms by giving a middleware layer. 

### Layered Architectures 

* Components are organized in a layered fasion. 

* Pure Layered Organization: This is where only downcalls to the next lower layer are made. This is common in network communication.

* Mixed Layered Organization: This is the most common found where libraries and such are called. Look at 2.1(b) for more of description image. 

* Layered Organization with Upcalls: This is convient to have a lower layer do an upcall to its next higher layer. 

* Layered Communication Sevices: Each layer implements one or several communication services allowing data to be sent form a destination to one or several targets. Some important concepts are that each layer offers an interface for functions to be called and in communication there is a protocol where set rules are established to exchange information. 

### Object-Based and Service-Oriented Architectures 

* Object-based Architetures: Each object corresponds with a component, and tehy are all connected through a procedure call mechnaism. In distributed systems, a procedure call can also take place over a network, this means that the calling object need not be executed on the same machines as the called object. 

* Service-Oriented Atchitectures (SOAs):  A distrivuted application can or system is a essentially constructed as a composition of many different services.  To take note, all these services does not have to be of the same administrative organization. 

* In cloud computing, the storage system is essentailly one logically one large single unit with an intergace avaliable to customers. 

### Resource-Based Architectures

* Representational State Transfer (REST): Resources may be added or removed by applications, and likewise can be retrieved or modified. This is widely apodated by the Web. 

There are four characteristics of what are known as RESTful architectures. 

* Resources are identified through a single naming scheme 
* All services offer the same interface consisiting of the operations suhc as PUT, GET, DELETE, and POST. 
* Messages sent to or from a service are fully self-described 
* After executing an operations at a service, that component forgets everything about the caller

* Stateless Execution: After executing an operation at a service, that component forgets everthing about the caller. 

* The RESTful atchitecutre has become popular because of its simplicity. 

### Publish-Subscribe Architectures 

* 





