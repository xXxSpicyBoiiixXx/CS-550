## Notes 

There are for important goals that should be met before the effort of making a distributed system worth it. 

* Support Resource Sharing 
* Distribution Transparent
* Being Open
* Being Scalable

### Support Resource Sharing 
The main point here to make it east for user to access and share remote resouruces. The purpose of this is that its cheaper because instead of having to buy something super expensive for each user, all users can just use the one expensive resource. It also makes it easier to collaborate and share infromation. 

### Distribution Transparent
This is just hiding the facts lol... Basicallt so the user doesn't does know that the processes and resources are distributed across multiple computers. Essentially making the processes and resources invisible to the user.

There are different "objects" in distributed systems. "Objects" meaning that its either a process or a resource. Below are different forms of transparency in distributed system. 

* Access: Hide difference in data representation and how an object is accessed
* Location: Hide where an object is located 
* Relocation: Hide that an object may be moved to another location while in use
* Migration: Hide that an object may move to another location
* Replication: Hide taht an object is replicated
* Concurrency: Hide that an object may be shared by serveral independent users
* Failure: Hide the failure and recovery of an object

Essentially the above is just hide everything from the user. 

***Access Transparency***
At a high level, we want to hide difference in the machine architectures but also how data is represented through these different machines and operating systems. 

***Location Transparency*** 
Users cannot tell where an object is physically located in the system.

***Relocation Transparency*** 
Users should not be able to tell if something has moved. For example, an entire webstie moving from one data center to another. 

***Migration Transparency*** 
This is not about anything being moved but the support if its mobile. For instance, if I was on the phone with my girlfriend and we were having a conversation, the conversation will continue even though we are moving. 

***Replication Transparency*** 
Hiding the fact taht several copies of a resource exist or taht several processes are operating in some form of lockstep mode so taht onecan take over if another fails. This should go hand in hand with location transparency, since a user should not be able to tell where it's located either. 

***Concurrency Transparency*** 
Using the same resource at the same time without noticing other users in the way of said resource. 

***Failure Transparency*** 
The user or application does not notice that a part of the system failed or even recovered from a failure. 

Sometimes hiding everything from the user is not a good thing either. There are some examples of this in the text. 

***Against Distribution Transparency***
Several researchers have argued that hiding distribution will only lead to further complicating the development of distributed systems, exactly for the reason that full distribution transparency can never be achieved. 

In conclusion, distribution is a nice goal when designing distributed system but there needs to be other considerations. It could be costly to do full transparency. 

### Being Open
An open distributed system is essentially a system that offers components that can easily be used by or integrated into other systems. 

* Interoperability: Basically when two system or components from completely different manufactures can co-exit and work together by relying on each other's servcies by a pre-existing standard. 

* Portability: What extent can an application on distributed system A can be excuted without modification on a different system B but implement the same interfaces as A.

* Extensible: It should be easy to add parts from a different operating system or even a whole ass filing system.

Some Notes on Open Systems: This is an ideal situtaion, but in practice many distributed systems are not as open as this and need a lot of work. One solution is just give the source code and give lots of deatils. This leads to open source projects and is as close as we can get from open systems. 

***Separting Policy and Mechanism***
The need for changing a distributed system is often caused by a component that does not provide the optimal policy for a specific user or application.

In theory, it seems that we need some strict speartion of polict and mechanism. There is a trade off. 

The stricter the separation, the more we need to make sure that we offer the appropriate collection of mechanisms.

### Being Scalable
There are three dimensions of scalability.

* Size Scalability: To respect in th e size (adding more users or resources without noticeable loss of performance)

* Geographical Scalability: Where users and resources can be far apart but the fact that communication delays be significant isn't noticeable.

* Administrative Scalability: Can managed easily even if it spans many independent administrative organizations.

***Size Scalability***
There are some limitiations when we try to add users or resources, these can be that services are centralized in that they are on a single server running on a specific machine in the distributed system. The problem is that you can have a bottleneck with an increase number of requests. 

There are several solution for size scalability problems. There is a nice mathematical solution to showcase it.

***Geographical Scalability*** 
This is difficult to scale as existing distributing systems that were designed for local-area networks is that all of them are based on synchronous communication. This works for LANs but not really for WANs.

Another problem is limited bandwidth in WANs as well as very limited facilites for multipoint communcations unlike thier counterparts LANs.

***Administrative Scalability*** 
This is the hardest one with regards to resource usage, management, and security. Since some policies maybe conflicting. One solution is a computational grid. This can be done to expand the system into another domain. Two things must be considered, the distributed system must protect itself against malicious attacks from the new domain and the new domain has to protect itself from malicious attacks from the distirbuted system. 

The textbook goes over a few scaling techniques such as...

* Scaling Up: Improving the capacity 

* Scaling Out: Expanding the distributed system by deploying more machines; This only come with three techniques which are hiding communication latencies, distribution of work, and replication. 

***Hiding Communication Latencies*** 
This is applicable in the case of geographical scalability. Try to avoid waiting for responses to remote-service request as much as possible. 

***Distribution of work***
This involves taking a component, splitting it into smaller parts, and subsequently spreading those parts across the system. The textbook has some examples. 

***Replication*** 
Replication increases availability, but balance the load between componenets leading to better performance. There 

## Pitfalls 
The following are false assumptions that everyone makes when making a distributed application for the first time...

* The network is reliable 

* The network is secure

* The network is homogenous 

* The topology does not change 

* Latency is zero 

* Bandwidth is infinite 

* Transport cost is zero 

* There is one administrator

Most of these things are discussed in this book relate to these assumptions. Some solution to problems are caused by the fact that one or more assumptions are false. 
