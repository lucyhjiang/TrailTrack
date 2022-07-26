default: run

run: TrailTrack.class
	java TrailTrack

TrailTrack.class: TrailTrack.java Node.java
	javac Node.java
	javac TrailTrack.java

clean:
	rm *.class

test: TrailTrackTester.class
	java -jar junit5.jar -cp . --scan-classpath --include-classname=TrailTrackTester

TrailTrackTester.class: TrailTrack.java TrailTrackTester.java Node.java
	javac -cp .:junit5.jar *.java

