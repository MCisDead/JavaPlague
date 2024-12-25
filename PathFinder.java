import java.util.ArrayList;

public class PathFinder {
    Game gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;
    
    // default constructor
    public PathFinder() {} 
    
    // constructor
    public PathFinder(Game gp) {
        this.gp = gp;  
        instantiateNodes();
    } // PathFinder

    public void instantiateNodes(){
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];
        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;
            } // if
        } // while
    } // instantiateNodes

    public void resetNodes() {
        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            } // if
        } // while

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    } // resetNodes

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, boolean canNoclip, Entity entity) {
        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
        	
            // set solid nodes and check tiles
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];

            if (gp.tileM.tile[tileNum].collision == true && canNoclip == false) {
                node[col][row].solid = true;
            } // if
            
            else if (canNoclip == true) {
                node[col][row].solid = false;
            } // else if

            // SET COST
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            } // if
        } // while
        
    } // setNodes

    // costs are how far a node is from the goal or start, determining the monsters' paths
    public void getCost(Node node) {
        
    	// G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F cost
        node.fCost = node.gCost + node.hCost;
    } // getCost

    // search for a path to goal
    public boolean search() { 	
        while(goalReached == false) {
            int col = currentNode.col;
            int row = currentNode.row;

            // Check the current node
            currentNode.checked = true;
            openList.remove(currentNode);

            // Open the Up node
            if(row - 1 >= 0) {
                openNode(node[col][row-1]);               
            } // if
            
            // Open the left node
            if(col - 1 >= 0) {
                openNode(node[col-1][row]);
            } // if

            // Open the down node
            if(row + 1 < gp.maxWorldRow) {
                openNode(node[col][row+1]);
            } // if

            if(col + 1 < gp.maxWorldCol) {
                openNode(node[col+1][row]);
            } // if

            // Find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            
            for(int i = 0; i < openList.size(); i++) {
                // Check if this node's F cost is better
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                    
                } // if
                
                // If F cost is equal, check the G cost
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    } // if
                } // else if
            } // for
            
            // If there is no node in the openList, end the loop
            if (openList.size() == 0) {
                break;
            } // if

            // After the loop, openList [bestNodeIndex) is the next step (= currentNode)
            currentNode = openList.get(bestNodeIndex);
            
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            } // if
        } // while

        return goalReached;
    } // search

    public void openNode(Node node) {
        if (node.open == false && node.checked == false && node.solid == false) {
            node.open = true;
            node.parent = currentNode;            
            openList.add(node);
        } // if
    } // openNode

    public void trackThePath() {
        Node current = goalNode;
        while (current != startNode) {      	
            pathList.add(0, current);
            current = current.parent;
        } // while
    } // trackThePath

} //PathFinder