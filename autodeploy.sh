#!/bin/zsh

mvn clean package -Dmaven.test.skip=true

scp target/fgw-backend-0.0.1-SNAPSHOT.jar root@192.168.110.239:/mysoft/fgw/archives

ssh root@192.168.110.239 /mysoft/fgw/rename.sh
ssh root@192.168.110.239 'cd /mysoft; /mysoft/restart.sh fgw'

exit 0
