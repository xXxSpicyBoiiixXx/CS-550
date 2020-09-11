# Notes

The organization of distirbuted systems is mostly about the software componenets that consititute the system. 

The goal of distributed system is to separate applications from underlying paltforms by giving a middleware layer. 

### Layered Architectures 

* Components are organized in a layered fasion. 

* Pure Layered Organization: This is where only downcalls to the next lower layer are made. This is common in network communication.

* Mixed Layered Organization: This is the most common found where libraries and such are called. Look at 2.1(b) for more of description image. 

* Layered Organization with Upcalls: This is convient to have a lower layer do an upcall to its next higher layer. 

* Layered Communication Sevices: 



