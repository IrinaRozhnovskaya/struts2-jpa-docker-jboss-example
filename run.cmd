@echo off
call mvnw.cmd clean
call mvnw.cmd -Pdocker docker-compose:down
call mvnw.cmd package
call mvnw.cmd -Pdocker docker-compose:up
call docker-compose logs -f -t
