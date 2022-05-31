package view;

import model.ChessColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;


public class ChessFrameGraphForUpOfFate extends JPanel implements ActionListener {
    /**计时器所需的一些成员变量*/
    private Chessboard chessboard;
    //public boolean visible = false;
    /**_____________________________________________*/



    private int chessboardSize;
    public ChessFrameGraphForUpOfFate(int chessboardSize, Chessboard chessboard){
        this.chessboard = chessboard;
        this.chessboardSize = chessboardSize;
        this.setBounds(385, 0, this.chessboardSize - 3, (964 - this.chessboardSize)/2 + 23);
        this.setVisible(true);
        this.repaint();
        //timer.start();
    }



    /**
     * 先弄一个Timer，然后再搞一个监听器
     * */
    Timer timer = new Timer(1000, this);
    @Override
    public void actionPerformed(ActionEvent e) {

        this.chessboard.setTime(this.chessboard.getTime() - 1);

        if(this.chessboard.getTime() == 0){
            this.chessboard.swapColor();
        }

        //timer.start();
    }









    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ChessGraphData.FateGrandOrder.paintIcon(this, g, 0, 0);
        //System.out.println("Use the Fate");
        g.setColor(new Color(206, 164, 64));
        g.setFont(new Font("Norse", Font.BOLD, 53));
        g.drawString("Holy Grail War", 243, 50);

        if(chessboard.getCurrentColor() == ChessColor.WHITE){
            g.setColor(Color.WHITE);
            g.fillRect(0, 53, 100, 65);
            g.setColor(Color.ORANGE);
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



    /**大概是这样一个方法，倒计时，然后每隔1秒就repaint，但是这个方法要和paintComponent*/
}
