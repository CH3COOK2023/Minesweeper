import javax.swing.*;

public class Counter extends JFrame {
    // 计时器

    private static final int FRAME_WIDTH = 82;
    private static final int FRAME_HEIGHT = 50;


    public static JPanel getCounter(int time, int x, int y) {
        JPanel panel = getCounter(time);
        panel.setLocation(x, y);
        return panel;
    }

    private static JPanel getCounter(int time) {
        JPanel panel = new JPanel();
        panel.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        panel.setOpaque(false); // 透明底 = false ; 非透明底 = true
        panel.setLayout(null);
        panel.add(labelConstructor(getResourceName(10), FRAME_WIDTH, FRAME_HEIGHT, 0, 0));
        int NUMBER_WIDTH = 26;
        int NUMBER_HEIGHT = 46;
        panel.add(labelConstructor(getResourceName(time / 100 % 10), NUMBER_WIDTH, NUMBER_HEIGHT, 2, 2));
        panel.add(labelConstructor(getResourceName(time / 10 % 10), NUMBER_WIDTH, NUMBER_HEIGHT, 2 + NUMBER_WIDTH, 2));
        panel.add(labelConstructor(getResourceName(time % 10), NUMBER_WIDTH, NUMBER_HEIGHT, 2 + NUMBER_WIDTH * 2, 2));
        return panel;
    }

    private static String getResourceName(int number) {
        if (number == 10) return "counter_frame.png";
        return "count_" + number + ".png";
    }

    private static JLabel labelConstructor(String resourceName, int width, int height, int x, int y) {
        JLabel label = new JLabel();
        ImageIcon imageIcon = new ImageIcon(Constant.UNDER_PATH + resourceName);
        label.setLayout(null);
        label.setIcon(imageIcon);
        label.setBounds(x, y, width, height);
        return label;
    }
}
