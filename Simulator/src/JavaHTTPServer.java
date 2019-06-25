import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class JavaHTTPServer implements Runnable {

    static final int PORT = 5000;

    static final boolean verbose = true;

    private Socket connect;
    ArrayList<Task> tasks;

    public JavaHTTPServer(Socket c, ArrayList<Task> tasks) {
        connect = c;
        this.tasks = tasks;
    }


    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            out = new PrintWriter(connect.getOutputStream());

            String input = "";
            String totalInput = "";
            do {
                input = in.readLine();
                totalInput += input;
                totalInput += " ";
            } while (!input.equals(""));

            StringTokenizer parse = new StringTokenizer(totalInput);
            String key = "";
            while (key.equals("")) {
                String token = parse.nextToken();
                if (token.equals("Sec-WebSocket-Key:"))
                    key = parse.nextToken();
            }
            key = getAcceptKey(key);

            out.println("HTTP/1.1 101 Switching Protocols");
            out.println("Upgrade: websocket");
            out.println("Connection: Upgrade");
            out.println("Sec-WebSocket-Accept: " + key);
            out.println();
            out.flush();

            while(true){
                String[] names = new String[]{"Parser", "Counter1", "Counter2", "Accumulator"};
                for(int i=0; i < tasks.size(); i++){
                    out.print(new String(createFrame(names[i] + "-Throughput: " + Double.toString(tasks.get(i).getThroughput()))));
                    out.flush();
                    out.print(new String(createFrame(names[i] + "-AvgLatency: " + Double.toString(tasks.get(i).getAvgLatency()))));
                    out.flush();
                    out.print(new String(createFrame(names[i] + "-MaxLatency: " + Double.toString(tasks.get(i).getMaxLatency()))));
                    out.flush();
                    out.print(new String(createFrame(names[i] + "-emittedTuples: " + Integer.toString(tasks.get(i).emittedTuples))));
                    out.flush();
                }
                out.print(new String(createFrame(names[0] + "-time: " + Integer.toString(((Parser)tasks.get(0)).time))));
                out.flush();
                Thread.sleep(1000);
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                connect.close();
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }

    }

    private String getAcceptKey(String clientKey) {
        String k = clientKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            return null;
        }
        byte[] o = md.digest(k.getBytes());
        k = Base64.getEncoder().encodeToString(o);
        return k;
    }

    private byte[] createFrame(String data) {
        byte[] dataBytes = data.getBytes();
        int extend = 0;
        byte[] extended_pay = new byte[0];
        int len = dataBytes.length;
        int payLen1 = len;
        if (len > 125 && len <= 65535) {
            extend = 2;
            payLen1 = 126;
            extended_pay = ByteBuffer.allocate(2).put((byte) len).array();
        } else if (len > 65535) {
            extend = 8;
            payLen1 = 127;
            extended_pay = ByteBuffer.allocate(8).put((byte) len).array();
        }
        byte[] res = new byte[2 + extend + len];
        res[0] = (byte) 0x81;
        res[1] = (byte) (payLen1);

        int ind = 2;
        while (extend > 0) {
            res[ind] = extended_pay[extend - 1];
            ind += 1;
            extend -= 1;
        }

        int cnt = 0;
        while (cnt < dataBytes.length) {
            res[ind] = dataBytes[cnt];
            cnt += 1;
            ind += 1;
        }

        return res;
    }
}
