clear:
	rm -rf ./target/classes

compile: clear
	mkdir -p ./target/classes
	javac -d ./target/classes ./src/main/java/games/Slot.java

build: compile
	jar cfe ./target/casino.jar games.Slot -C ./target/classes .

run:
	java -jar ./target/casino.jar

build-run: build run

.DEFAULT_GOAL := build-run