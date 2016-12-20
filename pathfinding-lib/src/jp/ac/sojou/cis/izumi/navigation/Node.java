package jp.ac.sojou.cis.izumi.navigation;

import java.util.ArrayList;
import java.util.List;

/**
 * 経路区間を示すノードを表すクラスです。
 * 
 * @author Shinobu Izumi (Kyushu Institute of Technology)
 * 
 */
public class Node extends Element {
	private List<Link> neighborLinks;

	/** The position of this node */
	private Vector3 position;

	private Double lat;
	private Double lon;

	/** Creates a new instance of Node */
	public Node() {
		_construct(0, 0, 0);
	}

	/**
	 * Creates a new instance of Node
	 * 
	 * @param position
	 */
	public Node(Vector3 position) {
		_construct(position.x, position.y, position.z);
	}

	/**
	 * Creates a new instance of Node
	 * 
	 * @param x
	 * @param y
	 */
	public Node(double x, double y, double z) {
		_construct(x, y, z);
	}

	void _construct(double x, double y, double z) {
		this.setPosition(new Vector3(x, y, z));
		neighborLinks = new ArrayList<Link>();
	}

	/**
	 * Add new Link.
	 */
	public void addLink(Link newLink) {
		int index = getNeighborLinks().indexOf(newLink);
		if (index >= 0) {
			throw new RuntimeException("newLink already exist: " + newLink);
		} else {
			getNeighborLinks().add(newLink);
		}
	}

	/**
	 * @param index
	 * @param newLink
	 */
	public void addLink(int index, Link newLink) {
		int oldIndex = getNeighborLinks().indexOf(newLink);
		if (oldIndex >= 0) {
			throw new RuntimeException("newLink already exist: " + newLink);
		} else {
			getNeighborLinks().add(index, newLink);
		}
	}

	/**
	 * Returns the link element at the specified position in this list.
	 * 
	 * @param index
	 *            index of element to return.
	 * @return the link element at the specified position in this list.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index &lt; 0 || index &gt;=
	 *             size()).
	 */
	public Link getLink(int index) {
		return getNeighborLinks().get(index);
	}

	/**
	 * Remove Link.
	 */
	public boolean removeLink(Link newLink) {
		return getNeighborLinks().remove(newLink);
	}

	/**
	 * Add Links.
	 */
	public void addAllLinks(Node node) {
		getNeighborLinks().addAll(node.getNeighborLinks());
	}

	/**
	 * Add Links.
	 */
	public void addAllLinks(int index, Node node) {
		getNeighborLinks().addAll(index, node.getNeighborLinks());
	}

	public String toString() {
		return super.toString() + "[" + getId() + "] ";
	}

	public List<Link> getNeighborLinks() {
		return neighborLinks;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

}
