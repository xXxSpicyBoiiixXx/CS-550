# Notes

The organization of distirbuted systems is mostly about the software componenets that consititute the system. 

The goal of distributed system is to separate applications from underlying paltforms by giving a middleware layer. 

### Layered Architectures 

* Components are organized in a layered fasion. 

* Pure Layered Organization: This is where only downcalls to the next lower layer are made. This is common in network communication.

* Mixed Layered Organization: This is the most common found where libraries and such are called. Look at 2.1(b) for more of description image. 

* Layered Organization with Upcalls: This is convient to have a lower layer do an upcall to its next higher layer. 

* Layered Communication Sevices: Each layer implements one or several communication services allowing data to be sent form a destination to one or several targets. Some important concepts are that each layer offers an interface for functions to be called and in communication there is a protocol where set rules are established to exchange information. 

* Object-based Architetures: Each object corresponds with a component, and tehy are all connected through a procedure call mechnaism. In distributed systems, a procedure call can also take place over a network, this means that the calling object need not be executed on the same machines as the called object. 







