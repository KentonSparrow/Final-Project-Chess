package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BishopChessComponent extends ChessComponent {
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;
    private Image bishopImage;
    private String name;


    public void loadResource() throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("./images/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("./images/bishop-black.png"));
        }
    }


    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initiateBishopName(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                name = "b";
            } else if (color == ChessColor.BLACK) {
                name = "B";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, String name) {
        super(chessboardPoint, location, color, listener, size, name);
        initiateBishopImage(color);
        initiateBishopName(color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();

        if (Math.abs(destination.getX() - source.getX()) == Math.abs(destination.getY() - source.getY()) && source.getY() < destination.getY() && source.getX() < destination.getX()) {
            int startCol = source.getY() + 1;
            int startRow = source.getX() + 1;
            while (startCol < destination.getY()) {
                if (!(chessComponents[startRow][startCol] instanceof EmptySlotComponent)) {
                    return false;
                }
                startCol = startCol + 1;
                startRow = startRow + 1;
            }

        } else if (Math.abs(destination.getX() - source.getX()) == Math.abs(destination.getY() - source.getY()) && source.getY() > destination.getY() && source.getX() > destination.getX()) {
            int startRow = destination.getX() + 1;
            int startCol = destination.getY() + 1;
            while (startCol < source.getY()) {
                if (!(chessComponents[startRow][startCol] instanceof EmptySlotComponent)) {
                    return false;
                }
                startCol = startCol + 1;
                startRow = startRow + 1;
            }
        } else if (Math.abs(destination.getX() - source.getX()) == Math.abs(destination.getY() - source.getY()) && source.getX() < destination.getX() && source.getY() > destination.getY()) {
            int startRow = source.getX() + 1;
            int startCol = source.getY() - 1;
            while (startRow < destination.getX()) {
                if (!(chessComponents[startRow][startCol] instanceof EmptySlotComponent)) {
                    return false;
                }
                startCol = startCol - 1;
                startRow = startRow + 1;
            }
        } else if (Math.abs(destination.getX() - source.getX()) == Math.abs(destination.getY() - source.getY()) && source.getX() > destination.getX() && source.getY() < destination.getY()) {
            int startRow = source.getX() - 1;
            int startCol = source.getY() + 1;
            while (startCol < destination.getY()) {
                if (!(chessComponents[startRow][startCol] instanceof EmptySlotComponent)) {
                    return false;
                }
                startCol = startCol + 1;
                startRow = startRow - 1;
            }
        } else return false;

        return true;
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bishopImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) {
            g.setColor(Color.ORANGE);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }


    /**
     * 这些都是用来看AI能否完成的——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
     * */

    @Override
    public void whatChessCanMoveTo(ChessComponent[][] chessComponents) {
        ChessboardPoint source = getChessboardPoint();
        /**
         * Bishop的canMoveToChess
         * ——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/
        BishopCanMoveTo(source, chessComponents);
        whetherBishopCanKillKing(chessComponents);
        /**————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

    }

    @Override
    public List<ChessComponent> getCanMoveToChess() {
        return this.canMoveToChess;
    }

    /**————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/


    /**
     * Bishop的canMoveToChess
     * 这个是直接从Queen的后半部分抄过来的，如果出错的话直接去找Queen的canMoveToChess
     * */
    private void BishopCanMoveTo(ChessboardPoint  source, ChessComponent[][] chessComponents){
        //现在来写Bishop的部分
        //先走左上角点和右下角点的连线对角线
        canMoveToChess = new ArrayList<>();
        int differenceX = 7 - source.getX();
        int differenceY = 7 - source.getY();
        if (differenceX >= differenceY) {//此时的点在该对角线及以上部分区域内
            //向右下角走，y先到达边界
            int j = source.getX() + 1;
            for (int i = source.getY() + 1; i <= 7; i++) {
                if (chessComponents[j][i] instanceof EmptySlotComponent) {
                    this.canMoveToChess.add(chessComponents[j][i]);
                } else {
                    if (this.getChessColor() != chessComponents[j][i].getChessColor()) {
                        this.canMoveToChess.add(chessComponents[j][i]);
                    }
                    break;
                }
                j = j + 1;
            }

            //向左上角走，x先到达边界
            int k = source.getY() - 1;
            for(int i = source.getX() - 1; i >= 0; i--){
                if (chessComponents[i][k] instanceof EmptySlotComponent) {
                    this.canMoveToChess.add(chessComponents[i][k]);
                } else {
                    if (this.getChessColor() != chessComponents[i][k].getChessColor()) {
                        this.canMoveToChess.add(chessComponents[i][k]);
                    }
                    break;
                }
                k = k - 1;
            }
        }
        else{//此时棋子在该对角线下方区域内
            //向右下角走，x先到达边界
            int j = source.getY() + 1;
            for(int i = source.getX() + 1; i <= 7; i++){
                if (chessComponents[i][j] instanceof EmptySlotComponent) {
                    this.canMoveToChess.add(chessComponents[i][j]);
                } else {
                    if (this.getChessColor() != chessComponents[i][j].getChessColor()) {
                        this.canMoveToChess.add(chessComponents[i][j]);
                    }
                    break;
                }
                j = j + 1;
            }

            //向左上角走，y先到达边界
            int k = source.getX() - 1;
            for(int i = source.getY() - 1; i >= 0; i--){
                if (chessComponents[k][i] instanceof EmptySlotComponent) {
                    this.canMoveToChess.add(chessComponents[k][i]);
                } else {
                    if (this.getChessColor() != chessComponents[k][i].getChessColor()) {
                        this.canMoveToChess.add(chessComponents[k][i]);
                    }
                    break;
                }
                k = k - 1;
            }
        }

        int sumXY = source.getX() + source.getY();
        if(sumXY <= 7){
            for(int i = source.getX() - 1; i >= 0; i--){
                if(chessComponents[i][sumXY - i] instanceof EmptySlotComponent){
                    this.canMoveToChess.add(chessComponents[i][sumXY - i]);
                }else{
                    if(this.getChessColor() != chessComponents[i][sumXY - i].getChessColor()){
                        this.canMoveToChess.add(chessComponents[i][sumXY - i]);
                    }
                    break;
                }
            }
            for(int i = source.getY() - 1; i >= 0; i--){
                if(chessComponents[sumXY - i][i] instanceof EmptySlotComponent){
                    this.canMoveToChess.add(chessComponents[sumXY - i][i]);
                }else{
                    if(this.getChessColor() != chessComponents[sumXY - i][i].getChessColor()){
                        this.canMoveToChess.add(chessComponents[sumXY - i][i]);
                    }
                    break;
                }
            }
        }
        else {
            for(int i = source.getX() + 1; i <= 7; i++){
                if(chessComponents[i][sumXY - i] instanceof EmptySlotComponent){
                    this.canMoveToChess.add(chessComponents[i][sumXY - i]);
                }else{
                    if(this.getChessColor() != chessComponents[i][sumXY - i].getChessColor()){
                        this.canMoveToChess.add(chessComponents[i][sumXY - i]);
                    }
                    break;
                }
            }

            for(int i = source.getY() + 1; i <= 7; i++){
                if(chessComponents[sumXY - i][i] instanceof EmptySlotComponent){
                    this.canMoveToChess.add(chessComponents[sumXY - i][i]);
                }else{
                    if(this.getChessColor() != chessComponents[sumXY - i][i].getChessColor()){
                        this.canMoveToChess.add(chessComponents[sumXY - i][i]);
                    }
                    break;
                }
            }
        }
    }

    private void whetherBishopCanKillKing(ChessComponent[][] chessComponents){
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


