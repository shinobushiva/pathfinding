package jp.ac.sojou.cis.izumi.navigation;

/**
 * A class representing a link
 * 
 * @author Shinobu Izumi (Kyushu Institute of Technology)
 * 
 */
public class Link extends Element {

	private Node headNode;

	private Node tailNode;

	// Without head and tail
	private Vector3[] points;

	/** Creates a new instance of Link */
	public Link() {
		_Construct(null, null, new Vector3[0]);
	}

	/**
	 * Creates a new instance of Link
	 * 
	 * @param headNode
	 * @param tailNode
	 */
	public Link(Node headNode, Node tailNode) {
		_Construct(headNode, tailNode, new Vector3[0]);
	}

	private void _Construct(Node headNode, Node tailNode, Vector3[] content) {
		setPoints(new Vector3[(content != null ? content.length : 0)]);
		for (int i = 0; i < (content != null ? content.length : 0); i++) {
			// by copy
			getPoints()[i] = new Vector3(content[i].x, content[i].y,
					content[i].z);
		}

		setHeadNode(headNode);
		if (headNode != null) {
			headNode.addLink(this);
		}
		setTailNode(tailNode);
		if (tailNode != null) {
			tailNode.addLink(this);
		}
	}

	public Vector3 GetPoint(int index) {
		Vector3 ret;
		int beginIndex = (getHeadNode() != null ? 1 : 0);

		if (getHeadNode() != null && index == 0) {
			ret = getHeadNode().getPosition();
		} else if (getTailNode() != null
				&& index == getPoints().length + beginIndex) {
			ret = getTailNode().getPosition();
		} else {
			ret = getPoints()[index - beginIndex];
		}
		return ret;
	}

	public int GetPointNum() {
		return (getPoints() != null ? getPoints().length : 0)
				+ (getHeadNode() != null ? 1 : 0)
				+ (getTailNode() != null ? 1 : 0);
	}

	public String toString() {
		return super.toString() + "[" + id + "] : (head=" + getHeadNode().id
				+ ", tail=" + getTailNode().id + ")";

	}

	public Node getHeadNode() {
		return headNode;
	}

	public void setHeadNode(Node headNode) {
		this.headNode = headNode;
	}

	public Node getTailNode() {
		return tailNode;
	}

	public void setTailNode(Node tailNode) {
		this.tailNode = tailNode;
	}

	public Vector3[] getPoints() {
		return points;
	}

	public void setPoints(Vector3[] points) {
		this.points = points;
	}

}
