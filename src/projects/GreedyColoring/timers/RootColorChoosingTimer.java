package projects.GreedyColoring.timers;

import projects.GreedyColoring.nodes.messages.BinaryColorMessage;
import projects.GreedyColoring.nodes.nodeImplementations.TreeNode;
import sinalgo.nodes.timers.Timer;

/**
 * Created by Roman_ on 03/05/2018.
 */
public class RootColorChoosingTimer extends Timer{

    private TreeNode node;

    public RootColorChoosingTimer(TreeNode tn){
        node = tn;
    }

    public void fire() {
        if(node.parent == null) {
            if(node.binaryColorString.length() > 3) {
                this.startRelative(1, node); // recursive restart of the timer
                int smallestDifferentIndex = 0;
                node.chooseNextColor(smallestDifferentIndex);
                BinaryColorMessage msg = new BinaryColorMessage(node.binaryColorString);
                node.sendMessageToAllChildren(msg);
            }
            else {

            }
        }
    }
}
