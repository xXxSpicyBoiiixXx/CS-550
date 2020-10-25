# System Architecture

* Deciding on software componenets, their interaction, and their placement leads to an instance of a software architecture, also known as a system architecture. 

### Centralized Organization

* Server: A process implementing a specific service e.g. a file system or database service.

* Client: A process that requests a service from a server by sending it a request and subsequently waiting for the server's reply. 

* Request-Reply Behavior: Client-server interaction.

* Idempotent: An operation that can be repeated multiple times without harm. 

### Multitiered Architectures

* There are distinction into two separate machiens the client and server

1. Client machine containing only the programs implements the user-interface level 

2. Server machine containing the rest, the programs implementing the processing and data level

* In this organization everything is hanled by the server while the client is essentially no more than a dumb terminal, possibly with only a convenient graphical interface. 

* REMEMBER THE 3 LAYERS

1. User interface layer 

2. Processing layer 

3. Data layer

* One solution is to approach for organizing clients and servers is then to distribute these layers across different machines. 

* Two-Tiered Architecture (Physically): A distinction between only two kinds of machines. 

* Having more functionality on the client machine means that a wide range of end users will need to be able to handle that software. 

* Three-Tiered Architecture (Physically): Having some middle componenet that interacts with the server and client where the server will also act as a client somewhat. 

### Decentralized Organizations: Peer-to-Peer Systems

* Vertical Distribution: Distributing processing is equivalent to organizing a client-server appilication as multitiered architecrure and the main characteristic feauture is that it is achieved by placing logically different componenet on different machines. 

* Horizontal Distribution: The distribution of the client and servers that counts, in this type of distribution we get taht a client or server may be physically splut up into logically equivalent parts, but each part is operating on its own share of the complete data set, thus balancing the load. 

* Peer-to-Peer Systems: Modern system architectures that supports horizontal distribution. From a high level persepective, the processes that constitute a peer-to-peer system are all equal. 

* Servant: The interaction between processes is symmetric: each process will act as a client and a server at the same time. 

* Overlay Network: A network in which the nodes are formed by the processes adn the link represent the possible communication channels (which are often realized as TCP connections)

* Two types of overlay networks exists, sturctured and those that are not. 

### Strucuted P2P Systems

* Structued peer-to-peer system the nodes are organized in an overlay that adheres to a specific, deterministic topology: a ring, binary tree, a grid, etc. The unique thing is that they have a semantic-free index. This means that each data item is mainted by the system, is uniquely assoicated with a key, and that key is used as an index. It is common to use a hash function. 
key(data item) = hash(data item's value)

The system as whole is now responsible for storing (key, value) pairs.

* Distirbuted Hash Table (DHT): Each node is assigned an idtenifier from the same set of all possible hash values, and each node is made responsible for storing data assoicated with a specfic subset of keys. 

* The main advantage of having the DHT is that an exisiting node = lookup(key) and can say taht this is specfic system is that this one. 

* Hypercube: N-dimensional cube

* A Chord system, the nodes are logically organized in a ring such that a data item with an m-bit key k is mapped to the node with the smallest (again, also m bit) identifier id less than or equal to k. This node is refered to as the successor of key k and denoted as succ(k). There are shortcuts that each node maintains to another. 

#### Unstrucuted P2P Systems

* An unstructed peer-to-peer system each node maintains an ad hoc list of neighbors.

* In an unstructed peer-to-peer system, when a node joins it often contacts a well-known node to obtain a starting list of other peers in the system. This list can be used to find more peers and ignore others. Generally, a node changes its local lsit almost continuously. 

* Unlike in structured p2p systems, looking up data cannot follow a predetermeined route when lists of neighbors are constructed in an ad hoc fashion. Instead, we resort to searching for data. There are two extremse to look at for searching for specfic data; Flooding and Random Walks.

* Flooding: An issuing node u simply passes a request for a data item to all its neighbors. This can be expenisve, this is why a request has a TTL or time-to-live value, giving the maximum number of hops at a request is allowed to be forwarded. An alternative to setting TTL values, a node can also start a search with an initial TTL value of 1 and then grow if there isn;t enough results returned.

* Random Walks: An issuing node u can simply try to fina data item by asking a randomly chosen neighbor, say v. IF v does not have the data, it forwards the request to one of its randomly chosen neighbors, and so on. This is less expenisve, but it may take longer before a node is reached that has the requested data. Now, a random walk needs to also be stopped, either again using TTL or when a node receives a lookup request, check with the issuer whether forwarding the request to antoher randomly selected neighbor is still needed. 

* To note, structed p2p systems, we use keys for compariosn while the two approaches descrives any compariosn technique would suffice. 

* Between flooding and random walks lie policy-based search methods. This essentailly keeps a log of where information was founded and what nodes to not go to first for information. 

#### Hierarchically Organized P2P Networks 

* Content Delivery Network (CDN): Collaboratiation of nodes that offer resouces to each other.

* Broker: Collects data on resource usage and availiability for a number of nodes that in each other's proximity will allow to quickly select a node iwth sufficient resources. 

* Super Peers: Nodes such as thsoe maintaing an index or acting as a broker. 

* Weak Peer: Nodes that are reffered to as regular peers in P2P networks. 

* In many cases, the association between a weak peer and its super peer is fixed, whenever a weak peer joins the network it attaches to one of the usper peers and reamins attached until it leaves the netowrk. 

#### Hybrid Architectures

* This is where client-server and P2P networks come together. This section effectly take specfic classes of distributed systems in which client-server solution are combined with decentralized architectures. 

#### Edge-Server Systems 

* Edge-server Systems: The systems are deployed on the Internet where servers are placed "at the edge" of the network. This edge is formed by the boundary between enterprise networks and the actual Internet. 

* Origin Server: One edge server acts as the origin server for which all content orginates. 

#### Collaborative Distributed Systems

* Typically hybrid strucutres are deployed in collaborative distributed systems. 

* Free Riding: In most file-sharing systems, a significany fraction of participants merely download files but otherwise contribute close to nothing. 

* Torrent File: Contain the information to download a specfic file, it also contain a link to what is knowns as a tracker, which is a server that is keeping an accurate accoubnt of active nodes that have chunks of the requested file. 
 
* BitTorrent comvines centralized with decentralized solutions. As it turns out, the bottleneck of the system is, not surprisinly, formed by the trackers. In an alternative implementation of BitTorrent, a node also joins a separate strucuted P2P (DHT) to assist in tracking file downloads. The initial tracker for the requested file is looked up in the DHT through a so-called magnet link. 


