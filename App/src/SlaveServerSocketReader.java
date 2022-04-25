import java.io.*;

public class SlaveServerSocketReader extends Thread {
    protected PrintWriter stdout;
    protected PrintWriter stderr;
    protected BufferedReader sockIn;

    public SlaveServerSocketReader(BufferedReader sockIn) {
        stdout = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.out)), true);
        stderr = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.err)), true);
        this.sockIn = sockIn;
    }

    public void run() {
        while(true) {
            String msg = null;
            try {
                if((msg = sockIn.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch(Exception e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }
}
