package controller;


import model.*;
import view.Chessboard;
import java.util.List;


public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;
    /**private  ChessFrameGraph chessFrameGraphW;
    private  ChessFrameGraph chessFrameGraphB;*/




    /**public ClickController(Chessboard chessboard, ChessFrameGraph chessFrameGraphW, ChessFrameGraph chessFrameGraphB) {
        this.chessboard = chessboard;
        this.chessFrameGraphW = chessFrameGraphW;
        this.chessFrameGraphB = chessFrameGraphB;
    }
    public ClickController(Chessboard chessboard) {
        this(chessboard, null, null);
    }*/

    /**
     * 仅仅只是为了AI显示
     * */
    private List<ChessComponent> whereFirstCanMoveTo;


    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();



                //如果点击到了就先set，再paint出来
                first.whatChessCanMoveTo(this.chessboard.getChessComponents());
                whereFirstCanMoveTo = first.getCanMoveToChess();
                this.chessboard.paintChessboardOfWhereFirstCanReach(whereFirstCanMoveTo);
                this.chessboard.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取

                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
                this.chessboard.repaint();

                //再次点击就paint回去
                //this.chessboard.paintChessboardWhereFirstHasReachedBack(whereFirstCanMoveTo);


            } else if (handleSecond(chessComponent)) {

                //paint回去
                //this.chessboard.paintChessboardWhereFirstHasReachedBack(whereFirstCanMoveTo);


                setAbilityOfPawnChessComponent(chessComponent);
                isBound(chessComponent);
                /**
                 * 测试代码，判断王是否走了王车易位的前提步骤？那么就是chessComponent的x坐标要么就为短易位或者长易位，
                 * 但实际上都没关系吧，只要走了俩格子？然后还要把自己的位置传给自己的车？但这是没必要的，因为王车易位是算一步的，一旦王动了，直接移动相应的车，这个方法要在swap中加上去就ok了
                 * 判断王和车是否移动，怎么说，如果能进入到这个用来移动的方法中的话，也就说明它要移动了吧——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
                 * */
                whetherKingHasMoved();
                whetherRookHasMoved();
                /**把我的写的隔离起来————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/


                //repaint in swap chess method.
                chessboard.swapChessComponents(first, chessComponent);
                chessboard.swapColor();

                first.setSelected(false);
                /**
                 * 又需要一个新的方法，和上面的方法联合起来，一个设置boolean值，一个进行操作
                 * */
                /**把我写的隔离起来————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/
                this.chessboard.repaint();
                if( !(chessComponent instanceof KingChessComponent)){
                    chessboard.PawnChange(first);
                }
                /**大概在这个位置进行王被将军的报警————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

                /**
                 * 或许是不需要的，也有可能是王移动之后被仍然被将军了，那么first调用就显得比较垃圾？
                 * 不确定是否还需要再调用一下whatChessCanMoveTo，但是还是搞一下尝试一下——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————--*/
                first.whatChessCanMoveTo(this.chessboard.getChessComponents());
                for(int i = 0; i < this.chessboard.getChessComponents().length; i++){
                    for(int j = 0; j < this.chessboard.getChessComponents()[i].length; j++){
                        if(!(this.chessboard.getChessComponents()[i][j] instanceof KingChessComponent) && !(this.chessboard.getChessComponents()[i][j] instanceof EmptySlotComponent)){
                            this.chessboard.getChessComponents()[i][j].whatChessCanMoveTo(this.chessboard.getChessComponents());
                        }
                    }
                }
                /**————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/
                chessboard.KingInDanger(first);
                /**——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

                /**然后还要判定一个胜负*/

                first = null;

            }
        }
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }


    /**
     * 进行兵的吃过路兵变量设置
     * */
    private void setAbilityOfPawnChessComponent(ChessComponent chessComponent){
        if((first instanceof PawnChessComponent) && (first.getChessColor() == ChessColor.BLACK) && (first.getChessboardPoint().getX() == 1) && (first.getChessboardPoint().getX() - chessComponent.getChessboardPoint().getX() == -2)){
            int x = chessComponent.getChessboardPoint().getX(), y1 = chessComponent.getChessboardPoint().getY() - 1, y2 = chessComponent.getChessboardPoint().getY() + 1;
            if(y1 >= 0){
                if(chessboard.getChessComponents()[x][y1] instanceof PawnChessComponent && (chessboard.getChessComponents()[x][y1].getChessColor() == ChessColor.WHITE)){
                    ((PawnChessComponent) chessboard.getChessComponents()[x][y1]).setAbleToMove(true);
                }
            }
            if(y2 <= 7){
                if(chessboard.getChessComponents()[x][y2] instanceof PawnChessComponent && (chessboard.getChessComponents()[x][y2].getChessColor() == ChessColor.WHITE)){
                    ((PawnChessComponent) chessboard.getChessComponents()[x][y2]).setAbleToMove(true);
                }
            }
        }
        else if((first instanceof PawnChessComponent) && (first.getChessColor() == ChessColor.WHITE) && (first.getChessboardPoint().getX() == 6) && (first.getChessboardPoint().getX() - chessComponent.getChessboardPoint().getX() == 2)){
            int x = chessComponent.getChessboardPoint().getX(), y1 = chessComponent.getChessboardPoint().getY() - 1, y2 = chessComponent.getChessboardPoint().getY() + 1;
            if(y1 >= 0){
                if(chessboard.getChessComponents()[x][y1] instanceof PawnChessComponent && (chessboard.getChessComponents()[x][y1].getChessColor() == ChessColor.BLACK)){
                    ((PawnChessComponent) chessboard.getChessComponents()[x][y1]).setAbleToMove(true);
                }
            }
            if(y2 <= 7){
                if(chessboard.getChessComponents()[x][y2] instanceof PawnChessComponent && (chessboard.getChessComponents()[x][y2].getChessColor() == ChessColor.BLACK)){
                    ((PawnChessComponent) chessboard.getChessComponents()[x][y2]).setAbleToMove(true);
                }
            }
        }
    }


    /**
     * 设置小兵是否到达边界的判断
     * */
    private void isBound(ChessComponent chessComponent){
        if(first instanceof PawnChessComponent && first.getChessColor() == ChessColor.BLACK){
            if(chessComponent.getChessboardPoint().getX() == 7){
                this.chessboard.setBoundB(true);
            }
        }
        if(first instanceof PawnChessComponent && first.getChessColor() == ChessColor.WHITE){
            if(chessComponent.getChessboardPoint().getX() == 0){
                this.chessboard.setBoundW(true);
            }
        }
    }


    /**
     * 设置判断函数，判断王车是否移动过
     * */
    private void whetherKingHasMoved(){
        if(first instanceof KingChessComponent){
            ((KingChessComponent) first).setHasMoved(false);
        }
    }
    private void whetherRookHasMoved(){
        if(first instanceof RookChessComponent){
            ((RookChessComponent) first).setHasMoved(false);
        }
    }

    /**
     * 不知道为什么要拿出来
     *
    private void upDateJPanel(){
        this.chessFrameGraphW.removeAll();
        this.chessFrameGraphB.removeAll();
        this.chessFrameGraphW.repaint();System.out.println("repaint W");
        this.chessFrameGraphB.repaint();System.out.println("repaint B");
        this.chessFrameGraphW = new ChessFrameGraph(Color.WHITE, chessboard.getCurrentColor());
        this.chessFrameGraphB = new ChessFrameGraph(Color.BLACK, chessboard.getCurrentColor());
        this.chessFrameGraphW.updateUI();
        this.chessFrameGraphB.updateUI();
    }*/


    /**
     * 不清楚有什么作用，到时候看着删
     * */
    public ClickController getClickController(){
        return this;
    }
    public ChessColor getChessColor(){
        return this.chessboard.getCurrentColor();
    }
}
