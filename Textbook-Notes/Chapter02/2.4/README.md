# Exampel Architecture

### The Network File system 

* Network File System (NFS): The basic idea is that each file server provides a standardized view of its local file system. In other words, it should not matter how that local file system implemented; each NFS server supports the same model. 

* Remote File Service: Cients are offered transparent access to a file system that is managed by a remote server. 

* Remote Access Model: The client is offered only an interface containing various file operations, but server is responsible for implementing those operations.

* Upload/Download Model: A client access a file locally after having downloaded it from the server. When the client is finihsed with the file, it is uploaded back to the server again sot htat it can be used by another client. 

* Virtual File System (VFS): Is the de facto standard for interfacing to different (distributed) file systems. 

* NFS Client: This takes care of handling access to files stored at a remote server. 

* Remote Procedure Calls (RPCs): All client-server communiciation is done through this. An RPC is essentially a standardized way to let a client on machine A make an ordinary call to a procedure that is implemented on antoher machine B.

* NFS Server: Responsible for handling incoming client requests. The RPC componet at the server converts incoming requests to regualar VFS file operations that are subsequently passsed to the VFS layer. 

* Stateless: Once a request has been completed, the server will forget about hte client and the operation it had requested. 

* Mount Point: Essentially a place in a naming system that connect to a another name system.

### The Web 

* The architecutre of the Web-based distributed systems is not fundamentally different from other distributed systems. 

* It used to be like a simple server-client model but now there is a lot more threading occuring. 
