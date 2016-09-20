package me.xdrop.passlock.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class GUtils {

    public static Properties loadPropertiesFile(String path) throws IOException {

        Properties properties = new Properties();
        InputStream in;

        in = new FileInputStream(path);
        properties.load(in);

        return properties;

    }

    public static String resolvePath(String path) {
        return path.replaceFirst("^~",System.getProperty("user.home"));
    }

    public static boolean createIfDoesntExist(String path) {

        /* Unix and friends */
        path = resolvePath(path);

        Path pathToFile = Paths.get(path);

        try {

            Files.createDirectories(pathToFile.getParent());
            Files.createFile(pathToFile);

            return true;

        } catch (FileAlreadyExistsException fa) {

            /* File already existed */
            return false;

        } catch (IOException ignored) {

            return false;

        }


    }

}
