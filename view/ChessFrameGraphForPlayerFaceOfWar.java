package view;

import model.ChessColor;

import javax.swing.*;
import java.awt.*;

public class ChessFrameGraphForPlayerFaceOfWar extends JPanel{
    private Color color;
    private final Chessboard chessboard;


    public ChessFrameGraphForPlayerFaceOfWar(Color color, Chessboard chessboard){
        this.color = color;
        this.chessboard = chessboard;

        this.setVisible(true);
        if(this.color == Color.WHITE){
            this.setBounds(0, 0, 385, 964);
        }
        if(this.color == Color.BLACK){
            this.setBounds(1153, 0, 374, 964);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //画出黑贞，作为黑棋的代表方
        if(color == Color.BLACK){
            ChessGraphData.CentralPowers.paintIcon(this, g, 0, 0);
            if(this.chessboard.getCurrentColor() == ChessColor.BLACK){
                g.setColor(Color.gray);
                g.fillRect(96, 1, 208, 47);
                g.setColor(new Color(208, 11, 11));
                g.drawRect(96, 1, 208, 47);
                g.setFont(new Font("Norse", Font.BOLD, 40));
                g.drawString("CentralPowers", 99, 38);
                repaint();
            }
            repaint();
        }
        //画出白贞，作为白棋的代表方
        if(color == Color.WHITE){
            ChessGraphData.TripleEntente.paintIcon(this, g, 0, 0);
            if(this.chessboard.getCurrentColor() == ChessColor.WHITE){
                g.setColor(Color.gray);
                g.fillRect(70, 1, 205, 44);
                g.setColor(new Color(208, 11, 11));
                g.drawRect(70, 1, 205, 44);
                g.setFont(new Font("Norse", Font.BOLD, 40));
                g.drawString("TripleEntente", 77, 38);
                repaint();
            }
            repaint();
        }
    }



}
