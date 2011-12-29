// William Gimson
// Experimental Assignment 2
// CSc 4520
// Dr. Prasad
package experimentalassignment2;

import java.util.*;


public class kruskals {
    
    // this int constant will represent nodes that have already been added 
    // to LinkedList inTree in the two dimensional array testMatrix as well
    // as node links which are tautological (node i to node i)
    public static final int MAX_POSSIBLE_INT = 2147483647;
    
    public static void main(String[] args) {
    
        // variable declarations
        int minWeight, totalWeight, saveWeight;
        long startTime, endTime;
        nodeObject firstNodeInTree, secondNodeInTree;
        boolean hasFirstNode, hasSecondNode;
        
        // create LinkedList to hold nodes which are in the tree
        LinkedList inTree = new LinkedList<nodeObject>();
        
        // generate two dimensional graph with randomly generated weights from
        // 1 to 101 - I did this rather than, say, from 0 - 100, because at
        // large input levels, there will always be another node to which a 
        // node inTree connects with a weight of zero, resulting in the 
        // paradoxical situation where larger and larger graph matrices produce
        // MST with total weights closer and closer to 0! - which just seems 
        // wrong somehow - still, at higher inputs the Math.random() generator
        // will always produce enough integers of the least possible magnitude
        // such that *every* new nodeObject added to inTree will have a 
        // 'selfWeight' of this least possible amount attached to it, and
        // totalWeight will be approximately ((testMatrix.length * minimum) - 1)
        // - user may wish to constrain the range of weights testMatrix can 
        // contain for this reason
        int[][] testMatrix = new int[100][100];
        for (int i = 0; i < testMatrix.length; i++) {
            for (int j = 0; j < testMatrix.length; j++) {
                if (i == j) {
                    testMatrix[i][j] = MAX_POSSIBLE_INT;
                } else {
                    testMatrix[i][j] = ((int)(Math.random() * 100) + 1);
                }
            }
        }

        // traverse the entirety of the matrix and find the smallest weight
        // the first and second nodes in tree will be the row column number of 
        // minWeight, respectively
        minWeight = testMatrix[0][0];
        firstNodeInTree = new nodeObject(0);
        secondNodeInTree = new nodeObject(0);
        for (int i = 0; i < testMatrix.length; i++) {
            for (int j = 0; j < testMatrix.length; j++) {
                if (testMatrix[i][j] < minWeight) {
                    minWeight = testMatrix[i][j];
                    firstNodeInTree = new nodeObject(i);
                    secondNodeInTree = new nodeObject(j);
                }
            }
        }
        
        // put the first and second nodes into LinkedList inTree
        inTree.addLast(firstNodeInTree);
        inTree.addLast(secondNodeInTree);
        
        // initialize relevant instance variables
        firstNodeInTree.setSelfWeight(0);
        secondNodeInTree.setSelfWeight(testMatrix
                [firstNodeInTree.getNodeCount()]
                [secondNodeInTree.getNodeCount()]);
        firstNodeInTree.setParent(null);
        secondNodeInTree.setParent(firstNodeInTree);
        

        // use the nodeCounts of firstNodeInTree and secondNodeInTree to set
        // matrix weight of these nodes to MAX_POSSIBLE_INT
        testMatrix[firstNodeInTree.getNodeCount()]
                [secondNodeInTree.getNodeCount()] = MAX_POSSIBLE_INT;

        // for every other weight in testMatrix, we only add the corresponding 
        // nodes if they aren't already inTree - otherwise, we either do nothing
        // or, in the case that the nodes are in separate subtrees that need
        // connecting, update the parent nodes and selfWeight of one of the 
        // nodes inTree
        
        // start time
        startTime = System.currentTimeMillis();
        while ((inTree.size()) < testMatrix.length) {
            // traverse testMatrix again and find smallest weight
            minWeight = testMatrix[0][0];
            for (int i = 0; i < testMatrix.length; i++) {
                for (int j = 0; j < testMatrix.length; j++) {
                    if (testMatrix[i][j] < minWeight) {
                        minWeight = testMatrix[i][j];
                        firstNodeInTree = new nodeObject(i);
                        secondNodeInTree = new nodeObject(j);
                    }
                }
            }
            
            hasFirstNode = false;
            for (int i = 0; i < inTree.size(); i++) {
                nodeObject checkNode = (nodeObject)inTree.get(i);
                if (checkNode.getNodeCount() == 
                        firstNodeInTree.getNodeCount()) {
                    hasFirstNode = true;
                }
            }
            
            // repeat node check for secondNodeInTree
            hasSecondNode = false;
            for (int i = 0; i < inTree.size(); i++) {
                nodeObject checkNode = (nodeObject)inTree.get(i);
                if (checkNode.getNodeCount() == 
                        secondNodeInTree.getNodeCount()) {
                    hasSecondNode = true;
                }
            }
            
            // if inTree has the first node but not the second, se need only
            // add and initialize the second
            if (hasFirstNode && !hasSecondNode) {
                inTree.addLast(secondNodeInTree);
                secondNodeInTree.setParent(firstNodeInTree);
                secondNodeInTree.setSelfWeight(minWeight);
                
                // use the nodeCounts of firstNodeInTree and secondNodeInTree 
                // to set matrix weight of these nodes to MAX_POSSIBLE_INT
                testMatrix[firstNodeInTree.getNodeCount()]
                [secondNodeInTree.getNodeCount()] = MAX_POSSIBLE_INT;
            } else if (!hasFirstNode && hasSecondNode) {
                inTree.addLast(firstNodeInTree);
                firstNodeInTree.setParent(secondNodeInTree);
                firstNodeInTree.setSelfWeight(minWeight);
                
                // use the nodeCounts of firstNodeInTree and secondNodeInTree 
                // to set matrix weight of these nodes to MAX_POSSIBLE_INT
                testMatrix[firstNodeInTree.getNodeCount()]
                [secondNodeInTree.getNodeCount()] = MAX_POSSIBLE_INT;
            } else if (!hasFirstNode && !hasSecondNode) {
                for (int i = 0; i < inTree.size(); i++) {
                    nodeObject checkNode = (nodeObject)inTree.get(i);
                    checkNode.setWasHereFirst(true);
                }
                inTree.addLast(firstNodeInTree);
                inTree.addLast(secondNodeInTree);
                secondNodeInTree.setParent(firstNodeInTree);
                secondNodeInTree.setSelfWeight(minWeight);
                
                saveWeight = minWeight;
                
                // use the nodeCounts of firstNodeInTree and secondNodeInTree 
                // to set matrix weight of these nodes to MAX_POSSIBLE_INT
                testMatrix[firstNodeInTree.getNodeCount()]
                [secondNodeInTree.getNodeCount()] = MAX_POSSIBLE_INT;
                
                minWeight = testMatrix[0][firstNodeInTree.getNodeCount()];
                nodeObject checkNode = (nodeObject)inTree.get(0);
                firstNodeInTree.setParent(checkNode);
                firstNodeInTree.setSelfWeight(minWeight);
                for (int i = 0; i < inTree.size(); i++) {
                    checkNode = (nodeObject)inTree.get(i);
                    if ((testMatrix[checkNode.getNodeCount()
                            ][firstNodeInTree.getNodeCount()]) < minWeight
                            && (checkNode.getWasHereFirst())) {
                        minWeight = testMatrix[checkNode.getNodeCount()
                            ][firstNodeInTree.getNodeCount()];
                        firstNodeInTree.setParent(checkNode);
                        firstNodeInTree.setSelfWeight(minWeight);
                    }
                }
                
                checkNode = (nodeObject)inTree.get(0);
                for (int i = 0; i < inTree.size(); i++) {
                    checkNode = (nodeObject)inTree.get(i);
                    if ((testMatrix[checkNode.getNodeCount()
                            ][secondNodeInTree.getNodeCount()] < minWeight)
                            && (checkNode.getWasHereFirst())) {
                        minWeight = testMatrix[checkNode.getNodeCount()
                            ][secondNodeInTree.getNodeCount()];
                        secondNodeInTree.setParent(checkNode);
                        secondNodeInTree.setSelfWeight(minWeight);
                        firstNodeInTree.setParent(secondNodeInTree);
                        firstNodeInTree.setSelfWeight(saveWeight);
                    }
                }
            } else {
                // use the nodeCounts of firstNodeInTree and secondNodeInTree 
                // to set matrix weight of these nodes to MAX_POSSIBLE_INT
                testMatrix[firstNodeInTree.getNodeCount()]
                [secondNodeInTree.getNodeCount()] = MAX_POSSIBLE_INT;
            }
        }
        
        //end time
        endTime = System.currentTimeMillis();
        
         // print total MST weight (add up self weights for all node in inTree)
        totalWeight = 0;
        for (int i = 0; i < inTree.size(); i++) {
            nodeObject summedNode = (nodeObject)inTree.get(i);
            totalWeight += summedNode.getSelfWeight();
        }

        System.out.println("\n            MST WEIGHT ANALYSIS                ");
        System.out.println("-------------------------------------------------");
        System.out.println("The total weight of the MST is: " + totalWeight);

        // print parent-child relationship of MST
        System.out.println("\n             TRAVERSAL ANALYSIS                ");
        System.out.println("-------------------------------------------------");
        for (int i =  1; i < inTree.size(); i++) {
            nodeObject findParent = (nodeObject)inTree.get(i);
            System.out.println("Node " + findParent.getNodeCount()
                    + "'s parent node is: " + findParent.getParent());
        }
        
         // print total run time
        System.out.println("\n           TIME COMPLEXITY ANALYSIS            ");
        System.out.println("-------------------------------------------------");
        System.out.println("Total run time to find an MST composed of " + 
                testMatrix.length + " inputs was: " + (endTime - startTime)
                + " milliseconds.");
    }
}



