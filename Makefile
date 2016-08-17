default: compile

compile:
	echo "Compiling..."
	javac JavaTestServer.java

run:
	echo "Running JavaTestServer..."
	java JavaTestServer

jar: compile
	echo "Packaging into jar file, JavaTestServer.jar..."
	jar cfm JavaTestServer.jar \
		manifest.mf \
		JavaTestServer.class \
		config.properties

clean:
	rm *.log *.class
