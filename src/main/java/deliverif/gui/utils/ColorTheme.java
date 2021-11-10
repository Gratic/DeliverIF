package deliverif.gui.utils;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorTheme {
    // palette1: https://paletton.com/#uid=3170T0k2ljc6hnI3vhb50fr6wdx

    // palette 1: base colors for panels
    public static Color MEDIUM_BASE_GREY = new Color(0x616966);
    public static final Color LIGHT_BASE_GREY = new Color(0x99968E);
    public static final Color LIGHT_BASE_GREY_ALT = new Color(0xC7C5BE);
    public static Color MEDIUM_BASE_PURPLE = new Color(0x635E65);

    public static final Color BOX_BG_LIGHT_BASE_GREY = new Color(0x89857A);

    public static final List<Color> REQUEST_PALETTE = new ArrayList<>() {
        {
            add(new Color(0x197278));
            add(new Color(0xFF90004A, true));
            add(new Color(0x018293));
            add(new Color(0x00653C));
            add(new Color(0x24B400));
            add(new Color(0x53231B));
            add(new Color(0xA44200));
            add(new Color(0x320069));
            add(new Color(0x8F7C00));
            add(new Color(0x765379));
            add(new Color(0x330233));
            add(new Color(0xFF6969));
            add(new Color(0x800000));
            add(new Color(0x808000));
            add(new Color(0x469990));
            add(new Color(0x000075));
            add(new Color(0xE55276));
            add(new Color(0xE79055));
            add(new Color(0x6DEDFF));
            add(new Color(0xC46DDB));
            add(new Color(0x808080));

        }
    };

    public static final Color DEPARTURE_COLOR = new Color(255, 0, 0);

    // general theme settings
    public static Color GENERAL_BASE_BG = Color.WHITE;
    public static Color PANEL_1_BASE_BG = Color.WHITE;
    public static Color PANEL_2_BASE_BG = new Color(0xdddddd);
    public static Color TEXT_COLOR = new Color(23, 43, 77);
    public static Color TEXT_COLOR_HIGHLIGHTED = new Color(0xf67280);

    public static Color BOX_GENERAL_BASE_BG = new Color(0xeeeeee);
}
