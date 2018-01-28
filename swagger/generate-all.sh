#!/bin/bash

set -e

if [[ -z "$SCALA_VERSION" ]]; then
    SCALA_VERSION="2.12.4"
fi;

echo "Using Scala version: $SCALA_VERSION"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
GROUP_ID="net.thenobody.clearscore.client"
OUTPUT_DIR=$DIR/generated

function generate_client {
    JSON_PATH=$1
    JSON_FILE=$(echo $JSON_PATH | awk '{print tolower($0)}')
    ARTIFACT=$(basename $JSON_FILE .json)
    PACKAGE=$(echo $GROUP_ID.$ARTIFACT | sed 's/-//g')

    swagger-codegen generate -l akka-scala -i $JSON_PATH -o $OUTPUT_DIR/$ARTIFACT \
      --group-id $GROUP_ID \
      --api-package $PACKAGE.api \
      --model-package $PACKAGE.model \
      --artifact-id $ARTIFACT

    cd $OUTPUT_DIR/$ARTIFACT
    sbt "++ $SCALA_VERSION! publishLocal"
}

for json in `ls $DIR/*.json`; do
    generate_client $json
done