# Chapter 02 Notes 

The notes here are broken up into sections on what I deemed most important from the textbook and written in a way that makes sense to me to review for later. Below are terms in this chapter that helped me understand it better.

## Some Vocabulary

* Software Architecture: Logical organziation of a distributed system into software components.

* Architectural Style: Formulated in terms of components. Basically the way they are connected, data exchanged, and finally how they are jointly configured into a system.

* Componenet: A modular unit with well-defined requried interfaces that is replaceable in the envrionment. 

* Connector: A mechanism that mediates communication, coordination, or cooperation with componenets. 

* Layer Fasion: Componenets are organized in layers.

* Downcall: A component at a lower-level layer expect a response.

* Upcall: A component at a high-level layer expect a response. 

* Communication Protocol: The rules that parties will have to follow in order to exchange information.

* Persistent: Even if no application is running, data will be stored somewhere for next use. 

* Object's State: Natural of encapsulating data.

* Object's Methods: Operatins that can be performed on that data.

* Proxy: Cleint binds to a distributed obejct, an implementation of the object's interface.

* Skelton: Server-Side stub.

* Remote Objects: Objects is that their state is not distributed, it resides at a single machine. Only the interfaces implemented by the object are made avaliable on other machiens. 

* Handle: When an operating system signals the occurence of an event, to which end it calls a user-defined operation for which an application had previously passed a reference. 

* Encapsulation: The service as a whole is realized as self-constaint entity, alrhough it can possibly make use of other serviecs. 

* Service-Oriented Architectures (SOA): A distributed application or system is constructed as a composition of many different services. 

* Coordination: Encompasses the communication and cooperation between processes.

* Direct Coordination: When processes are temoorally and coupled, this makes coordination in a direct way.

* Mailbox Coordination: When processes are temporally decoupled but referentially coupled.

* Event-Based Coordination: Combination of referentially decouples and temporally coupled systems.

* Publish a Notificiation: Describing the ocurrence of an event.

* Shared data space: Combination of referentially and temporally decoupled processes. 

* Tuples: Structured data records consisting of a number of fields, similar to a row in a database table. 

* Publish-Subscrribe: An architecure with the key characteristc taht processes have no explicity reference to each other. 

* Event Bus: When publishers and subscribers are matched.

* Attributes: Series of description for events.

* Published: When it is made avalilable for other processes to read. 

* Subscription: Contains a description of the event 

* Topic-Based Publish-Subscribe Systems: Contains a pair with (attribute, value)

* Content-Based Publish-Subscribe Systems: Contains a pair with (attribute, range)


