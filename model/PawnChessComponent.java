package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PawnChessComponent extends ChessComponent {
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;
    private Image pawnImage;
    private String name;

    /**
     * 兵的升变和吃过路兵；如果从被吃的的兵的角度出发的话，可以不用记录步数？；升变设置JDialog，在弹窗中设置单选按钮，按钮加图片，且不同的兵颜色不同
     * 先看兵的吃过路兵，每一个棋子有自己的坐标chessPoint在父类里面，而且在click监听器中实现了变化
     * 这已经是一个单独的点击事件了，不能放在原来的click监听器的位置了
     * 那么Boolean canMoveTo的方法也要变——————√√√
     * 那么如果被吃的那个兵达到条件后要相关办法传一个值给吃它的兵，告诉它，可以吃了；要用什么来传递？
     * 传一个boolean
     * */
    private boolean ableToMove = false;


    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./images/pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./images/pawn-black.png"));
        }
    }


    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initiatePawnName(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                name = "p";
            } else if (color == ChessColor.BLACK) {
                name = "P";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getName() {
        return name;
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, String name) {
        super(chessboardPoint, location, color, listener, size, name);
        initiatePawnImage(color);
        initiatePawnName(color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();

        if ((chessColor == ChessColor.WHITE) && (source.getX() == 6)) {
            if((source.getY()==destination.getY()) && (destination.getX() == 5 || destination.getX() == 4) && (source.getX() > destination.getX())) {
                for(int i = destination.getX(); i < source.getX();i++) {
                    if (!(chessComponents[i][source.getY()] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
                return true;
            }
            if (chessComponents[5][destination.getY()].chessColor == ChessColor.BLACK) {
                if (((destination.getY() == source.getY() + 1) || (destination.getY() == source.getY() - 1)) && (source.getX() - destination.getX() == 1)) {
                    return true;
                }
            }
            return false;
            //在这里要加上吃子规则！
        }

        else if ((chessColor == ChessColor.WHITE) && (source.getX() != 6) && (source.getX() > destination.getX())) {
            int difference1 = Math.abs(destination.getX() - source.getX()), difference2 = Math.abs(destination.getY() - source.getY());
            if (source.getY() == destination.getY() && (source.getX() - destination.getX() == 1)) {
                if (!(chessComponents[destination.getX()][source.getY()] instanceof EmptySlotComponent)) {
                    return false;
                }
                return true;
            }
            if (chessComponents[source.getX() - 1][destination.getY()].chessColor == ChessColor.BLACK) {
                if (((destination.getY() == source.getY() + 1) || (destination.getY() == source.getY() - 1)) && (source.getX() - destination.getX() == 1)) {
                    return true;
                }
            }

            /**
             * 测试一下，至少在canMoveTo方法里面是要这么写
             * 由即将被吃的那个棋子传一个参数给吃子的棋子————ableToMove————记得用完之后改回false————————————不能改！！！，它会先判断正误，要是改了的话判断完之后就不能移动了
             * */
            if((chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) && (chessComponents[destination.getX()+1][destination.getY()] instanceof PawnChessComponent) && ableToMove && (difference1 == 1) && (difference2 == 1)){
                //ableToMove = false;
                return true;
            }


            return false;
        }



        else if ((chessColor == ChessColor.BLACK) && (source.getX() == 1) && (source.getX() < destination.getX())) {
            if((source.getY()==destination.getY()) && (destination.getX() == 2 || destination.getX() == 3)) {
                for(int i = source.getX() + 1; i <= destination.getX();i++) {
                    if (!(chessComponents[i][source.getY()] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
                return true;
            }
            if (chessComponents[2][destination.getY()].chessColor == ChessColor.WHITE) {
                if (((destination.getY() == source.getY() + 1) || (destination.getY() == source.getY() - 1)) && (destination.getX() - source.getX() == 1)) {
                    return true;
                }
            }
            return false;
        }

        else if ((chessColor == ChessColor.BLACK) && (source.getX() != 1) && (source.getX() < destination.getX())) {
            int difference1 = Math.abs(destination.getX() - source.getX()), difference2 = Math.abs(destination.getY() - source.getY());
            if (source.getY() == destination.getY() && (destination.getX() - source.getX() == 1)) {
                if (!(chessComponents[destination.getX()][source.getY()] instanceof EmptySlotComponent)) {
                    return false;
                }
                return true;
            }
            if (chessComponents[source.getX() + 1][destination.getY()].chessColor == ChessColor.WHITE) {
                if (((destination.getY() == source.getY() + 1) || (destination.getY() == source.getY() - 1)) && (destination.getX() - source.getX() == 1)) {
                    return true;
                }
            }
            if((chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) && (chessComponents[destination.getX() - 1][destination.getY()] instanceof PawnChessComponent) && ableToMove && (difference1 == 1) && (difference2 == 1)){
               //ableToMove = false;
                return true;
            }

            return false;
        }

        //总的情况，最终要return false
        return false;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) {
            g.setColor(Color.ORANGE);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }


    /**——————————————————————————————————————————————————————————————————————测试样例——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

    @Override
    public void whatChessCanMoveTo(ChessComponent[][] chessComponents) {
        ChessboardPoint source = getChessboardPoint();
        /**
         * 这里做一个猜测？先看一下clickController，上一个兵如果能被当作过路兵吃掉的话，那么这个本回合将要移动的兵的ableToMove会被提前设置为true，那么只要鼠标一点击该棋子就可以亮标
         * ——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/
        //兵应该要分黑白棋，
        PawnCanMoveTo(source, chessComponents);
        whetherPawnCanKillKing(chessComponents);
        /**——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

    }

    @Override
    public List<ChessComponent> getCanMoveToChess() {
        return this.canMoveToChess;
    }
    /**_________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________*/

    /**
     * 测试代码，getter和setter，修改ableToMove的值
     * */
    public void setAbleToMove(boolean ableToMove){
        this.ableToMove = ableToMove;
    }
    public boolean getAbleToMove(){
        return this.ableToMove;
    }

    /**
     * Pawn的canMoveToChess
     * */
    private void PawnCanMoveTo(ChessboardPoint source, ChessComponent[][] chessComponents){
        canMoveToChess = new ArrayList<>();
        if(this.getChessColor() == ChessColor.BLACK){
            if(this.getChessboardPoint().getX() == 1){
                for(int i = 2; i <= 3; i++){
                    if(chessComponents[i][source.getY()] instanceof EmptySlotComponent){
                        this.canMoveToChess.add(chessComponents[i][source.getY()]);
                    }else break;
                }

                int y1 = source.getY() - 1, y2 = source.getY() + 1;
                if(y1 >= 0 && chessComponents[source.getX() + 1][y1].getChessColor() == ChessColor.WHITE){
                    this.canMoveToChess.add(chessComponents[source.getX() + 1][y1]);
                }
                if(y2 <= 7 && chessComponents[source.getX() + 1][y2].getChessColor() == ChessColor.WHITE){
                    this.canMoveToChess.add(chessComponents[source.getX() + 1][y2]);
                }
            }
            if(source.getX() <= 6 && source.getX() >= 2){
                if(chessComponents[source.getX()+1][source.getY()] instanceof EmptySlotComponent){
                    this.canMoveToChess.add(chessComponents[source.getX()+1][source.getY()]);
                }
                int y1 = source.getY() - 1, y2 = source.getY() + 1;
                if(y1 >= 0 && chessComponents[source.getX() + 1][y1].getChessColor() == ChessColor.WHITE){
                    this.canMoveToChess.add(chessComponents[source.getX() + 1][y1]);
                }
                if(y1 >= 0 && chessComponents[source.getX() + 1][y1].getChessColor() == ChessColor.NONE && chessComponents[source.getX()][y1].getChessColor() == ChessColor.WHITE && ableToMove){
                    this.canMoveToChess.add(chessComponents[source.getX() + 1][y1]);
                }
                if(y2 <= 7 && chessComponents[source.getX() + 1][y2].getChessColor() == ChessColor.WHITE){
                    this.canMoveToChess.add(chessComponents[source.getX() + 1][y2]);
                }
                if(y2 <= 7 && chessComponents[source.getX() + 1][y2].getChessColor() == ChessColor.NONE && chessComponents[source.getX()][y2].getChessColor() == ChessColor.WHITE && ableToMove){
                    this.canMoveToChess.add(chessComponents[source.getX() + 1][y2]);
                }
            }
        }
        if(this.getChessColor() == ChessColor.WHITE){
            if(this.getChessboardPoint().getX() == 6){
                for(int i = 5; i >= 4; i--){
                    if(chessComponents[i][source.getY()] instanceof EmptySlotComponent){
                        this.canMoveToChess.add(chessComponents[i][source.getY()]);
                    }else break;
                }

                int y1 = source.getY() - 1, y2 = source.getY() + 1;
                if(y1 >= 0 && chessComponents[source.getX() - 1][y1].getChessColor() == ChessColor.BLACK){
                    this.canMoveToChess.add(chessComponents[source.getX() - 1][y1]);
                }
                if(y2 <= 7 && chessComponents[source.getX() - 1][y2].getChessColor() == ChessColor.BLACK){
                    this.canMoveToChess.add(chessComponents[source.getX() - 1][y2]);
                }
            }
            if(source.getX() >= 1 && source.getX() <= 5){
                if(chessComponents[source.getX() - 1][source.getY()] instanceof EmptySlotComponent){
                    this.canMoveToChess.add(chessComponents[source.getX() - 1][source.getY()]);
                }
                int y1 = source.getY() - 1, y2 = source.getY() + 1;
                if(y1 >= 0 && chessComponents[source.getX() - 1][y1].getChessColor() == ChessColor.BLACK){
                    this.canMoveToChess.add(chessComponents[source.getX() - 1][y1]);
                }
                if(y1 >= 0 && chessComponents[source.getX() - 1][y1].getChessColor() == ChessColor.NONE && chessComponents[source.getX()][y1].getChessColor() == ChessColor.BLACK && ableToMove){
                    this.canMoveToChess.add(chessComponents[source.getX() - 1][y1]);
                }
                if(y2 <= 7 && chessComponents[source.getX() - 1][y2].getChessColor() == ChessColor.BLACK){
                    this.canMoveToChess.add(chessComponents[source.getX() - 1][y2]);
                }
                if(y2 <= 7 && chessComponents[source.getX() - 1][y2].getChessColor() == ChessColor.NONE && chessComponents[source.getX()][y2].getChessColor() == ChessColor.BLACK && ableToMove){
                    this.canMoveToChess.add(chessComponents[source.getX() - 1][y2]);
                }
            }
        }

    }


    private void whetherPawnCanKillKing(ChessComponent[][] chessComponents){
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
