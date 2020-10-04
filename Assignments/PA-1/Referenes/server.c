/*
# FILE: server.c
# USAGE: make sure to complie using gcc server.c -lpthread -o server and then you can excute the file as ./sever
# DESCRIPTION: If any client connects to the server retrieves a file with the command "Get FILENAME", the server will retrieve the file. If the file does not exist then the server will notify the client with an error message.
# OPTIONS: --
# REQUIREMENTS: --
# BUGS: --
# AUTHOR: xXxSpicyBoiiixXx (Md Ali)
# ORGANIZATION: --
# VERSION: 1.0
# CREATED: 09/28/2020
REVISION: --
*/

#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/sendfile.h>
#include <fcntl.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdbool.h>
#include <pthread.h>

#define SERVER_PORT 8080

void* ConnectionHandler(void* socket_desc);
char* GetFilenameFromRequest(char* request);

bool SendFileOverSocket(int socket_desc, char* file_name);
  
// Driver function 
int main(int argc, char **argv)
{
    //Intilizing
    int socket_desc,
        socket_client,
        *new_sock;
    
    int c = sizeof(struct sockaddr_in);
    
    stuct sockaddr_i server,
                     client;
    
    //Creating the socket
    socket_desc = socket(AF_INET, SOCK_STREAM, 0);
    
    if(socket_desc == -1)
    {
        perror("Could not create socket");
        return 1;
    }
    
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;
    server.sin_port = htons(SERVER_PORT);
    
    if(bind(socket_desc, (struct sockaddr *)&server, sizeof(server)) < 0)
    {
        perror("Bind failed");
        return 1;
    }
    
    listen(socket_desc, 3);
    
    while(socket_client = accept(socket_desc, (struct sockaddr *)&client, (socklen_t*)&c))
    {
        pthread_t sniffer_thread;
        new_sock = malloc(1);
        *new_sock = socket_client;
        pthread_create(&sniffer_thread, NULL, ConnectionHandler, (void*) new_sock);
        pthread_join(sniffer_thread, NULL)
    }
    
    if (socket_client < 0)
    {
        perror("Accept failed.");
        return 1;
    }
    
    return 0;
    
}

