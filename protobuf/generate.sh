#!/bin/sh

JAVA_OUTPUT_FOLDER="../app/src/main/java"
INPUT_FILES="beans.proto calls.proto common.proto connections.proto"

protoc --java_out=$JAVA_OUTPUT_FOLDER $INPUT_FILES

TS_PLUGIN_PATH="../ui/node_modules/.bin/protoc-gen-ts_proto"
TS_OUTPUT_FOLDER="../ui/src/api/protobuf"

protoc --plugin=$TS_PLUGIN_PATH --ts_proto_out=$TS_OUTPUT_FOLDER $INPUT_FILES