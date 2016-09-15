package me.xdrop.passlock.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GUtils {

    public static Properties loadPropertiesFile(String path) throws IOException {

        Properties properties = new Properties();
        InputStream in;

        in = new FileInputStream(path);
        properties.load(in);

        return properties;

    }

    public static boolean createIfDoesntExist(String path) {

        File file = new File(path);

        if(file.exists()) return false;

        if(file.isDirectory()) {
            return file.mkdirs();
        } else{
            return file.getParentFile().mkdirs();
        }

    }

}
