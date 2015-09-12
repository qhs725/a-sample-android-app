import pandas
import matplotlib as plt
from pylab import *

tango = pandas.read_csv("dataNew.csv")
names = tango['session_info'].unique()
sz = names.size
for i in xrange(sz):
  wrt = tango[tango['session_info'].str.contains(names[i])]
  plt.plot(wrt.tstamp,wrt.ax,'r',wrt.tstamp,wrt.ay,'g',wrt.tstamp,wrt.az,'b')
  savefig("ACCEL"+names[i]+".png",bbox_inches='tight')
  plt.clf()  
  plt.plot(wrt.tstamp,wrt.tx,'r',wrt.tstamp,wrt.ty,'g',wrt.tstamp,wrt.tz,'b')
  savefig("POSITION"+names[i]+".png",bbox_inches='tight')
  plt.clf()