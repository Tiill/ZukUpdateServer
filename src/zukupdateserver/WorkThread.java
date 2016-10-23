package zukupdateserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static zukupdateserver.ZukUpdateServer.maintext;

/**
 *
 * @author Тиилл
 */
public class WorkThread implements Runnable {

    private Socket client;
    private LinkedList<Programm> progList;

    public WorkThread(Socket client, LinkedList<Programm> progList) {
        this.client = client;
        this.progList = progList;
    }

    @Override
    public void run() {

        InputStream clientin = null;
        OutputStream clientout = null;
        try {
            clientin = client.getInputStream();
            clientout = client.getOutputStream();
            String answer;
            {
                client.setSoTimeout(7000);
                byte buffer = (byte) clientin.read();
                byte bpos[] = new byte[clientin.available() + 1];
                bpos[0] = buffer;
                clientin.read(bpos, 1, bpos.length - 1);
                answer = new String(bpos);
            }
            String[] answermass = answer.split("#");
            Programm currentpr = null;
            for (Programm i : progList) {
                if (i.getName().equals(answermass[1])) {
                    currentpr = i;
                }
            }

            if ("zapros".equals(answermass[2])) {
                if (Float.parseFloat(answermass[3]) == currentpr.getVersion()) {
                    clientout.write("#no#".getBytes());
                } else {
                    clientout.write("#ok#".getBytes());
                }
                clientout.flush();
                maintext.append("\r\nПоток обработал запрос о версиию. От клиента :" + client);
                return;
            } else if ("download".equals(answermass[2])) {
                clientout.write(("#" + currentpr.getFileName() + "#" + currentpr.getProgrammData().length + "#").getBytes());
                clientout.flush();

                byte buffer = (byte) clientin.read();                        //исключение
                byte bpos[] = new byte[clientin.available() + 1];
                bpos[0] = buffer;
                clientin.read(bpos, 1, bpos.length - 1);
                

                clientout.write(currentpr.getProgrammData());
                clientout.flush();
                maintext.append("\r\nПоток обработал запрос на скачивание. От клиента :" + client);
                return;
            }

            maintext.append("\r\nПоток не обработал запрос. От клиента :" + client);
        } catch (IOException ex) {
            Logger.getLogger(WorkThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                clientin.close();
            } catch (IOException ex) {
                Logger.getLogger(WorkThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
