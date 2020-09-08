# Chapter 01 Notes 

The notes here are broken up into sections on what I deemed most important from the textbook and written in a way that makes sense to me to review for later. Below are terms in this chapter that helped me understand it better.

## Some Vocabulary

* Plug Computers: Small computers that can be plugged directly into an outlet an offer near-desktop performance.

* Node: A computing element. Can be either hardware device or a software process.

* Open Group: Any node is allowed to join the distributed system. Meaning it can send messages to any node in the system.

* Closed Group: Only the members of that group can communicate with each other, there is a spiecal mechnaism needed to let a node join or leave this type of group. 

* Structured Overlay Network: Each node has a well-definded set of neighbors with who they communicate with. e.g. tree or logical ring.

* Unstructed Overlay Network: Each node has a number of referecnes to randomly selected other nodes. 

* Distribution Transparency: The user should not be able to tell which computer a process is executing, where the data is stored, or anything else. 

* Middleware: Often organized with distributed systems as a separate layer of software placed on top of the operating systems of computers that are in the system.

* Groupware: Software for collaborative editing, teleconferenceing, etc. 

* Access Transparency: Hiding differences in data representation and the way that objects can accessed.

* Location Transparency: Users cannot tell where an object is phyiscally located in the system.

* Reloacation Transparency: While relocating the a data center or other means, the user does not know this. 

* MIgration Transparency: When a distributed system supports mobility of processes and resources initated by users, without affecting ongoing operations. 

* Replication Transparency: Hiding the fact that several copies of a resource exits, or several processes are operating in some form of lockstep mode so that one can take over when another fails. 

* Concurrency Transparency: Sharing same resources, at the same time without knowing someone else is also using it. 

* Failure Transparency: The user or application does not notice that some piece of the system fails to work properly, and the system recoveres from the failure. 

* Open Distributed System: System that offers components that can easily be used by, or integrated into other systems. 

* Interoperability: The extent by which two implementation of sysytems or components from different manufactures can co-exits and work together by relying on each other's services as sadi by a common standard. 

* Portability: To what extent an application developed for a distributed system A can be execute without modification on a different distrivuted system B that implements the same interfaces as A.

* Extensible: It should be easy to add parts and run on a different operating system, or replace an entire file systems.

* Size Scalability: Respect to size, for instance, adding more users and resources to the system without any noticeable loss of performance. 

* Geographical Scalability: Where users and resources may be super far apare, but the communciation delays are not significant or not even noticeable. 

* Administrative Scalabliity: A system that is easily managed even if it spans many independent administrative organizations. 

* Synchoronus Communication: A party requesting service, generally a client, block until a reply is sent back from the server implementing the service.

* Computational Grid: Global distributed systems as a federation of local distributed system, allowing a program running on a computer at orgnization A to directly access resources at orgnization B.

* Scaling Up: Improving capacity, like hardware or network modules. 

* Scaling Out: Expanding the distributed system by deploying more machines. 

* Asynchrnonous Communication: When a reply come in, the application is interruped and a special handler is called to complete the previous issued request. 

* Cluster Computing: Consists of collection of similar workstations, closely connected by means of a high-speed LAN. 

* Grid Computing: Subgroup consists of distributed system that are often constructed as a federation of computer systems, where each system may fall under a different administrative domain, and may be very different when it comes to hardware, software, and deployed network technology. 

* Cloud Computing: Providing the facilities to dynamically constract an infrastructure and compose what is needed from avaliable services. 

* Multiprocessor Machiens: Multiple CPUs are organized in such a way that they all have access to the same physical memory. 

* Multicomputer System: Several computer are connected through a netwrk and there no sharing of main memory. 

* DSM (Distributed Shared-Memory Multicomputers) System: Allows a processor to address a memory location at another computer as if it were local memory. 

* Single-System Image: In regards of a cluster, that to a process a cluster computer offers the ultimate distribution transparency by appearing to be a single computer.

* Virtual Organization: The processes belonging to the same virtual organization have access rights to the resources that are provided to that organization. 

* Utility Computing: A customer can upload tasks to a data center and be charged one a per-resource basis. This is privided the basis for cloud computing. 

* Infrastructure-as-a-Service (IaaS): Convering the hardware and infrasturcture layer

* Platform-as-a-Service (PaaS): Convering the platform layer

* Software-as-a-Service (SaaS): Covering the applications

* Distributed Transaction: Single larger request that consists of a number of requests.

* Nested Transactions: Transactions constructed as a number of subtransactions. 

* Remote Method Invocation (RMI): The same as RPC, except that it operates on objects instead of functions. 

* Message Oriented Middleware (MOM): Applications send messages to logical contact points, often described by means of a subject. 

* Pervasive Systems: Intended to naturally blend into out environemnt. 

* Implicit Action: That is not primarily aimed to interact with a computerized system but which such a system understands as input. 

* Shared Data Spaces: Processes are decoupled in time and space. 

* MANET: Mobile ad hoc network.

* Disruption-Tolerant Netowrks: Networks in which connectivity between two nodes can simply not be guranteed. 
