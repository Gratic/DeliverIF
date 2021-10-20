package deliverif.gui.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    public static BufferedImage deliveryImage, pickupImage, departureImage, loadMapImage, addRequestImage, editRequestImage, loadRequestImage, removeRequestImage;
    public static ImageIcon deliveryIcon, pickupIcon, departureIcon, loadMapIcon, addRequestIcon, editRequestIcon, loadRequestIcon, removeRequestIcon;

    public static void init() {
        // fonts
        expletusSans = FontLoader.loadFontToSize(
                Path.of("resources/fonts/ExpletusSans-SemiBold.ttf"), 28f);
        //Minecraftia-Regular.ttf
        minecraftia = FontLoader.loadFontToSize(
                Path.of("resources/fonts/Minecraftia-Regular.ttf"), 28f);


        System.out.println("t");

        try {
            deliveryImage = ImageIO.read(new File("resources/assets/icons/delivery.png"));
            pickupImage = ImageIO.read(new File("resources/assets/icons/pickup.png"));
            departureImage = ImageIO.read(new File("resources/assets/icons/departure.png"));
            loadMapImage = ImageIO.read(new File("resources/assets/icons/loadMap.png"));
            addRequestImage = ImageIO.read(new File("resources/assets/icons/addRequest.png"));
            editRequestImage = ImageIO.read(new File("resources/assets/icons/editRequest.png"));
            loadRequestImage = ImageIO.read(new File("resources/assets/icons/loadRequest.png"));
            removeRequestImage = ImageIO.read(new File("resources/assets/icons/removeRequest.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        deliveryIcon = new ImageIcon(deliveryImage);
        pickupIcon = new ImageIcon(pickupImage);
        departureIcon = new ImageIcon(departureImage);
        loadMapIcon = new ImageIcon(loadMapImage);
        addRequestIcon = new ImageIcon(addRequestImage);
        editRequestIcon = new ImageIcon(editRequestImage);
        loadRequestIcon = new ImageIcon(loadRequestImage);
        removeRequestIcon = new ImageIcon(removeRequestImage);


    }


}
