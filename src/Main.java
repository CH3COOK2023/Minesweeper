import functions.Constant;
import functions.Game;

public class Main {
    public static void main(String[] args) {
        // 创建一个自定义的
        custom();
        Game.newGame();
    }
    // 自定义
    private static void custom(){
        Constant.x = 20;
        Constant.y = 20;
        Constant.mines = 35;
        Constant.scale = 1;
    }
    // 初级
    private static void beginner(){
        Constant.scale = 0.6; // 默认缩放 [0.6-3] 默认 1
        Constant.x = 9;  // x 大小
        Constant.y = 9;  // y 大小
        Constant.mines = 10; // 雷数量
        Constant.flagOnly = true; // 是否去除右键问号标记
        Constant.noFlag = false; // 是否采用NF
    }
    // 中级
    private static void intermediate(){
        Constant.scale = 0.6; // 默认缩放 [0.6-3] 默认 1
        Constant.x = 16;  // x 大小
        Constant.y = 16;  // y 大小
        Constant.mines = 40; // 雷数量
        Constant.flagOnly = true; // 是否去除右键问号标记
        Constant.noFlag = false; // 是否采用NF
    }
    // 高级
    private static void expert(){
        Constant.scale = 0.6; // 默认缩放 [0.6-3] 默认 1
        Constant.x = 30;  // x 大小
        Constant.y = 16;  // y 大小
        Constant.mines = 99; // 雷数量
        Constant.flagOnly = true; // 是否去除右键问号标记
        Constant.noFlag = false; // 是否采用NF
    }
}