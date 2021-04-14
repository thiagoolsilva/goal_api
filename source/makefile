build: clean
	@ cd Goal-API && ./gradlew bootJar;
	@ docker-compose build

clean:
	@ cd Goal-API && ./gradlew clean

clean-docker-cache:
	@ docker-compose rm 

start: 
	docker-compose up

stop:
	docker-compose stop