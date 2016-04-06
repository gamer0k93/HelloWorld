package Kostya;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.*;

/**
 * Created by KOT on 03.04.2016.
 */

public class HelloWorld {
    private static final Logger logger = Logger.getLogger(HelloWorld.class);

    public static void main(String[] args) {
        ResourceBundle.Control myControl = createMyControl();//инициализирует переписаный Control под кодировку CP1251
        logger.info("Контролер создан");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("data", myControl);
        logger.info("Пакет ресурсов проинициализирован");

        int localTime = new Date().getHours();
        logger.info("Локальное время(" + localTime + ") получено");
        printAMessage(localTime, resourceBundle);
        logger.info("Сообщение приветствия выведено, конец программы");
    }

    public static ResourceBundle.Control createMyControl(){
        return new ResourceBundle.Control() {
            @Override
            public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                    throws IllegalAccessException, InstantiationException, IOException {
                String bundleName = toBundleName(baseName, locale);
                ResourceBundle bundle = null;
                if (format.equals("java.properties")) {
                    final String resourceName = toResourceName(bundleName, "properties");
                    final ClassLoader classLoader = loader;
                    final boolean reloadFlag = reload;
                    InputStream stream;
                    try {
                        stream = AccessController.doPrivileged(
                                new PrivilegedExceptionAction<InputStream>() {
                                    public InputStream run() throws IOException {
                                        InputStream is = null;
                                        if (reloadFlag) {
                                            URL url = classLoader.getResource(resourceName);
                                            if (url != null) {
                                                URLConnection connection = url.openConnection();
                                                if (connection != null) {
                                                    connection.setUseCaches(false);
                                                    is = connection.getInputStream();
                                                }
                                            }
                                        } else {
                                            is = classLoader.getResourceAsStream(resourceName);
                                        }
                                        return is;
                                    }
                                });
                    } catch (PrivilegedActionException e) {
                        throw (IOException) e.getException();
                    }
                    if (stream != null) {
                        InputStreamReader reader = null;
                        try {
                            reader = new InputStreamReader(stream, "CP1251");
                            bundle = new PropertyResourceBundle(reader);
                        } finally {
                            stream.close();
                            if (reader != null) {
                                reader.close();
                            }
                        }
                    }
                } else {
                    throw new IllegalArgumentException("unknown format: " + format);
                }
                return bundle;
            }
        };
    }
    public static void printAMessage(int localTime, ResourceBundle resourceBundle){
        if((localTime >= 23) || (localTime < 6)){
            System.out.print(resourceBundle.getString("night"));
        }else if(localTime < 9){
            System.out.print(resourceBundle.getString("morning"));
        }else if(localTime < 19){
            System.out.print(resourceBundle.getString("day"));
        }else if(localTime < 23){
            System.out.print(resourceBundle.getString("evening"));
        }
        String userName = System.getenv().get("USERNAME");
        logger.info("Имя пользователя(\"" + userName + "\") получено");
        System.out.println(" " + userName + "!");
    }
}
