package checkerboard;



import java.util.ArrayList;
import java.util.List;

public class ChValMoves {
    private  int checkerType;
    private Position checkerPos;
    private  List<Position> valMoveList;
    public ChValMoves(int checkerType, Position checkerPos, List<Position> valMoveList){
        this.checkerType = checkerType;
        this.checkerPos = checkerPos;
        this.valMoveList = valMoveList;
    }

    public int getCheckerType() {
        return checkerType;
    }

    public List<Position> getValMoveList() {
        return valMoveList;
    }

    public Position getCheckerPos() {
        return checkerPos;
    }

    public void setCheckerType(int checkerType) {
        this.checkerType = checkerType;
    }

    public void setCheckerPos(Position checkerPos) {
        this.checkerPos = checkerPos;
    }

    public void setValMoveList(ArrayList<Position> valMoveList) {
        this.valMoveList = valMoveList;
    }
}
