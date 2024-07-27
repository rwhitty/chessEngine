#!/bin/bash

SRC_DIR="src"
BIN_DIR="bin"

mkdir -p $BIN_DIR

javac -d $BIN_DIR $SRC_DIR/Main.java $SRC_DIR/Engine.java $SRC_DIR/Board.java $SRC_DIR/Move.java $SRC_DIR/Constants.java

java -cp $BIN_DIR Main