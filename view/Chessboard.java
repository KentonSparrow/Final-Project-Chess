package view;


import controller.ClickController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    public int x = 0;

    /**
     * 又来测试了
     *, ChessFrameGraph chessFrameGraphW, ChessFrameGraph chessFrameGraphB
     private ChessFrameGraph chessFrameGraphW, chessFrameGraphB;*/

    /**
     * 胜负判定测试
     * */
    private ChessColor currentColor = ChessColor.WHITE;
    /**
     * 兵的升变测试，一直能变的是chessboard，那么应该在chessboard里面设置属于是否要升变的成员变量boolean isBound,然后在游戏主面板获得这个值？进行监听？然后弹出一个会使得原窗口失效的新窗口？
     */
    private boolean isBoundB = false;
    private boolean isBoundW = false;
    /**
     * 退回主界面
     */
    private JFrame frame;

    /**如果兵报过一次警了，就加1*/
    public int PawnHasAlreadyGivenAnAlarm = 0;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    /**计时器的时间设置在这里*/
    private int time = 60;

    public Chessboard(int width, int height, JFrame frame) {

        /**
         * 经典测试代码
         *
         this.chessFrameGraphW = chessFrameGraphW;this.chessFrameGraphB = chessFrameGraphB; */
        this.frame = frame;


        CHESS_SIZE = width / 8;//75
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

        initiateEmptyChessboard();

        // FIXME: Initialize chessboard for testing only.
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);


        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(1, i, ChessColor.BLACK);
        }
        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(CHESSBOARD_SIZE - 2, i, ChessColor.WHITE);
        }


        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);


        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);


        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);


        initKingOnBoard(0, 4, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        /**
         * 要在这个里面实现吃过路兵
         * 吃掉过路兵的过程其实只是比平时多一步，就是把chess2下面的棋子变成EmptySlotComponent
         * */
        if (chess1 instanceof PawnChessComponent) {
            if (((PawnChessComponent) chess1).getAbleToMove()) {
                ((PawnChessComponent) chess1).setAbleToMove(false);
                if (chess2 instanceof EmptySlotComponent) {
                    if (chess1.getChessColor() == ChessColor.BLACK && ((chess2.getChessboardPoint().getY() == chess1.getChessboardPoint().getY() + 1) || (chess2.getChessboardPoint().getY() == chess1.getChessboardPoint().getY() - 1))) {
                        ChessboardPoint chess3Point = new ChessboardPoint(chess2.getChessboardPoint().getX() - 1, chess2.getChessboardPoint().getY());
                        Point chess3LocationPoint = calculatePoint(chess2.getChessboardPoint().getX() - 1, chess2.getChessboardPoint().getY());
                        remove(chessComponents[chess2.getChessboardPoint().getX() - 1][chess2.getChessboardPoint().getY()]);
                        add(chessComponents[chess2.getChessboardPoint().getX() - 1][chess2.getChessboardPoint().getY()] = new EmptySlotComponent(chess3Point, chess3LocationPoint, ChessColor.NONE, clickController, CHESS_SIZE, chessComponents[chess2.getChessboardPoint().getX() - 1][chess2.getChessboardPoint().getY()].getName()));
                        chessComponents[chess2.getChessboardPoint().getX() - 1][chess2.getChessboardPoint().getY()].repaint();
                    }
                    if (chess1.getChessColor() == ChessColor.WHITE && ((chess2.getChessboardPoint().getY() == chess1.getChessboardPoint().getY() + 1) || (chess2.getChessboardPoint().getY() == chess1.getChessboardPoint().getY() - 1))) {
                        ChessboardPoint chess3Point = new ChessboardPoint(chess2.getChessboardPoint().getX() + 1, chess2.getChessboardPoint().getY());
                        Point chess3LocationPoint = calculatePoint(chess2.getChessboardPoint().getX() + 1, chess2.getChessboardPoint().getY());
                        remove(chessComponents[chess2.getChessboardPoint().getX() + 1][chess2.getChessboardPoint().getY()]);
                        add(chessComponents[chess2.getChessboardPoint().getX() + 1][chess2.getChessboardPoint().getY()] = new EmptySlotComponent(chess3Point, chess3LocationPoint, ChessColor.NONE, clickController, CHESS_SIZE, chessComponents[chess2.getChessboardPoint().getX() + 1][chess2.getChessboardPoint().getY()].getName()));
                        chessComponents[chess2.getChessboardPoint().getX() + 1][chess2.getChessboardPoint().getY()].repaint();
                    }
                }
            }
        }

        /**
         * 现在要弄个王车易位————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
         *
        if (chess1 instanceof KingChessComponent && chess1.getChessColor() == ChessColor.BLACK && chess2.getChessboardPoint().getX() == chess1.getChessboardPoint().getX() && chess1.getChessboardPoint().getX() == 0) {
            if (chess2.getChessboardPoint().getY() == 2) {
                ((KingChessComponent) chess1).setWhetherUseKRChange(false);
                chess1.swapLocation(chess2);
                int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
                chessComponents[row1][col1] = chess1;
                int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
                chessComponents[row2][col2] = chess2;
                chess1.repaint();
                chess2.repaint();

                chessComponents[0][0].swapLocation(chessComponents[0][3]);
                chessComponents[0][3] = chessComponents[0][0];
                remove(chessComponents[0][0]);
                ChessboardPoint chessboardPoint = new ChessboardPoint(0, 0);
                Point point = calculatePoint(0, 0);
                add(chessComponents[0][0] = new EmptySlotComponent(chessboardPoint, point, ChessColor.NONE, clickController, CHESS_SIZE, chessComponents[0][0].getName()));
                chessComponents[0][3].repaint();
                chessComponents[0][0].repaint();
            }
            if (chess2.getChessboardPoint().getY() == 6) {
                ((KingChessComponent) chess1).setWhetherUseKRChange(false);
                chess1.swapLocation(chess2);
                int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
                chessComponents[row1][col1] = chess1;
                int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
                chessComponents[row2][col2] = chess2;
                chess1.repaint();
                chess2.repaint();

                chessComponents[0][7].swapLocation(chessComponents[0][5]);
                chessComponents[0][5] = chessComponents[0][7];
                remove(chessComponents[0][7]);
                ChessboardPoint chessboardPoint = new ChessboardPoint(0, 7);
                Point point = calculatePoint(0, 7);
                add(chessComponents[0][7] = new EmptySlotComponent(chessboardPoint, point, ChessColor.NONE, clickController, CHESS_SIZE, chessComponents[0][7].getName()));
                chessComponents[0][7].repaint();
                chessComponents[0][5].repaint();
            }
        }
        if (chess1 instanceof KingChessComponent && chess1.getChessColor() == ChessColor.WHITE && chess2.getChessboardPoint().getX() == chess1.getChessboardPoint().getX() && chess1.getChessboardPoint().getX() == 7) {
            if (chess2.getChessboardPoint().getY() == 2) {
                ((KingChessComponent) chess1).setWhetherUseKRChange(false);
                chess1.swapLocation(chess2);
                int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
                chessComponents[row1][col1] = chess1;
                int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
                chessComponents[row2][col2] = chess2;
                chess1.repaint();
                chess2.repaint();

                chessComponents[7][0].swapLocation(chessComponents[7][3]);
                chessComponents[7][3] = chessComponents[7][0];
                remove(chessComponents[7][0]);
                ChessboardPoint chessboardPoint = new ChessboardPoint(7, 0);
                Point point = calculatePoint(7, 0);
                add(chessComponents[7][0] = new EmptySlotComponent(chessboardPoint, point, ChessColor.NONE, clickController, CHESS_SIZE, chessComponents[7][0].getName()));
                chessComponents[7][3].repaint();
                chessComponents[7][0].repaint();
            }
            if (chess2.getChessboardPoint().getY() == 6) {
                ((KingChessComponent) chess1).setWhetherUseKRChange(false);
                chess1.swapLocation(chess2);
                int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
                chessComponents[row1][col1] = chess1;
                int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
                chessComponents[row2][col2] = chess2;
                chess1.repaint();
                chess2.repaint();

                chessComponents[7][7].swapLocation(chessComponents[7][5]);
                chessComponents[7][5] = chessComponents[7][7];
                remove(chessComponents[7][7]);
                ChessboardPoint chessboardPoint = new ChessboardPoint(7, 7);
                Point point = calculatePoint(7, 7);
                add(chessComponents[7][7] = new EmptySlotComponent(chessboardPoint, point, ChessColor.NONE, clickController, CHESS_SIZE, chessComponents[7][7].getName()));
                chessComponents[7][7].repaint();
                chessComponents[7][5].repaint();
            }
        }*/
        /**————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        chess1.swapLocation(chess2);
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), ChessColor.NONE, clickController, CHESS_SIZE, "_"));
            chessComponents[chess2.getChessboardPoint().getX()][chess2.getChessboardPoint().getY()] = chess2;
        }
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;
        chess1.repaint();
        chess2.repaint();
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.NONE, clickController, CHESS_SIZE, "_"));
            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
        this.setTime(60);
    }

    public void initEmptyOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new EmptySlotComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, getName());
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, getName());
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    public void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, getName());
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    public void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, getName());
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    public void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, getName());
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    public void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, getName());
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    public void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, getName());
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(List<String> chessData) {
        chessData.forEach(System.out::println);
    }

    public boolean getBoundW() {
        return this.isBoundW;
    }

    public void setBoundW(boolean isBoundW) {
        this.isBoundW = isBoundW;
    }

    public boolean getBoundB() {
        return this.isBoundB;
    }

    /**
     * 自己写的，不知道有什么作用，到时候看着删
     * <p>
     * public ClickController getClickController(){
     * return this.clickController;
     * }
     * <p>
     * public void setChessFrameGraphW(Color color, ChessColor chessColor){
     * this.chessFrameGraphW = new ChessFrameGraph(color, chessColor);
     * }
     * public ChessFrameGraph getChessFrameGraphW(){
     * return chessFrameGraphW;
     * }
     * <p>
     * public void setChessFrameGraphB(Color color, ChessColor chessColor){
     * this.chessFrameGraphB = new ChessFrameGraph(color, chessColor);
     * }
     * public ChessFrameGraph getChessFrameGraphB(){
     * return chessFrameGraphB;
     * }
     */

    public void setBoundB(boolean isBoundB) {
        this.isBoundB = isBoundB;
    }

    public int getCHESS_SIZE() {
        return CHESS_SIZE;
    }


    /**
     * 当boolean值设置好之后，就进行升变的操作
     */
    public void PawnChange(ChessComponent chessComponent) {
        if (this.getBoundW() || this.getBoundB()) {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            ImageIcon iconW = ChessGraphData.WhitePawn;
            ImageIcon iconB = ChessGraphData.BlackPawn;
            ChessColor color = chessComponent.getChessColor();
            String[] options = {"Bishop", "Queen", "Rook", "Knight"};

            //int x = 0;

            if (this.getBoundW()) {
                x = JOptionPane.showOptionDialog(frame, "Please choose a chess; Default : Queen",
                        "The change of PawnChess", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconW, options, options[0]);
            }
            if (this.getBoundB()) {
                x = JOptionPane.showOptionDialog(frame, "Please choose a chess",
                        "The change of PawnChess", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconB, options, options[0]);
            }
            switch (x) {
                case -1, 1 -> {
                    if (chessComponent.getChessColor() == ChessColor.BLACK) {
                        remove(chessComponent);
                        add(chessComponent = new QueenChessComponent(chessComponent.getChessboardPoint(), chessComponent.getLocation(), color, this.clickController, getCHESS_SIZE(), "Q"));
                        this.chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()] = chessComponent;
                        chessComponent.repaint();
                    }
                    if (chessComponent.getChessColor() == ChessColor.WHITE) {
                        remove(chessComponent);
                        add(chessComponent = new QueenChessComponent(chessComponent.getChessboardPoint(), chessComponent.getLocation(), color, this.clickController, getCHESS_SIZE(), "q"));
                        this.chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()] = chessComponent;
                        chessComponent.repaint();
                    }

                }

                case 0 -> {
                    if (chessComponent.getChessColor() == ChessColor.BLACK) {
                        remove(chessComponent);
                        add(chessComponent = new BishopChessComponent(chessComponent.getChessboardPoint(), chessComponent.getLocation(), color, this.clickController, getCHESS_SIZE(), "B"));
                        this.chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()] = chessComponent;
                        chessComponent.repaint();
                    }
                    if (chessComponent.getChessColor() == ChessColor.WHITE) {
                        remove(chessComponent);
                        add(chessComponent = new BishopChessComponent(chessComponent.getChessboardPoint(), chessComponent.getLocation(), color, this.clickController, getCHESS_SIZE(), "b"));
                        this.chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()] = chessComponent;
                        chessComponent.repaint();
                    }

                }
                case 2 -> {
                    if (chessComponent.getChessColor() == ChessColor.BLACK) {
                        remove(chessComponent);
                        add(chessComponent = new RookChessComponent(chessComponent.getChessboardPoint(), chessComponent.getLocation(), color, this.clickController, getCHESS_SIZE(), "R"));
                        this.chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()] = chessComponent;
                        chessComponent.repaint();
                    }
                    if (chessComponent.getChessColor() == ChessColor.WHITE) {
                        remove(chessComponent);
                        add(chessComponent = new RookChessComponent(chessComponent.getChessboardPoint(), chessComponent.getLocation(), color, this.clickController, getCHESS_SIZE(), "r"));
                        this.chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()] = chessComponent;
                        chessComponent.repaint();
                    }

                }
                case 3 -> {
                    if (chessComponent.getChessColor() == ChessColor.BLACK) {
                        remove(chessComponent);
                        add(chessComponent = new KnightChessComponent(chessComponent.getChessboardPoint(), chessComponent.getLocation(), color, this.clickController, getCHESS_SIZE(), "N"));
                        this.chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()] = chessComponent;
                        chessComponent.repaint();
                    }
                    if (chessComponent.getChessColor() == ChessColor.WHITE) {
                        remove(chessComponent);
                        add(chessComponent = new KnightChessComponent(chessComponent.getChessboardPoint(), chessComponent.getLocation(), color, this.clickController, getCHESS_SIZE(), "n"));
                        this.chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()] = chessComponent;
                        chessComponent.repaint();
                    }
                }
            }
            this.repaint();
            this.setBoundW(false);
            this.setBoundB(false);
            chessComponent.whatChessCanMoveTo(this.getChessComponents());
            this.KingInDangerForPawn(chessComponent);
        }
    }


    /**
     * 现在搞一个操作，用来弄显示可以移动的位置和报警？报警的话感觉难受，应该是让King单独去paint，把自己的格子paint成红色？
     * 那么这个操作由first调用，也就是说点击了first后就显示可以移动的位置————不妨设置为蓝色的底色
     * 而且还需要一个函数在上面那个函数调用完之后重新接收到信息从而把棋盘变回去
     */
    public void paintChessboardOfWhereFirstCanReach(List<ChessComponent> chessComponents) {
        ChessComponent chessComponent = null;
        //chessComponent.setAI(false);
        //遍历整个可移动位置，然后让在其中的棋子一一重新paint？把重新paint的部分通过一个boolean控制？
        for (int i = 0; i < chessComponents.size(); i++) {
            if (chessComponents.get(i) instanceof EmptySlotComponent) {
                chessComponent = new EmptySlotComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.NONE, this.clickController, CHESS_SIZE, "_");
                chessComponent.setAI(false);
            }
            if (chessComponents.get(i) instanceof BishopChessComponent) {
                if (chessComponents.get(i).getChessColor() == ChessColor.BLACK) {
                    chessComponent = new BishopChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE, "B");
                    chessComponent.setAI(false);
                }
                if (chessComponents.get(i).getChessColor() == ChessColor.WHITE) {
                    chessComponent = new BishopChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE, "b");
                    chessComponent.setAI(false);
                }
            }
            if (chessComponents.get(i) instanceof KingChessComponent) {
                if (chessComponents.get(i).getChessColor() == ChessColor.BLACK) {
                    chessComponent = new KingChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE, "K");
                    chessComponent.setAI(false);
                    ((KingChessComponent) chessComponent).setUnderAttacked(true);
                }
                if (chessComponents.get(i).getChessColor() == ChessColor.WHITE) {
                    chessComponent = new KingChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE, "k");
                    chessComponent.setAI(false);
                    ((KingChessComponent) chessComponent).setUnderAttacked(true);
                }
            }
            if (chessComponents.get(i) instanceof KnightChessComponent) {
                if (chessComponents.get(i).getChessColor() == ChessColor.BLACK) {
                    chessComponent = new KnightChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE, "N");
                    chessComponent.setAI(false);
                }
                if (chessComponents.get(i).getChessColor() == ChessColor.WHITE) {
                    chessComponent = new KnightChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE, "n");
                    chessComponent.setAI(false);
                }
            }
            if (chessComponents.get(i) instanceof PawnChessComponent) {
                if (chessComponents.get(i).getChessColor() == ChessColor.BLACK) {
                    chessComponent = new PawnChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE, "P");
                    chessComponent.setAI(false);
                }
                if (chessComponents.get(i).getChessColor() == ChessColor.WHITE) {
                    chessComponent = new PawnChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE, "p");
                    chessComponent.setAI(false);
                }
            }
            if (chessComponents.get(i) instanceof QueenChessComponent) {
                if (chessComponents.get(i).getChessColor() == ChessColor.BLACK) {
                    chessComponent = new QueenChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE, "Q");
                    chessComponent.setAI(false);
                }
                if (chessComponents.get(i).getChessColor() == ChessColor.WHITE) {
                    chessComponent = new QueenChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE, "q");
                    chessComponent.setAI(false);
                }
            }
            if (chessComponents.get(i) instanceof RookChessComponent) {
                if (chessComponents.get(i).getChessColor() == ChessColor.BLACK) {
                    chessComponent = new RookChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE, "R");
                    chessComponent.setAI(false);
                }
                if (chessComponents.get(i).getChessColor() == ChessColor.WHITE) {
                    chessComponent = new RookChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE, "r");
                    chessComponent.setAI(false);
                }
            }
            chessComponent.repaint();
            System.out.println("fuck the java");
            remove(this.chessComponents[chessComponents.get(i).getChessboardPoint().getX()][chessComponents.get(i).getChessboardPoint().getY()]);
            System.out.println("remove");
            add(this.chessComponents[chessComponents.get(i).getChessboardPoint().getX()][chessComponents.get(i).getChessboardPoint().getY()] = chessComponent);
            this.chessComponents[chessComponents.get(i).getChessboardPoint().getX()][chessComponents.get(i).getChessboardPoint().getY()].repaint();
            repaint();
            System.out.println("repaint");
        }
    }

    /**
     * public void paintChessboardWhereFirstHasReachedBack(List<ChessComponent> chessComponents){
     * ChessComponent chessComponent = null;
     * //chessComponent.setAI(false);
     * //遍历整个可移动位置，然后让在其中的棋子一一重新paint？把重新paint的部分通过一个boolean控制？
     * for (int i = 0; i < chessComponents.size(); i++) {
     * if(chessComponents.get(i) instanceof EmptySlotComponent){
     * chessComponent = new EmptySlotComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * if(chessComponents.get(i) instanceof BishopChessComponent){
     * if(chessComponents.get(i).getChessColor() == ChessColor.BLACK){
     * chessComponent = new BishopChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * if(chessComponents.get(i).getChessColor() == ChessColor.WHITE){
     * chessComponent = new BishopChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * }
     * if(chessComponents.get(i) instanceof KingChessComponent){
     * if(chessComponents.get(i).getChessColor() == ChessColor.BLACK){
     * chessComponent = new KingChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * if(chessComponents.get(i).getChessColor() == ChessColor.WHITE){
     * chessComponent = new KingChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * }
     * if(chessComponents.get(i) instanceof KnightChessComponent){
     * if(chessComponents.get(i).getChessColor() == ChessColor.BLACK){
     * chessComponent = new KnightChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * if(chessComponents.get(i).getChessColor() == ChessColor.WHITE){
     * chessComponent = new KnightChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * }
     * if(chessComponents.get(i) instanceof PawnChessComponent){
     * if(chessComponents.get(i).getChessColor() == ChessColor.BLACK){
     * chessComponent = new PawnChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * if(chessComponents.get(i).getChessColor() == ChessColor.WHITE){
     * chessComponent = new PawnChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * }
     * if(chessComponents.get(i) instanceof QueenChessComponent){
     * if(chessComponents.get(i).getChessColor() == ChessColor.BLACK){
     * chessComponent = new QueenChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * if(chessComponents.get(i).getChessColor() == ChessColor.WHITE){
     * chessComponent = new QueenChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * }
     * if(chessComponents.get(i) instanceof RookChessComponent){
     * if(chessComponents.get(i).getChessColor() == ChessColor.BLACK){
     * chessComponent = new RookChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.BLACK, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * if(chessComponents.get(i).getChessColor() == ChessColor.WHITE){
     * chessComponent = new RookChessComponent(chessComponents.get(i).getChessboardPoint(), chessComponents.get(i).getLocation(), ChessColor.WHITE, this.clickController, CHESS_SIZE);
     * chessComponent.setAI(true);
     * }
     * }
     * chessComponent.repaint();
     * System.out.println("fuck the java");
     * remove(this.chessComponents[chessComponents.get(i).getChessboardPoint().getX()][chessComponents.get(i).getChessboardPoint().getY()]);
     * System.out.println("remove");
     * add(this.chessComponents[chessComponents.get(i).getChessboardPoint().getX()][chessComponents.get(i).getChessboardPoint().getY()] = chessComponent);
     * this.chessComponents[chessComponents.get(i).getChessboardPoint().getX()][chessComponents.get(i).getChessboardPoint().getY()].repaint();
     * repaint();
     * System.out.println("repaint");
     * }
     * }
     */


    //现在显示移动位置已经弄好了，差一个报警，并且想办法把所有的其它棋子设置为无法点击？只能够移动王？
    public void KingInDanger(ChessComponent chessComponent) {
        if (!(chessComponent instanceof KingChessComponent)) {
            ChessComponent KingB = null, KingW = null;
            for (int i = 0; i < chessComponents.length; i++) {
                for (int j = 0; j < chessComponents[i].length; j++) {
                    if (chessComponents[i][j] instanceof KingChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                        KingB = chessComponents[i][j];
                    }
                    if (chessComponents[i][j] instanceof KingChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                        KingW = chessComponents[i][j];
                    }
                }
            }
            //if(chessComponent.getChessColor() == ChessColor.BLACK) {//行棋方是黑子，危险的就是白王//但是也可能是黑王！！！！！

            if (KingW == null) {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                ImageIcon iconW = ChessGraphData.WhiteKing;
                String[] options = {"Restart a new game", "Back to the Home"};
                int x = 0;
                x = JOptionPane.showOptionDialog(frame, "The White Lose",
                        "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconW, options, options[0]);

                if (x == 0) {
                    //问题来了，这里要怎么实现重启
                    RestartTheGame();
                }
                if (x == -1 || x == 1) {
                    new GameMainFrame(875, 789);
                    this.frame.dispose();
                }
            }
            else {//胜负判定的必要条件是王在当前位置时，已经被将军// && ((KingChessComponent) KingW).isUnderAttacked()

                //int KingIsDeadW = 0;//判断标准是————王的每一个可移动位置都被吃，用一个int不断加一更好//但是这样很容易出现重复计算的情况

                ((KingChessComponent) KingW).whatChessCanMoveTo(this.chessComponents);//然后把王这个棋子的可以移动的位置弄出来
                chessComponents[KingW.getChessboardPoint().getX()][KingW.getChessboardPoint().getY()].whatChessCanMoveTo(this.chessComponents);//同理，王这个棋子所在的棋盘的对应位置棋子也要弄出来
                List<ChessComponent> tempKW = chessComponents[KingW.getChessboardPoint().getX()][KingW.getChessboardPoint().getY()].getCanMoveToChess();

                /**
                 * 1、通过一个for循环，设置相应个数的布尔值，组成一个数组，初始设置为false
                 * 2、然后遍历，如果有一个是存在就设置为true
                 * 3、但是现在还有一种可能性就是被将军方可能来救场————判断当前行棋方，就OK了，如果被将军方是行棋方，就可能来救场，不然的话就不可能
                 * */

                boolean[] KingIsDeadWBoolean = new boolean[KingW.getCanMoveToChess().size()];
                for (int i = 0; i < KingW.getCanMoveToChess().size(); i++){
                    KingIsDeadWBoolean[i] = false;
                }//现在已经把Boolean数组设置好了

                //为了之后的操作，如果王被一只棋子将到走投无路（看起来不可能，但是还是考虑一下）
                ChessComponent chessTemp = null;


                for (int i = 0; i < KingW.getCanMoveToChess().size(); i++) {//开始遍历王能够移动的位置，当且仅当王所有可能移动的位置都

                    for (int j = 0; j < chessComponents.length; j++) {//然后遍历棋盘，把所有的棋子都弄出来
                        for (int k = 0; k < chessComponents[j].length; k++) {
                            if (chessComponents[j][k].getChessColor() == ChessColor.BLACK && !(chessComponents[j][k] instanceof KingChessComponent)) {

                                chessComponents[j][k].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[j][k].getCanMoveToChess();
                                for (int a = 0; a < temp.size(); a++) {//遍历所获得的棋子的可移动位置，判断里面有没有包括王的可移动位置
                                    if (temp.get(a).getChessboardPoint().getX() == tempKW.get(i).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKW.get(i).getChessboardPoint().getY()) {
                                        KingIsDeadWBoolean[i] = true;
                                        chessTemp = temp.get(a);//如果tempKW里面只有一个的话，那么就后面直接用chessTemp；如果有多个的话也用不上了
                                    }
                                }
                            }
                        }
                    }
                }


                boolean whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;//还要设置一个判断Boolean
                int test = 0;
                for(int i = 0; i < KingW.getCanMoveToChess().size(); i++){
                    if(KingIsDeadWBoolean[i]){test = test + 1;}
                }
                if(test == KingW.getCanMoveToChess().size() && test != 0){whetherAllBooleanInKingIsDeadWBooleanIsTrue = true;}
                System.out.println("白王为什么为什么为什么为什么会" + whetherAllBooleanInKingIsDeadWBooleanIsTrue);



                if(getCurrentColor() == ChessColor.WHITE && tempKW.size() == 1 && chessTemp != null){
                    for(int i = 0; i < chessComponents.length; i++){
                        for(int j = 0; j < chessComponents[i].length; j++){
                            if(chessComponents[i][j].getChessColor() == ChessColor.WHITE && !(chessComponents[i][j] instanceof KingChessComponent)){
                                chessComponents[i][j].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[i][j].getCanMoveToChess();

                                if(chessTemp instanceof QueenChessComponent || chessTemp instanceof BishopChessComponent || chessTemp instanceof RookChessComponent){
                                    for(int a = 0; a < temp.size(); a++){
                                        for(int b = 0; b < tempKW.size(); b++){
                                            if(temp.get(a).getChessboardPoint().getX() == tempKW.get(b).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKW.get(b).getChessboardPoint().getY()){
                                                whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;
                                            }
                                        }
                                    }
                                }//对于王后，相，车来说，可以通过堵子的方法防范将死

                                for(int k = 0; k < temp.size(); k++){
                                    if(temp.get(k).getChessboardPoint().getX() == chessTemp.getChessboardPoint().getX() && temp.get(k).getChessboardPoint().getY() == chessTemp.getChessboardPoint().getY()){
                                        whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;
                                    }
                                }//所有的棋子都可以通过吃掉威胁棋子的方法防范将死

                            }
                        }
                    }



                }
                /**——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————*/

                    if((whetherAllBooleanInKingIsDeadWBooleanIsTrue && !(((KingChessComponent) chessComponents[KingW.getChessboardPoint().getX()][KingW.getChessboardPoint().getY()]).isLose())) || (getCurrentColor() == ChessColor.BLACK && ((KingChessComponent) KingW).isUnderAttacked()) ){

                            JFrame frame = new JFrame();
                            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                            ImageIcon iconW = ChessGraphData.WhiteKing;
                            String[] options = {"Restart a new game", "Back to the Home"};
                            int x = 0;
                            x = JOptionPane.showOptionDialog(frame, "The White Lose",
                                    "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconW, options, options[0]);

                            if (x == 0) {
                                //问题来了，这里要怎么实现重启
                                RestartTheGame();
                            }
                            if (x == -1 || x == 1) {
                                new GameMainFrame(875, 789);

                                //防止兵的升变将军导致的二次判负
                                ((KingChessComponent) chessComponents[KingW.getChessboardPoint().getX()][KingW.getChessboardPoint().getY()]).setLose(true);
                                ((KingChessComponent) KingW).setLose(true);


                                this.frame.dispose();
                            }
                    }

                        if (!(whetherAllBooleanInKingIsDeadWBooleanIsTrue) && PawnHasAlreadyGivenAnAlarm == 0 && ((KingChessComponent) KingW).isUnderAttacked() && !(getCurrentColor() == ChessColor.BLACK)) {
                            JOptionPane.showMessageDialog(new JFrame(), "白方被将军", "警报", JOptionPane.WARNING_MESSAGE);
                        }


            }



            //}


            //if(chessComponent.getChessColor() == ChessColor.WHITE) {//行棋方是白子，危险的是黑王
            if (KingB == null) {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                ImageIcon iconB = ChessGraphData.BlackKing;
                String[] options = {"Restart a new game", "Back to the Home"};
                int x = 0;
                x = JOptionPane.showOptionDialog(frame, "The Black Lose",
                        "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconB, options, options[0]);
                if (x == 0) {
                    //问题来了，这里要怎么实现重启
                    RestartTheGame();
                }
                if (x == -1 || x == 1) {
                    new GameMainFrame(875, 789);
                    this.frame.dispose();
                }
            }
            else {//胜负判定的必要条件是王在当前位置时，已经被将军// && ((KingChessComponent) KingB).isUnderAttacked()

                //int KingIsDeadB = 0;//判断标准是————王的每一个可移动位置都被吃，用一个int不断加一更好

                ((KingChessComponent) KingB).whatChessCanMoveTo(this.chessComponents);//然后把王这个棋子的可以移动的位置弄出来
                chessComponents[KingB.getChessboardPoint().getX()][KingB.getChessboardPoint().getY()].whatChessCanMoveTo(this.chessComponents);//同理，王这个棋子所在的棋盘的对应位置棋子也要弄出来
                List<ChessComponent> tempKB = chessComponents[KingB.getChessboardPoint().getX()][KingB.getChessboardPoint().getY()].getCanMoveToChess();


                boolean[] KingIsDeadWBoolean = new boolean[KingB.getCanMoveToChess().size()];
                for (int i = 0; i < KingB.getCanMoveToChess().size(); i++){
                    KingIsDeadWBoolean[i] = false;
                }//现在已经把Boolean数组设置好了

                //为了之后的操作，如果王被一只棋子将到走投无路（看起来不可能，但是还是考虑一下）
                ChessComponent chessTemp = null;


                for (int i = 0; i < KingB.getCanMoveToChess().size(); i++) {//开始遍历王能够移动的位置，当且仅当王所有可能移动的位置都

                    for (int j = 0; j < chessComponents.length; j++) {//然后遍历棋盘，把所有的棋子都弄出来
                        for (int k = 0; k < chessComponents[j].length; k++) {
                            if (chessComponents[j][k].getChessColor() == ChessColor.WHITE && !(chessComponents[j][k] instanceof KingChessComponent)) {

                                chessComponents[j][k].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[j][k].getCanMoveToChess();
                                for (int a = 0; a < temp.size(); a++) {//遍历所获得的棋子的可移动位置，判断里面有没有包括王的可移动位置
                                    if (temp.get(a).getChessboardPoint().getX() == tempKB.get(i).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKB.get(i).getChessboardPoint().getY()) {
                                        KingIsDeadWBoolean[i] = true;
                                        chessTemp = temp.get(a);//如果tempKW里面只有一个的话，那么就后面直接用chessTemp；如果有多个的话也用不上了
                                    }
                                }
                            }
                        }
                    }
                }



                //System.out.println("为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么为什么刷新了两次捏 " + KingIsDeadB);
                boolean whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;//还要设置一个判断Boolean
                int test = 0;
                for(int i = 0; i < KingB.getCanMoveToChess().size(); i++){
                    if(KingIsDeadWBoolean[i]){test = test + 1;}
                }
                if(test == KingB.getCanMoveToChess().size() && test != 0){whetherAllBooleanInKingIsDeadWBooleanIsTrue = true;}
                System.out.println("这个是个什么东西什么东西什么东向西为什么没有将死" + whetherAllBooleanInKingIsDeadWBooleanIsTrue);


                if(getCurrentColor() == ChessColor.BLACK && tempKB.size() == 1 && chessTemp != null){
                    for(int i = 0; i < chessComponents.length; i++){
                        for(int j = 0; j < chessComponents[i].length; j++){
                            if(chessComponents[i][j].getChessColor() == ChessColor.WHITE && !(chessComponents[i][j] instanceof KingChessComponent)){
                                chessComponents[i][j].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[i][j].getCanMoveToChess();

                                if(chessTemp instanceof QueenChessComponent || chessTemp instanceof BishopChessComponent || chessTemp instanceof RookChessComponent){
                                    for(int a = 0; a < temp.size(); a++){
                                        for(int b = 0; b < tempKB.size(); b++){
                                            if(temp.get(a).getChessboardPoint().getX() == tempKB.get(b).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKB.get(b).getChessboardPoint().getY()){
                                                whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;
                                            }
                                        }
                                    }
                                }//对于王后，相，车来说，可以通过堵子的方法防范将死

                                for(int k = 0; k < temp.size(); k++){
                                    if(temp.get(k).getChessboardPoint().getX() == chessTemp.getChessboardPoint().getX() && temp.get(k).getChessboardPoint().getY() == chessTemp.getChessboardPoint().getY()){
                                        whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;
                                    }
                                }//所有的棋子都可以通过吃掉威胁棋子的方法防范将死

                            }
                        }
                    }



                }

                if((whetherAllBooleanInKingIsDeadWBooleanIsTrue && !(((KingChessComponent) chessComponents[KingB.getChessboardPoint().getX()][KingB.getChessboardPoint().getY()]).isLose())) || (getCurrentColor() == ChessColor.WHITE && ((KingChessComponent) KingB).isUnderAttacked())){

                        JFrame frame = new JFrame();
                        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        ImageIcon iconB = ChessGraphData.BlackKing;
                        String[] options = {"Restart a new game", "Back to the Home"};
                        int x = 0;
                        x = JOptionPane.showOptionDialog(frame, "The Black Lose",
                                "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconB, options, options[0]);
                        if (x == 0) {
                            //问题来了，这里要怎么实现重启
                            RestartTheGame();
                        }
                        if (x == -1 || x == 1) {
                            new GameMainFrame(875, 789);

                            ((KingChessComponent) chessComponents[KingB.getChessboardPoint().getX()][KingB.getChessboardPoint().getY()]).setLose(true);
                            ((KingChessComponent) KingB).setLose(true);

                            this.frame.dispose();
                        }

                }

                    if (!(whetherAllBooleanInKingIsDeadWBooleanIsTrue) && PawnHasAlreadyGivenAnAlarm == 0 && ((KingChessComponent) KingB).isUnderAttacked() && !(getCurrentColor() == ChessColor.WHITE)) {
                        // System.out.println("White " + ((KingChessComponent) KingB).isUnderAttacked());
                        JOptionPane.showMessageDialog(new JFrame(), "黑方被将军", "警报", JOptionPane.WARNING_MESSAGE);
                    }



            }
            //System.out.println("White " + ((KingChessComponent) KingB).isUnderAttacked());
            //}
        } else {

            //现在是王移动的情况，按理来说，由于王不能将王，所以王移动只可能是把自己送去紫砂
            //王移动后判断它是否属于紫砂的逻辑和上面的应该是一样的
            //并且，王紫砂的行为是救不了的，本来就是行棋方，却送去紫砂的话，下一步就是别人吃掉它了

            for (int i = 0; i < chessComponents.length; i++) {
                for (int j = 0; j < chessComponents[i].length; j++) {
                    if (chessComponents[i][j].getChessColor() != chessComponent.getChessColor() && !(chessComponents[i][j] instanceof KingChessComponent)) {
                        chessComponents[i][j].whatChessCanMoveTo(this.getChessComponents());
                    }
                }
            }


            if (((KingChessComponent) chessComponent).isUnderAttacked() && chessComponent.getChessColor() == ChessColor.BLACK) {
                //int KingIsDead = 0;
                //这个时候是黑王被将军了，然后在判断黑王的可移动位置
                ((KingChessComponent) chessComponent).whatChessCanMoveTo(this.getChessComponents());
                chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()].whatChessCanMoveTo(this.getChessComponents());

                List<ChessComponent> tempKB = chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()].getCanMoveToChess();

                boolean[] KingIsDeadWBoolean = new boolean[chessComponent.getCanMoveToChess().size()];
                for (int i = 0; i < chessComponent.getCanMoveToChess().size(); i++){
                    KingIsDeadWBoolean[i] = false;
                }//现在已经把Boolean数组设置好了


                for (int i = 0; i < chessComponent.getCanMoveToChess().size(); i++) {//开始遍历王能够移动的位置，当且仅当王所有可能移动的位置都

                    for (int j = 0; j < chessComponents.length; j++) {//然后遍历棋盘，把所有的棋子都弄出来
                        for (int k = 0; k < chessComponents[j].length; k++) {
                            if (chessComponents[j][k].getChessColor() == ChessColor.WHITE && !(chessComponents[j][k] instanceof KingChessComponent)) {

                                chessComponents[j][k].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[j][k].getCanMoveToChess();
                                for (int a = 0; a < temp.size(); a++) {//遍历所获得的棋子的可移动位置，判断里面有没有包括王的可移动位置
                                    if (temp.get(a).getChessboardPoint().getX() == tempKB.get(i).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKB.get(i).getChessboardPoint().getY()) {
                                        KingIsDeadWBoolean[i] = true;
                                    }
                                }
                            }
                        }
                    }
                }


                boolean whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;//还要设置一个判断Boolean
                int test = 0;
                for(int i = 0; i < chessComponent.getCanMoveToChess().size(); i++){
                    if(KingIsDeadWBoolean[i]){test = test + 1;}
                }
                if(test == chessComponent.getCanMoveToChess().size()){whetherAllBooleanInKingIsDeadWBooleanIsTrue = true;}

                if(whetherAllBooleanInKingIsDeadWBooleanIsTrue || getCurrentColor() ==ChessColor.WHITE){
                    if (!(((KingChessComponent) chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()]).isLose())) {
                        JFrame frame = new JFrame();
                        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        ImageIcon iconB = ChessGraphData.BlackKing;
                        String[] options = {"Restart a new game", "Back to the Home"};
                        int x = 0;
                        x = JOptionPane.showOptionDialog(frame, "The Black Lose",
                                "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconB, options, options[0]);

                        if (x == 0) {
                            //问题来了，这里要怎么实现重启
                            RestartTheGame();
                        }
                        if (x == -1 || x == 1) {
                            new GameMainFrame(875, 789);

                            ((KingChessComponent) chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()]).setLose(true);
                            ((KingChessComponent) chessComponent).setLose(true);


                            this.frame.dispose();
                        }
                    }
                }
                else{
                    if (PawnHasAlreadyGivenAnAlarm == 0) {
                        JOptionPane.showMessageDialog(new JFrame(), "黑方被将军", "警报", JOptionPane.WARNING_MESSAGE);
                    }

                }



            }
            if (((KingChessComponent) chessComponent).isUnderAttacked() && chessComponent.getChessColor() == ChessColor.WHITE) {
                int KingIsDead = 0;//这个时候白王被将军

                ((KingChessComponent) chessComponent).whatChessCanMoveTo(this.chessComponents);//然后把王这个棋子的可以移动的位置弄出来
                chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()].whatChessCanMoveTo(this.chessComponents);//同理，王这个棋子所在的棋盘的对应位置棋子也要弄出来
                List<ChessComponent> tempKW = chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()].getCanMoveToChess();

                boolean[] KingIsDeadWBoolean = new boolean[chessComponent.getCanMoveToChess().size()];
                for (int i = 0; i < chessComponent.getCanMoveToChess().size(); i++){
                    KingIsDeadWBoolean[i] = false;
                }//现在已经把Boolean数组设置好了


                for (int i = 0; i < chessComponent.getCanMoveToChess().size(); i++) {//开始遍历王能够移动的位置，当且仅当王所有可能移动的位置都

                    for (int j = 0; j < chessComponents.length; j++) {//然后遍历棋盘，把所有的棋子都弄出来
                        for (int k = 0; k < chessComponents[j].length; k++) {
                            if (chessComponents[j][k].getChessColor() == ChessColor.BLACK && !(chessComponents[j][k] instanceof KingChessComponent)) {

                                chessComponents[j][k].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[j][k].getCanMoveToChess();
                                for (int a = 0; a < temp.size(); a++) {//遍历所获得的棋子的可移动位置，判断里面有没有包括王的可移动位置
                                    if (temp.get(a).getChessboardPoint().getX() == tempKW.get(i).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKW.get(i).getChessboardPoint().getY()) {
                                        KingIsDeadWBoolean[i] = true;
                                    }
                                }
                            }
                        }
                    }
                }

                boolean whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;//还要设置一个判断Boolean
                int test = 0;
                for(int i = 0; i < chessComponent.getCanMoveToChess().size(); i++){
                    if(KingIsDeadWBoolean[i]){test = test + 1;}
                }
                if(test == chessComponent.getCanMoveToChess().size()){whetherAllBooleanInKingIsDeadWBooleanIsTrue = true;}


                if (whetherAllBooleanInKingIsDeadWBooleanIsTrue || getCurrentColor() == ChessColor.BLACK) {
                    if ((((KingChessComponent) chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()]).isLose())) {
                        JFrame frame = new JFrame();
                        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        ImageIcon iconW = ChessGraphData.WhiteKing;
                        String[] options = {"Restart a new game", "Back to the Home"};
                        int x = 0;
                        x = JOptionPane.showOptionDialog(frame, "The White Lose",
                                "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconW, options, options[0]);

                        if (x == 0) {
                            //问题来了，这里要怎么实现重启
                            RestartTheGame();
                        }
                        if (x == -1 || x == 1) {
                            new GameMainFrame(875, 789);


                            ((KingChessComponent) chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()]).setLose(true);
                            ((KingChessComponent) chessComponent).setLose(true);


                            this.frame.dispose();
                        }
                    }
                }
                else{
                    if (PawnHasAlreadyGivenAnAlarm == 0) {
                        JOptionPane.showMessageDialog(new JFrame(), "白方被将军", "警报", JOptionPane.WARNING_MESSAGE);
                    }
                }

            }
        }
        //
        PawnHasAlreadyGivenAnAlarm = 0;
        //
    }
    public void KingInDangerForPawn(ChessComponent chessComponent) {
        if (!(chessComponent instanceof KingChessComponent)) {
            ChessComponent KingB = null, KingW = null;
            for (int i = 0; i < chessComponents.length; i++) {
                for (int j = 0; j < chessComponents[i].length; j++) {
                    if (chessComponents[i][j] instanceof KingChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                        KingB = chessComponents[i][j];
                    }
                    if (chessComponents[i][j] instanceof KingChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                        KingW = chessComponents[i][j];
                    }
                }
            }
            //if(chessComponent.getChessColor() == ChessColor.BLACK) {//行棋方是黑子，危险的就是白王//但是也可能是黑王！！！！！

            if (KingW == null) {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                ImageIcon iconW = ChessGraphData.WhiteKing;
                String[] options = {"Restart a new game", "Back to the Home"};
                int x = 0;
                x = JOptionPane.showOptionDialog(frame, "The White Lose",
                        "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconW, options, options[0]);

                if (x == 0) {
                    //问题来了，这里要怎么实现重启
                    RestartTheGame();
                }
                if (x == -1 || x == 1) {
                    new GameMainFrame(875, 789);
                    this.frame.dispose();
                }
            }
            if (KingW != null && ((KingChessComponent) KingW).isUnderAttacked()) {//胜负判定的必要条件是王在当前位置时，已经被将军

                int KingIsDeadW = 0;//判断标准是————王的每一个可移动位置都被吃，用一个int不断加一更好


                ((KingChessComponent) KingW).whatChessCanMoveTo(this.chessComponents);//然后把王这个棋子的可以移动的位置弄出来
                chessComponents[KingW.getChessboardPoint().getX()][KingW.getChessboardPoint().getY()].whatChessCanMoveTo(this.chessComponents);//同理，王这个棋子所在的棋盘的对应位置棋子也要弄出来
                List<ChessComponent> tempKW = chessComponents[KingW.getChessboardPoint().getX()][KingW.getChessboardPoint().getY()].getCanMoveToChess();


                boolean[] KingIsDeadWBoolean = new boolean[KingW.getCanMoveToChess().size()];
                for (int i = 0; i < KingW.getCanMoveToChess().size(); i++){
                    KingIsDeadWBoolean[i] = false;
                }//现在已经把Boolean数组设置好了

                //为了之后的操作，如果王被一只棋子将到走投无路（看起来不可能，但是还是考虑一下）
                ChessComponent chessTemp = null;



                for (int i = 0; i < KingW.getCanMoveToChess().size(); i++) {//开始遍历王能够移动的位置，当且仅当王所有可能移动的位置都

                    for (int j = 0; j < chessComponents.length; j++) {//然后遍历棋盘，把所有的棋子都弄出来
                        for (int k = 0; k < chessComponents[j].length; k++) {
                            if (chessComponents[j][k].getChessColor() == ChessColor.BLACK && !(chessComponents[j][k] instanceof KingChessComponent)) {

                                chessComponents[j][k].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[j][k].getCanMoveToChess();
                                for (int a = 0; a < temp.size(); a++) {//遍历所获得的棋子的可移动位置，判断里面有没有包括王的可移动位置
                                    if (temp.get(a).getChessboardPoint().getX() == tempKW.get(i).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKW.get(i).getChessboardPoint().getY()) {
                                        KingIsDeadWBoolean[i] = true;
                                        chessTemp = temp.get(a);
                                    }
                                }
                            }
                        }
                    }
                }


                boolean whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;//还要设置一个判断Boolean
                int test = 0;
                for(int i = 0; i < KingW.getCanMoveToChess().size(); i++){
                    if(KingIsDeadWBoolean[i]){test = test + 1;}
                }
                if(test == KingW.getCanMoveToChess().size()){whetherAllBooleanInKingIsDeadWBooleanIsTrue = true;}


                if(getCurrentColor() == ChessColor.WHITE && tempKW.size() == 1 && chessTemp != null){
                    for(int i = 0; i < chessComponents.length; i++){
                        for(int j = 0; j < chessComponents[i].length; j++){
                            if(chessComponents[i][j].getChessColor() == ChessColor.WHITE && !(chessComponents[i][j] instanceof KingChessComponent)){
                                chessComponents[i][j].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[i][j].getCanMoveToChess();

                                if(chessTemp instanceof QueenChessComponent || chessTemp instanceof BishopChessComponent || chessTemp instanceof RookChessComponent){
                                    for(int a = 0; a < temp.size(); a++){
                                        for(int b = 0; b < tempKW.size(); b++){
                                            if(temp.get(a).getChessboardPoint().getX() == tempKW.get(b).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKW.get(b).getChessboardPoint().getY()){
                                                whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;
                                            }
                                        }
                                    }
                                }//对于王后，相，车来说，可以通过堵子的方法防范将死

                                for(int k = 0; k < temp.size(); k++){
                                    if(temp.get(k).getChessboardPoint().getX() == chessTemp.getChessboardPoint().getX() && temp.get(k).getChessboardPoint().getY() == chessTemp.getChessboardPoint().getY()){
                                        whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;
                                    }
                                }//所有的棋子都可以通过吃掉威胁棋子的方法防范将死

                            }
                        }
                    }



                }



                if(whetherAllBooleanInKingIsDeadWBooleanIsTrue || getCurrentColor() == ChessColor.BLACK){
                    if (!(((KingChessComponent) chessComponents[KingW.getChessboardPoint().getX()][KingW.getChessboardPoint().getY()]).isLose())) {
                        JFrame frame = new JFrame();
                        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        ImageIcon iconW = ChessGraphData.WhiteKing;
                        String[] options = {"Restart a new game", "Back to the Home"};
                        int x = 0;
                        x = JOptionPane.showOptionDialog(frame, "The White Lose",
                                "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconW, options, options[0]);

                        if (x == 0) {
                            //问题来了，这里要怎么实现重启
                            RestartTheGame();
                        }
                        if (x == -1 || x == 1) {
                            new GameMainFrame(875, 789);

                            //防止兵的升变将军导致的二次判负
                            ((KingChessComponent) chessComponents[KingW.getChessboardPoint().getX()][KingW.getChessboardPoint().getY()]).setLose(true);
                            ((KingChessComponent) KingW).setLose(true);


                            this.frame.dispose();
                        }
                    }
                }else{
                    //
                    PawnHasAlreadyGivenAnAlarm = PawnHasAlreadyGivenAnAlarm + 1;
                    //

                    JOptionPane.showMessageDialog(new JFrame(), "白方被将军", "警报", JOptionPane.WARNING_MESSAGE);

                }



            }


            //}


            //if(chessComponent.getChessColor() == ChessColor.WHITE) {//行棋方是白子，危险的是黑王
            if (KingB == null) {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                ImageIcon iconB = ChessGraphData.BlackKing;
                String[] options = {"Restart a new game", "Back to the Home"};
                int x = 0;
                x = JOptionPane.showOptionDialog(frame, "The Black Lose",
                        "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconB, options, options[0]);
                if (x == 0) {
                    //问题来了，这里要怎么实现重启
                    RestartTheGame();
                }
                if (x == -1 || x == 1) {
                    new GameMainFrame(875, 789);
                    this.frame.dispose();
                }
            }
            if (KingB != null && ((KingChessComponent) KingB).isUnderAttacked()) {//胜负判定的必要条件是王在当前位置时，已经被将军

                int KingIsDeadB = 0;//判断标准是————王的每一个可移动位置都被吃，用一个int不断加一更好

                ((KingChessComponent) KingB).whatChessCanMoveTo(this.chessComponents);//然后把王这个棋子的可以移动的位置弄出来
                chessComponents[KingB.getChessboardPoint().getX()][KingB.getChessboardPoint().getY()].whatChessCanMoveTo(this.chessComponents);//同理，王这个棋子所在的棋盘的对应位置棋子也要弄出来
                List<ChessComponent> tempKB = chessComponents[KingB.getChessboardPoint().getX()][KingB.getChessboardPoint().getY()].getCanMoveToChess();

                boolean[] KingIsDeadWBoolean = new boolean[KingB.getCanMoveToChess().size()];
                for (int i = 0; i < KingB.getCanMoveToChess().size(); i++){
                    KingIsDeadWBoolean[i] = false;
                }//现在已经把Boolean数组设置好了

                //为了之后的操作，如果王被一只棋子将到走投无路（看起来不可能，但是还是考虑一下）
                ChessComponent chessTemp = null;


                for (int i = 0; i < KingB.getCanMoveToChess().size(); i++) {//开始遍历王能够移动的位置，当且仅当王所有可能移动的位置都

                    for (int j = 0; j < chessComponents.length; j++) {//然后遍历棋盘，把所有的棋子都弄出来
                        for (int k = 0; k < chessComponents[j].length; k++) {
                            if (chessComponents[j][k].getChessColor() == ChessColor.WHITE && !(chessComponents[j][k] instanceof KingChessComponent)) {

                                chessComponents[j][k].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[j][k].getCanMoveToChess();
                                for (int a = 0; a < temp.size(); a++) {//遍历所获得的棋子的可移动位置，判断里面有没有包括王的可移动位置
                                    if (temp.get(a).getChessboardPoint().getX() == tempKB.get(i).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKB.get(i).getChessboardPoint().getY()) {
                                        KingIsDeadWBoolean[i] = true;
                                        chessTemp = temp.get(a);
                                    }
                                }
                            }
                        }
                    }
                }



                boolean whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;//还要设置一个判断Boolean
                int test = 0;
                for(int i = 0; i < KingB.getCanMoveToChess().size(); i++){
                    if(KingIsDeadWBoolean[i]){test = test + 1;}
                }
                if(test == KingB.getCanMoveToChess().size()){whetherAllBooleanInKingIsDeadWBooleanIsTrue = true;}



                if(getCurrentColor() == ChessColor.BLACK && tempKB.size() == 1 && chessTemp != null){
                    for(int i = 0; i < chessComponents.length; i++){
                        for(int j = 0; j < chessComponents[i].length; j++){
                            if(chessComponents[i][j].getChessColor() == ChessColor.WHITE && !(chessComponents[i][j] instanceof KingChessComponent)){
                                chessComponents[i][j].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[i][j].getCanMoveToChess();

                                if(chessTemp instanceof QueenChessComponent || chessTemp instanceof BishopChessComponent || chessTemp instanceof RookChessComponent){
                                    for(int a = 0; a < temp.size(); a++){
                                        for(int b = 0; b < tempKB.size(); b++){
                                            if(temp.get(a).getChessboardPoint().getX() == tempKB.get(b).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKB.get(b).getChessboardPoint().getY()){
                                                whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;
                                            }
                                        }
                                    }
                                }//对于王后，相，车来说，可以通过堵子的方法防范将死

                                for(int k = 0; k < temp.size(); k++){
                                    if(temp.get(k).getChessboardPoint().getX() == chessTemp.getChessboardPoint().getX() && temp.get(k).getChessboardPoint().getY() == chessTemp.getChessboardPoint().getY()){
                                        whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;
                                    }
                                }//所有的棋子都可以通过吃掉威胁棋子的方法防范将死

                            }
                        }
                    }



                }

                //System.out.println("为什么刷新了两次捏 " + KingIsDeadB);



                if(whetherAllBooleanInKingIsDeadWBooleanIsTrue || getCurrentColor() == ChessColor.WHITE){
                    if (!(((KingChessComponent) chessComponents[KingB.getChessboardPoint().getX()][KingB.getChessboardPoint().getY()]).isLose())) {
                        JFrame frame = new JFrame();
                        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        ImageIcon iconB = ChessGraphData.BlackKing;
                        String[] options = {"Restart a new game", "Back to the Home"};
                        int x = 0;
                        x = JOptionPane.showOptionDialog(frame, "The Black Lose",
                                "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconB, options, options[0]);
                        if (x == 0) {
                            //问题来了，这里要怎么实现重启
                            RestartTheGame();
                        }
                        if (x == -1 || x == 1) {
                            new GameMainFrame(875, 789);

                            ((KingChessComponent) chessComponents[KingB.getChessboardPoint().getX()][KingB.getChessboardPoint().getY()]).setLose(true);
                            ((KingChessComponent) KingB).setLose(true);

                            this.frame.dispose();
                        }
                    }
                }else{
                    //
                    PawnHasAlreadyGivenAnAlarm = PawnHasAlreadyGivenAnAlarm + 1;
                    //

                    //System.out.println("White " + ((KingChessComponent) KingB).isUnderAttacked());
                    JOptionPane.showMessageDialog(new JFrame(), "黑方被将军", "警报", JOptionPane.WARNING_MESSAGE);

                }


            }
            //System.out.println("White " + ((KingChessComponent) KingB).isUnderAttacked());
            //}
        } else {
            for (int i = 0; i < chessComponents.length; i++) {
                for (int j = 0; j < chessComponents[i].length; j++) {
                    if (chessComponents[i][j].getChessColor() != chessComponent.getChessColor() && !(chessComponents[i][j] instanceof KingChessComponent)) {
                        chessComponents[i][j].whatChessCanMoveTo(this.getChessComponents());
                    }
                }
            }



            if (((KingChessComponent) chessComponent).isUnderAttacked() && chessComponent.getChessColor() == ChessColor.BLACK) {
                int KingIsDead = 0;
                //这个时候是黑王被将军了，然后在判断黑王的可移动位置
                ((KingChessComponent) chessComponent).whatChessCanMoveTo(this.getChessComponents());
                chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()].whatChessCanMoveTo(this.getChessComponents());

                List<ChessComponent> tempKB = chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()].getCanMoveToChess();

                boolean[] KingIsDeadWBoolean = new boolean[chessComponent.getCanMoveToChess().size()];
                for (int i = 0; i < chessComponent.getCanMoveToChess().size(); i++){
                    KingIsDeadWBoolean[i] = false;
                }//现在已经把Boolean数组设置好了


                for (int i = 0; i < chessComponent.getCanMoveToChess().size(); i++) {//开始遍历王能够移动的位置，当且仅当王所有可能移动的位置都

                    for (int j = 0; j < chessComponents.length; j++) {//然后遍历棋盘，把所有的棋子都弄出来
                        for (int k = 0; k < chessComponents[j].length; k++) {
                            if (chessComponents[j][k].getChessColor() == ChessColor.WHITE && !(chessComponents[j][k] instanceof KingChessComponent)) {

                                chessComponents[j][k].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[j][k].getCanMoveToChess();
                                for (int a = 0; a < temp.size(); a++) {//遍历所获得的棋子的可移动位置，判断里面有没有包括王的可移动位置
                                    if (temp.get(a).getChessboardPoint().getX() == tempKB.get(i).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKB.get(i).getChessboardPoint().getY()) {
                                        KingIsDeadWBoolean[i] = true;
                                    }
                                }
                            }
                        }
                    }
                }

                boolean whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;//还要设置一个判断Boolean
                int test = 0;
                for(int i = 0; i < chessComponent.getCanMoveToChess().size(); i++){
                    if(KingIsDeadWBoolean[i]){test = test + 1;}
                }
                if(test == chessComponent.getCanMoveToChess().size()){whetherAllBooleanInKingIsDeadWBooleanIsTrue = true;}


                if(whetherAllBooleanInKingIsDeadWBooleanIsTrue || getCurrentColor() == ChessColor.WHITE){
                    if (!(((KingChessComponent) chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()]).isLose())) {
                        JFrame frame = new JFrame();
                        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        ImageIcon iconB = ChessGraphData.BlackKing;
                        String[] options = {"Restart a new game", "Back to the Home"};
                        int x = 0;
                        x = JOptionPane.showOptionDialog(frame, "The Black Lose",
                                "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconB, options, options[0]);

                        if (x == 0) {
                            //问题来了，这里要怎么实现重启
                            RestartTheGame();
                        }
                        if (x == -1 || x == 1) {
                            new GameMainFrame(875, 789);

                            ((KingChessComponent) chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()]).setLose(true);
                            ((KingChessComponent) chessComponent).setLose(true);


                            this.frame.dispose();
                        }
                    }
                }else{
                    //
                    PawnHasAlreadyGivenAnAlarm = PawnHasAlreadyGivenAnAlarm + 1;
                    //

                    JOptionPane.showMessageDialog(new JFrame(), "黑方被将军", "警报", JOptionPane.WARNING_MESSAGE);
                    }
                }
            if (((KingChessComponent) chessComponent).isUnderAttacked() && chessComponent.getChessColor() == ChessColor.WHITE) {
                int KingIsDead = 0;//这个时候白王被将军

                ((KingChessComponent) chessComponent).whatChessCanMoveTo(this.chessComponents);//然后把王这个棋子的可以移动的位置弄出来
                chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()].whatChessCanMoveTo(this.chessComponents);//同理，王这个棋子所在的棋盘的对应位置棋子也要弄出来
                List<ChessComponent> tempKW = chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()].getCanMoveToChess();


                boolean[] KingIsDeadWBoolean = new boolean[chessComponent.getCanMoveToChess().size()];
                for (int i = 0; i < chessComponent.getCanMoveToChess().size(); i++){
                    KingIsDeadWBoolean[i] = false;
                }//现在已经把Boolean数组设置好了

                for (int i = 0; i < chessComponent.getCanMoveToChess().size(); i++) {//开始遍历王能够移动的位置，当且仅当王所有可能移动的位置都

                    for (int j = 0; j < chessComponents.length; j++) {//然后遍历棋盘，把所有的棋子都弄出来
                        for (int k = 0; k < chessComponents[j].length; k++) {
                            if (chessComponents[j][k].getChessColor() == ChessColor.BLACK && !(chessComponents[j][k] instanceof KingChessComponent)) {

                                chessComponents[j][k].whatChessCanMoveTo(this.chessComponents);//在获得一个棋子后，设置一下它的可移动位置
                                List<ChessComponent> temp = chessComponents[j][k].getCanMoveToChess();
                                for (int a = 0; a < temp.size(); a++) {//遍历所获得的棋子的可移动位置，判断里面有没有包括王的可移动位置
                                    if (temp.get(a).getChessboardPoint().getX() == tempKW.get(i).getChessboardPoint().getX() && temp.get(a).getChessboardPoint().getY() == tempKW.get(i).getChessboardPoint().getY()) {
                                        KingIsDeadWBoolean[i] = true;
                                    }
                                }
                            }
                        }
                    }
                }


                boolean whetherAllBooleanInKingIsDeadWBooleanIsTrue = false;//还要设置一个判断Boolean
                int test = 0;
                for(int i = 0; i < chessComponent.getCanMoveToChess().size(); i++){
                    if(KingIsDeadWBoolean[i]){test = test + 1;}
                }
                if(test == chessComponent.getCanMoveToChess().size()){whetherAllBooleanInKingIsDeadWBooleanIsTrue = true;}


                if(whetherAllBooleanInKingIsDeadWBooleanIsTrue || getCurrentColor() == ChessColor.BLACK){
                    if ((((KingChessComponent) chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()]).isLose())) {
                        JFrame frame = new JFrame();
                        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        ImageIcon iconW = ChessGraphData.WhiteKing;
                        String[] options = {"Restart a new game", "Back to the Home"};
                        int x = 0;
                        x = JOptionPane.showOptionDialog(frame, "The White Lose",
                                "Loser", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconW, options, options[0]);

                        if (x == 0) {
                            //问题来了，这里要怎么实现重启
                            RestartTheGame();
                        }
                        if (x == -1 || x == 1) {
                            new GameMainFrame(875, 789);


                            ((KingChessComponent) chessComponents[chessComponent.getChessboardPoint().getX()][chessComponent.getChessboardPoint().getY()]).setLose(true);
                            ((KingChessComponent) chessComponent).setLose(true);


                            this.frame.dispose();
                        }
                    }
                }else{

                    //
                    PawnHasAlreadyGivenAnAlarm = PawnHasAlreadyGivenAnAlarm + 1;
                    //

                    JOptionPane.showMessageDialog(new JFrame(), "白方被将军", "警报", JOptionPane.WARNING_MESSAGE);

                }


            }
        }
    }



    private void RestartTheGame() {
        if(this.frame instanceof ChessGameFrame){
            ((ChessGameFrame) frame).RestartGame();
        }
        if(this.frame instanceof ChessGameFrame2){
            ((ChessGameFrame2) frame).RestartGame();
        }
        /**
        remove(this);
        repaint();
        initiateEmptyChessboard();

        // FIXME: Initialize chessboard for testing only.
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);


        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(1, i, ChessColor.BLACK);
        }
        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(CHESSBOARD_SIZE - 2, i, ChessColor.WHITE);
        }


        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);


        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);


        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);


        initKingOnBoard(0, 4, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);

        setCurrentColor(ChessColor.WHITE);

        this.repaint();*/
    }
}
