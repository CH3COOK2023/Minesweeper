public class Constant {
    // 资源路径
    public static final String PATH = "src/resources";
    public static final String UNDER_PATH = "src/resources/";
    public static boolean showEdge = false;

    // 游戏设置
    public static boolean noFlag = false;
    public static boolean flagOnly = false;
    public static double scale = 3.0; // 默认缩放1.0
    public static int x = 10;
    public static int y = 10;
    public static int mines = (int) (x * y * 0.123 + 1);
    // 最大并发
    public static int maxConcurrent = 4;
}
