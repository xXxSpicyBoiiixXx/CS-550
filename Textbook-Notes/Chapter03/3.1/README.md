# Threads

* Threads are used to consitute better performance with finer granularity. 

### Introduction to Threads

* Virtual Processors: This is what an operating system creates, each running a different program. 

* Process Table: This table is what the operating system creates to keep track of all the virtual processors. This typically has entries to store CPU, register values, memory maps, open files, accounting information, privilges, etc. 

* Process Context: The above enetries are knowas process context.

* Processor Context: This is where you'll view a process contect as the software analog. 

* Process: This is definded as a program in execution, a program that is currently being executed on one of the OS's virtual processors. 

* The OS takes great care to ensure that independent processes cannot maliciously or inadvertently affect the correctness of each other's behavior. So this means that the fact that multiple processes may be concurrently shareing the same CPU and other hardware resources is made transparent. 

* This transparenct comes at a price. Like everytime a process is created then the OS must create a complete independent address space. 

* Like a process, a thread executes its own piece of code, idependently from other threads. But the difference is that their is not attempts made to achieve a high degree of concurrency transparency if this would result in performance degradation. 

* This means the thread system only keeps minimum informations to allow a CPU to be shared by several threads. 

* Thread Context: Consists of nothing more than the processor context, along with some other infromation for thread management. This leads that a processor context is contained in a thread context, whcih a thread context is contain in a process context. 

### Thread Usage in Nondistirbuted Systems

* In a single-threaded process, whenever a blocking system call is executed, the process as a whole is blocked. 


