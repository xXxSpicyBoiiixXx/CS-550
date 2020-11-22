import struct
import math
import hashlib
import piece
import glob
import os

class Bitfield(object):

	def __init__(self, torrent):
		self.torrent = torrent
		self.total_length = torrent.total_length
		self.no_of_subpieces = int(torrent.no_of_subpieces)
		self.no_of_bytes = int(math.ceil(int(torrent.no_of_subpieces) / 8.0))
		self.bitfield = [0] * self.no_of_bytes
		self.complete_bitfield = self.complete_bitfield()
	
	def set_bitfield_from_payload(self, payload):

		payload_unpacked = struct.unpack("!%dB" % (len(self.bitfield)), payload)
		for i in range(len(payload_unpacked)):
			self.bitfield[i] = payload_unpacked[i]

	def pack_bitfield(self):

		bitfield_list = [str(i) for i in self.bitfield]
		bitfield_string = ",".join(bitfield_list)
		bitfield_packed = struct.pack("%dB" % (len(self.bitfield)), *(i for i in self.bitfield))
		return bitfield_packed

	def update_bitfield(self, piece_index):
		byte_index = int(math.floor(piece_index / 8.0))
		self.bitfield[byte_index] += 2**(7 - (piece_index%8))

	def complete_bitfield(self):

		temp_list = [0] * self.no_of_bytes
		for i in range(self.torrent.no_of_subpieces):
			byte_index = int(math.floor(i / 8.0))
			temp_list[byte_index] += 2**(7 - (i%8))
		return temp_list

	def initialize_bitfield(self):
		if os.path.exists(os.path.join(piece.PATH,self.torrent.name)):
			self.bitfield = self.complete_bitfield
			return False
		else:
			for files in glob.glob(os.path.join(piece.PATH, self.torrent.name+".00*")):
				root, ext = os.path.splitext(files)
				piece_index = int(ext[1:])
				self.update_bitfield(piece_index)
			return True




			
		
