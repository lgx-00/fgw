#!/bin/zsh

mvn clean package -Dmaven.test.skip=true

scp target/*.jar root@192.168.110.241:/mysoft/fgw/archives

ssh root@192.168.110.241 /mysoft/fgw/rename.sh
ssh root@192.168.110.241 'cd /mysoft; /mysoft/restart.sh fgw'

exit 0
