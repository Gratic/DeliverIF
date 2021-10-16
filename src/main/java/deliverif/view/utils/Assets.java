package deliverif.view.utils;

import java.awt.*;

/**
 * This class contains assets for the program.
 * WARN: It's content need to be initialised once at app load.
 */
public class Assets {

    public static Font expletusSans, minecraftia;

    public static void init() {
        // fonts
        expletusSans = FontLoader.loadFontToSize(
                "./fonts/ExpletusSans-SemiBold.ttf",
                28f
        );
        //Minecraftia-Regular.ttf
        minecraftia = FontLoader.loadFontToSize(
                "./fonts/Minecraftia-Regular.ttf",
                28f
        );


    }
}
