# Minesweeper

**[[中文](https://github.com/CH3COOK2023/Minesweeper/tree/master) | [English](https://github.com/CH3COOK2023/Minesweeper/blob/master/README_EN.md)]**

### 如何启动

在`main`中需要先调整参数，可供调整的参数有：

```java
Constant.x = 20;		// 雷区的长度
Constant.y = 20;		// 雷区的宽度
Constant.mines = 35;	// 雷数量
Constant.scale = 1;		// 界面缩放，允许的值为 0.6 到 3
```

然后，使用`Game.newGame();`来启动游戏

### 如何玩初级、中级、高级

如果想玩初级，直接在`main`函数中：

```java
public static void main(String[] args) {
    beginner();			// 自动调整参数
    // Constant.scale = 1; // 可选择，默认0.6
    Game.newGame();
}
```

中级和高级也是如此，已经提供好了特定的参数



