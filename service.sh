echo "+-------------------------------------------------------------+"
echo "|                                                             |"
echo "|                    Setup webapp.service                     |"
echo "|                                                             |"
echo "+-------------------------------------------------------------+"
echo "cd to /lib/systemd/system"

sudo cp /opt/csye6225/webapp.service /lib/systemd/system

echo "+-------------------------------------------------------------+"
echo "|                                                             |"
echo "|                    Setup webappuser                         |"
echo "|                                                             |"
echo "+-------------------------------------------------------------+"
sudo groupadd csye6225
sudo useradd -s /bin/false -g csye6225 -d /opt/csye6225 -m csye6225

echo "+-------------------------------------------------------------+"
echo "|                                                             |"
echo "|                    setup new user permissions               |"
echo "|                                                             |"
echo "+-------------------------------------------------------------+"

echo "get the home directory of user"
echo ~csye6225
# sudo -u webappuser bash
echo "display permissions of user directory"
ls -la /opt/csye6225

echo "change permissions of webapp"
sudo chown -R csye6225:csye6225 /opt/csye6225/webapp-0.0.1-SNAPSHOT.jar
sudo chmod -R 750  /opt/csye6225/webapp-0.0.1-SNAPSHOT.jar

echo "display permissions of user directory"
ls -la /opt/csye6225

sudo -u csye6225 bash