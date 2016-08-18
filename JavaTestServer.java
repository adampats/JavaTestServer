// a simple Java server, an afternoon project

import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.logging.*;
import java.util.Date;
import java.text.*;

public class JavaTestServer {

    public static void main(String[] args) throws IOException {

        // Defaults
        Integer listen_port = 1337;
        String log_file_name = "JavaTestServer";

        // Load properties
        Properties prop = new Properties();
        InputStream input = null;
        String prop_file = "config.properties";
        try {
            input = JavaTestServer.class.getResourceAsStream(prop_file);
            if(input == null) {
                System.out.println("Error, cannot find " + prop_file);
                return;
            }

            prop.load(input);
            listen_port = Integer.parseInt(prop.getProperty("listen_port"));
            log_file_name = prop.getProperty("log_file_name");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        // Log file timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        Date now = new Date();
        String strDate = sdf.format(now);
        String log_file = log_file_name + "-" + strDate + ".log";

        // Setup logging
        Logger logger = Logger.getLogger("JavaTestServerLog");
        FileHandler fh;
        try {
            fh = new FileHandler(log_file);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String msg = "Starting server on TCP " + listen_port + "...";
        System.out.println(msg);
        logger.info(msg);

        System.out.println("Watch log output from: " + log_file);

        while (true) {
            try (
                ServerSocket serverSocket = new ServerSocket(listen_port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String client = clientSocket.getRemoteSocketAddress().toString();
                logger.info(client + " connected.");
                String inputLine;
                inputLine = in.readLine();
                out.println(inputLine);
                logger.info(client + " says: " + inputLine);
                if (inputLine == null) {
                  continue;
                }
                if (inputLine.toLowerCase().contains("exit")) {
                    logger.info("exit command received, quitting...");
                    break;
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                    + listen_port + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }
}
