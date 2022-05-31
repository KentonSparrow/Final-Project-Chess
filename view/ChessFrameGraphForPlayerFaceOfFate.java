package view;

import model.ChessColor;

import javax.swing.*;
import java.awt.*;

public class ChessFrameGraphForPlayerFaceOfFate extends JPanel{
    private Color color;
    private final Chessboard chessboard;



    public ChessFrameGraphForPlayerFaceOfFate(Color color, Chessboard chessboard){
        this.color = color;
        this.chessboard = chessboard;

        this.setVisible(true);
        if(this.color == Color.WHITE){
            this.setBounds(0, 0, 385, 964);
        }
        if(this.color == Color.BLACK){
            this.setBounds(1153, 0, 376, 964);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //画出黑贞，作为黑棋的代表方
        if(color == Color.BLACK){
            ChessGraphData.BlackRevenger.paintIcon(this, g, 0, 0);
            if(this.chessboard.getCurrentColor() == ChessColor.BLACK){
                g.setColor(Color.white);
                g.fillRect(83, 1, 208, 47);
                g.setColor(new Color(208, 11, 11));
                g.drawRect(83, 1, 208, 47);
                g.setFont(new Font("Norse", Font.BOLD, 40));
                g.drawString("PlayerBlack", 106, 38);
                repaint();
            }
            repaint();
        }
        //画出白贞，作为白棋的代表方
        if(color == Color.WHITE){
            ChessGraphData.WhiteRuler.paintIcon(this, g, 0, 0);
            if(this.chessboard.getCurrentColor() == ChessColor.WHITE){
                g.setColor(Color.white);
                g.fillRect(57, 1, 205, 44);
                g.setColor(Color.ORANGE);
                g.drawRect(57, 1, 205, 44);
                g.setFont(new Font("Norse", Font.BOLD, 40));
                g.drawString("PlayerWhite", 80, 38);
                repaint();
            }
            repaint();
        }
    }



}
