public class Knight implements Piece{
    int color;
    public Knight(int color) {
        this.color = color;
    }
    @Override
    public int type() {
        return 3;
    }
    public String toString() {
        if(color == 1) {
            return "♘";
        }
        return "♞";
    }
    public int color() {
        return color;
    }
}
