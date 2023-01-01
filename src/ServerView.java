import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerView {
    private JFrame window;
    public JButton btnQuit;
    public DefaultTableModel modelClient;
    public JTable tableClient;
    public DefaultTableModel modelFolder;
    public JTable tableFolder;

    public ServerView() {
        window = new JFrame();
        window.setTitle("Java Networking");
        window.setSize(1000, 600);
        window.setLocationRelativeTo(null);

        this.View();

        window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public void View() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        //header
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout());

        Label label_title = new Label("JAVA NETWORKING WATCH FOLDER", Label.CENTER);
        header.add(label_title);

        jPanel.add(header, BorderLayout.NORTH);
        //footer
        JPanel footer = new JPanel();
        footer.setLayout(new FlowLayout());

        btnQuit = new JButton("Quit");
        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        footer.add(btnQuit);

        jPanel.add(footer, BorderLayout.SOUTH);
        //center

        JPanel jPanel_center = new JPanel();
        jPanel_center.setLayout(new BorderLayout());

        String[] columnClient = {"port"};
        modelClient = new DefaultTableModel(null, columnClient);
        tableClient = new JTable(modelClient);

        tableClient.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableClient.rowAtPoint(e.getPoint());
                Object port = tableClient.getValueAt(row, 0);

                Socket sss = null;
                for (Socket socket : ServerHandle.listClient) {
                    if (socket.getPort() == Integer.parseInt(port.toString())) {
                        sss = socket;
                        break;
                    }
                }
                // đoạn code thảm khảo trên mạng
                JFileChooser myfileChooser = new JFileChooser();
                myfileChooser.setDialogTitle("select folder");
                if (Files.isDirectory(Paths.get("D:\\"))) {
                    myfileChooser.setCurrentDirectory(new File("D:\\"));
                }
                int findresult = myfileChooser.showOpenDialog(window);
                if (findresult == myfileChooser.APPROVE_OPTION) {
                    String pathClient = myfileChooser.getCurrentDirectory().getAbsolutePath();
                    try {
                        OutputStream os = sss.getOutputStream();
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

                        bw.write(pathClient + "," + sss.getPort());
                        bw.newLine();
                        bw.flush();

//                        bw.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                ServerHandle.listClient.remove(sss);
                modelClient.removeRow(row);
            }
        });

        String[] columnFolder = {"port", "path", "date", "status", "content"};
        modelFolder = new DefaultTableModel(null, columnFolder);
        tableFolder = new JTable(modelFolder);

        tableFolder.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableFolder.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableFolder.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableFolder.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableFolder.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableFolder.getColumnModel().getColumn(4).setPreferredWidth(430);

        JScrollPane jScrollPane = new JScrollPane(tableClient);

        final Dimension size = new Dimension(100, Short.MAX_VALUE);
        jScrollPane.setPreferredSize(size);

        jPanel_center.add(jScrollPane, BorderLayout.WEST);
        jPanel_center.add(new JScrollPane(tableFolder), BorderLayout.CENTER);

        jPanel.add(jPanel_center, BorderLayout.CENTER);

        // set content
        window.setContentPane(jPanel);
        window.setVisible(true);
    }
}
