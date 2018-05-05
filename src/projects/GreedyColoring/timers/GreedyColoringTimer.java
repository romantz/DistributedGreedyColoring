package projects.GreedyColoring.timers;

import projects.GreedyColoring.nodes.messages.BinaryColorMessage;
import projects.GreedyColoring.nodes.nodeImplementations.TreeNode;
import projects.GreedyColoring.CustomGlobal;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.timers.Timer;

/**
 * This timer is responsible of finding a greedy coloring after an 8-coloring is achieved.
 * After 8-coloring is achieved, there are 7 additional rounds - one for every color
 * from 1 to 7 (starting counting from 0). A node having color c > 0 waits for round c
 * to try and find a the smallest color smaller than c that still gives a valid coloring.
 * This way a greedy coloring is achieved.
 */
public class GreedyColoringTimer extends Timer{
    // The node that started this timer
    private TreeNode node;

    public GreedyColoringTimer(TreeNode tn){
        node = tn;
    }

    public void fire() {
        // Iterate over all available colors that are smaller then the nodes color
        // and see if it's a valid new color for the node
        for(int color = 0; color < node.color; color++) {
            boolean legalColor = true;
            for(Edge e : node.outgoingConnections) {
                TreeNode neighbor = (TreeNode)e.endNode;
                if(neighbor.color == color)
                    legalColor = false;
            }
            // If a new legal color was found, use it to color this node
            if(legalColor) {
                node.color = color;
                node.binaryColorString = node.convertIntToBinaryStringWithLength(color, 3);
                node.colorNode();
                break;
            }
        }
    }
}
