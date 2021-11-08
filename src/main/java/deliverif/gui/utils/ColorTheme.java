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
            add(new Color(0xf032e6));
            add(new Color(0x5CE9FF));
            add(new Color(0x009758));
            add(new Color(0x7BE85D));
            add(new Color(0x53231B));
            add(new Color(0xF75C03));
            add(new Color(0x691AC1));
            add(new Color(0x8F7C00));
            add(new Color(0x765379));
            add(new Color(0xA001A0));
            add(new Color(0xCF1259));
            add(new Color(0x800000));
            add(new Color(0x808000));
            add(new Color(0x469990));
            add(new Color(0x000075));
            add(new Color(0xE55276));
            add(new Color(0xE79055));
            add(new Color(0xbfef45));
            add(new Color(0xC46DDB));
            add(new Color(0x808080));
        }
    };

    public static final Color DEPARTURE_COLOR = new Color(255, 0, 0);

    // general theme settings
    public static final Color GENERAL_BASE_BG = LIGHT_BASE_GREY;
    public static final Color PANEL_1_BASE_BG = Color.WHITE;
    public static final Color PANEL_2_BASE_BG = Color.WHITE;

    public static final Color BOX_GENERAL_BASE_BG = BOX_BG_LIGHT_BASE_GREY;
}
