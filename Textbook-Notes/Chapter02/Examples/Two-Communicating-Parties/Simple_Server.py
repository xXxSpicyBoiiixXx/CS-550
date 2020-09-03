# FILE: Simple_Server.py
# USAGE: --
# DESCRIPTION: A simple server coded in python.
# OPTIONS: --
# REQUIREMENTS: --
# BUGS: --
# AUTHOR: xXxSpicyBoiiixXx
# ORGANIZATION: --
# VERSION: 1.0
# CREATED: 09/03/2020
# REVISION: --

from socket import *

s = socket(AF_INET, SOCK_STREAM)

(conn,addr) = s.accept()      # returns new socket and addr client

while True:                   # forever
    data = conn.recv(1024)    # receive data from client
    if not data: break        # stop if client is stopped
    conn.send(str(data)+"*")  # return sent data plus an "*"
conn.close()                  # close the connection