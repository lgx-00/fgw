scp target\fgw-backend-0.0.1-SNAPSHOT.jar root@192.168.110.239:/mysoft/archives

ssh root@192.168.110.239 /mysoft/rename.sh
ssh root@192.168.110.239 /mysoft/restart.sh
