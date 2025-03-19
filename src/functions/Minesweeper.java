package functions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Random;

public class Minesweeper extends JFrame {
    private final int marginLeft = 12;
    private final int marginTop = 12;
    private final int marginRight = 8;
    private final int marginBottom = 10;
    private final int FIX_BOUND_X = Math.max(12, (int) (-15 * Constant.scale + 45));
    private final int FIX_BOUND_Y = Math.max(25, (int) (-30 * Constant.scale + 100));
    private final JFrame frame = new JFrame("functions.Minesweeper");
    private final JPanel topPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final int mineFieldWidth = Constant.x;
    private final int mineFieldHeight = Constant.y;
    // default
    private boolean started = false;
    private final int numberOfMines = Constant.mines;
    private final JButton smileButton = new JButton();
    private final HashMap<Pairs, Boolean> mines = new HashMap<>(); // 储存雷图
    private String lastSmileButtonPath = Constant.UNDER_PATH + "smile_1.png";
    private boolean continueCounting = true; // 这个是用来控制计时器断点的，当算法卡住，就调用这个
    private boolean startCounting = false; // 这个是用来控制计时器总开关的
    private int leftCounterNumber = numberOfMines;
    private int frameWidth = 322;
    private int frameHeight = 408;
    private int time = 0;
    private ButtonGenerator buttonGenerator;
    private long startTime;
    private long programmeRunConsumeTime = 0;
    private long endTime;
    private boolean failed = false;
    private boolean successed = false;
    public Minesweeper() {

    }
    public double getFinalUsageTime() {
        return Math.max(0.0,((double)(endTime - startTime - programmeRunConsumeTime))/1000.00);
    }
    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
        if(failed) {
            startCounting = false;
        }
    }

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
        if(successed) {
            startCounting = false;
        }
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isContinueCounting() {
        return continueCounting;
    }

    public void setContinueCounting(boolean continueCounting) {
        this.continueCounting = continueCounting;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public int getNumberOfBlock() {
        return mineFieldWidth * mineFieldHeight;
    }

    public HashMap<Pairs, Boolean> getMines() {
        return mines;
    }

    // 开始运行
    public void run() {
        linkSource();
        showInterface();
    }

    private void linkSource() {
        buttonGenerator = new ButtonGenerator();
    }

    // 程序执行从这里开始：
    private void showInterface() {
        preGenerateMine(); // 预生成雷区
        initialInterface(); // 初始化界面
        initializeStructure(); // 加载界面结构

        frame.repaint();
        frame.setVisible(true);
    }

    // 初始化界面
    private void initialInterface() {

        // 定义frame的宽高
        frameWidth = 12 + 6 + 32 * mineFieldWidth + 6 + marginBottom;
        frameHeight = 104 + 16 + 32 * mineFieldHeight;

        // 设置frame的基本属性
        frame.setSize(frameWidth + FIX_BOUND_X, frameHeight + FIX_BOUND_Y);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(192, 192, 192));
        frame.setResizable(false);
        frame.setTitle("Minesweeper");
        Image icon = Toolkit.getDefaultToolkit().getImage(Constant.UNDER_PATH+"mine_icon.png");
        frame.setIconImage(icon);
    }

    private void initializeStructure() {
        // 现在制作计分板（上部分）
        {
            topPanel.setOpaque(false);
            topPanel.setLayout(null);
            topPanel.setVisible(true);
            topPanel.setBounds(marginLeft, marginTop, frameWidth - marginLeft - marginRight, 74);
            // topPanel.setBackground(new Color(255, 0, 0));
            topPanel.add(createImageLabel("src/resources/structure_TL.png", 6, 74, 0, 0));
            topPanel.add(createImageLabel("src/resources/structure_TR.png", 6, 74, 6 + 32 * mineFieldWidth, 0));
            topPanel.add(createImageLabel("src/resources/structure_TT.png", 32 * mineFieldWidth, 4, 6, 0));
            topPanel.add(createImageLabel("src/resources/structure_TB.png", 32 * mineFieldWidth, 4, 6, 70));

            topPanel.add(Counter.getCounter(numberOfMines, 14, 12));
            topPanel.add(Counter.getCounter(time, 32 * mineFieldWidth - 88, 12));

            smileButton();// 添加微笑按钮


            frame.getContentPane().add(topPanel);
        }
        // 制作雷版（下部分）
        {
            /*bottomPanel.setOpaque(false);*/
            bottomPanel.setLayout(null);
            bottomPanel.setVisible(true);
            // bottomPanel.setBackground(Color.red);
            bottomPanel.setBounds(marginLeft, 98, 12 + 32 * mineFieldWidth, 12 + 32 * mineFieldHeight);


            bottomPanel.add(createImageLabel(Constant.UNDER_PATH + "structure_BL_Horizontal.png", 6, 6 + 32 * mineFieldHeight, 0, 0)); // 绘制WestLine
            bottomPanel.add(createImageLabel(Constant.UNDER_PATH + "structure_BL_Corner.png", 6, 6, 0, 6 + 32 * mineFieldHeight)); // 绘制 L-Corner
            bottomPanel.add(createImageLabel(Constant.UNDER_PATH + "structure_BL_Vertical.png", 6 + 32 * mineFieldWidth, 6, 0, 0)); // 绘制 N-Line
            bottomPanel.add(createImageLabel(Constant.UNDER_PATH + "structure_BR_Corner.png", 6, 6, 6 + 32 * mineFieldWidth, 0));//绘制 R-Corner
            bottomPanel.add(createImageLabel(Constant.UNDER_PATH + "structure_BR_Vertical.png", 32 * mineFieldWidth, 6, 6, 6 + 32 * mineFieldHeight)); // 绘制 S-Line
            bottomPanel.add(createImageLabel(Constant.UNDER_PATH + "structure_BR_Horizontal.png", 6, 6 + 32 * mineFieldHeight, 6 + 32 * mineFieldWidth, 6)); // 绘制 E-Line


            // 添加按钮
            for (int height = 0; height < mineFieldHeight; height++) {
                for (int width = 0; width < mineFieldWidth; width++) {
                    // 添加按钮 最上方
                    bottomPanel.add(buttonGenerator.newButton(6 + 32 * width, 6 + 32 * height));

                }
            }


            frame.getContentPane().add(bottomPanel);
        }
    }

    private void generateLabelHere(int x, int y) {
        if (mines.containsKey(new Pairs(x, y))) {
            ImageIcon backgroundIcon = new ImageIcon(Constant.UNDER_PATH + "mine_0.png");
            JLabel label = new JLabel(backgroundIcon);
            label.setBounds(6 + 32 * x, 6 + 32 * y, 32, 32);
            bottomPanel.add(label);
            return;
        }
        int numberOfMinesAround = getMineNumberAround(x, y);
        if (numberOfMinesAround == 0) {
            ImageIcon backgroundIcon = new ImageIcon(Constant.UNDER_PATH + "block_empty.png");
            JLabel label = new JLabel(backgroundIcon);
            label.setBounds(6 + 32 * x, 6 + 32 * y, 32, 32);
            bottomPanel.add(label);
            return;
        }
        ImageIcon backgroundIcon = new ImageIcon(Constant.UNDER_PATH + "block_" + numberOfMinesAround + ".png");
        JLabel label = new JLabel(backgroundIcon);
        label.setBounds(6 + 32 * x, 6 + 32 * y, 32, 32);
        bottomPanel.add(label);
    }

    public int getMineNumberAround(int x, int y) {
        int count = 0;
        for (int xx = x - 1; xx <= x + 1; xx++) {
            for (int yy = y - 1; yy <= y + 1; yy++) {
                if (xx == x && yy == y) continue;
                if (mines.containsKey(new Pairs(xx, yy))) count++;
            }
        }
        return count;
    }

    // 创建一个图像放入JLabel，这个图像可以拉伸...
    private JLabel createImageLabel(String path, int width, int height, int x, int y) {
        JLabel label = new JLabel();
        label.setOpaque(false);
        label.setLayout(null);
        label.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_REPLICATE)));
        label.setBounds(x, y, width, height);
        return label;
    }

    public void smileButton() {

        ImageIcon defaultIcon = new ImageIcon(Constant.UNDER_PATH + "smile_0.png");
        smileButton.setIcon(defaultIcon);
        smileButton.setBounds(32 * mineFieldWidth / 2 - 20 + marginLeft, 12 + marginTop, 52, 52);
        smileButton.setBorderPainted(Constant.showEdge);
        smileButton.addMouseListener(new MouseListener() {
            private boolean exited = false;

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ImageIcon defaultIcon = new ImageIcon(lastSmileButtonPath);
                lastSmileButtonPath = smileButton.getIcon().toString();
                smileButton.setIcon(defaultIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ImageIcon defaultIcon = new ImageIcon(lastSmileButtonPath);
                lastSmileButtonPath = smileButton.getIcon().toString();
                smileButton.setIcon(defaultIcon);
                if (!exited) restart();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exited = false;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exited = true;
            }
        });
        frame.add(smileButton);
        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {

                ((JButton) ((JFrame) e.getComponent()).getContentPane().getComponentAt(32 * mineFieldWidth / 2 - 20 + marginLeft, 12 + marginTop)).setIcon(new ImageIcon(Constant.UNDER_PATH + "smile_2.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ((JButton) ((JFrame) e.getComponent()).getContentPane().getComponentAt(32 * mineFieldWidth / 2 - 20 + marginLeft, 12 + marginTop)).setIcon(new ImageIcon(Constant.UNDER_PATH + "smile_0.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    private void restart() {
        frame.dispose();
        Game.newGame();
    }

    // 开启计时器
    public void startTimeCounter() {
        Thread timeThread = new Thread(this::task);
        timeThread.start();
    }

    private void task() {
        startCounting = true;
        while (startCounting) {
            try {
                Thread.sleep(50);
                if (time == 999) {
                    forceTerminate();
                    break;
                }
                if (((System.currentTimeMillis() - (startTime + programmeRunConsumeTime)) / 1000 - time < 1) || !continueCounting)
                    continue;
                topPanel.remove(topPanel.getComponentAt(32 * mineFieldWidth - 88, 12));
                topPanel.add(Counter.getCounter(++time, 32 * mineFieldWidth - 88, 12));
                topPanel.repaint();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    // to be done
    private void forceTerminate() {

    }

    private void preGenerateMine() {
        int leftMine = numberOfMines;
        Random r = new Random(System.currentTimeMillis());
        if (leftMine > mineFieldWidth * mineFieldHeight) return;
        while (leftMine != 0) {
            Pairs newPair = new Pairs(r.nextInt(mineFieldWidth), r.nextInt(mineFieldHeight));
            if (!mines.containsKey(newPair)) {
                mines.put(newPair, true); // true 代表这是个雷
                leftMine--;
            }
        }
    }

    // 不生成的雷区
    private void preGenerateMine(int x, int y) {
        // 清空原先的雷区表
        mines.clear();
        int leftMine = numberOfMines;
        Random r = new Random(System.currentTimeMillis());
        if (leftMine - 1 > mineFieldWidth * mineFieldHeight) return;
        while (leftMine != 0) {
            Pairs newPair = new Pairs(r.nextInt(mineFieldWidth), r.nextInt(mineFieldHeight));
            if (!mines.containsKey(newPair) && !(newPair.getX() == x && newPair.getY() == y)) {
                mines.put(newPair, true); // true 代表这是个雷
                leftMine--;
            }
        }
    }

    public JButton getSmileButton() {
        return smileButton;
    }

    public boolean isStart() {
        return started;
    }

    public void setStart(boolean start) {
        this.started = start;
        if(started)
        {
            startCounting = true;
            startTime = System.currentTimeMillis();
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getBottomPanel() {
        return bottomPanel;
    }

    public void leftCounterIncrease() {
        topPanel.remove(topPanel.getComponentAt(14, 12));
        topPanel.add(Counter.getCounter(++leftCounterNumber, 14, 12));
        topPanel.repaint();
    }

    public void leftCounterDecrease() {
        topPanel.remove(topPanel.getComponentAt(14, 12));
        topPanel.add(Counter.getCounter(--leftCounterNumber, 14, 12));
        topPanel.repaint();
    }


    public void regenerate(int x, int y) {
        continueCounting = false;
        long startTime = System.currentTimeMillis();
        preGenerateMine(x, y);
        // 添加按钮
        for (int height = 0; height < mineFieldHeight; height++) {
            for (int width = 0; width < mineFieldWidth; width++) {
                // 添加按钮 最上方
                //bottomPanel.add(buttonGenerator.newButton(6 + 32 * width, 6 + 32 * height));


                generateLabelHere(width, height);
                // 添加空格子 最下方
                ImageIcon backgroundIcon = new ImageIcon(Constant.UNDER_PATH + "block_empty.png");
                JLabel label = new JLabel(backgroundIcon);
                label.setBounds(6 + 32 * width, 6 + 32 * height, 32, 32);
                bottomPanel.add(label);
            }
        }
        long endTime = System.currentTimeMillis();
        programmeRunConsumeTime += endTime - startTime;
        continueCounting = true;
    }

    public void normalGenerate() {
        continueCounting = false;
        long startTime = System.currentTimeMillis();
        // 添加按钮
        for (int height = 0; height < mineFieldHeight; height++) {
            for (int width = 0; width < mineFieldWidth; width++) {
                // 添加按钮 最上方
                //bottomPanel.add(buttonGenerator.newButton(6 + 32 * width, 6 + 32 * height));
                generateLabelHere(width, height);
                // 添加空格子 最下方
                ImageIcon backgroundIcon = new ImageIcon(Constant.UNDER_PATH + "block_empty.png");
                JLabel label = new JLabel(backgroundIcon);
                label.setBounds(6 + 32 * width, 6 + 32 * height, 32, 32);
                bottomPanel.add(label);
            }
        }
        long endTime = System.currentTimeMillis();
        programmeRunConsumeTime += endTime - startTime;
        continueCounting = true;
    }

    private int get3BV() {
        int countOf3BV = 0;
        if (numberOfMines == mineFieldWidth * mineFieldHeight) return 0;
        return 1;
    }

    // 失败了
    public void failed(JLabel button) {
        setContinueCounting(false);
        setFailed(true);
        button.setIcon(new ImageIcon(Constant.UNDER_PATH + "mine_1.png"));
        smileButton.setIcon(new ImageIcon(Constant.UNDER_PATH + "smile_3.png"));
        //System.out.println("Failed");
    }
    public void success() {
        setEndTime(System.currentTimeMillis());
        setSuccessed(true);
        setContinueCounting(false);
        smileButton.setIcon(new ImageIcon(Constant.UNDER_PATH + "smile_5.png"));
        System.out.println("胜利!");
        System.out.println("Time = " + getFinalUsageTime() + "s");
    }

    public long getProgrammeRunConsumeTime() {
        return programmeRunConsumeTime;
    }

    public void setProgrammeRunConsumeTime(long programmeRunConsumeTime) {
        this.programmeRunConsumeTime = programmeRunConsumeTime;
    }
}
