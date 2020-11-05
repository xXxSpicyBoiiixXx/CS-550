# Clock Synchronization 

* In centralized system, time is unambigouos. 

* In DS its different, so for example if process A request from the system at the current instance and the Process B asks for the time right after it will not be less. 

* For example, the command make goes off a certain time; This depends on what the time signature is with the clocks 

* This works for simple stuff, but in a DS this could be problematic because you maybe running older stuff with newer code and will most likey crash the program and it would be impossible to debug.

* Is it possible to synchronize all the clokcs in a DS? 

### Physical Clocks

* Clocks is bad working, think of more like a timer. 

* Timer: Usually a precisely machines quartz crystal. When kept under tension, quartz crystalls oscillate at a well-definded frequenct that depends on the kind of crystal, how its cut, and the amount of tension.

* Assoicated with each of these crystals are two things which are counter and holding register. 

* Counter: each oscillation of the crystal decrements the coutner by one, when hitting zero an interrupt will occur and the coutner is reloaded from the hodling register. 

* Holding Register: This is where counters are reloaded. 

* Clock Tick: each of these interupts are called a clock tick. 

* Clock Skew: When a sysytem has n computers, all n crystals will run slightly different rates, cause the software clocks gradually to get our of sync and give different values when read out.

* There are two questions to ask: 

1. How do we synchronize them with real-world clocks?
2. How do we synchronize the clocks with each other? 

* The basis of global time is called "Universal Coordinated Time" or UTC.  

### Clock Synchronization Algorithms

* All clocks are based on some harmonic oscillator. 

* Hardware clokcs in most computers use a crystal oscillator based on quartz, which is also capable of producing a very high, stable frequenct, although not as stable as that of atomic clokcs.

* The goal of clock synchronization algorithms is to keep the deviation between the respective clocks of any two machines in a distributed sysstem, within a specififed bound, knowns as the precison pi. 

* The whole idea of clock synchronization is that we keep clocks precise and accurate. 

* Internal Synchronization: referred to as percision 

* External Synchronization: referred to as accuracy

* Clock Drift: This is happening in clokcs becuase their frequencies are not perfect and affected by external sources such as temperature, thus clocks on different machines will gradually start showing different values for that time. 

* Clock Drift Rate: The difference per unit of time from a perfect reference clock. 

* Maximum Clock Drift Rate: This is where specification of a hardware clock include.

### Network Time Protocol

* A can estimate its offset relative to B as the equation in the text, but if lower than a theta value than that means the clokc must be set backwards. This can't happen because it causes issues like an object file compiled just after the clock change having a time earlier than the source which was modified just before the clock change. 

* Network time Portocol (NTP): This protocol is set up pairwise between servers.

### The Berkeley Algorithm 

* This is a typically an internal clock synchronization algorithm. 

### Clock Synchronization in Wireless Networks 

* The reason these are harder is that the adcantage that more traditional distributed systems is that we acan easily and efficiently deploy time servers. Also, all the machines are contect to each other. 

* Reference Broadcast Synchronization (RBS): A clock synchronization protocol that is quite different from toher proposals. 

* So this protocol does not assume that there is a single node with an accurate account of the actual time available. Instead of aiming to provide all nodes UTC time, it aims at merely internally synchronizing the clocks, just as the Berkeley algorithm does.  

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







