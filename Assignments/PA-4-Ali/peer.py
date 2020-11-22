import socket
import messages
from struct import *
import bitfield
from piece import Piece
from threading import Thread

class Peer(object):

    def __init__(self, peer_dict, torrent, my_id):
        self.ip = peer_dict['ip']
        self.port = peer_dict['port']
        self.id = peer_dict.get('id') or peer_dict.get('peer id')
        if not self.id:
            print "No ID found. Not a valid peer."
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.torrent = torrent
        self.my_id = my_id

        self.bitfield = bitfield.Bitfield(self.torrent)
        self.bitfield.initialize_bitfield() 

        self.handshake = False
        self.choked = True
        self.downloading = False
        self.requesting = False
        self.complete = False
        self.connecting = False
        self.interested = False
        
        self.message_incomplete = False
        self.incomplete_data = ''
        self.incomplete_message_id = -1
        self.data_length = -1
        self.piece_data = ''
        self.have = -1
        self.pieces_i_have = 0
        self.last_piece_index = (self.torrent.no_of_subpieces - 1)

    def connect(self):
        self.socket.settimeout(10)
        try:
            self.socket.connect((self.ip, self.port))
        except:
            return False
        self.send_handshake()
        self.connecting = True
        return True

    def disconnect(self):

        print "Disconnecting....!"
        self.socket.close()
        self.connecting = False
        self.handshake = False

    def refresh_socket_and_connect(self):
        
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.settimeout(60)
        self.socket.connect((self.ip, self.port))
        self.send_handshake()
        self.connecting = True

    def is_unchoked(self):

        if self.choked == False:
                return True
        return False

    def send_handshake(self):

        pstr = 'BitTorrent portocol'
        handshake = pack("!B19s8x20s20s", len(pstr), pstr, self.torrent.info_hash, self.my_id)
        print "Initiating handshake with ", self.id
        self.socket.send(handshake)

    def recv_handshake(self):
        """Receives and processes peer's handshake reply"""

        print "<----- Receiving handshake reply from ", self.id
        re_handshake = self.socket.recv(68)
        if len(re_handshake) > 68 or len(re_handshake) < 68:
            print "Expected handshake but received something else: ", re_handshake
        else:
            reh_unpacked = unpack("!B19s8x20s20s", re_handshake)
            self.handshake = True
            self.interested = True
            interested = messages.Interested()
            self.send_next_message(interested.assemble())

    def get_entire_message(self):
        receiving = self.socket.recv(self.data_length - len(self.incomplete_data))
        self.incomplete_data += receiving
        if len(self.incomplete_data) < self.data_length-1:
                self.recv_message()
        else:
                self.react_to_message(self.incomplete_message_id, self.incomplete_data)
                self.message_incomplete = False
                self.incomplete_data = ''
                self.incomplete_message_id = -1
                self.data_length = -1

    def recv_message(self):

        if self.handshake:
                if self.message_incomplete:
                    self.get_entire_message()

                else:
                        self.data = self.socket.recv(4)
                        if self.data:
                                self.length = int(unpack("!I", self.data)[0])
                        else:
                                return False

                        if self.length < 1:
                                print "Message length < 1 - keepalive or invalid message."
                                return False

                        m = self.socket.recv(1)
                        message_id = int(unpack('!B', m)[0])
                        payload = None
                        if self.length == 1:
                                self.react_to_message(message_id, payload)
                        if self.length > 1:
                                if message_id == 7:
                                        i = self.socket.recv(4)
                                        index = str(unpack("!I", i)[0])
                                        b = self.socket.recv(4)
                                        begin = str(unpack("!I", b)[0])

                                        block = self.socket.recv(self.length - 9)
                                        payload = i+b+block
                                        actual_length = len(i)+len(b)+len(block)
                                
                                else:
                                        payload = self.socket.recv(self.length - 1)
                                        actual_length = len(payload)

                                if (actual_length != self.length-1):
                                        self.incomplete_data+=payload
                                        self.data_length = self.length
                                        self.incomplete_message_id = message_id
                                        self.message_incomplete = True
                                        self.recv_message()

                                else:
                                        self.react_to_message(message_id, payload)
        else:
            self.recv_handshake()

        return True

    def react_to_message(self, message_id, payload):

        mlist = {"keepalive":None, "choke": 0, "unchoke": 1, "interested":2, "not interested": 3, "have": 4, "bitfield": 5, "request": 6, "piece":7, "cancel": 8, "port": 9}

        if message_id == mlist["keepalive"]:
            pass

        if message_id == mlist["choke"]:
            print "<----- Got 'Choke' message from peer ", self.id
            self.choked = True

        if message_id == mlist["unchoke"]:
            # Unchoke received, ready to send request
            print "====================== Got 'Unchoke' message from peer %r ======================" %self.id
            self.choked = False

        if message_id == mlist["interested"]:
            print "<----- Got 'Interested' message from peer ", self.id

        if message_id == mlist["not interested"]:
            print "<----- Got 'Not Interested' message from peer ", self.id
            self.interested = False

        if message_id == mlist["have"]:
            print "====================== Got 'Have' message from peer %r advertising piece of index %r ======================" %(self.id, (unpack("!I", payload))[0])
            self.bitfield.update_bitfield((unpack("!I", payload))[0])
            self.interested = True
            interested = messages.Interested()
            self.send_next_message(interested.assemble())
            print "Sending 'Interested' message to peer %r..." %self.id

        if message_id == mlist["bitfield"]: 
            print "====================== Got 'Bitfield' message from peer %r ======================" % self.id
            self.bitfield.set_bitfield_from_payload(payload)
            bitfield_message = messages.BitMessage()
            self.send_next_message(bitfield_message.assemble(self.torrent))
            print "Sending 'Bitfield' message to peer %r..." %self.id

        if message_id == mlist["piece"]:
            self.downloading = True
            piece_index = unpack("!I", payload[0:4])[0]
            piece_begin = unpack("!I", payload[4:8])[0]
            self.piece_data += payload[8:]
            if piece_index != self.last_piece_index:
                    self.check_piece_completeness(piece_index)
            else:
                    self.process_last_piece(piece_index)

        if message_id == mlist["cancel"]:
            print "<----- Got 'Cancel' message!"

        if message_id == mlist["port"]:
            print "<----- Got 'Port' message!"

        if message_id == mlist["request"]:
            print "<----- Got 'Request' message!"

    def piece_to_request(self, bitfield_from_peer):

        for byte in range(0, len(self.bitfield.bitfield)):
                for bit in reversed(range(8)):                          
                        if ((int(self.bitfield.bitfield[byte]) >> bit) & 1) == 0:
                            if ((int(bitfield_from_peer.bitfield[byte]) >> bit) & 1) == 1:
                                return int((8*byte + (7 - bit))) 
        return -1

    def send_piece_request(self, piece_index, block, begin=0):

        msg = messages.Request()
        send = msg.assemble(self.torrent, piece_index, begin, block)
        self.send_next_message(send)
        self.requesting = True

    def check_piece_completeness(self, piece_index):

        if len(self.piece_data) < self.torrent.piece_length:
            self.send_piece_request(piece_index, self.torrent.block_size, len(self.piece_data))

        else:
            print "<----- Received piece of index %r from peer %s" %(piece_index, self.id)
            self.piece_complete(piece_index)
            self.downloading = False
            self.requesting = False     

    def piece_complete(self, piece_index):
        pass

    def file_complete(self):
        pass

    def process_last_piece(self, piece_index):

        self.last_piece_size = self.torrent.total_length % self.torrent.piece_length
        if len(self.piece_data) < self.last_piece_size:
            self.last_subpiece_length = self.last_piece_size % self.torrent.block_size
            self.send_piece_request(piece_index, self.last_subpiece_length, len(self.piece_data))

        else:
            self.piece_complete(piece_index)
            self.downloading = False
            self.requesting = False    
                    
    def send_keepalive(self):
        keepalive = messages.Keepalive()
        self.send_next_message(keepalive.assemble())

    def send_next_message(self, assembled_message):
        self.socket.sendall(assembled_message)

    def fileno(self):
    
        return self.socket.fileno()
