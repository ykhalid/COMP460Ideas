from pymd5 import md5, padding
m = "Use HMAC, not hashes"
print md5(m).hexdigest()


length_of_m = 8
bits = (length_of_m + len(padding(length_of_m * 8))) * 8
print "bits: " + str(bits)
h = md5(state="3ecc68efa1871751ea9b0b1a5b25004d".decode("hex"), count=512)
print h.hexdigest()

x = "Good advice"
h.update(x)
print h.hexdigest()

print md5(m + padding(len(m)*8) + x).hexdigest()