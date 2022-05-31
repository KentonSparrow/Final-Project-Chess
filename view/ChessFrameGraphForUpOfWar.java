package view;

import model.ChessColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessFrameGraphForUpOfWar extends JPanel implements ActionListener {
    private Chessboard chessboard;


    private int chessboardSize;
    public ChessFrameGraphForUpOfWar(int chessboardSize, Chessboard chessboard){
        this.chessboardSize = chessboardSize;
        this.chessboard = chessboard;
        this.setBounds(385, 0, this.chessboardSize - 3, (964 - this.chessboardSize)/2 + 23);
        this.setVisible(true);
        this.repaint();
        //System.out.println(this.chessboardSize);
        //System.out.println((964 - this.chessboardSize)/2 + 23);
        timer.start();
    }


    Timer timer = new Timer(1000, this);
    @Override
    public void actionPerformed(ActionEvent e) {

        this.chessboard.setTime(this.chessboard.getTime() - 1);

        if(this.chessboard.getTime() == 0){
            this.chessboard.swapColor();
        }

        timer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ChessGraphData.WWI.paintIcon(this, g, 0, 0);
        //System.out.println("Use the WWI");
        g.setColor(new Color(192, 3, 36));
        g.setFont(new Font("Norse", Font.BOLD, 53));
        g.drawString("World War I", 273, 80);


        if(chessboard.getCurrentColor() == ChessColor.WHITE){
            g.setColor(Color.GRAY);
            g.fillRect(0, 53, 100, 65);
            g.setColor(new Color(208, 11, 11));
            g.drawRect(0, 53, 100, 65);

            g.setFont(new Font("Norse", Font.BOLD, 60));
            g.drawString(String.valueOf(this.chessboard.getTime()), 22, 107);
            repaint();
        }
        if (chessboard.getCurrentColor() == ChessColor.BLACK){
            g.setColor(Color.GRAY);
            g.fillRect(667, 53, 100, 65);
            g.setColor(new Color(208, 11, 11));
            g.drawRect(667, 53, 100, 65);
            repaint();

            g.setFont(new Font("Norse", Font.BOLD, 60));
            g.drawString(String.valueOf(this.chessboard.getTime()), 692, 105);
            repaint();
        }


        repaint();
    }

}
