package deliverif.gui.utils;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorTheme {
    // palette1: https://paletton.com/#uid=3170T0k2ljc6hnI3vhb50fr6wdx

    // palette 1: base colors for panels
    public static Color MEDIUM_BASE_GREY = new Color(0x616966);
    public static Color LIGHT_BASE_GREY = new Color(0x99968E);
    public static Color MEDIUM_BASE_PURPLE = new Color(0x635E65);

    public static Color BOX_BG_LIGHT_BASE_GREY = new Color(0x89857A);

    public static List<Color> REQUEST_PALETTE = new ArrayList<>(){
        {
            add(new Color(0x197278));
            add(new Color(0x5CE9FF));
            add(new Color(0x009758));
            add(new Color(0x7BE85D));
            add(new Color(0x772E25));
            add(new Color(0xF75C03));
            add(new Color(0x691AC1));
            add(new Color(0x765379));
            add(new Color(0xA001A0));
            add(new Color(0xCF1259));
        }
    };

    // general theme settings
    public static Color GENERAL_BASE_BG = LIGHT_BASE_GREY;
    public static Color PANEL_1_BASE_BG = MEDIUM_BASE_GREY;
    public static Color PANEL_2_BASE_BG = MEDIUM_BASE_PURPLE;

    public static Color BOX_GENERAL_BASE_BG = BOX_BG_LIGHT_BASE_GREY;
}
