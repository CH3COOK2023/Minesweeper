import java.util.Objects;

public class Pairs {
    private int x;
    private int y;
    public Pairs(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pairs pairs = (Pairs) o;
        return x == pairs.x && y == pairs.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
