import java.io.*;

public class SimpleClient extends Correspondent {
    protected BufferedReader stdin;
    protected PrintWriter stdout;
    protected PrintWriter stderr;

    public SimpleClient(String host, int port) {
        requestConnection(host, port);
        stdout = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.out)), true);
        stderr = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.err)), true);
        stdin = new BufferedReader(
                new InputStreamReader(System.in));
    }

    public void repl() {
        stdout.print("Write message to the server: ");

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
                send(msg);

                msg = receive();
                stdout.println("received: " + msg);
            } catch(IOException e) {
                stderr.println(e.getMessage());
                break;
            }
        }
        send(RequestHandler.QUIT_MESSAGE);
        stdout.println("bye");
    }

    public static void main(String[] args) {
        int port = 5555;

        String host = "localhost";

        if (1 <= args.length) {
            port = Integer.parseInt(args[0]);
        }
        if (2 <= args.length) {
            host = args[1];
        }

        SimpleClient client = new SimpleClient(host, port);
        client.repl();
    }
}
