#!/usr/bin/env python
# -*- coding: latin-1 -*-

import sys, serial, time

ser = serial.Serial('/dev/ttyACM0', 9600)
time.sleep(1)
s = sys.stdin.readline().strip()

while 1:
	ser.write(s + '$')
	s = sys.stdin.readline().strip()