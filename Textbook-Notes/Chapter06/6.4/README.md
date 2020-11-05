# Election Algorithms 

In a Distributed Algorithms reuire one process to act as coordinator, initiator, or otherwise perform some special role.

If all processe are exactly the same, with no distingusing characteristics, there now way yo select one of them to be speical. 

### The Bully Algorithm

* At any moment, process can get an ELECTION message from one of its lower-numbered collegues. When such a message arrives, the receiver sends an OK message back to the sender to indicate that he is alive and will take over. 



