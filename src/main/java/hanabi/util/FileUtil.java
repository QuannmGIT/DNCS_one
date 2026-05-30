package hanabi.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUtil {

    private static final String UPLOAD_DIR = "src/main/resources/hanabi/assets/uploads";

    public static String uploadFile(File sourceFile) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = sourceFile.getName();
        String newName = UUID.randomUUID().toString() + "_" + originalName;
        Path targetPath = uploadPath.resolve(newName);

        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return "uploads/" + newName;
    }

    public static File getUploadedFile(String relativePath) {
        return Paths.get("src/main/resources/hanabi/assets", relativePath).toFile();
    }
}
