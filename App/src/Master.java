import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Master {
    protected ServerSocket mySocket;
    protected int myPort;

    protected List<RequestHandler> clientRequestHandlers = new ArrayList<RequestHandler>();
    private MasterInputReader masterInputReader;

    public Master() { this(5555); }
    public Master(int port) {
        try {
            myPort = port;
            mySocket = new ServerSocket(myPort);
            masterInputReader = new MasterInputReader(clientRequestHandlers);
            masterInputReader.setDaemon(true);
            masterInputReader.start();
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void listen() throws IOException {
        while(true) {
            // accept a connection socket
            Socket newClient = mySocket.accept();
            System.out.println("Connected a new worker " + newClient.getPort());
            // create handler
            RequestHandler requestHandler = makeHandler(newClient);
            clientRequestHandlers.add(requestHandler);
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

        Master server = new Master(port);
        server.listen();
    }
}