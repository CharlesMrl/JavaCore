
#!/bin/bash
apt-get install $(tr "\n" " " < ./packets_rasp_pi.txt)
apt-get install git
cd /home/pi/Desktop/wechess/
git clone https://github.com/bpascard/weChess_Stepper.git
git clone https://github.com/CharlesMrl/JavaCore.git
git clone git://git.drogon.net/wiringPi
cd wiringPi
./build