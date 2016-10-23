package zukupdateserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author Тиилл
 */
public class Programm {

    private File instance;
    private float version;
    private String name;
    private byte[] programmData;

    public Programm(File instance, float version, String name) throws Exception {
        if (!instance.exists()) {
            throw new Exception("File " + instance + "do not exist. Can not construct object!");
        }
        if (version < 0) {
            version = 0;
        }
        if (name == null) {
            name = "?";
        }

        this.instance = instance;
        this.version = version;
        this.name = name;

        long freemem = Runtime.getRuntime().freeMemory();
        long needmem = instance.length();
        if (freemem > needmem) {
            programmData = new byte[(int) instance.length()];
            InputStream input = new BufferedInputStream(new FileInputStream(instance));
            input.read(programmData);
        } else {
            throw new Exception("No more memory too construct object!");
        }

    }
    
    public String getFileName(){
        return instance.getName();
    }

    public String getName() {
        return new String(name);
    }

    public float getVersion() {
        return version;
    }

    public byte[] getProgrammData() {
            return programmData;
    }
    
    public static Programm parseProgramm(File fileini){
        
        return null;
    }

}