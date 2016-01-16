package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * @author Marc Karassev
 */
public class JarFromPathsCreator {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String TEMP_FILE_NAME = "tmp.jar";

    // Methods

    public String createJarFromPaths(Map<String, String> pathsAndPackages) throws IOException {
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(TEMP_FILE_NAME));
        File reading;

        for (Map.Entry<String, String> path: pathsAndPackages.entrySet()) {
            reading = new File(path.getKey());
            addFileToJar(outputStream, reading, path.getValue().replace(".", "/") + "/");
        }
        outputStream.close();
        return TEMP_FILE_NAME;
    }

    private void addFileToJar(JarOutputStream outputStream, File file, String packagePath) throws IOException {
        LOGGER.debug(file.getPath() + "\t" + packagePath);
        if (file.getName().endsWith(".class")) {
            writeFile(outputStream, file, packagePath);
        }
        else if (file.isDirectory()) {
            String directoryPath = file.getPath().replace("\\", "/") + "/";

            if (directoryPath.endsWith(packagePath)) {
                for (File child: file.listFiles()) {
                    addFileToJar(outputStream, child, packagePath);
                }
            }
            else {
                for (File child: file.listFiles()) {
                    addFileToJar(outputStream, child, packagePath + file.getName() + "/");
                }
            }
        }
    }

    private void writeFile(JarOutputStream outputStream, File file, String packagePath) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[1024];
        int count;
        JarEntry entry = new JarEntry(packagePath + file.getName());

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
}
