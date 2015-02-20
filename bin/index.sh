#!/usr/bin/env bash
HERE=$(dirname $0)

${SPARK_HOME}/bin/spark-submit --master local[4] --jars $(ls ${HERE}/../target/universal/stage/lib/*.jar | tr "\n" ,) --class "mrs.app.MRSApp" ${HERE}/../target/scala-2.10/music-review-spider_2.10-latest.jar ${SPARQL_QUERY} ${SPARQL_UPDATE}
