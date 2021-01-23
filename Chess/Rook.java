public class Rook implements Piece{
    int color;
    public Rook(int color) {
        this.color = color;
    }
    public String toString() {
        if(color == 1) {
            return "♖";
        }
        return "♜";
    }
    public int color() {
        return color;
    }
}
