import java.io.*;
import java.util.List;

public class MasterInputReader extends Thread {
    public List<RequestHandler> clientRequestHandlers;

    private BufferedReader stdin;
    private PrintWriter stdout;
    private PrintWriter stderr;

    public MasterInputReader(List<RequestHandler> clientRequestHandlers) {
        this.clientRequestHandlers = clientRequestHandlers;

        stdout = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.out)), true);
        stdin = new BufferedReader(
                new InputStreamReader(System.in));
    }

    public void run() {
        stdout.println("Write message to a Slaves: ");

        while(true) {
            try {
                stdout.flush();

                String msg = stdin.readLine();

                if (msg == null) {
                    continue;
                }
                if (msg.equals(RequestHandler.QUIT_MESSAGE)) {
                    break;
                }

                stdout.println("sending: " + msg);
                clientRequestHandlers.forEach(clientRequestHandler -> {
                    clientRequestHandler.send(msg);
                });
            } catch(IOException e) {
                stderr.println(e.getMessage());
                break;
            }
        }

        clientRequestHandlers.forEach(Correspondent::close);
        stdout.println("Shutting down a Master");
    }
}
