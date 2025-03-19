# Minesweeper

**[[中文](https://github.com/CH3COOK2023/Minesweeper/tree/master) | [English](https://github.com/CH3COOK2023/Minesweeper/blob/master/README_EN.md)]**

### How to get started

You have to configure the parameter in `main`. These are the parameter you can adjust...

```java
Constant.x = 20;		// length of mine fields
Constant.y = 20;		// width of mine fields
Constant.mines = 35;	// number of mines
Constant.scale = 1;		// scaling the interface from 0.6 to 3
```

Then, use `Game.newGame(); to start the game!

### How to play default beginner mode

If you wan to play beginner mode，set these in `main`：

```java
public static void main(String[] args) {
    beginner();			   // This will configure beginner parameter automatically.
    // Constant.scale = 1; // optional，default value is 0.6
    Game.newGame(); 	   // start a new game
}
```

and as well as `intermedia()` and `expert()`;



