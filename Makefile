all:
	sbt stage
	npm install node-schedule # install the deps for job.js

test:
	sbt test
