package model;

import controller.ClickController;
import view.ChessboardPoint;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {
    private String name;

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, String name) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size, name);
        initiateEpmtyName(color);
    }

    public void initiateEpmtyName(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.NONE) {
                name = "_";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public void loadResource() throws IOException {
        //No resource!
    }




    /**——————————————————————————————————纯测试用————————————————————————————————————————————————不一定对————————————————————————————————————————————————————————————*/

    @Override
    public void whatChessCanMoveTo(ChessComponent[][] chessComponents) {

    }

    @Override
    public List<ChessComponent> getCanMoveToChess() {
        return null;
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
