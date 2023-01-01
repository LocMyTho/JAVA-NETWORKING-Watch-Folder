public class Server {
    public static void main(String arg[]) {
        new Thread(new ServerHandle()).start();
    }
}