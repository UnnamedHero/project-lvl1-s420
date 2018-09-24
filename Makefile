clear:
	rm -rf ./target/classes

compile: clear
	mkdir -p ./target/classes
	javac -d ./target/classes ./src/main/java/games/Slot.java

build: compile
	jar cf ./target/casino.jar -C ./target/classes .

run:
	java -cp ./target/casino.jar games.Slot

build-run: build run

.DEFAULT_GOAL := build-run