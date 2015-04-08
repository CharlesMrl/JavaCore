#!/usr/bin/env python
# -*- coding: latin-1 -*-

import sys, serial, time

ser = serial.Serial('/dev/ttyACM1', 9600)
time.sleep(1)
logfile = open("writer.log", "w")
s = sys.stdin.readline().strip()

while 1:
	ser.write(s + '$')
	logfile.write("{}".s)
	s = sys.stdin.readline().strip()
