package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ChessGraphData {
    public static URL url1 = ChessGraphData.class.getResource("BlackRevenger.png");
    public static URL url2 = ChessGraphData.class.getResource("WhiteRuler.png");
    public static URL url3= ChessGraphData.class.getResource("FateGrandOrder.png");


    public static URL url4 = ChessGraphData.class.getResource("pawn-white.png");
    public static URL url5 = ChessGraphData.class.getResource("pawn-black.png");
    public static URL url10 = ChessGraphData.class.getResource("king-white.png");
    public static URL url11 = ChessGraphData.class.getResource("king-black.png");
    public static URL url9 = ChessGraphData.class.getResource("Choose.png");


    public static URL url6 = ChessGraphData.class.getResource("CentralPowers.png");
    public static URL url7 = ChessGraphData.class.getResource("TripleEntente.png");
    public static URL url8 = ChessGraphData.class.getResource("WWI.png");





    public static Image BlackR;

    static {
        try {
            BlackR = ImageIO.read(url1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public static ImageIcon BlackRevenger = new ImageIcon(url1);
    public static ImageIcon WhiteRuler = new ImageIcon(url2);
    public static ImageIcon FateGrandOrder = new ImageIcon(url3);


    public static ImageIcon WhitePawn = new ImageIcon(url4);
    public static ImageIcon BlackPawn = new ImageIcon(url5);
    public static ImageIcon BlackKing = new ImageIcon(url11);
    public static ImageIcon WhiteKing = new ImageIcon(url10);
    public static ImageIcon Choose = new ImageIcon(url9);


    public static ImageIcon CentralPowers = new ImageIcon(url6);
    public static ImageIcon TripleEntente = new ImageIcon(url7);
    public static ImageIcon WWI = new ImageIcon(url8);

}
