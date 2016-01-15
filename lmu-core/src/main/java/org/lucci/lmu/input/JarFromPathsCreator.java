package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * @author Marc Karassev
 */
public class JarFromPathsCreator {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String TEMP_FILE_NAME = "tmp.jar";

    // Methods

    public String createJarFromPaths(List<String> paths) throws IOException {
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(TEMP_FILE_NAME));
        File reading;

        for (String path: paths) {
            reading = new File(path);
            addFileToJar(outputStream, reading);
        }
        outputStream.close();
        return TEMP_FILE_NAME;
    }

    private void addFileToJar(JarOutputStream outputStream, File file) throws IOException {
        JarEntry entry;

        if (file.getName().endsWith(".class")) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int count;

            entry = new JarEntry(file.getPath().replace("\\", "/"));
            entry.setTime(file.lastModified());
            outputStream.putNextEntry(entry);
            count = in.read(buffer);
            while (count >= 0) {
                outputStream.write(buffer, 0, count);
                count = in.read(buffer);
            }
            outputStream.closeEntry();
            in.close();
        }
        else if (file.isDirectory()) {
            String name = file.getPath().replace("\\", "/");

            if (!name.endsWith("/")) {
                name += "/";
            }
            entry = new JarEntry(name);
            entry.setTime(file.lastModified());
            outputStream.putNextEntry(entry);
            outputStream.closeEntry();
            for (File child: file.listFiles()) {
                addFileToJar(outputStream, child);
            }
        }
    }
}
