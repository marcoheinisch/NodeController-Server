package io.github.marcoheinisch;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class SaveVar {
    private static String FILE = "settings.txt";
    private Properties p;


    static public void saveThis(String key, String value)  {
        Properties p = loadData();
        p.setProperty(key, value);
        saveData(p);
    }

    static public void saveThis(String key, String[] valuearray)  {
        Properties p = loadData();
        String value = valuearray[0];
        for(int i=1; i<valuearray.length;i++){
            value += ";" + valuearray[i];
        }
        p.setProperty(key, value);
        saveData(p);
    }

    static public Object getThis(String key){
        Properties p = loadData();
        return p.get(key);
    }

    static public String[] getArray(String key){
        String array = String.valueOf(getThis(key));
        return array.split(";");
    }



    static private void saveData(Properties p)  {
        try {
            FileWriter fw = new FileWriter(FILE);
            p.store(fw,FILE);
            fw.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    static private Properties loadData()  {
        Properties p = new Properties();
        try {
            FileReader fr = new FileReader(FILE);
            p.load(fr);
            fr.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return p;
    }
}
