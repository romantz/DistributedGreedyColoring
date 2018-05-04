package projects.GreedyColoring.nodes.nodeImplementations;


import projects.GreedyColoring.timers.GreedyColoringTimer;
import projects.GreedyColoring.timers.InitialTimer;
import projects.GreedyColoring.timers.PrintTimer;
import projects.GreedyColoring.timers.RootColorChoosingTimer;
import projects.defaultProject.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import projects.GreedyColoring.nodes.messages.BinaryColorMessage;
import sinalgo.nodes.messages.Message;

import java.awt.*;
import java.util.HashMap;

/**
 * An internal node (or leaf node) of the tree.
 */
public class TreeNode extends Node {

    public static HashMap<String, Color> stringToColor = new HashMap<String, Color>();

	public TreeNode parent = null; // the parent in the tree, null if this node is the root
    private int BINARY_COLOR_STRING_INITIAL_LENGTH = 31;
    public String binaryColorString = "";
    public int color = -1;

    public void colorNode(){
        Color color = stringToColor.get(binaryColorString);
        if (color != null)
            this.setColor(color);
    }

    public void startGreedyTimer(){
        color = Integer.parseInt(binaryColorString, 2);
        if(color != 0){
            GreedyColoringTimer gct = new GreedyColoringTimer(this);
            gct.startRelative(color, this);
        }
    }

    public void chooseNextColor(int smallestDifferentIndex){
        int newDesiredLength = (int)Math.ceil(log2(binaryColorString.length()));
        String newBinaryColorString =
                convertIntToBinaryStringWithLength(smallestDifferentIndex, newDesiredLength) +
                        binaryColorString.charAt(smallestDifferentIndex);
        binaryColorString = newBinaryColorString;
        colorNode();
    }

    public void sendMessageToAllChildren(Message msg) {
        for(Edge e : outgoingConnections) {
            if(!e.endNode.equals(parent)) { // don't send it to the parent
                send(msg, e.endNode);
            }
        }
    }

    public String convertIntToBinaryStringWithLength(int num, int desiredLength) {
        StringBuilder sb = new StringBuilder();
        String binNum = Integer.toBinaryString(num);
        for(int i = binNum.length(); i < desiredLength; i++)
            sb.append("0");
        sb.append(binNum);
        return sb.toString();
    }

	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}

	private double log2(double x){
        return Math.log(x) / Math.log(2);
    }

	@Override
	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext()) {
            Message m = inbox.next();
            if (m instanceof BinaryColorMessage) {
                String data = ((BinaryColorMessage) m).data;
                if(data.length() <= 3) {
                    startGreedyTimer();
                }
                else {
                    int smallestDifferentIndex = -1;
                    for(int i = 0; i < binaryColorString.length(); i++) {
                        if(smallestDifferentIndex == -1 &&
                                binaryColorString.charAt(i) !=
                                        data.charAt(i))
                            smallestDifferentIndex = i;
                    }
                    chooseNextColor(smallestDifferentIndex);
                    BinaryColorMessage msg = new BinaryColorMessage(binaryColorString);
                    sendMessageToAllChildren(msg);
                }
            }
        }
	}

	@Override
	public void init() {
        binaryColorString = convertIntToBinaryStringWithLength(ID, BINARY_COLOR_STRING_INITIAL_LENGTH);
        InitialTimer it = new InitialTimer(this);
        it.startRelative(1, this);

        RootColorChoosingTimer rcct = new RootColorChoosingTimer(this);
        rcct.startRelative(2, this);
	}

	@Override
	public void neighborhoodChange() {
	}

	@Override
	public void preStep() {
	}

	@Override
	public void postStep() {
	}

	public void draw(Graphics g, PositionTransformation pt, boolean highlight){
		super.drawAsDisk(g, pt, highlight, 12);
	}

	@NodePopupMethod(menuText = "Color children")
	public void colorKids() {
		BinaryColorMessage msg = new BinaryColorMessage(binaryColorString);
		MessageTimer timer = new MessageTimer(msg);
		timer.startRelative(1, this);
	}

}
