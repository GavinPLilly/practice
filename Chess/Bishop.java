public class Bishop implements Piece{
    int color;
    public Bishop(int color) {
        this.color = color;
    }
    @Override
    public int type() {
        return 4;
    }
    public String toString() {
        if(color == 1) {
            return "♗";
        }
        return "♝";
    }
    public int color() {
        return color;
    }
}
