#!/bin/bash

docker run -p 127.0.0.1:3306:3306 --name mariadb -e MARIADB_ROOT_PASSWORD=hello -d --restart=always mariadb:10