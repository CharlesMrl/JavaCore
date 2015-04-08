#!/usr/bin/env python
# -*- coding: latin-1 -*-

import serial,time 
ser = serial.Serial('/dev/ttyACM1', 9600)
time.sleep(1)
while 1 :
	s=ser.readline()
	print(s+'\n')
	logfile = open("reader.log", "a")
	logfile.write(s)
	logfile.close()