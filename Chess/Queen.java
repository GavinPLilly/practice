public class Queen implements Piece{
    int color;
    public Queen(int color) {
        this.color = color;
    }
    public String toString() {
        if(color == 1) {
            return "♕";
        }
        return "♛";
    }
    public int color() {
        return color;
    }
}
