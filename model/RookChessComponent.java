package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示国际象棋里面的车
 */
public class RookChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image ROOK_WHITE;
    private static Image ROOK_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image rookImage;
    private String name;

    /**
     * 1、车也需要一个判断函数来控制进行王车易位—————这里可以设置成传入当前王的位置，然后往它后面挪一格
     * 2、车不能被移动过————在clickController里面？一旦first为Rook且Rook移动了那么在clickController里面就把它设置为false;
     * */
    private boolean performKRChange = false;
    private boolean hasMoved = true;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (ROOK_WHITE == null) {
            ROOK_WHITE = ImageIO.read(new File("./images/rook-white.png"));
        }

        if (ROOK_BLACK == null) {
            ROOK_BLACK = ImageIO.read(new File("./images/rook-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateRookImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                rookImage = ROOK_WHITE;
            } else if (color == ChessColor.BLACK) {
                rookImage = ROOK_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initiateRookName(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                name = "r";
            } else if (color == ChessColor.BLACK) {
                name = "R";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public RookChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, String name) {
        super(chessboardPoint, location, color, listener, size, name);
        initiateRookImage(color);
        initiateRookName(color);
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();

        if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else { // Not on the same row or the same column.
            return false;
        }

        /**
         * 王车易位的内部判断
         *
        if(performKRChange){
        }*/
        return true;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(rookImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.ORANGE);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }



    /**_______________________________________________________________________________________________________________________________________________________________________________________-*/
    @Override
    public void whatChessCanMoveTo(ChessComponent[][] chessComponents) {
        ChessboardPoint source = getChessboardPoint();
        /**
         * 另外一个猜测，是否在每一个棋子中都应该在写完click之后就判断一下King有没有在它们的攻击范围内呢？但是King不能实时更新，不能及时地把自己的位置给传到这些棋子中，那么这个办法应该是不可行的
         * 但是好像，如果是判断王车易位的话，王的位置是始终位于初始位置的吧？那么只要判断王车易位的要求点如果在攻击范围内加上，如果初始位置是王，那么就修改它的Boolean
         * 一个猜测，由于每次点击鼠标的时候，我们都会调用这个canMoveTo方法，所以就在这个里面写一个设置或修改canMoveToChess的方法————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
         * */
        RookCanMoveTo(source, chessComponents);
        whetherRookCanKillKing(chessComponents);
        /**——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

    }

    @Override
    public List<ChessComponent> getCanMoveToChess() {
        return this.canMoveToChess;
    }
    /**_____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________*/


    /**
     * 王车易位
     * */
    public void setPerformKRChange(boolean performKRChange) {
        this.performKRChange = performKRChange;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    public boolean isHasMoved(){
        return hasMoved;
    }

    /**
     * Rook的canMoveToChess
     * */
    private void RookCanMoveTo(ChessboardPoint source, ChessComponent[][] chessComponents){
        canMoveToChess = new ArrayList<>();
        //向上移动
        for(int i = source.getX() - 1; i >= 0; i--) {
            if(chessComponents[i][source.getY()] instanceof EmptySlotComponent){
                this.canMoveToChess.add(chessComponents[i][source.getY()]);
            }else {
                if(this.getChessColor() != chessComponents[i][source.getY()].getChessColor()){
                    this.canMoveToChess.add(chessComponents[i][source.getY()]);
                }
                break;
            }
        }
        //向下移动
        for(int i = source.getX() + 1; i <= 7; i++){
            if(chessComponents[i][source.getY()] instanceof EmptySlotComponent){
                this.canMoveToChess.add(chessComponents[i][source.getY()]);
            }else {
                if(this.getChessColor() != chessComponents[i][source.getY()].getChessColor()){
                    this.canMoveToChess.add(chessComponents[i][source.getY()]);
                }
                break;
            }
        }
        //向右移动
        for(int i = source.getY() + 1; i <= 7; i++){
            if(chessComponents[source.getX()][i] instanceof  EmptySlotComponent){
                this.canMoveToChess.add(chessComponents[source.getX()][i]);
            }else {
                if(this.getChessColor() != chessComponents[source.getX()][i].getChessColor()){
                    this.canMoveToChess.add(chessComponents[source.getX()][i]);
                }
                break;
            }
        }
        //向左移动
        for(int i = source.getY() - 1; i >= 0; i--){
            if(chessComponents[source.getX()][i] instanceof  EmptySlotComponent){
                this.canMoveToChess.add(chessComponents[source.getX()][i]);
            }else {
                if(this.getChessColor() != chessComponents[source.getX()][i].getChessColor()){
                    this.canMoveToChess.add(chessComponents[source.getX()][i]);
                }
                break;
            }
        }
    }


    /**
     * 判断王车易位以及将军
     * */
    private void whetherRookCanKillKing(ChessComponent[][] chessComponents){
        if (this.getChessColor() == ChessColor.BLACK) {
            boolean testWK = true;
            for (int i = 0; i < this.canMoveToChess.size(); i++) {
                if (canMoveToChess.get(i) instanceof KingChessComponent) {//判断是否将军的函数
                    ((KingChessComponent) chessComponents[canMoveToChess.get(i).getChessboardPoint().getX()][canMoveToChess.get(i).getChessboardPoint().getY()]).setUnderAttacked(true);
                }
                if (canMoveToChess.get(i).getChessboardPoint().getX() == 7 && (canMoveToChess.get(i).getChessboardPoint().getY() >= 2 && canMoveToChess.get(i).getChessboardPoint().getY() <= 6)) {
                    if (chessComponents[7][4] instanceof KingChessComponent && chessComponents[7][4].getChessColor() == ChessColor.WHITE) {
                        ((KingChessComponent) chessComponents[7][4]).setAllUnderAttacked(false);//判断是否满足王车易位前提条件的函数
                    }
                }
            }
        }
        if (this.getChessColor() == ChessColor.WHITE) {
            for (int i = 0; i < this.canMoveToChess.size(); i++) {
                if (canMoveToChess.get(i) instanceof KingChessComponent) {//判断是否将军的函数
                    ((KingChessComponent) chessComponents[canMoveToChess.get(i).getChessboardPoint().getX()][canMoveToChess.get(i).getChessboardPoint().getY()]).setUnderAttacked(true);
                }
                if (canMoveToChess.get(i).getChessboardPoint().getX() == 0 && (canMoveToChess.get(i).getChessboardPoint().getY() >= 2 && canMoveToChess.get(i).getChessboardPoint().getY() <= 6)) {
                    if (chessComponents[0][4] instanceof KingChessComponent && chessComponents[0][4].getChessColor() == ChessColor.BLACK) {
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
