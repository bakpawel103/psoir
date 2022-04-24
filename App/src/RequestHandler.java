import java.io.IOException;
import java.net.*;
import java.util.Objects;

public class RequestHandler extends Correspondent implements Runnable {
    public static String QUIT_MESSAGE = "/quit";

    public RequestHandler(Socket s) { super(s); }
    public RequestHandler() { }

    protected String response(String msg) {
        return "echo: " + msg;
    }

    public void run() {
        while(true) {
            // receive a message
            String message;
            try {
                if((message = sockIn.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // if quit break
            if(Objects.equals(message, QUIT_MESSAGE)) {
                System.out.println("Closing RequestHandler...");
                break;
            }
            // send a response
            send(response(message));
            // yield or sleep
        }

        close();
    }
}