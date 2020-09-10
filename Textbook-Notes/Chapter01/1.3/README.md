## Notes 

This chapter deals with various types of distributed systems. There are distinctions between distributed computing systems, distributed informations systems, and pervasive systems. 

### High Perfromance Distirbuted Computing

***Cluster Computing*** consists of the underlying hardware consists of a collection of similar workstations or PCs, closely connected by means of LANs. Also each node is running the same OS. 

***Grid Computing*** consists of being a subgroup of distributed systems that are often constructed as federation of computer systems, which all maybe different within the admin domain, hardware, software, and network technology. 

***Cloud Computing*** consists of have the entire infranstructure outsource for applications. This is essentailly providing dunamical infrasturcture and makes it where what is needed. 

### Cluster Computing 

Cluster computing became popular when the price/performance ratio of personal computers and workstations improved. There is a master node that acts like a middleware for all the slave machines. This shows a problem, as the compute nodes are seen to be highly identical. There is a even more symmetric approach followed in the MOSIX system where it attempts to be a ***single-system image*** of a cluster, where its the ultimate distribution transparency by appearing to be a single computer. In traditonal cluster computing, the computers are largely the same, having the same operating system, all while being on the same netowrk. 

### Grid Computing 
There are no assumpitoins made of the hardware, operating systems, networks, domains,etc. This results in virtual organization. There seems to be architectural issues as different orginzations try and use the same resources. There is a basis that was formed architecturely first, which consisted of four layers. 

***Fabric Layer*** - The lowest level that provides interfaces to local resources at a specific site. 

The next two laters are on the same row. 

***Connectivity Layer*** - Consist of communication protocol for supporting grid transactions that span the usage of multiple resources. 

***Resource Layer*** - responsible for managing a single resource. It uses the functions provided by the connectivity layer and calls directly the interfaces made available by the fabric layer. 

***Collective Layer*** - Deals with handling access to multiple resources and typically consists of services for resouruce discovery, allocation and scheduling of tasks onto multiple resources, data replication, and so on. 

***Application Layer*** - The applications that operate within a virtual organziation and which make use of the grid computing environment. 

The collective, connectivity, and resource layers are the hearrt of grid middleware layer. 

Its important to note that there was a gradual shift towards a ***service-oriented architecture*** which sites offer access to the various layers through a collection of web services. 

### Cloud Computing 
All this lead to the cocpet of utility computing by which a customer could upload tasks to a data center and be charged on a per-resource basis. Utility computing formed the basis for what is now called cloud computing. There is a section about cloud computer not nessarily cheaper but on a case by case basis. This is a question about being cheaper requires a lot of detailed information and carful planning of exactly what to migrate. 

Cloud computing has multiple layers as well. 

***Hardware Layer*** - The lowest layer is formed with the necessary hardware. 

***Infrastructure Layer*** -  Most important layer forming the backbone for the most cloud computing platforms. 

***Platform Layer*** - This provides to a cloud computing customer what an OS proides to an application developers. Namely the means to easily develop and deploy applications that need to run in a cloud. In this layer they use something called buckets which is like a directory within the cloud. 

***Application Layer*** - Applications run in this later and offer users for futher customization. 

There are three different types of services:

***IaaS (Infrastrucutre-as-a-Service)*** covering the hardware and infrasturucture
***PaaS (Platform-as-a-Service)*** covering the platform layer
***SaaS (Software-as-a-Service)*** in which their application are covered

### Distributed Transaction Processsing 
The transaction adhere to the so-called ACID properties: 

* Atomic: To the outside world, the transaction happs indivisibily
* Consistent: The transaction does not violate system invariants 
* Isolated: Concurrent transactions do not interfere with each other 
* Durable: Once a transaction commits, the changes are permanent 

In distributed system, transactions are often constructed as a number of subtransactions, jointly forming a *nested transactions* 

Nested transactions are important in distributed systems, for it is a natural way of distributing a transaction across multiple systems. 

### Enterprise Application Integration 
This is where RPC are taking contorl in process and RMI is the same as RPC except it works on objects instead of functions. The tight coupling is often experiences as serious drawback which lead to message oriented middleware or MOM. 

### Pervasive Systems 
This is basically the system are intended to naturally bled into our environment. This is equiped with many sensors that pick up various aspects of a user's behavior. There was mtriad of actuators to provide infromation and feedback, often even purposefully aiming to steer behavior. 

There are three different types of pervasive systems. 

* Ubiquitous Computing Systems 
* Mobile Systems 
* Sensor Networks 

### Ubiquitous Computing Systems
This goes one step further from pervasive system in the since that the system will be pervasive and continuouosly present. 

The following are the core requirements for a ubiquitious computing system. 

* Distribution: Devices are networked, distributed, and accessible in a transparent manner
* Interaction: Interaction between users and devices is highly unobtrusive
* Context Awareness: The system is aware of a user's context in order to optimize interaction 
* Autonomy: Devices operate autonomously without human intervention, and thus are highly self-managed
* Intelligence: The system as a whole can handle a wide range of dynamic actions and interactions. 

### Mobile Computing Systems
All aspects so far apply to mobile computing systems. 

These are obviously mobile as in can move. The trick is not to attempt to set up a communciation path from the source to the destination but to rely on two things. 

* The first is using speical flooding-based techniques will allow a message to gradually spread throught a part of the network, to eventually reach the destination. 

* The second is distrpution-tolerant netowrk, there is an intermediate node store a received message until it encounters another node to which it can pass it along to. 

### Sensor Networks
Many solutions for sensor netowrks are returned into pervasive applications. This usually consists of tens to hunders or thouses of relativelt small nodes, all with one more senesing devices. This requires a lot of energy and there is some solutions that are not favorable. 





