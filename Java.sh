#!/bin/bash

#installing the unzip 
sudo apt-get install unzip
# unzip Sangram_Vuppala_002958963_03.zip


#install jdk
#echo "---x---Adding Java Repository---x---"
#sudo apt update && sudo apt upgrade -y

echo "---x---Installing Java 17---x---"
sudo apt install openjdk-17-jdk -y

echo "---x---Setting Java Environment Variables---x---"
echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/" >> ~/.bashrc
echo "export PATH=$PATH:$JAVA_HOME/bin" >> ~/.bashrc
source ~/.bashrc

#Install Maven
echo "---x---Installing Maven---x---"
sudo apt install maven -y

#Install MariaDb
##sudo apt update
#echo "sudo apt install mariadb-server"
#sudo apt install -y mariadb-server
#
#sudo systemctl start mariadb
#echo "sudo systemctl enable mariadb"
#sudo systemctl enable mariadb
#
#echo "sudo mysql_secure_installation"
#sudo mysql_secure_installation <<EOF
#Ssangramm@12
#n
#y
#Ssangramm@12
#Ssangramm@12
#n
#n
#n
#y
#EOF
#

sudo apt update
sudo apt upgrade -y

#sudo mysql --version
sudo java --version
#sudo mariadb --version
sudo mvn --version
