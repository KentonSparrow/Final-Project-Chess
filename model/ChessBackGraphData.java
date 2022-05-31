package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ChessBackGraphData {
    public static URL url1 = ChessBackGraphData.class.getResource("BlackBack.png");
    public static URL url2 = ChessBackGraphData.class.getResource("WhiteBack.png");



    public static Image BlackBack;
    static {
        try {
            BlackBack = ImageIO.read(url1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image WhiteBack;
    static {
        try {
            WhiteBack = ImageIO.read(url2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
