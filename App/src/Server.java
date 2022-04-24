import java.io.IOException;
import java.net.*;

public class Server {
    protected ServerSocket mySocket;
    protected int myPort;

    public Server() { this(5555); }
    public Server(int port) {
        try {
            myPort = port;
            mySocket = new ServerSocket(myPort);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void listen() throws IOException {
        while(true) {
            // accept a connection socket
            Socket newClient = mySocket.accept();
            System.out.println("Accepted new client " + newClient.getPort());
            // create handler
            RequestHandler requestHandler = makeHandler(newClient);
            // launch handler
            requestHandler.run();
        }
    }

    public RequestHandler makeHandler(Socket s) {
        // make a RequestHandler
        return new RequestHandler(s);
    }

    public static void main(String[] args) throws IOException {
        int port = 5555;

        if (1 <= args.length) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);
        server.listen();
    }
}