package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnightChessComponent extends ChessComponent{
    private static Image KNIGHT_WHITE;
    private static Image KNIGHT_BLACK;
    private Image knightImage;
    private String name;


    public void loadResource() throws IOException {
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("./images/knight-white.png"));
        }

        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("./images/knight-black.png"));
        }
    }


    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (color == ChessColor.BLACK) {
                knightImage = KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initiateKnightName(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                name = "n";
            } else if (color == ChessColor.BLACK) {
                name = "N";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, String name) {
        super(chessboardPoint, location, color, listener, size, name);
        initiateKnightImage(color);
        initiateKnightName(color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();

        int rowcha1 = Math.abs(source.getX() - destination.getX());
        int colcha1 = Math.abs(source.getY()) - destination.getY();
        int rowcha2 = Math.abs(destination.getX() - source.getX());
        int colcha2 = Math.abs(destination.getY() - source.getY());
        if (((rowcha1 == 2) && (colcha1 == 1)) || ((rowcha1 == 1) && (colcha1 == 2)) || ((rowcha2 == 1) && (colcha2 == 2)) || ((rowcha2 == 2) && (colcha2 == 1))) {
            if (chessComponents[source.getX()][source.getY()].chessColor == chessComponents[destination.getX()][destination.getY()].chessColor) {
                return false;
            }
                return true;
        }
        else {
         return false;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(knightImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) {
            g.setColor(Color.ORANGE);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }



    /**————————————————————————————————————————————————————————————————————————————————————————————测试样例——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

    @Override
    public void whatChessCanMoveTo(ChessComponent[][] chessComponents) {
        ChessboardPoint source = getChessboardPoint();
        /**
         * Knight的canMoveToChess
         *———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————— */
        KnightCanMoveTo(source, chessComponents);
        whetherKnightCanKillKing(chessComponents);
        /**————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

    }

    @Override
    public List<ChessComponent> getCanMoveToChess() {
        return this.canMoveToChess;
    }

    /**_________——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

    /**
     * Knight的canMoveTo
     * */
    private void KnightCanMoveTo(ChessboardPoint source, ChessComponent[][] chessComponents){
        canMoveToChess = new ArrayList<>();
        int x1 = source.getX() - 2, x2 = source.getX() - 1, x3 = source.getX() + 1, x4 = source.getX() + 2;
        int y1 = source.getY() - 2, y2 = source.getY() - 1, y3 = source.getY() + 1, y4 = source.getY() + 2;
        if(x1 >= 0 && y2 >= 0){
            if(this.getChessColor() != chessComponents[x1][y2].getChessColor()){
                this.canMoveToChess.add(chessComponents[x1][y2]);
            }
        }
        if(x1 >= 0 && y3 <= 7){
            if(this.getChessColor() != chessComponents[x1][y3].getChessColor()){
                this.canMoveToChess.add(chessComponents[x1][y3]);
            }
        }
        if(x2 >= 0 && y4 <= 7){
            if(this.getChessColor() != chessComponents[x2][y4].getChessColor()){
                this.canMoveToChess.add(chessComponents[x2][y4]);
            }
        }
        if(x2 >= 0 && y1 >= 0){
            if(this.getChessColor() != chessComponents[x2][y1].getChessColor()){
                this.canMoveToChess.add(chessComponents[x2][y1]);
            }
        }
        if(x3 <= 7 && y1 >= 0){
            if(this.getChessColor() != chessComponents[x3][y1].getChessColor()){
                this.canMoveToChess.add(chessComponents[x3][y1]);
            }
        }
        if(x3 <= 7 && y4 <= 7){
            if(this.getChessColor() != chessComponents[x3][y4].getChessColor()){
                this.canMoveToChess.add(chessComponents[x3][y4]);
            }
        }
        if(x4 <= 7 && y3 <= 7){
            if(this.getChessColor() != chessComponents[x4][y3].getChessColor()){
                this.canMoveToChess.add(chessComponents[x4][y3]);
            }
        }
        if(x4 <= 7 && y2 >= 0){
            if(this.getChessColor() != chessComponents[x4][y2].getChessColor()){
                this.canMoveToChess.add(chessComponents[x4][y2]);
            }
        }
    }

    private void whetherKnightCanKillKing(ChessComponent[][] chessComponents){
        if(this.getChessColor() == ChessColor.BLACK){
            for(int i = 0; i < this.canMoveToChess.size(); i++){
                if(canMoveToChess.get(i) instanceof KingChessComponent){//判断是否将军的函数
                    ((KingChessComponent) chessComponents[canMoveToChess.get(i).getChessboardPoint().getX()][canMoveToChess.get(i).getChessboardPoint().getY()]).setUnderAttacked(true);
                }
                if(canMoveToChess.get(i).getChessboardPoint().getX() == 7 && (canMoveToChess.get(i).getChessboardPoint().getY() >= 2 && canMoveToChess.get(i).getChessboardPoint().getY() <= 6)){
                    if(chessComponents[7][4] instanceof KingChessComponent && chessComponents[7][4].getChessColor() == ChessColor.WHITE){
                        ((KingChessComponent) chessComponents[7][4]).setAllUnderAttacked(false);//判断是否满足王车易位前提条件的函数
                    }
                }
            }
        }
        if(this.getChessColor() == ChessColor.WHITE){
            for(int i = 0; i < this.canMoveToChess.size(); i++){
                if(canMoveToChess.get(i) instanceof KingChessComponent){//判断是否将军的函数
                    ((KingChessComponent) chessComponents[canMoveToChess.get(i).getChessboardPoint().getX()][canMoveToChess.get(i).getChessboardPoint().getY()]).setUnderAttacked(true);
                }
                if(canMoveToChess.get(i).getChessboardPoint().getX() == 0 && (canMoveToChess.get(i).getChessboardPoint().getY() >= 2 && canMoveToChess.get(i).getChessboardPoint().getY() <= 6)){
                    if(chessComponents[0][4] instanceof KingChessComponent && chessComponents[0][4].getChessColor() == ChessColor.BLACK){
                        ((KingChessComponent) chessComponents[0][4]).setAllUnderAttacked(false);//判断是否满足王车易位前提条件的函数
                    }
                }
            }
        }
    }

    /**
     * 难道说所有的setter和getter得写到这个里面？
     * */
    public boolean isAI() {
        return AI;
    }

    public void setAI(boolean AI) {
        this.AI = AI;
    }

}
