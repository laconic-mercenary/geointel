#!/bin/bash

docker build . -t=repository:local

docker run --rm \
    -p 31999:8080 \
    --security-opt=no-new-privileges \
    --restart on-failure:2 \
    --icc=false \
    --read-only \
    repository:local

echo "curl -v http://localhost:31999/"
