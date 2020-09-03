## Notes 

ALWAYS REMEMBER THE FOLLOWING DEFINITION
***Distributed System - A distributed system is a collection of autonomous computing elements that appears to its users as a single coherent system.***

### Characteristic 1: Collection of autonoumous computing elements
Basically just a bunch of nodes acting as one. There are set up as two different overlay netowrks, either structured or unstructured. In any case, there should always be a connection path between two nodes. 

### Characteristic 2: Single coherent system
In the name, it should appear as one system. Now its usually opted to appear as one system instead of actually being one system. 

### Middleware and distributed systems 
Usually, distributed systems often have a layer of software on top of the operating system as a middleware layer. Middleware is the same to a distributed system as what an operating system is to a computer, a manager of resources offering its applications to efficiently share and deploy those resouruces across a network. The difference between the operating-system equibaleny is middleware is offered in networked environments. Typical middleware services...

* ***Communication***: Remote Procedure Call (RPC), this allows an application to use a function and execute it on a remote comupter like it was their own. You just need to specify the function header.

* ***Transactions***: This is like an all-or-nothing scenario. We just need to only specify the remote services we need, and with whatever standadized protocol is set, the middleware makes sure that everything is used or none at all. 

* ***Service Composition***: Basically smashing together exisiting programs to make a new application. 

* ***Reliability***: This just gurantees a certain service will work. There is an example in the text about "The Horus toolkit"
