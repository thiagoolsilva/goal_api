build: clean
	@ docker rmi -f goal-rest-api
	@ cd Goal-API && ./gradlew bootJar;	
	@ docker-compose build

clean: clean-docker-cache
	@ cd Goal-API && ./gradlew clean

clean-docker-cache:
	@ docker-compose rm -f

start: 
	docker-compose up

stop:
	docker-compose stop