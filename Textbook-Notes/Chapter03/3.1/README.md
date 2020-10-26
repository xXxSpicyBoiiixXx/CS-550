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

* In the example they discussed how multithreading is probably needed. 

* The advantage of multithreading is that it becomes possible to exploit parallelism when executing the program on a multiprocessor or multicore system. 

* Regarding parallelism, each thread is assignmed to a different CPU or core while shared data are stored in share main memory. This is useful in large applications. 

* Interprocess Communication (IPC): In Unix based systems, these mechanisms typically include pipes, message queues, and share memory segments. The major drawback of all IPC mechanisms is that communication often requires relativelt extensive context switching.

* IPC requres kernal intervention, this process will generally first have to switch from user mode to kernel mode. This requires the memory map in the MMU, and flushing the TLB. 

* Thread switching can sometimes be done entirely in user space, although in othere implementations the kernel is aware of threads and schedules them. 

* Threads are also useful for many applications are simply easier to strcutre as a collection copperating threads. 

* Direct Overhead: Consists of the time it takes to do the actual context switch along with the time it takes for the handler to do its work and subsequently switching back to the interrupted task. 

* Indirect Overhead: This is everything else, mainly caused by cache perturbations. 

### Thread Implementation 

* Threads are often provided in the form of a thread package. 

* There are two approaches to implement a thread package, either by user space or kernel. The user space is to constrcut a thread library that executed entirely in the user space while the second option is to have the kernel be aware of thread and schedule them. 

* The user-level has many advantages such as its cheap to create and destroy threads, all thread administration is kept in the user address space, the price of creating thread is primarily determinded by the cost for allocating memory to set up a thread stack. Another advantage is that switch thread context can often be done in just a few instructions. Essentially, only the values of the CPU registers need to be stored and subsequently reloaded with the previouslty stored values of the thread to which it is being switched. 

* There is a major draw back whcih is that user-level threads comes from deploying the many-to-one threading model, where multiple threads are mapped to a single schedulable entity. If a blocking system call is implemented this iwll immediately block the entire process to which the thread belo0ngs, and this also all the other threads in that process. This means user-level threads are notfor large applications. 

* The kernel level has the advanatge known as one-to-one threading model in whcih every thread is schedulable entity. The price is that every thread operation will have to carried out by the kernel, which requires a system call. This means that switching thread context may now become as expensive as switching process contexts. 

* An alternative to this is many-to-many threading model. This is meant for lightweight processes (LWP). A LWP runs in the cotext of a single heavy weight process and there severl LWPs per process. The most important part is that all operations on threads are carried out without intervention of the kernel. When an LWP finds a runnable thread, it switches context to that thread. Meanwhile, other LWPs may be looking for other runnable threads as well. 

* The real beauty of LWP executing the thread need not be informed: the context switch is implemented completely in the user space and appears to the LWP as normal program code. 

* Now in the many-to-many threading model case when a blocking system call, the execution changes from user mode to kernel mode, but still continues 

* There are advanatges to using LWPs in combination with user-level threads. First it is relatively cheap, second it will not suspend the entire process, third there is no need for an application to know about hte LWPs, fourth this can easily be used for multiprocessing and can be hidden entirely from the applications. 

* There is one drawback for LWPs and taht we need to create and destroy them and this cost jsut as much as kernel-level threads. However this is only done occasionaly and fully controled by the OS. 

### Threads in Distributed Systems 

* The most important part is that threads is that it can provide a convenient means of allowing blocking system calls without blokcing the entire process in which the thread is running. 

### Multithreaded Clients 

* In a webpage it comes in peices because different threads are handling different parts of the webpage. 

* When using multithreaded client, connections may be set up to different replicas, allowing data to be transferred in parallel. This lets the page load much faster. 

### Multithreaded Servers 

* This is the main use found in distributed systems. 

* Dispatcher: One thread that reads incoming requests for a file operation. 

* There is three ways to construct a server and their characteristics are given below. 

1. Multithreading: parallelism, blocking system calls 

2. Single=threaded Process: No parallelism, blokcing system calls 

3. Finite-state machine: Parallelism, nonblokcing system calls

* The things threads can offer is to retain the idea of sequential processes that make blokcing system calls and still achieve parallelism. 

* Blocking system call is one that must wait until the action can be compelted. read() is a good example and if no input is ready, it'll just sit there waiting. 









