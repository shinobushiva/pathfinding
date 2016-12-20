package jp.ac.sojou.cis.izumi.navigation;

import jp.ac.sojou.cis.izumi.navigation.csv.NodeLinkMaster;

/**
 * PathPlanner implementation to calculate physically shortest path
 * 
 * @author Shinobu Izumi (Kyushu Institute of Technology)
 */
public class PhysicalBurdenPathPlanner extends AbstractPathPlanner {
	/**
	 * Calculate a link logical length
	 * 
	 * @return link logical length
	 */
	@Override
	public Double calcLinkLength(Link l, Node baseNode, Node targetNode,
			Double physicalLength) {

		LinkAttribute la = NodeLinkMaster.get().getAttribute(l);

		Double f = calc(la);
		if (f <= 0)
			return physicalLength;
		else
			return physicalLength * f;

	}

	public static Double calc(LinkAttribute la) {
		if (la == null)
			return 0.0;

		Double f = 0 + la.rightBrachioradialis + la.rightBicepsBrachii
				+ la.rightTricepsBrachii + la.rightDeltoid
				+ la.leftBrachioradialis + la.leftBicepsBrachii
				+ la.leftTricepsBrachii + la.leftDeltoid;
		f = f / 10262.46f;

		return f;
	}

	public PhysicalBurdenPathPlanner() {
		// LinkAttribute[] las = NodeLinkMaster.get().getAttributes();
		//
		// for (LinkAttribute la : las) {
		// Double f = calc(la);
		// }

		divByNum = false;
	}
}
