import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ServerThread implements Runnable {
    public static ArrayList<Object[]> listWatchFolder;
    private ServerView serverView;
    private Socket s;
    private ArrayList<Socket> listClient;
    private HashMap<String, Socket> map;

    public ServerThread(Socket s, ArrayList<Socket> listClient, ArrayList<Object[]> listWatchFolder) {
        this.serverView = ServerHandle.serverView;
        this.s = s;
        this.listClient = listClient;
        this.listWatchFolder = listWatchFolder;

    }
    @Override
    public void run() {
        InputStream is = null;
        BufferedReader br = null;
        String receivedMessage;

        try {
            is = s.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            while (true) {
                receivedMessage = br.readLine();
                if (receivedMessage != null) {
                    String[] message = receivedMessage.split(",");
                    System.out.println(receivedMessage);
                    String port = message[0];
                    String path = message[1];
                    String date = message[2];
                    String status = message[3];
                    String content = message[4];
                    Object[] objWF = new Object[]{port, path, date, status, content};
                    listWatchFolder.add(objWF);
                    serverView.modelFolder.addRow(objWF);
                }
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
