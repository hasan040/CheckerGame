package checkerboard;



public class Position {
    private int rowPos;
    private int colPos;

    public Position(int row,int col){
        this.rowPos = row;
        this.colPos = col;
    }

    public int getRowPos() {
        return rowPos;
    }

    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }

    public int getColPos() {
        return colPos;
    }

    public void setColPos(int colPos) {
        this.colPos = colPos;
    }

    @Override
    public boolean equals(Object obj) {
        return this.rowPos == ((Position)obj).rowPos && this.colPos == ((Position)obj).colPos;
    }
}
