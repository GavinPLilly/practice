public class King implements Piece{
    int color;
    boolean hasMoved;
    public King(int color) {
        this.color = color;
        hasMoved = false;
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
    public boolean hasMoved() {
        return hasMoved;
    }
    public void setMoved() {
        hasMoved = true;
    }
}
