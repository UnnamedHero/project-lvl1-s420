clean:
	./mvnw clean

build: clean
	./mvnw package -DskipTests

run:
	java -jar ./target/project-lvl1-s420-1.0-SNAPSHOT-jar-with-dependencies.jar

build-run: build run

test:
	./mvnw surefire:test

update-libs:
	./mvnw versions:update-properties

update-plugins:
	./mvnw versions:display-plugin-updates

update: update-libs update-plugins

.DEFAULT_GOAL := build-run