import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandle implements Runnable {
    public static ServerView serverView;
    public static ArrayList<Socket> listClient;
    public static ArrayList<Object[]> listWatchFolder;
    private Socket s;

    public ServerHandle() {

    }

    @Override
    public void run() {
        try {
            serverView = new ServerView();
            listClient = new ArrayList<Socket>();
            listWatchFolder = new ArrayList<Object[]>();

            ServerSocket ss = new ServerSocket(3200);
            while (true) {
                s = ss.accept();
                listClient.add(s);
                serverView.modelClient.addRow(new Object[]{s.getPort()});
                new Thread(new ServerThread(s, listClient, listWatchFolder)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
