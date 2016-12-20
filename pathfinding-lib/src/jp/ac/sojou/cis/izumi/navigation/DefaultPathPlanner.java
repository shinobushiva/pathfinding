package jp.ac.sojou.cis.izumi.navigation;

/**
 * PathPlanner implementation to culculate physically shortest path
 * 
 * @author Shinobu Izumi (Kyushu Institute of Technology)
 */
public class DefaultPathPlanner extends AbstractPathPlanner {

	/**
	 * Culculate a link logical length
	 * 
	 * @return link logical length
	 */
	@Override
	public Double calcLinkLength(Link l, Node baseNode, Node targetNode,
			Double physicalLength) {
		return physicalLength;
	}

	public DefaultPathPlanner() {
		divByNum = false;
	}

}