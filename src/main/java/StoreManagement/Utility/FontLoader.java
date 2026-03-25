package StoreManagement.Utility;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {
//    public static Font load(String path, float size) {
//        try {
//            File fontFile = new File(path);
//            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
//            return font;
//        } catch (FontFormatException | IOException e) {
//            e.printStackTrace();
//            return new Font("resources/Fonts/HelveticaNeue.ttf", Font.PLAIN, (int) size);
////            return FontLoader.load("resources/Fonts/HelveticaNeue.ttf", (int) size);
//        }
        

    public static Font load(String resourcePath, float size) {
        try (InputStream is = FontLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Font not found in resources: " + resourcePath);
                return new Font("Arial", Font.PLAIN, (int) size);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size);
        }
    }
}