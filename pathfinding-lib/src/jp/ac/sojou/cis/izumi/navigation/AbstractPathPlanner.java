package jp.ac.sojou.cis.izumi.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class that culculate a shortest path by Dijestra algorithem.<br>
 * <br>
 * You must specify the way to culculate logical distance for a link
 * 
 * @author Shinobu Izumi (Kyushu Institute of Technology)
 * 
 */
public abstract class AbstractPathPlanner {

	/**
	 * Holds the shortest path distance to the goal node
	 */
	private Double minDistance;

	/**
	 * Holds the start node
	 */
	private Node start;

	/**
	 * Holds the goal node
	 */
	private Node goal;

	/**
	 * Holds the logically shortest path from start to goal node
	 */
	private List<Element> path = new ArrayList<Element>();

	/**
	 * Holds nodes that has been fixed the distance from the start node.
	 */
	private List<Node> visited = new ArrayList<Node>();

	/**
	 * Holds the previous node for each nodes
	 */
	private Map<Node, Node> previousMap = new HashMap<Node, Node>();

	/**
	 * Hold distances for each nodes
	 */
	private Map<Node, Double> distMap = new HashMap<Node, Double>();

	/**
	 * Holds links to each nodes
	 */
	private Map<Node, Link> nodeLinkMap = new HashMap<Node, Link>();

	/**
	 * Holds physical distance to each links
	 */
	private Map<Link, Double> physicalDistMap = new HashMap<Link, Double>();

	/**
	 * Holds logical distance to each links
	 */
	private Map<Link, Double> logicalDistMap = new HashMap<Link, Double>();

	/**
	 * Holds candidate nodes
	 */
	private List<Node> candidateNodes = new ArrayList<Node>();
	private Double physicalDistance = 0.0;

	/**
	 * Holds reachable paths from start to goal nodes
	 * 
	 * TODO: Currently this in not used. It may be used when we want to have
	 * more than one path in the future.
	 */
	// private List<Object> paths = new List<Object>();

	/**
	 * Holds if it divides the distance by link number
	 */
	protected boolean divByNum = false;

	/**
	 * Hold if it uses the directed graph.
	 * 
	 * The direction of the graph is from HeadNode to TailNode
	 */
	protected boolean useDAG = false;

	/**
	 * Get the node at the end or this link
	 * 
	 * @param l
	 *            The link
	 * @param n
	 *            Current node
	 * @return Next node (if not return null)
	 */
	private Node getNextNode(Link l, Node n) {

		Node hn = l.getHeadNode();
		Node tn = l.getTailNode();
		if (hn == null || tn == null) {
			return null;
		}
		if (hn == n) {
			return l.getTailNode();
		} else {
			return l.getHeadNode();
		}
	}

	/**
	 * Run the path finding
	 * 
	 * @param s
	 *            Start node
	 * @param g
	 *            Goal Node
	 * 
	 * @return If it can not find the shortest path then return false
	 */
	public boolean search(Node s, Node g) {
		// init
		visited.clear();
		previousMap.clear();
		distMap.clear();
		nodeLinkMap.clear();
		physicalDistMap.clear();
		logicalDistMap.clear();
		path.clear();
		candidateNodes.clear();
		minDistance = 0.0;
		physicalDistance = 0.0;

		if (s == null || g == null) {
			return false;
		}

		start = s;
		goal = g;

		Node next = start;
		distMap.put(next, 0.0);

		Node n = null;
		Double min = Double.POSITIVE_INFINITY;

		do {
			n = next;
			visited.add(n);
			min = Double.POSITIVE_INFINITY;

			// Collects candidate nodes
			// nodeLinkMap.clear();
			for (Link l : n.getNeighborLinks()) {
				// for directed graph
				if (useDAG && n != l.getHeadNode()) {
					continue;
				}
				Node nn = getNextNode(l, n);
				if (visited.contains(nn)) {
					continue;
				}
				if (!nodeLinkMap.containsKey(nn)) {
					nodeLinkMap.put(nn, l);
				} else {
					Double distN = distMap.get(n);
					// Get the logial distance
					Double ll1 = _calcLinkLength(l, n, nn,
							calcPhysicalLinkLength(l)) + distN;
					Double ll2 = Double.POSITIVE_INFINITY;
					if (distMap.containsKey(nn)) {
						ll2 = distMap.get(nn);
					}

					if (ll1 < ll2) {
						nodeLinkMap.put(nn, l);
					}
				}
				if (!candidateNodes.contains(nn)) {
					candidateNodes.add(nn);
				}
			}
			for (Node node : candidateNodes) {
				// Skip if the node is already fixed
				if (visited.contains(node)) {
					continue;
				}

				Link l = nodeLinkMap.get(node);

				// Get the physical distance
				Double pl = calcPhysicalLinkLength(l);
				// Get the logical Distance
				Double ll = _calcLinkLength(l, n, node, pl);

				// update the shortest path if needed
				Double distI = Double.POSITIVE_INFINITY;
				Double distJ = Double.POSITIVE_INFINITY;
				if (distMap.containsKey(n)) {
					distI = distMap.get(n);
				}
				if (distMap.containsKey(node)) {
					distJ = distMap.get(node);
				}
				if (distI + ll < distJ) {
					distJ = distI + ll;
					distMap.put(node, distJ);
					previousMap.put(node, n);
				}
				// holds the next node that can be used in the shortest path
				if (distJ < min) {
					min = distJ;
					next = node;
				}
			}
			candidateNodes.remove(next);
			// Routes for all nodes are fixed
		} while (min < Double.POSITIVE_INFINITY);

		path = new ArrayList<Element>();
		n = goal;
		path.add(n);

		int count = 0;
		while (previousMap.containsKey(n)) {
			Link link = nodeLinkMap.get(n);
			path.add(link);
			Node pre = previousMap.get(n);
			path.add(pre);
			n = pre;

			minDistance += logicalDistMap.get(link);
			physicalDistance += physicalDistMap.get(link);

			count++;
		}
		if (n != start) {
			return false;
		}

		if (divByNum) {
			minDistance /= count;
		}

		return true;
	}

	/**
	 * Get the list of Elements of the shortest path
	 * 
	 * @return if it's not found then null
	 */
	public List<Element> getPath() {
		return path;
	}

	/**
	 * Get the shortest path's distance from start to goal node
	 * 
	 * @return if it's not found then - POSITIVE_INFINITY
	 */
	public Double getDistance() {
		return minDistance;
	}

	/**
	 * Get the shortest path's physical distance from start to goal node
	 * 
	 * @return if it's not found then - POSITIVE_INFINITY
	 */
	public Double getPhysicalDistance() {
		return physicalDistance;
	}

	/**
	 * Calculate the link length<br>
	 * <br>
	 * This method is called in path finding method
	 * 
	 * @param l
	 *            target link
	 * @param baseNode
	 *            begin node
	 * @param targetNode
	 *            end node
	 * @param phisicalLength
	 *            physical distance
	 * 
	 * @return distance
	 */
	public abstract Double calcLinkLength(Link l, Node baseNode,
			Node targetNode, Double phisicalLength);

	/**
	 * Calculate the link length。
	 * 
	 * @param l
	 *            target link
	 * @param baseNode
	 * @param targetNode
	 * @param phisicalLength
	 * 
	 * @return distance
	 */
	private Double _calcLinkLength(Link l, Node baseNode, Node targetNode,
			Double phisicalLength) {
		if (logicalDistMap.containsKey(l)) {
			return logicalDistMap.get(l);
		} else {
			Double d = calcLinkLength(l, baseNode, targetNode, phisicalLength);
			logicalDistMap.put(l, d);
			return d;
		}

	}

	/**
	 * Caluculate the physical distance of the given link
	 * 
	 * @return Euclid distance of the link
	 */
	private Double calcPhysicalLinkLength(Link l) {
		if (physicalDistMap.containsKey(l)) {

		} else {

			Vector3 p1;
			Vector3 p2;

			Double dist = 0.0;
			for (int i = 0; i < l.GetPointNum() - 1; i++) {
				p1 = l.GetPoint(i);
				p2 = l.GetPoint(i + 1);
				dist += Vector3.distance(p1, p2);
			}
			physicalDistMap.put(l, dist);
		}
		return physicalDistMap.get(l);
	}

	/**
	 * Get physical positions of nodes that are contained in the found path
	 * 
	 * @return list of nodes' position
	 */
	public Vector3[] getPathPoints() {
		Node node = null;
		Link link;

		List<Element> vec = getPath();
		List<Vector3> result = new ArrayList<Vector3>();

		Vector3[] p3ds;

		for (Element o : vec) {
			if (o instanceof Node) {
				node = (Node) o;
				result.add(node.getPosition());
			} else {
				link = (Link) o;
				p3ds = link.getPoints();
				if (link.getHeadNode() == node) {
					result.addAll(Arrays.asList(p3ds));
				} else {
					for (int i = p3ds.length; i > 0; i--) {
						result.add(p3ds[i - 1]);
					}
				}
			}
		}

		return result.toArray(new Vector3[0]);
	}

	public boolean isDivByNum() {
		return divByNum;
	}

	public void setDivByNum(boolean divByNum) {
		this.divByNum = divByNum;
	}

	public boolean isUseDAG() {
		return useDAG;
	}

	public void setUseDAG(boolean useDAG) {
		this.useDAG = useDAG;
	}

}