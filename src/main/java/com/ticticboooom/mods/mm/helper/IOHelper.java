package com.ticticboooom.mods.mm.helper;

import com.ticticboooom.mods.mm.MM;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOHelper {
    public static File getFileAndCreate(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                MM.LOG.fatal("failed to create path: {}", path.toAbsolutePath());
            }
        }
        return path.toFile();
    }
}
