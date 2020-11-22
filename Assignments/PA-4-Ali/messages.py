import bencode
from struct import *
import bitfield
import math

class Message(object):

    def __init__(self):
        self.prefix = "!IB"    

class Keepalive(Message):

    def __init__(self):
        self.length  = 0

    def assemble(self):
        self.m_id = None
        message = pack("!I", 0)
        return message

class Choke(Message):

    def assemble(self):
        self.m_id = 0
        message = pack(self.prefix, 1, self.m_id)
        return message

class Unchoke(Message):

    def assemble(self):

        self.m_id = 1
        message = pack(self.prefix, 1, self.m_id)
        return message

class Interested(Message):
    
    def assemble(self):

        self.m_id = 2
        message = pack(self.prefix, 1, self.m_id)
        return message

class Not_interested(Message):
    
    def assemble(self):
        self.m_id = 3
        message = pack(self.prefix, 1, self.m_id)
        return message

class Have(Message):
    
    def assemble(self, torrent, piece_index):

        self.m_id = 4
        message = pack(self.prefix, 5, self.m_id, piece_index)
        return message

class BitMessage(Message):

    def assemble(self, torrent):

        self.m_id = 5
        bitfield_to_send = bitfield.Bitfield(torrent).pack_bitfield()
        bit_length = int(math.ceil(torrent.no_of_subpieces / 8.0))
        message = pack(self.prefix+'%ds' % bit_length, bit_length+1, self.m_id, bitfield_to_send)
        return message

class Request(Message):
    
    def assemble(self, torrent, piece_index, begin, block):

        self.m_id = 6
        message = pack(self.prefix+"III", 13, 6, piece_index, begin, block)
        return message

class Piece(Message):

    def assemble(self, torrent):
        self.m_id = 7

class Cancel(Message):

    def assemble(self):
        self.m_id = 8

class Port(Message):
    
    def assemble(self):   
        self.m_id = 9

