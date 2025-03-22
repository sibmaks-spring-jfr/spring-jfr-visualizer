#!/bin/sh

OUTPUT_FOLDER="../app/src/main/java"
INPUT_FILES="beans.proto calls.proto common.proto connections.proto"

protoc --java_out=$OUTPUT_FOLDER $INPUT_FILES