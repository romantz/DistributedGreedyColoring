package projects.GreedyColoring.timers;

import projects.GreedyColoring.nodes.messages.BinaryColorMessage;
import projects.GreedyColoring.nodes.nodeImplementations.TreeNode;
import sinalgo.nodes.timers.Timer;

/**
 * A timer which is responsible of calculating a new color for the root node every round until
 * an 8-coloring is a achieved
 */
public class RootColorChoosingTimer extends Timer{
    // The node that started this timer
    private TreeNode node;

    public RootColorChoosingTimer(TreeNode tn){
        node = tn;
    }

    public void fire() {
        // Check if this is indeed the root
        if(node.parent == null) {
            // Check that an 8-coloring has not yet been achieved
            if(node.binaryColorString.length() > 3) {
                // recursive restart of the timer
                this.startRelative(1, node);

                // Choose k as 0
                int smallestDifferentIndex = 0;
                node.chooseNextColor(smallestDifferentIndex);

                // Send new chosen color to children
                BinaryColorMessage msg = new BinaryColorMessage(node.binaryColorString);
                node.sendMessageToAllChildren(msg);
            }
            else {
                node.startGreedyTimer();
            }
        }
    }
}
