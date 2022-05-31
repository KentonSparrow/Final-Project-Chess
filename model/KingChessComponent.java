package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KingChessComponent extends ChessComponent{
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    private Image kingImage;
    private String name;

    /**
     * 设置变量用于判断王车易位
     * 1、第一个————判断是否被将军————所在地、经过地、目的地都不能被将军
     * 2、第二个判断王和车之间是否存在棋子（可以通过车的canMoveToChess数组进行判断）
     * 3、只能走一次，走完之后就不能再发生任何变化————也就是说应该设置另外一个控制boolean，一开始为true，一旦进行了一次王车易位，那就变为false
     * 4、还需要一个判断是否移动过的函数
     * */
    private boolean isAllUnderAttacked = true;//判断是否被将军和王车易位中的所在地、经过地、目的地都不能被将军————如果先设置为true更好感觉，因为要在每个棋子中判断一下王的路径是否在它们的攻击范围内，一旦有就给false，无法进行后面的操作
    private boolean whetherEmptyBetweenKRChange = false;//判断是否用户想要做王车易位————必须先动王————这个感觉暂时没有什么用处—————现在有用了，用来判断王和车之间是否有棋子
    private boolean whetherUseKRChange = true;//用于判断是否已经用过王车易位了————true就是没有，进入王车易位步骤
    private boolean hasMoved = true; //在clickController里面进行判断————true就是没有，一旦移动过就是false，无法进入之后的步骤
    private boolean isUnderAttacked = false;//单纯用来显示是否被将军————false就是没有，一旦被将军就true，并且想办法传递给chessboard，标红

    /**
     * 要判断胜负的话，就需要把王周围都遍历一次，如果王本身已经被将军了并且王周围的格子都处于攻击范围下，那么就会触发胜负判定，就输了
     * */
    private boolean lose = false;//一开始设置为false，然后再设置为true
    /**结果这个用来防止兵升变的时候二次判断了*/

    public void loadResource() throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("./images/king-white.png"));
        }

        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("./images/king-black.png"));
        }
    }


    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initiateKingName(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                name = "k";
            } else if (color == ChessColor.BLACK) {
                name = "K";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//这是加的东西！

    @Override
    public String getName() {
        return name;
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, String name) {
        super(chessboardPoint, location, color, listener, size, name);
        initiateKingImage(color);
        initiateKingName(color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();

        if (chessComponents[source.getX()][source.getY()].chessColor == ChessColor.BLACK) {
            if (((Math.abs(source.getX() - destination.getX()) + Math.abs(source.getY() - destination.getY())) == 1) || ((zhanxiexian(source, destination)) && (Math.abs(source.getX() - destination.getX()) == 1))) {
                if ((chessComponents[destination.getX()][destination.getY()].chessColor == ChessColor.WHITE)) {
                    if(chessComponents[destination.getX()][destination.getY()] instanceof KingChessComponent){
                        return false;
                    }else return true;
                }
                if (chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent){
                    return true;
                }
            }
//            if(hasMoved && isAllUnderAttacked){
//                if(destination.getX() == source.getX() && destination.getY() == 2) {
//                    if (chessComponents[source.getX()][0] instanceof RookChessComponent){
//                        if (((RookChessComponent) chessComponents[source.getX()][0]).isHasMoved()){
//                            if(chessComponents[source.getX()][1] instanceof  EmptySlotComponent && chessComponents[source.getX()][2] instanceof EmptySlotComponent && chessComponents[source.getX()][3] instanceof EmptySlotComponent){
//                                return true;
//                            }
//                        }
//                    }
//                }
//                if (destination.getY() == 6 && destination.getX() == source.getX()){
//                    if(chessComponents[source.getX()][7] instanceof  RookChessComponent){
//                        if(((RookChessComponent) chessComponents[source.getX()][7]).isHasMoved()){
//                            if(chessComponents[source.getX()][5] instanceof EmptySlotComponent && chessComponents[source.getX()][6] instanceof EmptySlotComponent){
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
        }
        else if (chessComponents[source.getX()][source.getY()].chessColor == ChessColor.WHITE) {
            if (((Math.abs(source.getX() - destination.getX()) + Math.abs(source.getY() - destination.getY())) == 1) || ((zhanxiexian(source, destination)) && (Math.abs(source.getX() - destination.getX()) == 1))) {
                if ((chessComponents[destination.getX()][destination.getY()].chessColor == ChessColor.BLACK)) {
                    if(chessComponents[destination.getX()][destination.getY()] instanceof KingChessComponent){
                        return false;
                    }else return true;
                }
                if (chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent){
                    return true;
                }
            }
//            if(hasMoved && isAllUnderAttacked){
//                if(destination.getX() == source.getX() && destination.getY() == 2) {
//                    if (chessComponents[source.getX()][0] instanceof RookChessComponent){
//                        if (((RookChessComponent) chessComponents[source.getX()][0]).isHasMoved()){
//                            if(chessComponents[source.getX()][1] instanceof  EmptySlotComponent && chessComponents[source.getX()][2] instanceof EmptySlotComponent && chessComponents[source.getX()][3] instanceof EmptySlotComponent){
//                                return true;
//                            }
//                        }
//                    }
//                }
//                if (destination.getY() == 6 && destination.getX() == source.getX()){
//                    if(chessComponents[source.getX()][7] instanceof  RookChessComponent){
//                        if(((RookChessComponent) chessComponents[source.getX()][7]).isHasMoved()){
//                            if(chessComponents[source.getX()][5] instanceof EmptySlotComponent && chessComponents[source.getX()][6] instanceof EmptySlotComponent){
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }

        }
        return false;
    }

    /**
     * 写一个canMoveTo，设置成员变量canMoveToChess??但是好像不行。要怎么搞？
     * */


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isUnderAttacked){
            g.setColor(Color.RED);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            setUnderAttacked(false);
        }
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(kingImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.ORANGE);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }


    /**——————————————————————————————————————————————————————————————测试用例——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/
    @Override
    public void whatChessCanMoveTo(ChessComponent[][] chessComponents) {
        ChessboardPoint source = getChessboardPoint();
        /**
         * King的canMoveToChess设置
         * ————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
         * */
        KingCanMoveTo(source, chessComponents);
        /**————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

    }

    @Override
    public List<ChessComponent> getCanMoveToChess() {
        return this.canMoveToChess;
    }

    /**————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/


    private boolean zhanxiexian(ChessboardPoint source, ChessboardPoint destination) {
        return (Math.abs(source.getX()-destination.getX()) == Math.abs(source.getY()- destination.getY()));
    }


    /**
     * 王车易位的getter和setter
     * */
    public boolean isAllUnderAttacked() {
        return isAllUnderAttacked;
    }

    public void setAllUnderAttacked(boolean underAttacked) {
        this.isAllUnderAttacked = underAttacked;
    }

    public boolean isWhetherEmptyBetweenKRChange() {
        return whetherEmptyBetweenKRChange;
    }

    public void setWhetherEmptyBetweenKRChange(boolean whetherEmptyBetweenKRChange) {
        this.whetherEmptyBetweenKRChange = whetherEmptyBetweenKRChange;
    }

    public boolean isWhetherUseKRChange() {
        return whetherUseKRChange;
    }

    public void setWhetherUseKRChange(boolean whetherUseKRChange) {
        this.whetherUseKRChange = whetherUseKRChange;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean isUnderAttacked() {
        return isUnderAttacked;
    }

    public void setUnderAttacked(boolean underAttacked) {
        this.isUnderAttacked = underAttacked;
    }

    /**
     * King的canMoveTo
     * */
    private void KingCanMoveTo(ChessboardPoint source, ChessComponent[][] chessComponents){
        canMoveToChess = new ArrayList<>();
        int x1 = source.getX() + 1, x2 = source.getX() - 1, y1 = source.getY() + 1, y2 = source.getY() - 1;
        for(int i = x2; i <= x1; i++){
            for(int j = y2; j <= y1; j++){
                if((i >= 0 && i <= 7) && (j >= 0 && j <= 7) ){//&& (i != source.getX() && j != source.getY())
                    if(chessComponents[i][j] instanceof EmptySlotComponent){
                        this.canMoveToChess.add(chessComponents[i][j]);
                    }else{
                        if(this.getChessColor() != chessComponents[i][j].getChessColor() && !(chessComponents[i][j] instanceof KingChessComponent) ){
                            this.canMoveToChess.add(chessComponents[i][j]);
                        }
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

    /**
     * 胜负判定用的getter和setter
     * */

    public boolean isLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
    }
}
