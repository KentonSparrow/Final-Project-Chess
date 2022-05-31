package view;

import controller.GameController;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

import static model.ChessComponent.counter;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    public static GameController gameController;
    private boolean flag = true;

    //用于reset的成员变量
    private Chessboard chessboard;

    //用于展示玩家当前状态的成员变量
    private ChessFrameGraphForPlayerFaceOfFate chessFrameGraphW, chessFrameGraphB;

    //惊喜地发现UpGraph也得new
    public ChessFrameGraphForUpOfFate chessFrameGraphForUpOfFate;

    //控制计时器
    private boolean whetherTiming = false;

    //private ChessFrameGraph chessFrameGraphW1 = new ChessFrameGraph(Color.WHITE, ChessColor.WHITE);
    //private ChessFrameGraph chessFrameGraphW2 = new ChessFrameGraph(Color.WHITE, ChessColor.BLACK);
    //private ChessFrameGraph chessFrameGraphB1 = new ChessFrameGraph(Color.BLACK, ChessColor.WHITE);
    //private ChessFrameGraph chessFrameGraphB2 = new ChessFrameGraph(Color.BLACK, ChessColor.BLACK);
    //private ChessColor currentPlayerColor;


    public ChessGameFrame(int width, int height){
        Container container = this.getContentPane();
        setTitle("Game"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;


        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        //实现基本的游戏和用户状态
        addChessboard();
        addPlayerGraph();
        addUpGraph();

        //兵的升变？弹窗设置？放在这个里面能行吗？有点搞不懂，先试一试？——————这么写的话，只会跑一次，还是要写在那个ClickController里面
        //PawnChessChange();
        //如下为暂时废弃的代码
        /**addLabel();
        //addResetButton();
        //addLoadButton();

        //add(this.chessboard.getChessFrameGraphB());
        //add(this.chessboard.getChessFrameGraphW());

        if(currentPlayerColor == ChessColor.WHITE){
            add(this.chessFrameGraphB1);
            add(this.chessFrameGraphW1);
            repaint();
            revalidate();
        }
        if(currentPlayerColor == ChessColor.BLACK){
            add(this.chessFrameGraphB2);
            add(this.chessFrameGraphW2);
            repaint();
            revalidate();
        }*/


        //设置重置键；拟设置主菜单键，悔棋键，音效键
        addResetButton();
        addHomeButton();
        addUndoButton();
        addVoiceButton();
        addLoadButton();
        addSaveButton();
        addStopTime();
        /**repaint();
        container.setVisible(true);*/


        repaint();
    }


    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this);//600//, chessFrameGraphW, chessFrameGraphB
        gameController = new GameController(chessboard);
        chessboard.setLocation((WIDTH - CHESSBOARD_SIZE)/2+1, (HEIGTH - CHESSBOARD_SIZE)/2+23);
        add(chessboard);
        //this.currentPlayerColor = chessboard.getCurrentColor();
    }


    /**
     * 在游戏面板中添加标签

    private void addLabel() {
        JLabel statusLabel = new JLabel("Sample label");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }*/



    /**
     * 第一个主题：fate——————第一步，画贞德
     * */
    private void addPlayerGraph(){
        chessFrameGraphW = new ChessFrameGraphForPlayerFaceOfFate(Color.WHITE, this.chessboard);
        chessFrameGraphB = new ChessFrameGraphForPlayerFaceOfFate(Color.BLACK, this.chessboard);
        add(chessFrameGraphW);
        add(chessFrameGraphB);
    }

    /**
     * fate——————第二步，画顶部
     * */
    private void addUpGraph(){
        chessFrameGraphForUpOfFate = new ChessFrameGraphForUpOfFate(CHESSBOARD_SIZE, this.chessboard);
        add(chessFrameGraphForUpOfFate);
    }

    /**
     * * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!


    private void addHelloButton() {
        JButton button = new JButton("Show Hello Here");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }



    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this,"Input Path here");
            gameController.loadGameFromFile(path);
        });
    }
     private class  JPanelBelow extends JPanel{
     public JPanelBelow() {
     JPanel panel = new JPanel(new GridLayout(1, 4));

     JButton button1 = new JButton("Reset");
     button1.addActionListener((e) -> {
     remove(chessboard);
     repaint();
     addChessboard();
     });
     button1.setFont(new Font("Maragsa", Font.PLAIN, 20));

     JButton button2 = new JButton("Home");
     button2.setFont(new Font("Maragsa", Font.PLAIN, 20));

     JButton button3 = new JButton("Undo");
     button3.setFont(new Font("Maragsa", Font.PLAIN, 20));

     JButton button4 = new JButton("Voice");
     button4.setFont(new Font("Maragsa", Font.PLAIN, 20));

     panel.add(button1);
     panel.add(button2);
     panel.add(button3);
     panel.add(button4);

     panel.setVisible(true);
     }
     }*/

    /**
     * 在游戏的底部设置四个功能按钮
     * */

    private void addResetButton(){
        JButton button = new JButton("Reset");
        button.addActionListener((e) -> {
            RestartGame();
            //this.chessFrameGraphForUpOfFate.visible = true;
            //this.chessFrameGraphForUpOfFate.timer.start();
        });
        button.setLocation((WIDTH - CHESSBOARD_SIZE)/2+1, (HEIGTH + CHESSBOARD_SIZE)/2 + 20);
        button.setSize(CHESSBOARD_SIZE/7+1, (HEIGTH - CHESSBOARD_SIZE)/2 - 53);
        button.setFont(new Font("Parisienne", Font.BOLD, 28));
        add(button);
    }

    private void addHomeButton(){//之后再加入load和save，home只起到回到主界面作用
        JButton button = new JButton("Home");
        button.addActionListener((e) -> {
            this.chessFrameGraphForUpOfFate.timer.stop();
            int x;
            x = JOptionPane.showConfirmDialog(null, "If you return to the HOME, you'll lose your current progress of this game", "HOME", JOptionPane.YES_NO_OPTION);
            switch (x){
                case 0:
                    new GameMainFrame(875, 789);
                    this.dispose();
                case -1, 1:
                    this.chessFrameGraphForUpOfFate.timer.start();
            }

        });
        button.setLocation((WIDTH/2 - 5*CHESSBOARD_SIZE/14)-1, (HEIGTH + CHESSBOARD_SIZE)/2 + 20);
        button.setSize(CHESSBOARD_SIZE/7+1, (HEIGTH - CHESSBOARD_SIZE)/2 - 53);
        button.setFont(new Font("Parisienne", Font.BOLD, 28));
        add(button);
    }
    private void addUndoButton(){
        JButton button = new JButton("Undo");
        button.addActionListener((e) -> {
            System.out.println("clicked Undo ");
            gameController.undo(counter);//这里改了
        });
        button.setLocation(WIDTH/2 - 3*CHESSBOARD_SIZE/14 - 2, (HEIGTH + CHESSBOARD_SIZE)/2 + 20);
        button.setSize(CHESSBOARD_SIZE/7, (HEIGTH - CHESSBOARD_SIZE)/2 - 53);
        button.setFont(new Font("Parisienne", Font.BOLD, 28));
        add(button);
    }
    private void addVoiceButton(){
        JButton button = new JButton("Voice");
        flag = false;
        button.addActionListener((e) -> {
            new Thread(() -> {
                flag = !flag;
                while (flag) {
                    playMusic();
                }//这就是说按了按键会发生什么事！
            }).start();
        });
        button.setLocation(WIDTH / 2 - CHESSBOARD_SIZE/14 - 3,(HEIGTH + CHESSBOARD_SIZE) / 2 + 20);
        button.setSize(CHESSBOARD_SIZE/7, (HEIGTH - CHESSBOARD_SIZE) / 2 - 53);
        button.setFont(new Font("Parisienne", Font.BOLD, 28));
        add(button);
    }

    private void playMusic() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("C:\\Users\\Kenton Sparrow\\Desktop\\project music\\背景音乐.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {// 判断 播放/暂停 状态
                if(flag) {
                    nByte = ais.read(buffer, 0, SIZE);
                    sdl.write(buffer, 0, nByte);
                }else {
                    nByte = ais.read(buffer, 0, 0);
                }
            }
            sdl.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addSaveButton() {//记得改大小和位置
        JButton button = new JButton("Save");
        button.addActionListener((e) ->{} );
        button.setLocation((WIDTH / 2 - 4 + CHESSBOARD_SIZE/14), (HEIGTH + CHESSBOARD_SIZE) / 2 + 20);//放在了那个holy war的右边
        button.setSize(CHESSBOARD_SIZE/7 + 2, (HEIGTH - CHESSBOARD_SIZE) / 2 - 53);
        button.setFont(new Font("Parisienne", Font.BOLD, 28));
        add(button);

        button.addActionListener(e -> {
            this.chessFrameGraphForUpOfFate.timer.stop();
            System.out.println("clicked Save ");
            String filePath = JOptionPane.showInputDialog(this, "请输入存档名");
            gameController.saveDataToFile(filePath);
            //this.chessFrameGraphForUpOfFate.timer.start();
        });//设置保存按钮
    }


    private void addLoadButton() {
            JButton button = new JButton("Load");
            button.addActionListener((e) ->{} );
            button.setLocation((WIDTH / 2 - 4 + 3*CHESSBOARD_SIZE/14), (HEIGTH + CHESSBOARD_SIZE) / 2 + 20);//放在了那个holywar的左边
            button.setSize(CHESSBOARD_SIZE/7 + 3, (HEIGTH - CHESSBOARD_SIZE) / 2 - 53);
            button.setFont(new Font("Parisienne", Font.BOLD, 28));
            add(button);

            button.addActionListener(e -> {
                this.chessFrameGraphForUpOfFate.timer.stop();
                    System.out.println("clicked Load ");
                    //第一步
                    JFileChooser jfchooser = new JFileChooser();
//第二步
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("json", "txt");
                    jfchooser.setFileFilter(filter);
//第三步
                    int option = jfchooser.showOpenDialog(null);
                    if (option == JFileChooser.APPROVE_OPTION) {       //说明选定了一个文件
                        String address = jfchooser.getSelectedFile().getPath();//把文件地址取出给address变量
                        gameController.readFileData(address);//设置读档按钮（读档有bug，可以擅自修改存档，但是不能修改最右边的，不然存档会自动缩进，使得读档时格式错误！）
                    }
                    //this.chessFrameGraphForUpOfFate.timer.start();
            });

    }




    public void addStopTime(){
        JButton button = new JButton("Time");
        button.addActionListener((e) -> {
            this.whetherTiming = !this.whetherTiming;
            if(this.whetherTiming){
                this.chessFrameGraphForUpOfFate.timer.start();
            }else{
                this.chessFrameGraphForUpOfFate.timer.stop();
            }
        });
        button.setLocation((WIDTH / 2 - 4 + 5*CHESSBOARD_SIZE/14), (HEIGTH + CHESSBOARD_SIZE)/2 + 20);
        button.setSize(CHESSBOARD_SIZE/7+2, (HEIGTH - CHESSBOARD_SIZE)/2 - 53);
        button.setFont(new Font("Parisienne", Font.BOLD, 28));
        add(button);
    }




    /**
     * 尝试，设置一个void方法，然后，。。。先给个
     *
    public void PawnChessChange(){
        if(this.chessboard.getBoundB()){
            this.setEnabled(false);
            this.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
            //创建新的窗口
            JFrameForPawnChessToChangeB frameForPawnChessToChangeB = new JFrameForPawnChessToChangeB(this);
            frameForPawnChessToChangeB.setVisible(true);
            this.chessboard.setBoundB(false);
        }
        if(this.chessboard.getBoundW()){
            this.setEnabled(false);
            this.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
            //创建新的窗口
            JFrameForPawnChessToChangeW frameForPawnChessToChangeW = new JFrameForPawnChessToChangeW(this, this.chessboard);
            frameForPawnChessToChangeW.setVisible(true);
            this.chessboard.setBoundW(false);
        }
    }*/



    /**为了在游戏里成功地reset*/
    public void RestartGame(){
        remove(chessboard);
        repaint();
        addChessboard();
        remove(chessFrameGraphW);repaint();
        remove(chessFrameGraphB);repaint();
        addPlayerGraph();
        remove(chessFrameGraphForUpOfFate);repaint();
        addUpGraph();
    }


}
