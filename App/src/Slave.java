import java.io.*;

public class Slave extends Correspondent {
    protected BufferedReader stdin;
    protected PrintWriter stdout;
    protected PrintWriter stderr;

    private SlaveServerSocketReader slaveServerSocketReader;

    public Slave(String host, int port) {
        requestConnection(host, port);
        stdout = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.out)), true);
        stderr = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.err)), true);
        stdin = new BufferedReader(
                new InputStreamReader(System.in));

        slaveServerSocketReader = new SlaveServerSocketReader(sockIn);
        slaveServerSocketReader.setDaemon(true);
        slaveServerSocketReader.start();
    }

    public void repl() {
        stdout.println("Write message to a Master: ");

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

        Slave client = new Slave(host, port);
        client.repl();
    }
}