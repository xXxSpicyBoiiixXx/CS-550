# Programming Assignment 2

Make sure you type 

"make clean" first 

You can compile all the java files as entering 

make

then tpye "make run" you will be prompted with certain options.

Now this assignment I made into more flexible as a uncentralized filing system as I am not compelte with it and will continue to work
on it for Programming Assignment number 3. Due to time contrastraits I was unable to test all 64 clients but this should work and I will give it a try on the next assignemnt. 

## DHT_FileTransfer.java

This will grab all the peer's IP addresses from the network config file and who will also be replication servers if we configure it that way. After that we have a sinle thread on port 11000; all peers will be connecting to this port. This is also where the hashTable, replicatedHashTable, networkMap and replicatedNodes strucutres will be stored. The DHT will be in the network itself instead of singular place. The hashTable will be simutaneould storing all the key/value pair of it own peer. I used concurrent hash maps becuase it is thread safe for multiple threads espiecally concurrently. 

## Network.config and NetworkUtility.java

The network config file is where we can set up our scenario or clients and replica servers. I have not tested it out due to time constraits but will do on the 3rd programing assignment. The NetworkUtility file gives the IP address of the current peer. This is to insure the peer is in the hashtable. 

## IPAddressValidator.java

This is to confirm our IP 

## FileCheckSumMD5.java

This gives the MD5 hash of a file with a checksum.

## Request.java and Response.java

Used for requests from other nodes. This inluces the 4 options the user will be given which are register, lookup, unregister, print logs, and exit. The response.java sends a response over to the requested peer.

## PeerClient.java

This give the 5 options the yser will see. It can connect to other peers using socket connection on port 11000.

## Server.java

This will take what the client says and do that accordingly. 

## ReplicationService.java 

This will deal with registeration and replication of peers. As well as unregistered when a peer will be deactivated. 

## LogUtility.java

This will keep the logs of each peer.
