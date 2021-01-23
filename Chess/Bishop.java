public class Bishop implements Piece{
    int color;
    public Bishop(int color) {
        this.color = color;
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
