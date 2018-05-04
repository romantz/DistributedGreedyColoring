package projects.GreedyColoring.timers;

import projects.GreedyColoring.nodes.messages.BinaryColorMessage;
import projects.GreedyColoring.nodes.nodeImplementations.TreeNode;
import projects.GreedyColoring.CustomGlobal;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.timers.Timer;

/**
 * Created by Roman_ on 03/05/2018.
 */
public class GreedyColoringTimer extends Timer{

    private TreeNode node;

    public GreedyColoringTimer(TreeNode tn){
        node = tn;
    }

    public void fire() {
        for(int color = 0; color < node.color; color++) {
            boolean legalColor = true;
            for(Edge e : node.outgoingConnections) {
                TreeNode neighbor = (TreeNode)e.endNode;
                if(neighbor.color == color)
                    legalColor = false;
            }
            if(legalColor) {
                node.color = color;
                node.binaryColorString = node.convertIntToBinaryStringWithLength(color, 3);
                node.colorNode();
                break;
            }
        }
    }
}
