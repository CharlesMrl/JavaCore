#!/usr/bin/env python
# -*- coding: latin-1 -*-

import serial,time
time.sleep(5)
ser = serial.Serial('/dev/ttyACM1', 9600)
time.sleep(1)
#sio = io.TextIOWrapper(io.BufferedRWPair(ser, ser))
while 1 :
	s=ser.readline()
	#s=sio.readline()
	print(s)
	print('\n')