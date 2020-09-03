# FILE: Simple_Client.py
# USAGE: --
# DESCRIPTION: A simple client coded in python.
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

s.connect((HOST,PORT))      # connect to server (block until accepted)
s.send('Hello World')       # Sending data to server
data = s.recv(1024)         # receive the response from server
print data                  # print results
s.close()                   # close the connection