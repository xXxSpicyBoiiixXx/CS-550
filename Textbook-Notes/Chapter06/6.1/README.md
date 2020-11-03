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





