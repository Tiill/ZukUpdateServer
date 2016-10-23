package zukupdateserver;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Тиилл
 */
public class ZukUpdateServer {

    static float currentversionZuk = 1.8F;
    static String fileForDownload = "Zuk18.exe";
    static LinkedList<Programm> progList;

    static JTextArea maintext;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initcomponents();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                JFrame mainframe = new JFrame("Сервер обновлений для Зук");
                mainframe.setSize(700, 700);
                mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                maintext = new JTextArea();
                JPanel panel = new JPanel();
                JScrollPane scrolpanel = new JScrollPane(maintext);

                panel.setLayout(new BorderLayout());
                panel.add(scrolpanel, BorderLayout.CENTER);
                mainframe.setContentPane(panel);

                maintext.append("\r\n\r\n\r\nВключение программы в " + DateAndTime());
                maintext.append("\r\n\r\nОжидание подключения...");

                ThreadWaitClient workthread = new ThreadWaitClient();
                workthread.start();

                mainframe.setVisible(true);

                mainframe.addWindowListener(new WindowListener() {

                    @Override
                    public void windowOpened(WindowEvent e) {
                    }

                    @Override
                    public void windowClosing(WindowEvent e) {
                        FileOutputStream outlog = null;
                        try {
                            File logfile = new File("log.txt");
                            if (!logfile.exists()) {
                                logfile.createNewFile();
                            }
                            outlog = new FileOutputStream(logfile, true);
                            outlog.write(ZukUpdateServer.maintext.getText().getBytes());
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(ZukUpdateServer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(ZukUpdateServer.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                outlog.close();
                            } catch (IOException ex) {
                                Logger.getLogger(ZukUpdateServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.exit(0);
                        }
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                    }

                    @Override
                    public void windowIconified(WindowEvent e) {
                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {
                    }

                    @Override
                    public void windowActivated(WindowEvent e) {
                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {
                    }
                });

            }
        });

    }

    static String DateAndTime() {
        SimpleDateFormat dateform = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        return dateform.format(new Date());
    }

    static void initcomponents() {
        progList = new LinkedList<>();
        Programm prog1 = null;
        try {
            prog1 = new Programm(new File(fileForDownload), currentversionZuk, "zuk");
        } catch (Exception ex) {
            Logger.getLogger(ZukUpdateServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        progList.add(prog1);
    }

}
