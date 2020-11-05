# Logical Clocks

Speaking of clocks as logical clocks. 

### Lamport's Logical Clocks

* Lmaport defined a relation called happens-before. a -> b os read as "even a happens before event b"

* The happens-before relation can be observed directly in two situations: 

1. If a and b are events in the same process, and a occurs before b, then a -> b is true.
2. If a is the event of a message being sent by one process, and b is the event of the message being received by another process, then a -> b is also true. A message cannot be received before it is sent, or even at the same time it is sent since it ake a finiite nonzero amount of time to arrive .

* Events are said to be concurrent, meaning htat nothing can be said (or need be said) about when the events happened or which event happened first.

* Event Counters: Count events

* Total-ordered multicast: a multicast operation by which all messages are delivered in the same order to each receiver. 

* State Machine Replication: The same transition in the same finite state machine. 

* Critical Section: A section of code that can be executed by at most one process at a time. 

### Vector Clocks 

* Lamport's logical clocks lead to a situation where all events in a distributed system are totally ordered with the property that if event a happened before event b, then a will also be postitioned in that ordering before b that is. 

* The problem with lamport clock that they do not cpature causality. In practice, causlity is is caputure in vector clocks. 

* The problem then boils down to keeping track of causal histories. 

* It is important to note that causalordrered multicasting is weaker than total-ordered multicasting .


