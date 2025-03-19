public class Game {
    public static Queue<Minesweeper> runtimeInstance = new Queue<>();
    private Game(){}
    public static void newGame(){
        Constant.scale = Math.max(0.6,Constant.scale);
        Constant.scale = Math.min(3,Constant.scale);
        Constant.x = Math.max(9,Constant.x);
        Constant.y = Math.max(9,Constant.y);
        Constant.mines = Math.min(Constant.x*Constant.y-1,Constant.mines);

        System.setProperty("sun.java2d.uiScale",Double.toString(Constant.scale));
        boolean run = runtimeInstance.add(new Minesweeper());
        if(!run)
        {
            System.err.println("程序上限");
            return;
        }
        runtimeInstance.getTail().run(); // 运行实例
    }
}
