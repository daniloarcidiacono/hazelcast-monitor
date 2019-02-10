#!/usr/bin/env bash
curl --user ${CIRCLE_TOKEN}: \
    --request POST \
    --form revision=${COMMIT_HASH}\
    --form config=@.circleci/config.yml \
    --form notify=false \
        https://circleci.com/api/v1.1/project/github/daniloarcidiacono/hazelcast-monitor/tree/prerelease