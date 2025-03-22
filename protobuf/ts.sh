#!/bin/sh

PLUGIN_PATH="../ui/node_modules/.bin/protoc-gen-ts_proto"
OUTPUT_FOLDER="../ui/src/api/protobuf"
INPUT_FILES="beans.proto calls.proto common.proto connections.proto"

protoc --plugin=$PLUGIN_PATH --ts_proto_out=$OUTPUT_FOLDER $INPUT_FILES