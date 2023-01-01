import java.io.*;
import java.net.*;
public class Client {

    private static BufferedReader br;
    private static BufferedWriter bw;
    private static Socket s;
    public static Client client;
    public Client(Socket socket){
        this.s = socket;
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String arg[]) {
        try {
            s = new Socket("localhost", 3200);
            client = new Client(s);
            String[] receivedMessage;
            do {
                receivedMessage = client.receiveMessage().split(",");
                new Thread(new WatchClientFolder(s, receivedMessage[1],receivedMessage[0])).start();
            }
            while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMessage(){
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String data){
        try {
            bw.write(data);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
