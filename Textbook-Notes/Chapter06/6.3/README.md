# Mutal Exclusion

Distributed muytal exlusion algorithms can be classified into two different categories......

* Token-Based Solutions: Mutal exclusion is acieved by passing a special message betwreen the processes, known as a token.

1. There is only one token avalilble and who ever has that token  is allowed to access the shraed resource. 
2. Depending on how the processes are organized they can fairly easilt ensure that every process will get a change at accesssing the resource. This avoid starvation
3. Deadlocks by whcih serveral process are indefinitely waiting for eahc other to proceed, can easitly be avoided contributing to their simplcity. 

* There is a drawback, is that when a token is lost, an intricate distributed procedure needs to be started to ensure that a new token is created, but above all, there is also the only one token

* THIS IS WHAT MOST PROCESSES FOLLOW::::::::Permission-Based Approach: A process wanting to access the resource first requires the permission from toher processes. There are many different ways towards granting suhc a permission and in the sections that follow we will consider a few of them. 

### A Centralized Algorithm

Kinda like asking permission from a coordinator.....

It is easst to see that the algorithm gurantees mutual exclusion: the coordinator lets only one process at a time access the resource.

* There are some shortcomings. The coordinator is a single point of failure, so if it crashes the entire system may go down. If processes noramlly block after making a request they cannot distinguish a dead coordinator from a "permission denied" since in both cases no message comes back. In addtion, in a large system, a single coordinator can become a performance bottleneck. 

* Nevertheless, the benefits coming from its simplicity outweigh in many cases the potential drawbacks.

### A Distributed Algortihm - A different appraoch that uses ackowledgements

### A Token-Ring Algorithm - A different appraoch using a ring 

* This will be given one token. 

* There is no starvation 

### Decentralized Algorithm 

* Some other stuff I don't understand 







