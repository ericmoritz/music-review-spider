${SPARK_HOME}/bin/spark-submit --master local[4] --jars $(ls target/universal/stage/lib/*.jar | tr "\n" ,) --class "mrs.app.MRSApp" target/scala-2.10/music-review-spider_2.10-latest.jar http://localhost:3030/music-reviews/query ${SPARQL_UPDATE}