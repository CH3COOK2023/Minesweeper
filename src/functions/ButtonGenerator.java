package functions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

public class ButtonGenerator {

    // 获取到ButtonGenerator对应的minesweeper对象
    private final Minesweeper minesweeper = Game.runtimeInstance.popTail();
    // 临时存储九宫格的icon数据
    private final String[][] tempIcon = new String[3][3];
    private final HashSet<Point> pointSet = new HashSet<>(minesweeper.getNumberOfBlock());
    // 按键控制
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private int combination = -1;
    private int preCombination = -1;
    private int prePreCombination = -1;
    // 记录当前格子是不是完成了
    private int blockShouldOpen = minesweeper.getNumberOfBlock() - minesweeper.getNumberOfMines(); // 需要完成的最大操作数

    public boolean leftPressed(MouseEvent e) {
        return e.getButton() == MouseEvent.BUTTON1;
    }

    public boolean rightPressed(MouseEvent e) {
        return e.getButton() == MouseEvent.BUTTON3;
    }

    public JLabel newButton(int x, int y) {
        ImageIcon icon = new ImageIcon(Constant.UNDER_PATH + "block_unopened.png");
        JLabel button = new JLabel(icon);
        button.setOpaque(false);
        button.addMouseListener(new MouseAdapter() {
            public String lastIconPath = Constant.UNDER_PATH + "block_unopened.png";
            boolean smileFaceStatus = false;

            // 0 -> 没有按下
            // 1 -> 仅仅左键
            // 2 -> 仅仅右键
            // 3 -> 双键
            @Override
            public void mousePressed(MouseEvent e) {
                // 失败了不允许执行
                if (minesweeper.isFailed() || minesweeper.isSuccessed()) return;
                clicked(e);
                smileFaceStatus = smileFace(smileFaceStatus);
                // 仅仅左键，而且格子没开，才更换图标
                if (combination == 1) {
                    // 如果当前格子是空的、或者是flag、或者上一次是左右键  就返回
                    if (button.getIcon().toString().isEmpty() || button.getIcon().toString().equals(Constant.UNDER_PATH + "flag.png") || preCombination == 3)
                        return;
                    // 现在，这个格子被按下，而且是根格子...储存图像，然后换成empty样式
                    lastIconPath = button.getIcon().toString();
                    ImageIcon icon = new ImageIcon(Constant.UNDER_PATH + "block_empty.png");

                    // 如果这个格子是questio就换为questio_pressed
                    if (lastIconPath.equals(Constant.UNDER_PATH + "questio.png")) {
                        icon = new ImageIcon(Constant.UNDER_PATH + "questio_pressed.png");
                    }
                    button.setIcon(icon);
                    return;
                }
                if (combination == 2) // 仅仅右键
                {
                    changeStatus(button);
                    return;
                }
                if (combination == 3) {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            clickedFromRoot(e, x, y);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // 鼠标被松开
                // 失败了不允许执行
                if (minesweeper.isFailed() || minesweeper.isSuccessed()) return;
                released(e);
                smileFaceStatus = smileFace(smileFaceStatus);
                // 双键操作
                if (prePreCombination == 3 && combination == 0) {
                    // 旗子数量和方块数量不同就返回
                    if (getFlagsAround(e) != getMinsAround(e)) {
                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                resumeFromRoot(e, xx, yy);
                            }
                        }
                        //System.out.println("未能打开：雷="+getMinsAround(e)+"旗="+getFlagsAround(e));
                        return;
                    } else {
                        // 这里是需要打开！周围的9个格子
                        Point fromLocation = e.getComponent().getLocation();
                        Point toLocation = e.getPoint();
                        int xBias = fromLocation.x + toLocation.x;
                        int yBias = fromLocation.y + toLocation.y;
                        Point newPoint = new Point(xBias, yBias);
                        Point temp = new Point(newPoint.x, newPoint.y);
                        if (minesweeper.getBottomPanel().contains(temp) && verifyButton(minesweeper.getBottomPanel().getComponentAt(temp)) && !((JLabel) (minesweeper.getBottomPanel().getComponentAt(temp))).getIcon().toString().equals(Constant.UNDER_PATH + "block_unopened.png"))
                            for (int iterX = -1; iterX <= 1; iterX++) {
                                for (int iterY = -1; iterY <= 1; iterY++) {
                                    // 获取newPoint，newPoint是目标点
                                    newPoint = new Point(xBias + 32 * iterX, yBias + 32 * iterY);
                                    // 目标点越界则不执行
                                    if (!minesweeper.getBottomPanel().contains(newPoint)) continue;
                                    // 获取到的不是按钮，不执行
                                    JLabel fromLabel = (JLabel) (minesweeper.getBottomPanel().getComponentAt(newPoint));
                                    if (!verifyButton(fromLabel)) continue;

                                    // 如果是旗子、""、直接continue，其余打开
                                    if (fromLabel.getIcon().toString().equals(Constant.UNDER_PATH + "flag.png") || fromLabel.getIcon().toString().isEmpty())
                                        continue;
                                    else {
                                        pointSet.add(fromLabel.getLocation());
                                        openThisButton();
                                    }
                                }
                            }
                        //else System.out.println(1);
                    }
                }

                // 当前 = 0 ， 上一次左键
                if (preCombination == 1) {
                    // 不执行操作的情况：
                    if (prePreCombination == 3) return;

                    // 对于根格子而言，就需要删除目标格子
                    // 调用这个函数删除目标格子而不是根格子
                    // 如果这个格子是旗子，不删除
                    if (button.getIcon().toString().equals(Constant.UNDER_PATH + "flag.png")) return;

                    // 现在明确要打开这个格子了
                    // 计算格子相对位置
                    Point fromLocation = e.getComponent().getLocation();
                    Point toLocation = e.getPoint();
                    int xBias = fromLocation.x + toLocation.x;
                    int yBias = fromLocation.y + toLocation.y;
                    JLabel deleteThisButton = (JLabel) minesweeper.getBottomPanel().getComponentAt(xBias, yBias);
                    deleteThisButton.setIcon(new ImageIcon(Constant.UNDER_PATH + "block_unopened.png"));
                    // 如果合法，就删除
                    // =====================================

                    // 先检测这是不是个button
                    if (!verifyButton(deleteThisButton)) return;
                    // 首先，特殊判断，如果这个是雷...
                    if (minesweeper.getMines().containsKey(new Pairs((deleteThisButton.getLocation().x - 6) / 32, (deleteThisButton.getLocation().y - 6) / 32))) {
                        // 第一次开游戏，点到雷，就重新生成雷
                        if (!minesweeper.isStart()) {
                            minesweeper.regenerate((deleteThisButton.getLocation().x - 6) / 32, (deleteThisButton.getLocation().y - 6) / 32);
                            minesweeper.setStart(true);
                            minesweeper.startTimeCounter();
                            // 正常打开
                            // 如果碰到了空格子，那么用迭代的方法删除。
                            if (minesweeper.getMineNumberAround((deleteThisButton.getLocation().x - 6) / 32, (deleteThisButton.getLocation().y - 6) / 32) == 0) {
                                pointSet.add(deleteThisButton.getLocation());
                                openThisButton();
                            }
                            // 其余情况是：是数字，仅打开
                            else {
                                deleteThisButton.setIcon(new ImageIcon(""));
                                // 记录操作数
                                if (--blockShouldOpen == 0) minesweeper.success();
                                //System.out.println(blockShouldOpen);
                            }
                            return;
                        }
                        minesweeper.failed(deleteThisButton);
                        return;
                    }
                    // 这个不是雷！
                    // 第一次开游戏
                    if (!minesweeper.isStart()) {
                        minesweeper.normalGenerate();
                        minesweeper.setStart(true);
                        minesweeper.startTimeCounter();
                    }

                    // 如果碰到了空格子，那么用迭代的方法删除。
                    if (minesweeper.getMineNumberAround((deleteThisButton.getLocation().x - 6) / 32, (deleteThisButton.getLocation().y - 6) / 32) == 0) {
                        pointSet.add(deleteThisButton.getLocation());
                        openThisButton();
                    }
                    // 其余情况是：是数字，仅打开
                    else {
                        deleteThisButton.setIcon(new ImageIcon(""));
                        // 记录操作数
                        if (--blockShouldOpen == 0) minesweeper.success();
                        //System.out.println(blockShouldOpen);
                    }

                    return;
                }

                // 当前 = 单键，上一次：双键->恢复
                if (preCombination == 3) {
                    for (int xx = -1; xx <= 1; xx++) {
                        for (int yy = -1; yy <= 1; yy++) {
                            resumeFromRoot(e, xx, yy);
                        }
                    }
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 失败了不允许执行
                if (minesweeper.isFailed() || minesweeper.isSuccessed()) return;
                // 鼠标离开
                // 如果双键离开
                if (combination == 3) {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            resumeFromNonRoot(e, x, y);
                        }
                    }
                    return;
                }
                if (button.getIcon().toString().isEmpty()) return;
                // 鼠标仅仅左键，而且离开，就恢复上一次图片样式
                button.setIcon(new ImageIcon(lastIconPath));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 鼠标进入
                // 失败了不允许执行
                if (minesweeper.isFailed() || minesweeper.isSuccessed()) return;
                // 鼠标仅仅左键，而且进入
                if (combination == 1) {
                    // 如果格子是空的或者是旗子，就返回
                    if (button.getIcon().toString().isEmpty() || button.getIcon().toString().equals(Constant.UNDER_PATH + "flag.png"))
                        return;
                    lastIconPath = button.getIcon().toString();
                    ImageIcon icon = new ImageIcon(Constant.UNDER_PATH + "block_empty.png");
                    // 如果这个格子是questio就换为questio_pressed
                    if (lastIconPath.equals(Constant.UNDER_PATH + "questio.png")) {
                        icon = new ImageIcon(Constant.UNDER_PATH + "questio_pressed.png");
                    }
                    button.setIcon(icon);
                    return;
                }
                // 如果是双键进入
                if (combination == 3) {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            clickedFromNonRoot(e, x, y);
                        }
                    }
                }
            }


            /*从根节点点击*/
            private void clickedFromRoot(MouseEvent e, int iterX, int iterY) {

                // 获取newPoint，newPoint是目标点
                Point fromLocation = e.getComponent().getLocation(); // 相对于ButtomPanel的位置
                Point toLocation = e.getPoint();
                int xBias = fromLocation.x + toLocation.x;
                int yBias = fromLocation.y + toLocation.y;
                Point newPoint = new Point(xBias + 32 * iterX, yBias + 32 * iterY);
                // 目标点越界则不执行
                if (!minesweeper.getBottomPanel().contains(newPoint)) return;
                // 获取到的不是按钮，不执行
                JLabel fromLabel = (JLabel) (minesweeper.getBottomPanel().getComponentAt(newPoint));
                if (!verifyButton(fromLabel)) return;
                // 只有questio和unopened能被替换为empty，其余不变
                String iconPath = fromLabel.getIcon().toString();

                // 如果当前icon是empty，意思是被点击的，直接变为unopened
                if (iconPath.equals(Constant.UNDER_PATH + "block_empty.png")) {
                    iconPath = Constant.UNDER_PATH + "block_unopened.png";
                }

                tempIcon[iterX + 1][iterY + 1] = iconPath;
                if (iconPath.equals(Constant.UNDER_PATH + "questio.png"))
                    fromLabel.setIcon(new ImageIcon(Constant.UNDER_PATH + "questio_pressed.png"));
                else if (iconPath.equals(Constant.UNDER_PATH + "block_unopened.png"))
                    fromLabel.setIcon(new ImageIcon(Constant.UNDER_PATH + "block_empty.png"));
                else fromLabel.setIcon(new ImageIcon(iconPath));

            }

            /*从根节点恢复*/ // 这里是相当于松开操作了，需要打开周围的格子
            private void resumeFromRoot(MouseEvent e, int iterX, int iterY) {
                // 获取目标位置
                Point fromLocation = e.getComponent().getLocation();
                Point toLocation = e.getPoint();
                int xBias = fromLocation.x + toLocation.x;
                int yBias = fromLocation.y + toLocation.y;
                Point newPoint = new Point(xBias + 32 * iterX, yBias + 32 * iterY);
                // 目标点越界则不执行
                if (!minesweeper.getBottomPanel().contains(newPoint)) return;
                // 获取到的不是按钮，不执行
                JLabel fromLabel = (JLabel) (minesweeper.getBottomPanel().getComponentAt(newPoint));
                if (!verifyButton(fromLabel)) return;

                // 替换图标
                fromLabel.setIcon(new ImageIcon(tempIcon[iterX + 1][iterY + 1]));
            }

            /*从NON-根节点点击*/
            private void clickedFromNonRoot(MouseEvent e, int iterX, int iterY) {
                // 获取目标位置
                Point currentPoint = ((JLabel) (e.getSource())).getLocation();
                Point newPoint = new Point(currentPoint.x + 32 * iterX, currentPoint.y + 32 * iterY);
                // 如果越界，就返回
                if (!minesweeper.getBottomPanel().contains(newPoint)) return;
                // 获取到的不是按钮，不执行
                JLabel fromLabel = (JLabel) (minesweeper.getBottomPanel().getComponentAt(newPoint));
                if (!verifyButton(fromLabel)) return;
                // 替换图标
                String iconPath = fromLabel.getIcon().toString();
                tempIcon[iterX + 1][iterY + 1] = iconPath;
                if (iconPath.equals(Constant.UNDER_PATH + "questio.png"))
                    fromLabel.setIcon(new ImageIcon(Constant.UNDER_PATH + "questio_pressed.png"));
                else if (iconPath.equals(Constant.UNDER_PATH + "block_unopened.png"))
                    fromLabel.setIcon(new ImageIcon(Constant.UNDER_PATH + "block_empty.png"));
                else fromLabel.setIcon(new ImageIcon(iconPath));
            }

            /*从NON-根节点恢复*/
            private void resumeFromNonRoot(MouseEvent e, int iterX, int iterY) {
                // 获取相对位置
                Point currentPoint = ((JLabel) (e.getSource())).getLocation();
                Point newPoint = new Point(currentPoint.x + 32 * iterX, currentPoint.y + 32 * iterY);
                // 如果越界，就返回
                if (!minesweeper.getBottomPanel().contains(newPoint)) return;
                // 如果不是button，就返回
                JLabel fromLabel = (JLabel) (minesweeper.getBottomPanel().getComponentAt(newPoint));
                if (!verifyButton(fromLabel)) return;

                // 替换图标
                fromLabel.setIcon(new ImageIcon(tempIcon[iterX + 1][iterY + 1]));
            }


            private void changeStatus(JLabel button) {
                if(Constant.noFlag) return;
                Icon currentIcon = button.getIcon();
                if (currentIcon.toString().isEmpty()) return;
                if (currentIcon.toString().equals(Constant.UNDER_PATH + "block_unopened.png")) {
                    // unopened 到 flag，雷 -1
                    minesweeper.leftCounterDecrease();
                    button.setIcon(new ImageIcon(Constant.UNDER_PATH + "flag.png"));
                    lastIconPath = button.getIcon().toString();
                    return;
                }
                if (!Constant.flagOnly && currentIcon.toString().equals(Constant.UNDER_PATH + "flag.png")) {
                    // flag 到 questio 雷+1
                    minesweeper.leftCounterIncrease();
                    button.setIcon(new ImageIcon(Constant.UNDER_PATH + "questio.png"));
                    lastIconPath = button.getIcon().toString();
                    return;
                }
                if(Constant.flagOnly  && currentIcon.toString().equals(Constant.UNDER_PATH + "flag.png"))
                {
                    // flag 到 questio 雷+1
                    minesweeper.leftCounterIncrease();
                    button.setIcon(new ImageIcon(Constant.UNDER_PATH + "block_unopened.png"));
                    lastIconPath = button.getIcon().toString();
                    return;
                }
                if (currentIcon.toString().equals(Constant.UNDER_PATH + "questio.png")) {
                    button.setIcon(new ImageIcon(Constant.UNDER_PATH + "block_unopened.png"));
                    lastIconPath = button.getIcon().toString();
                }
            }

            private void clicked(MouseEvent e) {
                if (leftPressed(e)) {
                    leftPressed = true;
                }

                if (rightPressed(e)) {
                    rightPressed = true;
                }

                changeCombination();

            }

            private void released(MouseEvent e) {
                if (leftPressed(e)) leftPressed = false;
                if (rightPressed(e)) rightPressed = false;

                changeCombination();
            }

            private void changeCombination() {
                if (!leftPressed && !rightPressed) { // 什么都没按下
                    prePreCombination = preCombination;
                    preCombination = combination;
                    combination = 0;
                    return;
                }
                if (leftPressed && !rightPressed) { // 仅仅左键
                    prePreCombination = preCombination;
                    preCombination = combination;
                    combination = 1;
                    return;
                }
                // 这里右键的情况必然是按下的
                if (!leftPressed) { // 仅仅右键
                    prePreCombination = preCombination;
                    preCombination = combination;
                    combination = 2;
                    return;
                }
                if (leftPressed) { // 双键
                    prePreCombination = preCombination;
                    preCombination = combination;
                    combination = 3;
                }


            }

            private boolean smileFace(boolean smileFaceStatus) {
                // 仅仅左键
                // 双键
                // 这2个Combination都要让smileFace Smile
                if (!smileFaceStatus && !minesweeper.isSuccessed() && !minesweeper.isFailed()) {
                    if (combination != 0 && combination != 2) {
                        ImageIcon defaultIcon = new ImageIcon(Constant.UNDER_PATH + "smile_2.png");
                        minesweeper.getSmileButton().setIcon(defaultIcon);
                        return true;
                    }
                } else {
                    // 全松开才关闭smileface
                    if (combination == 0 || combination == 2) {
                        ImageIcon defaultIcon = new ImageIcon(Constant.UNDER_PATH + "smile_0.png");
                        minesweeper.getSmileButton().setIcon(defaultIcon);
                        return false;
                    }
                }
                return smileFaceStatus;
            }
        });
        button.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
        return button;
    }


    private void openThisButton() {
        minesweeper.setContinueCounting(false);
        long startTime = System.currentTimeMillis();
        while (!pointSet.isEmpty()) {
            HashSet<Point> temp = new HashSet<>(); // 临时的
            for (Point point : pointSet) {
                JLabel currentButton = (JLabel) minesweeper.getBottomPanel().getComponentAt(point);
                // 如果这个按钮是开过的，或者这个不是按钮，或者是旗子，就返回
                if (!verifyButton(currentButton) || currentButton.getIcon().toString().isEmpty() || currentButton.getIcon().toString().equals(Constant.UNDER_PATH + "flag.png"))
                    continue;

                // 如果当前位置是雷
                if (minesweeper.getMines().containsKey(new Pairs((point.x - 6) / 32, (point.y - 6) / 32))) {
                    // 踩到了雷
                    minesweeper.failed(currentButton);
                    continue;
                }
                // 如果是数字，那么打开后就返回
                if (minesweeper.getMineNumberAround((currentButton.getLocation().x - 6) / 32, (currentButton.getLocation().y - 6) / 32) != 0) {
                    currentButton.setIcon(new ImageIcon(""));
                    // 记录操作数
                    if (--blockShouldOpen == 0) minesweeper.success();
                    //System.out.println(blockShouldOpen);
                    continue;
                }
                // 如果这个地方无雷，而且是非数字，证明是空的
                // System.out.println("不包含：[" + (point.x - 6) / 32 + "," + (point.y - 6) / 32 + "]");
                currentButton.setIcon(new ImageIcon(""));
                // 记录操作数
                if (--blockShouldOpen == 0) minesweeper.success();
                //System.out.println(blockShouldOpen);
                // 递归
                if (minesweeper.getBottomPanel().contains(point.x - 32, point.y))
                    temp.add(new Point(point.x - 32, point.y));
                if (minesweeper.getBottomPanel().contains(point.x + 32, point.y))
                    temp.add(new Point(point.x + 32, point.y));
                if (minesweeper.getBottomPanel().contains(point.x, point.y - 32))
                    temp.add(new Point(point.x, point.y - 32));
                if (minesweeper.getBottomPanel().contains(point.x, point.y + 32))
                    temp.add(new Point(point.x, point.y + 32));

                if (minesweeper.getBottomPanel().contains(point.x - 32, point.y - 32))
                    temp.add(new Point(point.x - 32, point.y - 32));
                if (minesweeper.getBottomPanel().contains(point.x - 32, point.y + 32))
                    temp.add(new Point(point.x - 32, point.y + 32));
                if (minesweeper.getBottomPanel().contains(point.x + 32, point.y - 32))
                    temp.add(new Point(point.x + 32, point.y - 32));
                if (minesweeper.getBottomPanel().contains(point.x + 32, point.y + 32))
                    temp.add(new Point(point.x + 32, point.y + 32));
            }
            pointSet.clear();
            pointSet.addAll(temp);
        }
        long endTime = System.currentTimeMillis();
        minesweeper.setProgrammeRunConsumeTime(minesweeper.getProgrammeRunConsumeTime() + endTime - startTime);
        minesweeper.setContinueCounting(true);
    }


    private boolean verifyButton(Object obj) {
        if (!(obj instanceof JLabel)) return false;
        return ((JLabel) obj).getWidth() == 32;

    }

    private int getMinsAround(MouseEvent e) {
        int count = 0;
        // 获取newPoint，newPoint是目标点
        Point fromLocation = e.getComponent().getLocation(); // 相对于ButtomPanel的位置
        Point toLocation = e.getPoint();
        int xBias = fromLocation.x + toLocation.x;
        int yBias = fromLocation.y + toLocation.y;
        for (int iterX = -1; iterX <= 1; iterX++) {
            for (int iterY = -1; iterY <= 1; iterY++) {
                Point newPoint = new Point(xBias + 32 * iterX, yBias + 32 * iterY);
                // 目标点越界则不执行
                if (!minesweeper.getBottomPanel().contains(newPoint)) {
                    //System.err.println("ERR1");
                    continue;
                }
                // 获取到的不是按钮，不执行
                JLabel fromLabel = (JLabel) (minesweeper.getBottomPanel().getComponentAt(newPoint));
                if (!verifyButton(fromLabel)) {
                    //System.err.println("ERR2");
                    continue;
                }
                // 开始检测是否是mine
                if (minesweeper.getMines().containsKey(new Pairs((fromLabel.getLocation().x - 6) / 32, (fromLabel.getLocation().y - 6) / 32)))
                    count++;
            }
        }
        return count;
    }

    private int getFlagsAround(MouseEvent e) {
        int count = 0;
        // 获取newPoint，newPoint是目标点
        Point fromLocation = e.getComponent().getLocation(); // 相对于ButtomPanel的位置
        Point toLocation = e.getPoint();
        int xBias = fromLocation.x + toLocation.x;
        int yBias = fromLocation.y + toLocation.y;
        for (int iterX = -1; iterX <= 1; iterX++) {
            for (int iterY = -1; iterY <= 1; iterY++) {
                Point newPoint = new Point(xBias + 32 * iterX, yBias + 32 * iterY);
                // 目标点越界则不执行
                if (!minesweeper.getBottomPanel().contains(newPoint)) {
                    //System.err.println("ERR3");
                    continue;
                }
                // 获取到的不是按钮，不执行
                JLabel fromLabel = (JLabel) (minesweeper.getBottomPanel().getComponentAt(newPoint));
                if (!verifyButton(fromLabel)) {
                    //System.err.println("ERR4");
                    continue;
                }
                // 开始检测是否是旗子
                if (fromLabel.getIcon().toString().equals(Constant.UNDER_PATH + "flag.png")) count++;
            }
        }
        return count;
    }
}
