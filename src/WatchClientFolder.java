import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WatchClientFolder implements Runnable {
    public static WatchService watchService;
    private Socket s;
    private String path;
    private String port;


    public WatchClientFolder(Socket s, String port, String path) {
        this.s = s;
        this.path = path;
        this.port = port;
    }

    public void dispose() throws IOException {
        watchService.close();
    }

    public void run() {
        DateFormat dateFormat;
        Date date;
        String data = "";

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        data = port + "," + path + "," + dateFormat.format(date).toString() + "," + "Connect" + "," + "A new client is connected";
        Client.client.sendMessage(data);

        // đoạn code tham khảo trên mạng

        try {
            // STEP1: Create a watch service
            WatchService watchService = FileSystems.getDefault().newWatchService();

            // STEP2: Get the path of the directory which you want to monitor.
            Path directory = Path.of(path);

            // STEP3: Register the directory with the watch service
            WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            // STEP4: Poll for events
            while (true) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {

                    // STEP5: Get file name from even context
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

                    Path fileName = pathEvent.context();

                    // STEP6: Check type of event.
                    WatchEvent.Kind<?> kind = event.kind();

                    // STEP7: Perform necessary action with the event
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        date = new Date();
                        data = port + "," + path + "," + dateFormat.format(date).toString() + "," + "Created" + "," + "A new file is created : " + fileName;
                        Client.client.sendMessage(data);
                    }

                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {

                        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        date = new Date();

                        data = port + "," + path + "," + dateFormat.format(date).toString() + "," + "Deleted" + "," + "A file has been deleted : " + fileName;
                        Client.client.sendMessage(data);

                    }
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {

                        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        date = new Date();

                        data = port + "," + path + "," + dateFormat.format(date).toString() + "," + "Modified" + "," + "A file has been modified : " + fileName;
                        Client.client.sendMessage(data);
                    }
                    System.out.println(data);
                    TimeUnit.SECONDS.sleep(1);
                }

                // STEP8: Reset the watch key everytime for continuing to use it for further
                // event polling
                boolean valid = watchKey.reset();
                if (!valid) {
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
