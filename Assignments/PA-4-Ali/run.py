#!/usr/bin/env python

from torrent import Torrent
from tracker import Tracker
from brain import Brain
import select
import messages
from struct import *
from bitfield import Bitfield
import random
import sys

def main(argv):
        if len(argv) != 2:
                print "Usage: %s <torrent-file>" % argv[0]
                sys.exit(1)

        inputs = []
        outputs = []

        tfile = Torrent(argv[1])
        tfile.print_info()

        my_id = "xxxSpicyboiiixx%20d"%random.randint(0,99999)

        tracker = Tracker(tfile.tracker_url)
        tracker.connect(tfile, my_id)

        brain = Brain({ "ip": "localhost", "port": 1050, "id": my_id}, tfile, tracker)

        if brain.is_complete():
                print "Aborting"

        else:
                print "Received list of peers: %r" % tracker.peers
                brain.add_peers()
                brain.connect_all(3)
                brain.run()

                print "Downloaded a torrent"

if __name__=="__main__":
        main(sys.argv)
