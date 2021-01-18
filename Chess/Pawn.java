public class Pawn implements Piece{
    int color;
    public Pawn(int color) {
        this.color = color;
    }
    @Override
    public int type() {
        return 1;
    }
    public String toString() {
        if(color == 1) {
            return "♙";
        }
        return "♟";
    }
    public int color() {
        return color;
    }
}
