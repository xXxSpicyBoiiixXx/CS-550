from peer import Peer
from client import Client
import socket
from piece import Piece
import select
import random

class Brain(Peer):

    def __init__(self, peer_dict, torrent, tracker):
        self.tracker = tracker
        self.torrent = torrent
        Peer.__init__(self, peer_dict, torrent, peer_dict['id'])
        self.peers = []
        self.done = False
        self.piece_dict = { i : None for i in range(self.torrent.no_of_subpieces)}

    def add_peers(self):
        appended_peer = -1
        for peer_dict in self.tracker.peers:
            if peer_dict.get('id') != self.id:
                c = Client(peer_dict, self.torrent, self)
                self.peers.append(c)
                appended_peer += 1 

    def connect_all(self, n):

        self.current_peers = random.sample(self.peers, n)
        for current_peer in self.current_peers:
            current_peer.connect()

    def reconnect_all(self, n):

        self.current_peers = random.sample(self.peers, n)
        print "New current peers: %r" %[i.id for i in self.current_peers]
        for current_peer in self.current_peers:
            current_peer.refresh_socket_and_connect()

    def is_complete(self):
        if self.bitfield.initialize_bitfield():
            return False
        else:
            return True

    def handle_piece(self, piece):

        if piece.check_piece_hash:
            piece.write_to_disk()
            self.pieces_i_have += 1
            self.bitfield.update_bitfield(piece.index)
            if self.bitfield.bitfield == self.bitfield.complete_bitfield:
                piece.concatenate_pieces()
                self.complete = True
        else:
            print "Hash Incorrect."

    def lock_this_piece(self, piece_index, client_id):
        self.piece_dict[piece_index] = client_id

    def unlock_this_piece(self, piece_index):
        self.piece_dict[piece_index] = None

    def refresh_piece_dict(self):
        self.piece_dict = { i : None for i in range(self.torrent.no_of_subpieces)}

    def run(self):
        while self.bitfield.bitfield != self.bitfield.complete_bitfield:

            ready_peers, ready_to_write, in_error = select.select(self.current_peers, [], [], 3)

            if self.complete:
                return False

            for p in ready_peers:
                status = p.process_messages()

            transfer_peers = self.current_peers
            random.shuffle(transfer_peers)

            for p in transfer_peers:
                status = p.start_transfer()
                if status == False:
                    if self.complete:
                        return False
                    else:
                        print "Nothing left to download from peer %r." %p.id
                        self.current_peers.remove(p)
                        p.disconnect()
                        for peer in self.current_peers:
                            peer.send_keepalive()
                        for index, peer in self.piece_dict.iteritems():
                            if peer == p.id:
                                self.piece_dict[index] = None

            if not self.current_peers:
                print "No, get new ones!"
                self.reconnect_all(3)
                self.refresh_piece_dict()
        
        
