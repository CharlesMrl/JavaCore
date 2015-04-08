#!/usr/bin/env python
# -*- coding: latin-1 -*-

import serial,time 
ser = serial.Serial('/dev/ttyACM1', 9600, timeout=1)
time.sleep(1)
sio = io.TextIOWrapper(io.BufferedRWPair(ser, ser))
time.sleep(1)
while 1 :
	#s=ser.readline()
	s=sio.readline()
	print(s)
	print('\n')