package view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


public class GameMainFrame extends JFrame {
    private int WIDTH;
    private int HEIGHT;


    public GameMainFrame(int width, int height){
        this.WIDTH = width;
        this.HEIGHT = height;

        setTitle("Main Frame");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addBackGraph();
    }

    /***/private void addBackGraph() {
        BackGraphPanel graphPanel = new BackGraphPanel(this);
        add(graphPanel);
        repaint();
    }



    public static void main(String[] args) throws IOException {
        new GameMainFrame(875, 789);
        //frame.setTitle("Main Frame");
        //frame.setSize(875, 789);
        //frame.setLocationRelativeTo(null); // Center the window.
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        //frame.setLayout(null);
        //frame.setVisible(true);


        /**看来要自适应的关键在于在main方法中加入JPanel，才能自适应*/
        //BufferedImage image = ImageIO.read(new File("./images/GameMainFrameGraph2.png"));
        //BackGraphPanel backGraphPanel = new BackGraphPanel(frame, image);

        //frame.add(backGraphPanel);
        //BackgroundImagePanel backgroundImagePanel = new BackgroundImagePanel(image);
        //backgroundImagePanel.setOpaque(true);
        //add(backgroundImagePanel);
        //frame.repaint();
    }
}

class BackGraphPanel extends JPanel{
    private static final URL url = GameMainFrame.class.getResource("GameMainFrameGraph2.png");
    //BufferedImage image;


    private static final ImageIcon icon = new ImageIcon(url);
    private GameMainFrame gameMainFrame;


    public BackGraphPanel(GameMainFrame gameMainFrame){
        this.gameMainFrame = gameMainFrame;
        //this.image = image;
        repaint();

        this.setBounds(0, 0, 875, 789);
        this.setLayout(null);
        this.setVisible(true);
        repaint();

        addStartNewGame();
        addRank();
        addLoadButton();
        repaint();
    }

    private void addStartNewGame(){
        JButton button = new JButton("New  Game");
        button.addActionListener((e -> {
            StartGame();
        }));
        button.setBounds(200,120,475,103);
        button.setFont(new Font("Parisienne", Font.BOLD, 40));
        button.setForeground(new Color(7, 197, 241));
        button.setContentAreaFilled(false);
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        add(button);
        //repaint();
    }

   private void addLoadButton() {
        JButton button = new JButton("Load  Game");
        button.setBounds(200, 343, 475, 103);
        button.setFont(new Font("Parisienne", Font.BOLD, 40));
        button.setForeground(new Color(236, 8, 218));
        button.setContentAreaFilled(false);
        add(button);
        //repaint();

         /*button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this,"Input Path here");
            gameController.loadGameFromFile(path);
        });*/
    }

    private void addRank(){
        JButton button = new JButton("Rank  List");

        button.setBounds(200,566,475,103);
        button.setFont(new Font("Parisienne", Font.BOLD, 40));
        button.setForeground(new Color(217, 98, 4));
        button.setContentAreaFilled(false);
        add(button);
        //repaint();
    }

    private void StartGame(){
        JFrame frame = new JFrame();

        ImageIcon iconW = ChessGraphData.Choose;
        //frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        String[] options = {"World War I", "Holy Grail War"};

        int x;

        x = JOptionPane.showOptionDialog(frame, "Please choose a theme",
                "The Theme of Game", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconW, options, options[0]);

        switch (x) {
            case 0 -> {
                SwingUtilities.invokeLater(() -> {
                    ChessGameFrame2 mainFrame = new ChessGameFrame2(1540, 964);
                    mainFrame.setVisible(true);
                    this.gameMainFrame.dispose();
                });
            }
            case 1 -> {
                SwingUtilities.invokeLater(() -> {
                    ChessGameFrame mainFrame = new ChessGameFrame(1540, 964);
                    mainFrame.setVisible(true);
                    this.gameMainFrame.dispose();
                });
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackGraphPanel.icon.paintIcon(this, g, 0, 0);
        //Graphics2D g2 = (Graphics2D)g;
        //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                //RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        //int w = getWidth();
        //int h = getHeight();
        //int iw = image.getWidth();
        //int ih = image.getHeight();
        //double xScale = (double)w/iw;
        //double yScale = (double)h/ih;
        //double scale = Math.min(xScale, yScale);    // scale to fit
        //Math.max(xScale, yScale);  // scale to fill
        //int width = (int)(scale*iw);
        //int height = (int)(scale*ih);
        //int x = (w - width)/2;
        //int y = (h - height)/2;
        //g2.drawImage(image, 0, 0, w, h, this);

        g.setColor(new Color(7, 197, 241));
        g.drawRect(200,120,475,103);

        g.setColor(new Color(217, 98, 4));
        g.drawRect(200,566,475,103);

        g.setColor(new Color(236, 8, 218));
        g.drawRect(200, 343, 475, 103);
        //g.setFont(new Font("Parisienne", Font.BOLD, 40));
        //g.drawString("New Game", 437, 150);
        repaint();
    }
}


/**class BackgroundImagePanel extends JPanel {
    protected Image image;
    public BackgroundImagePanel() {
        this(null);
    }

    public BackgroundImagePanel(Image background) {
        this.image = background;
        this.setOpaque(false);
    }

    public BackgroundImagePanel(Image background, LayoutManager layout) {
        super(layout);
        this.image = background;
        this.setOpaque(false);
    }

    public void setBackgroundImage(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}*/
