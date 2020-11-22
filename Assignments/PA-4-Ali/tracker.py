import re
import bencode
import requests
from torrent import Torrent

class Tracker(object):
	def __init__(self, url):
		url2 = url.split("://")
		if "/" in url2[1]:
			host_and_port, self.path = url2[1].split("/")
		else:
			host_and_port = url2[1]
		if ":" in url2[1]:
			self.host, self.port = host_and_port.split(':')
		else:
			self.host = host_and_port
			self.port = None 

		self.peers = []
		print "Connecting to tracker on host: %s  port: %s  path: %s..." % (self.host, self.port, self.path)

	def connect(self, torrent, my_id):

		payload = {'info_hash': torrent.info_hash, 'peer_id': my_id, 'ip': self.host, 'port': self.port, 'uploaded': 0, 'downloaded': 0, 'left': torrent.total_length, 'event': 'started'}
		peers_from_tracker = requests.get(torrent.tracker_url, params = payload)
		self.peers = bencode.bdecode(peers_from_tracker.text)['peers']

