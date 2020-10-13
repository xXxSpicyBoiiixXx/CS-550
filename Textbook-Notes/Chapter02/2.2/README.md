# Notes

* There are two important types of organization of a distributed system or application to middleware they are wrappers and interceptors. 

* They both target different issues, but have the same goal of middleware to achieve openness.

* To achieve ultimate opennes when we can compos middleware at runtime. 

### Wrappers 

* Wrapper (Adapter): A special component that offers an interface acceptalbe to a client application, which function are transformed into those available at the component. This solve the problem on incompatible interfaces. 

* Object Adapter: A component that allows application to invoke remote objects.

* Using wrappes does not scale very well due othe that if there was N applications then in theroy you would need O(N^2) wrappers. Thats exponentially large which makes scaling hard. 

* Reducing the number of wrappers is done through the middleware, and this is done through something called the broker. 

* Broker: A centralized component that handles all the access between different applications. 

* Message Broker: Applications sends requests to the broker containing information on what they need. 

* With brokers, instead of O(N^2) we get that 2N = O(N), which is so much better. 

### Interceptors

* Interceptor: A software constrcut taht will break the usual flow of control and allow otehr code to be executed. 

* Interceptros are the primary means for adpating middleware to the specific needs of an application. 

* We should consider interception as supported in many object-based distriburted systems. Such remote-object invocation is carried out in 3 steps that are below... 

1. Object A is offered a local interface that is exactly the same as the interface offered by by Object B. A calls the method available in that interface. 

2. The call by A is transformed into a generic object invoacation, made possible through a general object-invocation interface offered by the middleware at the machine where A resides. 

3. The generic object invocation is transformed into a message that is sent through the transport-level network interface as offered by A's local operating system. 

* Request-Level Interceptor: calls each of the replicas. THe beauty of this is that object A need not to be aware of replication of B, but also the object middleware need not to have special componenets that deal with this replicated call. Only the request-level interceptor, which may be added to the middleware needs to know about B's replication. 

* Message-Level Interceptor: Assist in transferring the invocation to the target object. The middleware does not need to be aware of this fragmentation, the lower-level interceprot will transparently handle the rest of the communcation with the local OS. 

### Modifiable Middleware

* Wrappers and interceptors offer the avaliablilty ot extend and adapt the middleware. 

* One of the most popular approaches towards modifiable middleware is that of dynamically constructing middleware from components. 

* A system may either be configured statically at design time, or dynamically at runtime. 

* To confiure it dynamically at runtime requires support for late binding, a technique that has been successfully applied in programming languages envrionements, but also for operating system where mdoules can be loaded and unloaded at will. 

* Essentially in order to accommodate dynamic chnages to the software that makes up middleware, we need at least basic support to laod and unload compnents at runtime. 

 
