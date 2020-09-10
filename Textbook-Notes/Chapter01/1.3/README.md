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

***Infrastructure Layer*** - 




### Distributed Transaction Processsing 
The transaction adhere to the so-called ACID properties: 

* Atomic: To the outside world, the transaction happs indivisibily
* Consistent: The transaction does not violate system invariants 
* Isolated: Concurrent transactions do not interfere with each other 
* Durable: Once a transaction commits, the changes are permanent 
