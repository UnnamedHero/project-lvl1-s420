clear:
	rm -rf ./target/classes

compile: clear
	mkdir -p ./target/classes
	javac -d ./target/classes ./src/main/java/games/Slot.java

run:
	java -cp ./target/classes games.Slot

compile-run: compile run

.DEFAULT_GOAL := compile-run