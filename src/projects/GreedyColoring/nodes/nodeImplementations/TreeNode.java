package projects.GreedyColoring.nodes.nodeImplementations;


import projects.GreedyColoring.timers.GreedyColoringTimer;
import projects.GreedyColoring.timers.InitialTimer;
import projects.GreedyColoring.timers.RootColorChoosingTimer;
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

    // The mapping between binary representation and color
    public static HashMap<String, Color> stringToColor = new HashMap<String, Color>(){{
        put("000", new Color(255, 0, 0));
        put("001", new Color(255, 128, 0));
        put("010", new Color(255, 255, 0));
        put("011", new Color(50, 255, 0));
        put("100", new Color(0, 120, 200));
        put("101", new Color(40, 0, 200));
        put("110", new Color(100, 0, 130));
        put("111", new Color(200, 100, 200));
    }};

    // The length of the initial binary representation of the node's color
    public static final int BINARY_COLOR_STRING_INITIAL_LENGTH = 31;

    // The binary representation of the node's color
    public String binaryColorString = "";

    // The parent in the tree, null if this node is the root
    public TreeNode parent = null;

    // The integer representation of the node's color. Used in the last stage of the algorithm
    public int color = -1;

    /**
     * Color this node according to its binary color using the hash map stringToColor
     */
    public void colorNode(){
        Color color = stringToColor.get(binaryColorString);
        if (color != null)
            this.setColor(color);
    }

    /**
     * After an 8-coloring was achieved, a GreedyColoringTimer is started and sets off in X rounds
     * where X is the integer value of the node's color
     */
    public void startGreedyTimer(){
        color = Integer.parseInt(binaryColorString, 2);
        if(color != 0){
            GreedyColoringTimer gct = new GreedyColoringTimer(this);
            gct.startRelative(color, this);
        }
    }

    /**
     * calculate the new color of the node as a concatenation of
     * bin(smallestDifferentIndex) and binaryColorString[smallestDifferentIndex]
     *
     * @param smallestDifferentIndex The binaryColorString of this node and the parent node differ
     *                               in this index
     */
    public void chooseNextColor(int smallestDifferentIndex){
        int newDesiredLength = (int)Math.ceil(log2(binaryColorString.length()));
        String newBinaryColorString =
                convertIntToBinaryStringWithLength(smallestDifferentIndex, newDesiredLength) +
                        binaryColorString.charAt(smallestDifferentIndex);
        binaryColorString = newBinaryColorString;
        colorNode();
    }

    /**
     * Send the given message to all children of this node
     * @param msg The message to send
     */
    public void sendMessageToAllChildren(Message msg) {
        for(Edge e : outgoingConnections) {
            if(!e.endNode.equals(parent)) { // don't send it to the parent
                send(msg, e.endNode);
            }
        }
    }

    /**
     * Convert the given number to a binary string of the desired length
     * @param num The number to convert
     * @param desiredLength The desired length of the binary numbers
     * @return
     */
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

    /**
     * Calculate log base 2 of the given number
     * @param x The number to calculate log base 2 of
     * @return
     */
	private double log2(double x){
        return Math.log(x) / Math.log(2);
    }

	@Override
	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext()) {
            Message m = inbox.next();
            // We received the parent's color
            if (m instanceof BinaryColorMessage) {
                String data = ((BinaryColorMessage) m).data;
                // Check if 8-coloring has been achieved
                if(data.length() <= 3) {
                    startGreedyTimer();
                }
                else {
                    // Calculate an index where binaryColorString and parent.binaryColorString
                    // differ and then use this index to calculate the new color of this node
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
	    // On initialization, calculate the node's color based on its ID
        binaryColorString = convertIntToBinaryStringWithLength(ID, BINARY_COLOR_STRING_INITIAL_LENGTH);

        // Start an InitialTimer which is responsible of sending the first color message to children
        // a single round after this node was created
        InitialTimer it = new InitialTimer(this);
        it.startRelative(1, this);

        // Start a RootColorChoosingTimer which will be responsible for the coloring of the root node
        // since unlike all other nodes, the root node does not receive a color message from
        // its parent which is used as a trigger to calculate a new color
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
	    super.drawAsDisk(g, pt, highlight, 10);
	}

}
