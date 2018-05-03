package projects.GreedyColoring.timers;

import projects.GreedyColoring.nodes.messages.BinaryColorMessage;
import projects.GreedyColoring.nodes.nodeImplementations.TreeNode;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.timers.Timer;

/**
 * Created by Roman_ on 03/05/2018.
 */
public class InitialTimer extends Timer{

    private TreeNode node;

    public InitialTimer(TreeNode tn){
        node = tn;
    }

    public void fire() {
        BinaryColorMessage msg = new BinaryColorMessage(node.binaryColorString);
        node.sendMessageToAllChildren(msg);
    }
}
