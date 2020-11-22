from peer import Peer
import socket
from piece import Piece

class Client(Peer):

    def __init__(self, peer_dict, torrent, brain):
        Peer.__init__(self, peer_dict, torrent, brain.my_id)
        self.brain = brain

    @property
    def transferring(self):
        return self.requesting

    def process_messages(self):
        
        if not self.recv_message():
            self.socket.close()
            return False

        return True

    def start_transfer(self):

        if self.brain.complete:
            return False

        if self.choked:
            return True

        next_piece = -1

        if not self.transferring:
            if not self.choked:
                next_piece = self.brain.piece_to_request(self.bitfield)

                if next_piece >= 0: 
                    if not self.brain.piece_dict.get(next_piece):
                        self.brain.lock_this_piece(next_piece, self.id)
                        self.send_piece_request(next_piece, self.torrent.block_size)

                else:
                    return False

        return True

    def piece_complete(self, piece_index):

        p = Piece(self.torrent, piece_index, self.piece_data)
        self.brain.handle_piece(p)
        self.brain.unlock_this_piece(piece_index) 
        self.piece_data = ''

    def file_complete(self):

        self.brain.complete = True
