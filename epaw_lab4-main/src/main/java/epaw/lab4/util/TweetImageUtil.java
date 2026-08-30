package epaw.lab4.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import jakarta.servlet.http.Part;

public final class TweetImageUtil {

	private TweetImageUtil() {
	}

	public static String save(Part imagePart, String username) {
		try {
			String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
			String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.')) : "";
			String baseName = username.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + System.currentTimeMillis();
			String newFileName = baseName + extension;

			String resourcesDir = "EXTERNAL_RESOURCES";
			Files.createDirectories(Paths.get(resourcesDir));
			try (InputStream input = imagePart.getInputStream()) {
				Files.copy(input, Paths.get(resourcesDir, newFileName), StandardCopyOption.REPLACE_EXISTING);
			}
			return newFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
