package checkerboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static checkerboard.DefinedConstants.*;

public class NodeInfo {
    private NodeInfo parentNode;
    //private NodeInfo currentNode;
    private List<NodeInfo> childNodeList;
    private int currentNodeValue;
    private boolean isLeafNode;

    private Position coOrdinate;

    private int alphaValue;
    private int betaValue;

    private boolean isMaximizer;

    //if the parent node is maximizer or minimizer

    public NodeInfo(){

    }

    public NodeInfo(NodeInfo parentNode,Position coOrdinate, boolean isLeafNode, boolean isMaximizer) {
        this.parentNode = parentNode;
        this.isLeafNode = isLeafNode;
        this.alphaValue = POS_INFINITY;
        this.betaValue = NEG_INFINITY;
        this.isMaximizer = isMaximizer;

        this.coOrdinate = coOrdinate;

        this.childNodeList = new ArrayList<>();

        if(this.isLeafNode){
            this.childNodeList = null;
            //this.currentNodeValue = CheckerBoard.tilesBoardArray[coOrdinate.getRowPos()][coOrdinate.getColPos()];
        }

    }

    public List<NodeInfo> getChildNodeList() {
        return childNodeList;
    }

    public void setChildNodeList(List<NodeInfo> childNodeList) {
        this.childNodeList = childNodeList;
    }

    public NodeInfo getParentNode() {
        return parentNode;
    }

    public void setParentNode(NodeInfo parentNode) {
        this.parentNode = parentNode;
    }


    public int getCurrentNodeValue() {
        if(this.coOrdinate != null){
            this.currentNodeValue = CheckerBoard.tilesBoardArray[coOrdinate.getRowPos()][coOrdinate.getColPos()];
        }

        return currentNodeValue;
    }

    public void setCurrentNodeValue(int currentNodeValue) {
        this.currentNodeValue = currentNodeValue;
    }

    public boolean isLeafNode() {
        return isLeafNode;
    }

    public void setLeafNode(boolean leafNode) {
        isLeafNode = leafNode;
    }

    public boolean isMaximizer() {
        return isMaximizer;
    }

    public void setMaximizer(boolean maximizer) {
        isMaximizer = maximizer;
    }

    public int getAlphaValue() {
        return alphaValue;
    }

    public void setAlphaValue(int alphaValue) {
        this.alphaValue = alphaValue;
    }

    public int getBetaValue() {
        return betaValue;
    }

    public void setBetaValue(int betaValue) {
        this.betaValue = betaValue;
    }

    public Position getCoOrdinate() {
        return coOrdinate;
    }
}


//Map<Integer,List<NodeInfo>>