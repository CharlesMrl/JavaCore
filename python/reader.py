#!/usr/bin/env python
# -*- coding: latin-1 -*-

import serial,time 
ser = serial.Serial('/dev/ttyACM1', 9600)
time.sleep(1)
while 1 :
	s=ser.readline()
	print(s)
	print('\n')