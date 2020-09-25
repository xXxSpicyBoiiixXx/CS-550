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

* Class of distributed systems have adopted an architecutre in which there is a strong separation between processing and coordination. 

* Coordination: Encompasses the communication and cooperation between processes. 

* Direct Coordination: When processes are temporally and referentially coupled, coordination takes place in a direct way. 

* Mailbox Coordination: Processes are temporally decoupled, but refernetailly coupled. There is no need for two things to communicate to have communication, now its like a mailbox. Just drop it off kind of thing like a shared system. This will hold all messages that need to be exchnaged. 

* In an event-based coordination type of system, this is decoupled so the processes do not know each other explicitly. The only thing a process can do is publish a notificitoin. e.g. event-based coordinaton. In and ideal event-based coordination model, a publishing notificaiton will be delivered eactly to thosse proesses that have subscribed to it. 

* Both these types, led to a sahre data space. The idea is taht the communciation is wokring through tuples, or structured data records consisting of number of fields, like row in a database table. These tuples can carry any type of data and a process must provide a search pattern to match a tuple and the tuples that match will be outputted. 

* Shared data spaces are often combined iwth event-based coordination. Regardless in both of these cases we are going to face a publish-subscribe architecture. The key feature here is no explicit reference to each other. 

* Event Bus are abstarction of the mechnaism by which publishers and subscribers are matched. 

### Linda Tuple Spaces (1980s)

* There is a program modeling called "Linda" which is knows as a tuple space. This supports three oprtations: 
- in(t): removing a tuple that matches the template t
- rd(t): geta  copy of a tuple taht matches the template t 
- out(t): add the tuple t to the tuple space

* Formally speaking, a tuple space is always modeled as a multiset, but in and rd are considered blocking operations. Meaning that they will be blocked until a matching tuple is found or becomes avaliable. 

* An important aspect of publish-subscribe systems is that communications takes place by descrbing the events that a subscriber is interest in. This makes naming very important. 


