public class King implements Piece{
    int color;
    public King(int color) {
        this.color = color;
    }
    @Override
    public int type() {
        return 99;
    }
    public String toString() {
        if(color == 1) {
            return "♔";
        }
        return "♚";
    }
    public int color() {
        return color;
    }
}
