clean:
	./mvnw clean

build: clean
	./mvnw package

run:
	java -jar ./target/project-lvl1-s420-1.0-SNAPSHOT-jar-with-dependencies.jar

build-run: build run

.DEFAULT_GOAL := build-run