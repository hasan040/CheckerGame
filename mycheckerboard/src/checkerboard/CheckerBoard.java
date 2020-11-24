package checkerboard;

import java.util.ArrayList;
import java.util.List;

import static checkerboard.DefinedConstants.*;

public class CheckerBoard {

    private int [][] checkerBoardArray;

    private int dimension;

    protected static int [][] tilesBoardArray;


    public CheckerBoard(int dimension){
        checkerBoardArray = new int[dimension][dimension];
        tilesBoardArray = new int[dimension][dimension];
        this.dimension = dimension;

        for (int row=0;row<dimension;row++){
            for (int col=0;col<dimension;col++){
                if(row == 0 &&  col < dimension-1 && col > 0){
                    checkerBoardArray[row][col] = BLACK_CHECKER;
                }
                else if(row == dimension-1 && col > 0 && col < dimension-1){
                    checkerBoardArray[row][col] = WHITE_CHECKER;
                }
                else{
                    checkerBoardArray[row][col] = EMPTY_CHECKER;
                }
            }
        }

        setupTilesValue();


    }

    public void setBlackCheckerAt(int row,int col){

        checkerBoardArray[row][col] = BLACK_CHECKER;

    }

    public void setWhiteCheckerAt(int row,int col){
        checkerBoardArray[row][col] = WHITE_CHECKER;
    }

    public void deleteCheckerFrom(int row,int col){
        checkerBoardArray[row][col] = EMPTY_CHECKER;
    }

    public int checkerTypeAt(int row,int col){
        if(row >=0 && row < dimension && col >=0 && col <dimension){
            return checkerBoardArray[row][col];
        }

        return INFINITY;

    }

    public List<Position> getLegalMovesFor(int row,int col){
        List<Position> gmLegalPosList = new ArrayList<>();

        int chType = checkerTypeAt(row,col);

        if(chType == EMPTY_CHECKER){
            return null;
        }
        else {

            int opponentChType = 99;
            int ownChType = 99;

            if(chType == BLACK_CHECKER){
                opponentChType = WHITE_CHECKER;
                ownChType = BLACK_CHECKER;
            }
            else{
                opponentChType = BLACK_CHECKER;
                ownChType = WHITE_CHECKER;
            }



            //checking row for total checkers at that row

            int total_checker_at_row = 0;//total checkers at that row

            for(int i=0;i<dimension;i++){
                 int tempCheckerType = checkerBoardArray[row][i];
                 if(tempCheckerType == WHITE_CHECKER || tempCheckerType == BLACK_CHECKER){
                     total_checker_at_row ++;
                 }
            }

            //checkers move from right to left

            int rightMostPos = col + total_checker_at_row;

            if(rightMostPos < dimension){
                if(checkerTypeAt(row,rightMostPos) != ownChType){
                    boolean possible = true;
                    for(int i=col+1;i<rightMostPos;i++){
                        if(checkerTypeAt(row,i) == opponentChType){
                            possible = false;
                            break;
                        }
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(row,rightMostPos));
                    }
                }
            }

            //checker move from left to right

            int leftMostPos = col - total_checker_at_row;
            if(leftMostPos >= 0){
                if(checkerTypeAt(row,leftMostPos) != ownChType){
                    boolean possible = true;
                    for(int i=leftMostPos+1;i<col;i++){
                        if(checkerTypeAt(row,i) == opponentChType){
                            possible = false;
                            break;
                        }
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(row,leftMostPos));
                    }
                }
            }

            //checking fixed column for legal moves

            int total_checker_at_column = 0;

            for(int i=0;i<dimension;i++){
                int getTheType = checkerTypeAt(i,col);
                if(getTheType == WHITE_CHECKER || getTheType == BLACK_CHECKER){
                    total_checker_at_column ++;
                }
            }

            //checker move from  up to down

            int downMaxPos = row + total_checker_at_column;

            if(downMaxPos < dimension){
                if(checkerTypeAt(downMaxPos,col) != ownChType){
                    boolean possible = true;
                    for (int i=row+1;i<downMaxPos;i++){
                        int tempChType = checkerTypeAt(i,col);
                        if(tempChType == opponentChType){
                            possible = false;
                            break;
                        }
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(downMaxPos,col));
                    }
                }
            }

            //checker move from down to up

            int upMostPos = row - total_checker_at_column;

            if(upMostPos >=0){
                if(checkerTypeAt(upMostPos,col) != ownChType){
                    boolean possible = true;
                    for(int i=upMostPos+1;i<row;i++){
                        int tmpChType = checkerTypeAt(i,col);
                        if(tmpChType == opponentChType){
                            possible = false;
                            break;
                        }
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(upMostPos,col));
                    }
                }
            }

            //checking cross positions 45 degree for legal moves

            int initial_row_pos = row;
            int initial_col_pos = col;

            while (initial_row_pos < dimension-1 && initial_col_pos >0){
                initial_row_pos ++;
                initial_col_pos--;
            }

            int v_row = initial_row_pos;
            int v_col = initial_col_pos;

            int total_checker_at_cross_right = 0;

            while (v_row >= 0 && v_col < dimension){
                int get_the_checker = checkerTypeAt(v_row,v_col);
                if(get_the_checker == BLACK_CHECKER || get_the_checker == WHITE_CHECKER){
                    total_checker_at_cross_right++;
                }
                v_row--;
                v_col++;
            }



            //checker move towards cross-right-up

            int goal_row = row - total_checker_at_cross_right;
            int goal_column = col + total_checker_at_cross_right;

            if(goal_row >= 0 && goal_column < dimension){
                int goal_checker_type = checkerTypeAt(goal_row,goal_column);

                if(goal_checker_type != ownChType){
                    boolean possible = true;
                    int i_row = row - 1;
                    int i_col = col + 1;
                    while (i_row > goal_row && i_col < goal_column){

                        if(checkerTypeAt(i_row,i_col) == opponentChType){
                            possible = false;
                            break;
                        }
                        i_row--;
                        i_col++;
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(goal_row,goal_column));
                    }
                }
            }

            //checker moves towards down

            int starting_row = row + total_checker_at_cross_right;
            int starting_col = col - total_checker_at_cross_right;

            if(starting_row < dimension && starting_col >= 0){
                int now_goal_type = checkerTypeAt(starting_row,starting_col);
                if(now_goal_type != ownChType){
                    boolean possible = true;

                    int i_row = row + 1;
                    int i_col = col - 1;

                    while (i_row < starting_row && i_col > starting_col){
                        if(checkerTypeAt(i_row,i_col) == opponentChType){
                            possible = false;
                            break;
                        }
                        i_row++;
                        i_col--;
                    }

                    if(possible){
                        gmLegalPosList.add(new Position(starting_row,starting_col));
                    }
                }

            }

            //checking cross position for 135 degree legal moves

            int current_row_start = row;
            int current_col_start = col;

            while (current_row_start < dimension -1 && current_col_start < dimension-1){
                current_row_start ++;
                current_col_start ++;
            }

            int valid_row_now = current_row_start;
            int valid_col_now = current_col_start;

            int all_checkers = 0;

            while (valid_row_now >=0 && valid_col_now >=0){
                int get_the_checker_tp = checkerTypeAt(valid_row_now,valid_col_now);

                if(get_the_checker_tp != EMPTY_CHECKER){
                    all_checkers ++;
                }
                valid_row_now--;
                valid_col_now--;
            }

            //checking towards up left

            int dest_row_num = row - all_checkers;
            int dest_col_num = col - all_checkers;

            if(dest_row_num >= 0 && dest_col_num >= 0){
                if(checkerTypeAt(dest_row_num,dest_col_num) != ownChType){
                    boolean possible = true;

                    int i_row_st = row-1;
                    int i_col_st = col-1;

                    while (i_row_st > dest_row_num && i_col_st > dest_col_num){
                        if(checkerTypeAt(i_row_st,i_col_st) == opponentChType){
                            possible = false;
                            break;
                        }

                        i_row_st--;
                        i_col_st--;

                    }

                    if(possible){
                        gmLegalPosList.add(new Position(dest_row_num,dest_col_num));
                    }
                }
            }

            //checking towards down right

            int r_dest_n = row + all_checkers;
            int c_dest_n = col + all_checkers;

            if(r_dest_n < dimension && c_dest_n < dimension){
                if(checkerTypeAt(r_dest_n,c_dest_n) != ownChType){
                    boolean possible = true;

                    int starter_row = row + 1;
                    int starter_col = col + 1;

                    while (starter_row < r_dest_n && starter_col < c_dest_n){

                        if(checkerTypeAt(starter_row,starter_col) == opponentChType){
                            possible = false;
                            break;
                        }

                        starter_row++;
                        starter_col++;
                    }

                    if(possible){
                        gmLegalPosList.add(new Position(r_dest_n,c_dest_n));
                    }

                }
            }

        }

        return gmLegalPosList;
    }

    private void setupTilesValue(){
        /*
             for 8 * 8 tiles

             -80 -25 -20 -20 -20 -20 -25 -80  //1st 0
             -25  10  10  10  10  10  10 -25  //2nd 1
             -20  10  25  25  25  25  10 -20  //3rd 2
             -20  10  25  50  50  25  10 -20  //4th 3
             -20  10  25  50  50  25  10 -20  //4th 4
             -20  10  25  25  25  25  10 -20   //3rd 5
             -25  10  10  10  10  10  10 -25   //2nd 6
             -80 -25 -20 -20 -20 -20 -25 -80   //1st 7
         */

        /*
             for 6*6 tiles

             -80 -25 -20 -20 -25 -80  //1st 0
             -25  10  10  10  10 -25  //2nd 1
             -20  10  50  50  10 -20  //3rd 2
             -20  10  50  50  10 -20  //3rd 3
             -25  10  10  10  10 -25  //2nd 4
             -80 -25 -20 -20 -25 -80  //1st 5
         */

        int arrOne_8 [] = {-80,-25,-20,-20,-20,-20,-25,-80};
        int arrTwo_8 [] = {-25,10,10,10,10,10,10,-25};
        int arrThree_8[] = {-20,10,25,25,25,25,10,-20};
        int arrFour_8[] = {-20,10,25,50,50,25,10,-20};

        int arrOne_6 [] = {-80,-25,-20,-20,-25,-80};
        int arrTwo_6 [] = {-25,10,10,10,10,-25};
        int arrThree_6 [] = {-20,10,50,50,10,-10};

        if(dimension == DIMENSION_8){

            for(int i=0;i<dimension;i++){
                for(int j=0;j<dimension;j++){
                    if(i == 0 || i == 7){
                        tilesBoardArray[i][j] = arrOne_8[j];
                    }
                    else if(i ==1 || i == 6){
                        tilesBoardArray[i][j] = arrTwo_8[j];
                    }
                    else if(i==2 || i== 5){
                        tilesBoardArray[i][j] = arrThree_8[j];
                    }
                    else{
                        tilesBoardArray[i][j] = arrFour_8[j];
                    }
                }
            }

        }
        else if(dimension == DIMENSION_6){

            for(int i=0;i<dimension;i++){
                for(int j=0;j<dimension;j++){
                    if(i==0 || i==5){
                        tilesBoardArray[i][j] = arrOne_6[j];
                    }
                    else if(i==1 || i==4){
                        tilesBoardArray[i][j] = arrTwo_6[j];
                    }
                    else{
                        tilesBoardArray[i][j] = arrThree_6[j];

                    }
                }
            }

        }




    }

    public List<Integer> getTotalScore(){

        int scoreBlack = 0;
        int scoreWhite = 0;

        List<Integer> scoreSaverList = new ArrayList<>();

        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                int myCheckerType = checkerBoardArray[i][j];
                if(myCheckerType == WHITE_CHECKER){
                    scoreWhite += tilesBoardArray[i][j];
                }
                else if(myCheckerType == BLACK_CHECKER){
                    scoreBlack += tilesBoardArray[i][j];
                }

            }
        }

        scoreSaverList.add(WHITE_CHECKER,scoreWhite);
        scoreSaverList.add(BLACK_CHECKER,scoreBlack);

        return scoreSaverList;

    }

    public List<Position> getLegalMovesPruning(Position tempPosition,int checkerType){

        List<Position> gmLegalPosList = new ArrayList<>();

        int row = tempPosition.getRowPos();
        int col = tempPosition.getColPos();

        //int chType = checkerTypeAt(row,col);

        int chType = checkerType;

        if(chType == EMPTY_CHECKER){
            return null;
        }
        else {

            int opponentChType = 99;
            int ownChType = 99;

            if(chType == BLACK_CHECKER){
                opponentChType = WHITE_CHECKER;
                ownChType = BLACK_CHECKER;
            }
            else{
                opponentChType = BLACK_CHECKER;
                ownChType = WHITE_CHECKER;
            }



            //checking row for total checkers at that row

            int total_checker_at_row = 0;//total checkers at that row

            for(int i=0;i<dimension;i++){
                int tempCheckerType = checkerBoardArray[row][i];
                if(tempCheckerType == WHITE_CHECKER || tempCheckerType == BLACK_CHECKER){
                    total_checker_at_row ++;
                }
            }

            //checkers move from right to left

            int rightMostPos = col + total_checker_at_row;

            if(rightMostPos < dimension){
                if(checkerTypeAt(row,rightMostPos) != ownChType){
                    boolean possible = true;
                    for(int i=col+1;i<rightMostPos;i++){
                        if(checkerTypeAt(row,i) == opponentChType){
                            possible = false;
                            break;
                        }
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(row,rightMostPos));
                    }
                }
            }

            //checker move from left to right

            int leftMostPos = col - total_checker_at_row;
            if(leftMostPos >= 0){
                if(checkerTypeAt(row,leftMostPos) != ownChType){
                    boolean possible = true;
                    for(int i=leftMostPos+1;i<col;i++){
                        if(checkerTypeAt(row,i) == opponentChType){
                            possible = false;
                            break;
                        }
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(row,leftMostPos));
                    }
                }
            }

            //checking fixed column for legal moves

            int total_checker_at_column = 0;

            for(int i=0;i<dimension;i++){
                int getTheType = checkerTypeAt(i,col);
                if(getTheType == WHITE_CHECKER || getTheType == BLACK_CHECKER){
                    total_checker_at_column ++;
                }
            }

            //checker move from  up to down

            int downMaxPos = row + total_checker_at_column;

            if(downMaxPos < dimension){
                if(checkerTypeAt(downMaxPos,col) != ownChType){
                    boolean possible = true;
                    for (int i=row+1;i<downMaxPos;i++){
                        int tempChType = checkerTypeAt(i,col);
                        if(tempChType == opponentChType){
                            possible = false;
                            break;
                        }
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(downMaxPos,col));
                    }
                }
            }

            //checker move from down to up

            int upMostPos = row - total_checker_at_column;

            if(upMostPos >=0){
                if(checkerTypeAt(upMostPos,col) != ownChType){
                    boolean possible = true;
                    for(int i=upMostPos+1;i<row;i++){
                        int tmpChType = checkerTypeAt(i,col);
                        if(tmpChType == opponentChType){
                            possible = false;
                            break;
                        }
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(upMostPos,col));
                    }
                }
            }

            //checking cross positions 45 degree for legal moves

            int initial_row_pos = row;
            int initial_col_pos = col;

            while (initial_row_pos < dimension-1 && initial_col_pos >0){
                initial_row_pos ++;
                initial_col_pos--;
            }

            int v_row = initial_row_pos;
            int v_col = initial_col_pos;

            int total_checker_at_cross_right = 0;

            while (v_row >= 0 && v_col < dimension){
                int get_the_checker = checkerTypeAt(v_row,v_col);
                if(get_the_checker == BLACK_CHECKER || get_the_checker == WHITE_CHECKER){
                    total_checker_at_cross_right++;
                }
                v_row--;
                v_col++;
            }



            //checker move towards cross-right-up

            int goal_row = row - total_checker_at_cross_right;
            int goal_column = col + total_checker_at_cross_right;

            if(goal_row >= 0 && goal_column < dimension){
                int goal_checker_type = checkerTypeAt(goal_row,goal_column);

                if(goal_checker_type != ownChType){
                    boolean possible = true;
                    int i_row = row - 1;
                    int i_col = col + 1;
                    while (i_row > goal_row && i_col < goal_column){

                        if(checkerTypeAt(i_row,i_col) == opponentChType){
                            possible = false;
                            break;
                        }
                        i_row--;
                        i_col++;
                    }
                    if(possible){
                        gmLegalPosList.add(new Position(goal_row,goal_column));
                    }
                }
            }

            //checker moves towards down

            int starting_row = row + total_checker_at_cross_right;
            int starting_col = col - total_checker_at_cross_right;

            if(starting_row < dimension && starting_col >= 0){
                int now_goal_type = checkerTypeAt(starting_row,starting_col);
                if(now_goal_type != ownChType){
                    boolean possible = true;

                    int i_row = row + 1;
                    int i_col = col - 1;

                    while (i_row < starting_row && i_col > starting_col){
                        if(checkerTypeAt(i_row,i_col) == opponentChType){
                            possible = false;
                            break;
                        }
                        i_row++;
                        i_col--;
                    }

                    if(possible){
                        gmLegalPosList.add(new Position(starting_row,starting_col));
                    }
                }

            }

            //checking cross position for 135 degree legal moves

            int current_row_start = row;
            int current_col_start = col;

            while (current_row_start < dimension -1 && current_col_start < dimension-1){
                current_row_start ++;
                current_col_start ++;
            }

            int valid_row_now = current_row_start;
            int valid_col_now = current_col_start;

            int all_checkers = 0;

            while (valid_row_now >=0 && valid_col_now >=0){
                int get_the_checker_tp = checkerTypeAt(valid_row_now,valid_col_now);

                if(get_the_checker_tp != EMPTY_CHECKER){
                    all_checkers ++;
                }
                valid_row_now--;
                valid_col_now--;
            }

            //checking towards up left

            int dest_row_num = row - all_checkers;
            int dest_col_num = col - all_checkers;

            if(dest_row_num >= 0 && dest_col_num >= 0){
                if(checkerTypeAt(dest_row_num,dest_col_num) != ownChType){
                    boolean possible = true;

                    int i_row_st = row-1;
                    int i_col_st = col-1;

                    while (i_row_st > dest_row_num && i_col_st > dest_col_num){
                        if(checkerTypeAt(i_row_st,i_col_st) == opponentChType){
                            possible = false;
                            break;
                        }

                        i_row_st--;
                        i_col_st--;

                    }

                    if(possible){
                        gmLegalPosList.add(new Position(dest_row_num,dest_col_num));
                    }
                }
            }

            //checking towards down right

            int r_dest_n = row + all_checkers;
            int c_dest_n = col + all_checkers;

            if(r_dest_n < dimension && c_dest_n < dimension){
                if(checkerTypeAt(r_dest_n,c_dest_n) != ownChType){
                    boolean possible = true;

                    int starter_row = row + 1;
                    int starter_col = col + 1;

                    while (starter_row < r_dest_n && starter_col < c_dest_n){

                        if(checkerTypeAt(starter_row,starter_col) == opponentChType){
                            possible = false;
                            break;
                        }

                        starter_row++;
                        starter_col++;
                    }

                    if(possible){
                        gmLegalPosList.add(new Position(r_dest_n,c_dest_n));
                    }

                }
            }

        }

        return gmLegalPosList;

    }

    private int miniMaxFunc(NodeInfo mNodeInfo,int alpha,int beta,boolean maxPlayer){
        if(mNodeInfo.isLeafNode() || mNodeInfo.getChildNodeList().size() == 0){
            return mNodeInfo.getCurrentNodeValue();
        }

        if(maxPlayer){
            int maxEva = NEG_INFINITY;
            for(NodeInfo node : mNodeInfo.getChildNodeList()){
                int temp = miniMaxFunc(node,alpha,beta,false);
                maxEva = Math.max(maxEva,temp);
                alpha = Math.max(alpha,maxEva);
                if(beta <= alpha)
                    break;

            }
            return maxEva;

        }

        else{
            int minEva = POS_INFINITY;
            for(NodeInfo node : mNodeInfo.getChildNodeList()){
                int temp = miniMaxFunc(node,alpha,beta,true);
                minEva = Math.min(minEva,temp);
                beta = Math.min(beta,minEva);
                if(beta <= alpha)
                    break;
            }
            return minEva;
        }

    }

    public Position getBestMove(Position maxParentPos){
        //int typeOfChecker = WHITE_CHECKER;

        int typeOfChecker = checkerTypeAt(maxParentPos.getRowPos(),maxParentPos.getColPos());

        NodeInfo maxParentNode = new NodeInfo(null,maxParentPos,false,true);

        List<Position> getValidAllMoves = getLegalMovesPruning(maxParentPos,typeOfChecker);

        for(Position temp : getValidAllMoves){

            NodeInfo minNode = new NodeInfo(maxParentNode,temp,false,false);

            List<Position> innerMoves = getLegalMovesPruning(temp,typeOfChecker);

            for(Position tempInner : innerMoves){
                List<Position> leafMoves = getLegalMovesPruning(tempInner,typeOfChecker);

                for(Position tempLeaf : leafMoves){
                    NodeInfo tempLeafNode = new NodeInfo(minNode,tempLeaf,true,true);
                    minNode.getChildNodeList().add(tempLeafNode);
                }
            }
            maxParentNode.getChildNodeList().add(minNode);

        }

        NodeInfo getTheMiniMaxValue = miniMax(maxParentNode,NEG_INFINITY,POS_INFINITY,true);

        NodeInfo nt = getTheMiniMaxValue.getParentNode();
        while (nt.getParentNode().getParentNode() != null){
            nt = nt.getParentNode();
        }
        return nt.getCoOrdinate();
    }


    private NodeInfo miniMax(NodeInfo mNodeInfo,int alpha,int beta,boolean maxPlayer){
        if(mNodeInfo.isLeafNode() || mNodeInfo.getChildNodeList().size() == 0){
            return mNodeInfo;
        }

        if(maxPlayer){
            int maxEva = NEG_INFINITY;
            NodeInfo tobeReturned = new NodeInfo();
            tobeReturned.setCurrentNodeValue(maxEva);

            for(NodeInfo node : mNodeInfo.getChildNodeList()){
                NodeInfo temp = miniMax(node,alpha,beta,false);
                if(temp.getCurrentNodeValue() > tobeReturned.getCurrentNodeValue()){
                    tobeReturned = temp;
                }
                maxEva = Math.max(maxEva,temp.getCurrentNodeValue());
                alpha = Math.max(alpha,maxEva);
                if(beta <= alpha)
                    break;

            }
            return tobeReturned;

        }

        else{
            int minEva = POS_INFINITY;
            NodeInfo tobeReturned = new NodeInfo();
            tobeReturned.setCurrentNodeValue(minEva);

            for(NodeInfo node : mNodeInfo.getChildNodeList()){
                NodeInfo temp = miniMax(node,alpha,beta,true);
                if(temp.getCurrentNodeValue() < tobeReturned.getCurrentNodeValue()){
                    tobeReturned = temp;
                }
                minEva = Math.min(minEva,temp.getCurrentNodeValue());
                beta = Math.min(beta,minEva);
                if(beta <= alpha)
                    break;
            }
            return tobeReturned;
        }

    }

    public List<Position> getTheBlackCheckerList(){

        List<Position> allBlackCheckers = new ArrayList<>();

        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(checkerTypeAt(i,j) == BLACK_CHECKER){
                    allBlackCheckers.add(new Position(i,j));
                }
            }
        }

      return allBlackCheckers;
    }


}



