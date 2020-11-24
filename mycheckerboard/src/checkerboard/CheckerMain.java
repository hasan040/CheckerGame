package checkerboard;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;

import static checkerboard.DefinedConstants.*;

public class CheckerMain extends Application {

    private GraphicsContext mGraphicsContext;

    private final int DIMENSION = DIMENSION_8;

    private ChValMoves dataCheckerValidMoves = null;

    private CheckerBoard mCheckerBoard;

    private int GAMING_MODE = MODE_NONE;

    private boolean setGamingMode = false;

    private Random mRandom;


    @Override
    public void start(Stage primaryStage) throws Exception{

        mRandom = new Random();

        Group root = new Group();

        mCheckerBoard = new CheckerBoard(DIMENSION);

        primaryStage.setTitle("CheckerGame");

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(DIMENSION * TILE_SIZE,DIMENSION * TILE_SIZE + 100);

        root.getChildren().add(canvas);
        mGraphicsContext = canvas.getGraphicsContext2D();



        //drawTheBoard();

        initMessage(mGraphicsContext,TILE_SIZE/2,(DIMENSION * TILE_SIZE)/2,30);

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if(GAMING_MODE == MODE_HUMAN && setGamingMode){
                    humanVsHuman(event);
                }

                else if(GAMING_MODE == MODE_AI && setGamingMode){
                    humanVsAi(event);

                }
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(!setGamingMode){

                    if(event.getText().equalsIgnoreCase("h")){
                        GAMING_MODE = MODE_HUMAN;
                        setGamingMode = true;
                        refreshTheBoard();
                    }
                    else if(event.getText().equalsIgnoreCase("c")){
                        GAMING_MODE = MODE_AI;
                        setGamingMode = true;
                        refreshTheBoard();
                    }


                }

            }
        });

        primaryStage.show();
    }

    private void drawTheBoard(){

        mGraphicsContext.setFill(Color.LIGHTGRAY);

        for(int i=0;i<DIMENSION;i++){
            for(int j = (i%2 == 0) ? 1 : 0;j<DIMENSION;j+=2){
                mGraphicsContext.fillRect( j * TILE_SIZE,  i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        mGraphicsContext.setStroke(Color.BLACK);
        mGraphicsContext.strokeRect(0,0,DIMENSION*TILE_SIZE,DIMENSION*TILE_SIZE);

        for (int row=0;row<DIMENSION;row++){
            for(int col=0;col<DIMENSION;col++){

                int getTheType = mCheckerBoard.checkerTypeAt(row,col);

                if(getTheType == BLACK_CHECKER){
                    mGraphicsContext.setFill(Color.BLACK);
                    mGraphicsContext.fillOval(POS_ALIGN + row*TILE_SIZE,POS_ALIGN + col * TILE_SIZE,OVAL_SIZE,OVAL_SIZE);
                    mGraphicsContext.setStroke(Color.WHITE);
                    mGraphicsContext.strokeOval(POS_ALIGN + row*TILE_SIZE,POS_ALIGN + col * TILE_SIZE,OVAL_SIZE,OVAL_SIZE);
                }
                else if(getTheType == WHITE_CHECKER){
                    mGraphicsContext.setFill(Color.WHITE);
                    mGraphicsContext.fillOval(POS_ALIGN + row*TILE_SIZE,POS_ALIGN + col * TILE_SIZE,OVAL_SIZE,OVAL_SIZE);
                    mGraphicsContext.setStroke(Color.BLACK);
                    mGraphicsContext.strokeOval(POS_ALIGN + row*TILE_SIZE,POS_ALIGN + col * TILE_SIZE,OVAL_SIZE,OVAL_SIZE);
                }

            }
        }


    }

    private void refreshTheBoard(){

        mGraphicsContext.clearRect(0,0,mGraphicsContext.getCanvas().getWidth(),mGraphicsContext.getCanvas().getHeight());
        drawTheBoard();
        if(setGamingMode && GAMING_MODE == MODE_HUMAN){
            drawMessage(mGraphicsContext,TILE_SIZE/2,(DIMENSION * TILE_SIZE)+30,20);
        }
        else if(setGamingMode && GAMING_MODE == MODE_AI){
            drawMessageAI(mGraphicsContext,TILE_SIZE/2,(DIMENSION * TILE_SIZE)+30,20);
        }

    }

    boolean clickedFromPreviousData(Position position){
        if(dataCheckerValidMoves == null){
            return false;
        }
        else{
            for(Position pos : dataCheckerValidMoves.getValMoveList()){
                if(pos.equals(position)){

                    return true;
                }
            }

        }
        return false;
    }

    private void humanVsHuman(MouseEvent event){

        refreshTheBoard();

        int rowClicked = (int) event.getX()/TILE_SIZE;
        int colClicked = (int) event.getY()/TILE_SIZE;

        int checker_type_clicked = mCheckerBoard.checkerTypeAt(rowClicked,colClicked);



        if(clickedFromPreviousData(new Position(rowClicked,colClicked))){
            int dataCheckerType = dataCheckerValidMoves.getCheckerType();
            Position removePos = dataCheckerValidMoves.getCheckerPos();
            if(checker_type_clicked == EMPTY_CHECKER){

                if(dataCheckerType == WHITE_CHECKER){
                    mCheckerBoard.setWhiteCheckerAt(rowClicked,colClicked);

                    mCheckerBoard.deleteCheckerFrom(removePos.getRowPos(),removePos.getColPos());
                }

                else if(dataCheckerType == BLACK_CHECKER){
                    mCheckerBoard.setBlackCheckerAt(rowClicked,colClicked);
                    mCheckerBoard.deleteCheckerFrom(removePos.getRowPos(),removePos.getColPos());
                }

            }

            else if(checker_type_clicked == WHITE_CHECKER){
                if(dataCheckerType == BLACK_CHECKER){
                    mCheckerBoard.setBlackCheckerAt(rowClicked,colClicked);
                    mCheckerBoard.deleteCheckerFrom(removePos.getRowPos(),removePos.getColPos());
                }
            }

            else if(checker_type_clicked == BLACK_CHECKER){
                if(dataCheckerType == WHITE_CHECKER){
                    mCheckerBoard.setWhiteCheckerAt(rowClicked,colClicked);
                    mCheckerBoard.deleteCheckerFrom(removePos.getRowPos(),removePos.getColPos());
                }
            }
            dataCheckerValidMoves = null;
            refreshTheBoard();


        }

        else{

            if(checker_type_clicked == WHITE_CHECKER){

                List<Position> getValidMoves = mCheckerBoard.getLegalMovesFor(rowClicked,colClicked);

                if(getValidMoves.size() > 0){
                    mGraphicsContext.setFill(Color.GREEN);
                    mGraphicsContext.setStroke(Color.BLACK);


                    for(Position cord : getValidMoves){
                        mGraphicsContext.fillRect(cord.getRowPos() * TILE_SIZE,cord.getColPos() * TILE_SIZE,TILE_SIZE,TILE_SIZE);
                        mGraphicsContext.strokeRect(cord.getRowPos() * TILE_SIZE,cord.getColPos() * TILE_SIZE,TILE_SIZE,TILE_SIZE);
                    }

                    dataCheckerValidMoves = new ChValMoves(WHITE_CHECKER,new Position(rowClicked,colClicked),getValidMoves);


                }
            }
            else if(checker_type_clicked == BLACK_CHECKER){
                List<Position> getValidMoves = mCheckerBoard.getLegalMovesFor(rowClicked,colClicked);

                if(getValidMoves.size() > 0){
                    mGraphicsContext.setFill(Color.VIOLET);
                    mGraphicsContext.setStroke(Color.BLACK);

                    for(Position cord : getValidMoves){
                        mGraphicsContext.fillRect(cord.getRowPos() * TILE_SIZE,cord.getColPos() * TILE_SIZE,TILE_SIZE,TILE_SIZE);
                        mGraphicsContext.strokeRect(cord.getRowPos() * TILE_SIZE,cord.getColPos() * TILE_SIZE,TILE_SIZE,TILE_SIZE);
                    }

                    dataCheckerValidMoves = new ChValMoves(BLACK_CHECKER,new Position(rowClicked,colClicked),getValidMoves);

                }


            }
            else{

                dataCheckerValidMoves = null;

            }
        }

    }

    private void drawMessage(GraphicsContext gc,double x, double y, double size) {
        gc.setFont(new Font(FONT_NAME, size));
        gc.setFill(javafx.scene.paint.Color.BLACK);

        gc.fillText("human vs human gaming mode enabled!", x, y);

        List<Integer> mList = mCheckerBoard.getTotalScore();
        String blackScore = "black score : "+mList.get(BLACK_CHECKER);
        String whiteScore = "white score : "+mList.get(WHITE_CHECKER);

        gc.fillText(blackScore, x, y+20);
        gc.fillText(whiteScore,x,y+40);
    }

    private void drawMessageAI(GraphicsContext gc,double x,double y,double size){
        gc.setFont(new Font(FONT_NAME, size));
        gc.setFill(Color.BLACK);

        gc.fillText("human vs ai gaming mode enabled!", x, y);

        List<Integer> mList = mCheckerBoard.getTotalScore();
        String blackScore = "black score (ai): "+mList.get(BLACK_CHECKER);
        String whiteScore = "white score (human): "+mList.get(WHITE_CHECKER);

        gc.fillText(blackScore, x, y+20);
        gc.fillText(whiteScore,x,y+40);

    }

    private void initMessage(GraphicsContext gc,double x, double y, double size) {
        gc.setFont(new Font(FONT_NAME, size));
        gc.setFill(Color.LIGHTGRAY);

        gc.fillRect(0,0,DIMENSION * TILE_SIZE,DIMENSION * TILE_SIZE + 100);
        gc.setFill(Color.BLACK);

        gc.fillText("Click h for human mode!", x, y);
        gc.fillText("Click c for ai mode!",x,y+50);
    }

    private void humanVsAi(MouseEvent event){
        refreshTheBoard();
        int rowClicked = (int) event.getX()/TILE_SIZE;
        int colClicked = (int) event.getY()/TILE_SIZE;

        int checker_type_clicked = mCheckerBoard.checkerTypeAt(rowClicked,colClicked);

        //human clicks first & only on white checkers

        if(clickedFromPreviousData(new Position(rowClicked,colClicked))){

            int dataCheckerType = dataCheckerValidMoves.getCheckerType();
            Position removePos = dataCheckerValidMoves.getCheckerPos();
            if(checker_type_clicked == EMPTY_CHECKER){

                if(dataCheckerType == WHITE_CHECKER){
                    mCheckerBoard.setWhiteCheckerAt(rowClicked,colClicked);

                    mCheckerBoard.deleteCheckerFrom(removePos.getRowPos(),removePos.getColPos());
                }

            }

            else if(checker_type_clicked == BLACK_CHECKER){
                if(dataCheckerType == WHITE_CHECKER){
                    mCheckerBoard.setWhiteCheckerAt(rowClicked,colClicked);
                    mCheckerBoard.deleteCheckerFrom(removePos.getRowPos(),removePos.getColPos());
                }
            }
            dataCheckerValidMoves = null;
            refreshTheBoard();

            // preparing for ai move

            List<Position> currentBlackCheckerList = mCheckerBoard.getTheBlackCheckerList();
            Position fixedBlackPosition = null;
            Position newPosition = null;

            int sizeList = currentBlackCheckerList.size();
            if(sizeList > 0){
                fixedBlackPosition = currentBlackCheckerList.get(mRandom.nextInt(sizeList));
            }
            if(fixedBlackPosition != null){
                newPosition = mCheckerBoard.getBestMove(fixedBlackPosition);
            }

            if(newPosition != null){
                mCheckerBoard.setBlackCheckerAt(newPosition.getRowPos(),newPosition.getColPos());
                mCheckerBoard.deleteCheckerFrom(fixedBlackPosition.getRowPos(),fixedBlackPosition.getColPos());
            }
            refreshTheBoard();

        }

        else if(checker_type_clicked == WHITE_CHECKER){
            List<Position> getValidMoves = mCheckerBoard.getLegalMovesFor(rowClicked,colClicked);

            if(getValidMoves.size() > 0){
                mGraphicsContext.setFill(Color.GREEN);
                mGraphicsContext.setStroke(Color.BLACK);


                for(Position cord : getValidMoves){
                    mGraphicsContext.fillRect(cord.getRowPos() * TILE_SIZE,cord.getColPos() * TILE_SIZE,TILE_SIZE,TILE_SIZE);
                    mGraphicsContext.strokeRect(cord.getRowPos() * TILE_SIZE,cord.getColPos() * TILE_SIZE,TILE_SIZE,TILE_SIZE);
                }

                dataCheckerValidMoves = new ChValMoves(WHITE_CHECKER,new Position(rowClicked,colClicked),getValidMoves);


            }
        }

        else{
            dataCheckerValidMoves = null;
        }


    }



    public static void main(String[] args) {
        launch(args);
    }
}
