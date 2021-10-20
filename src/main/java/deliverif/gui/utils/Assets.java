package deliverif.gui.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This class contains assets for the program.
 * WARN: It's content need to be initialised once at app load.
 */
public class Assets {

    public static Font expletusSans, minecraftia;
    public static BufferedImage deliveryIcon, pickupIcon, departureIcon, loadMapIcon;

    public static void init() {
        // fonts
        expletusSans = FontLoader.loadFontToSize(
                Path.of("resources/fonts/ExpletusSans-SemiBold.ttf"), 28f);
        //Minecraftia-Regular.ttf
        minecraftia = FontLoader.loadFontToSize(
                Path.of("resources/fonts/Minecraftia-Regular.ttf"), 28f);


        try {
            deliveryIcon = ImageIO.read(new File("resources/assets/icons/delivery.png"));
            pickupIcon = ImageIO.read(new File("resources/assets/icons/pickup.png"));
            departureIcon = ImageIO.read(new File("resources/assets/icons/departure.png"));
            loadMapIcon = ImageIO.read(new File("resources/assets/icons/loadMap.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
