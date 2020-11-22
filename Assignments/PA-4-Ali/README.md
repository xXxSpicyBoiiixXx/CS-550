BitTorrent - Programming Assignmnet 4

Please have some libraries available, there should be bencode already. Also, in the linux kernel please use pip install and make sure it is installing packages in the python 2.7 and not python 3.X Also use the previous version of python, in my case python 2.7. If there are any problems please let me know 

### Compling 

Just run make and then type in "python run.py" followed by the torrent file to share with the network.

### Cleaning 

Make clean should get ride of all the .pyc files. 

### Info on what it does

The clients will open and parse a torrent file to get more information about the file it will download. It connects to a tracker and feom there it gains a list and sees what peers have parts or all the firles. From the list it will pick 3 peers to connect to and then the BitTorrent protocol will be talking to all these peers and start the transfer portion of the file’s pieces. Once all the parts of the file is recived by a request peer then it is reconstructed and then we can see about another torrent file. I have included 3 of them.  
