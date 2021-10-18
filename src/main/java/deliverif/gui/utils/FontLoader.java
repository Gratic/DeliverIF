package deliverif.gui.utils;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;

public class FontLoader {

    public static Font loadFontToSize(Path path, float size) {
        Font font = loadFont(path);
        font = resizeFont(font, size);
        return font;
    }

    private static Font loadFont(Path path) {
        Font loadFont;
        try {
            // read file
            File file = path.toFile();
            //FontLoader.class.getResourceAsStream(path)
            loadFont = Font.createFont(Font.TRUETYPE_FONT, file);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println(e);
            loadFont = new Font("Arial", Font.BOLD, 24);
        }
        return loadFont;
    }

    private static Font resizeFont(Font givenFont, float size) {
        return givenFont.deriveFont(Font.PLAIN, size);
    }

}