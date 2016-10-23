package zukupdateserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static zukupdateserver.ZukUpdateServer.maintext;

/**
 *
 * @author Тиилл
 */
public class ThreadWaitClient extends Thread {
    
    

    @Override
    public void run() {
        try {
            SimpleDateFormat dateform = new SimpleDateFormat("dd.MM.yyyy hh:mm");
            
            ServerSocket server = new ServerSocket(732);
            while (true) {
                Socket client = server.accept();

                maintext.append("\r\n\r\nПодключился клиент: " + client.getInetAddress() + "\t" + dateform.format(new Date()));
                
                new Thread(new WorkThread(client, ZukUpdateServer.progList)).start();
                
                
            }   } catch (IOException ex) {
            Logger.getLogger(ThreadWaitClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception ex){
            Logger.getLogger(ThreadWaitClient.class.getName()).log(Level.SEVERE, null, ex);
        }

}
}

class ZaprosException extends Exception{
}

//    @Override
//    public void run() {
//        SimpleDateFormat dateform = new SimpleDateFormat("dd.MM.yyyy hh:mm");
//
//        try {
//            ServerSocket server = new ServerSocket(732);
//            while (true) {
//                Socket client = server.accept();
//
//                maintext.append("\r\n\r\nПодключился клиент: " + client.getInetAddress() + "\t" + dateform.format(new Date()));
//                
//                InputStream clientin = client.getInputStream();
//                OutputStream clientout = client.getOutputStream();
//                FileInputStream fin = null;
//
////Получение запроса от клиента
//                String answer;
//                if (clientin.available() > 0)                                   //Можно обойтись без проверки и перейти сразу к else
//                {
//                    byte bpos[] = new byte[clientin.available()];
//                    clientin.read(bpos);
//                    answer = new String(bpos);
//                }else {
//                    client.setSoTimeout(7000);
////собственный способ ожидания данных
//                    byte buffer = (byte)clientin.read();                        //исключение
//                    byte bpos[] = new byte[clientin.available() + 1];
//                    bpos[0] = buffer;
//                    clientin.read(bpos, 1, bpos.length-1);
//                    answer = new String(bpos);
//                    
////с помощью библиотеки ожидания данных
////                    PushbackInputStream pin = new PushbackInputStream(clientin);
////                    int readp = pin.read();
////                    pin.unread(readp);
////                    byte bpos [] = new byte [pin.available()];
////                    pin.read(bpos);
////                    answer = new String(bpos);
//                }
//                try{
//                
//                    if(answer == null) {
//                        maintext.append("\r\nОшибка: не сформирован запрос клиента.\r\n\r\n\r\nОжидание подключения...");
//                        throw new ZaprosException();
//                    }
//                maintext.append("\r\nЗапрос клиентом: " + answer );
//                
//                if(answer.equals("")){
//                    maintext.append("\r\nОшибка: пустой запрос клиента.\r\n\r\n\r\nОжидание подключения...");
//                    throw new ZaprosException();
//                }
//                    
//                StringTokenizer parser = new StringTokenizer(answer, "#");
//                
//                if (!"zuk".equals(parser.nextToken())){
//                    maintext.append("\r\nОшибка: неверный запрос.\r\n\r\n\r\nОжидание подключения...");
//                    throw new ZaprosException();
//                }
//                
//                String token = parser.nextToken();
//                if ("zapros".equals(token)){
//                
//                double pasd = Double.parseDouble(parser.nextToken());
//                if (ZukUpdateServer.currentversionZuk < pasd){
//                    maintext.append("\r\nОшибка: ВЕРСИЯ ВЫШЕ ТЕКУЩЕЙ.");
//                    throw new ZaprosException();
//                }else if (ZukUpdateServer.currentversionZuk == pasd){
//                    clientout.write("#no#".getBytes());
//                    maintext.append("\r\nОтвет клиенту: Запрещено");
//                    clientout.flush();
//                }else if (ZukUpdateServer.currentversionZuk > pasd){
//                    clientout.write("#ok#".getBytes());
//                    clientout.flush();
//                    maintext.append("\r\nОтвет клиенту: Разрешено");
//                }
//                } else if ("download".equals(token)){
//                    maintext.append("\r\nЗапрос клиентом: скачивание новой версии.");
//                    File fileforupload = new File(ZukUpdateServer.fileForDownload);
//                    fin = new FileInputStream(fileforupload);
//                    
//                    String otvet = '#' + fileforupload.getName() + '#' + fileforupload.length() + '#';
//                    maintext.append("\r\n" + otvet);
//                    clientout.write(otvet.getBytes());
//                    clientout.flush();
//                    
//                    byte buffer = (byte)clientin.read();                        //исключение
//                    byte bpos[] = new byte[clientin.available() + 1];
//                    bpos[0] = buffer;
//                    clientin.read(bpos, 1, bpos.length-1);
//                    answer = new String(bpos);
//                    
//                    if(!"#zuk#startDownload#".equals(answer)){
//                        maintext.append("\r\nОшибка: неверное подтверждение скачивания файла" + ZukUpdateServer.DateAndTime());
//                        throw new ZaprosException();
//                    }
//                    
//                    long filesize = fileforupload.length();
//            
//                    long countreading = filesize / 8192;
//                    long ostatokreading = filesize % 8192;
//            maintext.append("\r\nОтправка файла " + ZukUpdateServer.DateAndTime());
//            
//            byte [] readingposrednik = new byte [8192];
//            
//            for (int ix = 0; ix < countreading; ix++){
//                fin.read(readingposrednik);
//                clientout.write(readingposrednik);
//                clientout.flush();
//                
//            }
//            byte [] readingposrednikostatok = new byte [(int)ostatokreading];
//                fin.read(readingposrednikostatok);
//                clientout.write(readingposrednikostatok);
//                clientout.flush();
//                    maintext.append("\r\nОтправка файла завершена " + ZukUpdateServer.DateAndTime());
//                }
//                maintext.append("\r\nОтключился от клиента: " + 
//                        client.getInetAddress() + "\t" + 
//                        dateform.format(new Date()) + 
//                        "\r\n\r\n\r\nОжидание подключения...\r\n");
//                }catch(ZaprosException ex){
//                }
//                
//                
//                if(fin != null){
//                    fin.close();
//                }
//                client.close();
//                clientin.close();
//                clientout.close();
//
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(ThreadWaitClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }