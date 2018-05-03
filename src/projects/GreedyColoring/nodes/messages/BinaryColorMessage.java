package projects.GreedyColoring.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * A containing the binary representation of the nodes color
 */
public class BinaryColorMessage extends Message {

	/**
	 * The payload of the BinaryColorMessage.
	 */
	public String data;

	/**
	 * Constructs a new Message of type BinaryColorMessage.
	 *
	 * @param data
	 */
	public BinaryColorMessage(String data) {
		this.data = data;
	}

	@Override
	public Message clone() {
		return new BinaryColorMessage(data);
	}

}
