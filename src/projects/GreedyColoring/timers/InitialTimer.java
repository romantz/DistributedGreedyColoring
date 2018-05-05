package projects.GreedyColoring.timers;

import projects.GreedyColoring.nodes.messages.BinaryColorMessage;
import projects.GreedyColoring.nodes.nodeImplementations.TreeNode;
import sinalgo.nodes.timers.Timer;

/**
 * A timer which is responsible of sending the color of the given node to its children
 * on the first round after the node has been created
 */
public class InitialTimer extends Timer{
    // The node that started this timer
    private TreeNode node;

    public InitialTimer(TreeNode tn){
        node = tn;
    }

    public void fire() {
        BinaryColorMessage msg = new BinaryColorMessage(node.binaryColorString);
        node.sendMessageToAllChildren(msg);
    }
}
