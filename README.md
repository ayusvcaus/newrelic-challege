1) Run unit test

cd newrelic-code-challenge

mvn test

2) Run server and client seperately with bulk data

On console of server:

cd newrelic-code-challenge

mvn compile exec:java -Dexec.mainClass="com.newrelic.codingchallenge.Application"

On console of client

cd newrelic-code-challenge

mvn compile exec:java -Dexec.mainClass="com.newrelic.codingchallenge.SocketBulkValidDataTester" -Dexec.classpathScope=test

mvn compile exec:java -Dexec.mainClass="com.newrelic.codingchallenge.SocketBulkInvalidDataTester" -Dexec.classpathScope=test
