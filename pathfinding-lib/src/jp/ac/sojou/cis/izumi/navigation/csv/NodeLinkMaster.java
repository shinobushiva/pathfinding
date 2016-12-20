package jp.ac.sojou.cis.izumi.navigation.csv;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.sojou.cis.izumi.navigation.AbstractPathPlanner;
import jp.ac.sojou.cis.izumi.navigation.Element;
import jp.ac.sojou.cis.izumi.navigation.Link;
import jp.ac.sojou.cis.izumi.navigation.LinkAttribute;
import jp.ac.sojou.cis.izumi.navigation.Node;
import jp.ac.sojou.cis.izumi.navigation.PhysicalBurdenPathPlanner;

import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.manager.CsvEntityManager;

public class NodeLinkMaster {

	public static void main(String[] args) {

		NodeLinkMaster master = NodeLinkMaster.get();
		Node s = master.getNodeMap().get("14");
		Node g = master.getNodeMap().get("154");

		AbstractPathPlanner dpp = new PhysicalBurdenPathPlanner();
		dpp.setUseDAG(true);
		dpp.search(s, g);
		List<Element> path = dpp.getPath();
		Collections.reverse(path);
		for (Element element : path) {
			System.out.println(element);
		}
	}

	private static NodeLinkMaster instance;

	private List<Element> routes;

	private Map<String, Node> nodeMap;

	private Map<String, Link> linkMap;

	private Map<Link, LinkAttribute> attrMap;

	private NodeLinkMaster() {
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static NodeLinkMaster get() {
		if (instance == null) {
			instance = new NodeLinkMaster();
		}
		return instance;
	}

	public LinkAttribute getAttribute(Link l) {
		return attrMap.get(l);
	}

	public LinkAttribute[] getAttributes() {
		return attrMap.values().toArray(new LinkAttribute[0]);
	}

	private void load() throws IOException {

		String folder = "data";

		routes = new ArrayList<Element>();

		CsvConfig conf = new CsvConfig();
		conf.setSkipLines(1);

		nodeMap = new HashMap<String, Node>();
		linkMap = new HashMap<String, Link>();

		List<NodeCSV> nodeList = new CsvEntityManager(conf).load(NodeCSV.class)
				.from(new FileReader(new File(folder + "/nodes.csv")));
		for (NodeCSV node : nodeList) {
			Node n = new Node(node.x, node.y, node.z);
			n.setLat(node.lat);
			n.setLon(node.lon);
			n.setId(node.nodeId);
			getNodeMap().put(node.nodeId, n);
			routes.add(n);
		}

		conf.setSkipLines(3);

		List<LinkCSV> linkList = new CsvEntityManager(conf).load(LinkCSV.class)
				.from(new FileReader(new File(folder + "/links.csv")));
		for (LinkCSV link : linkList) {
			String[] ids = link.linkId.split("-");
			Node n1 = getNodeMap().get(ids[0]);
			Node n2 = getNodeMap().get(ids[1]);
			Link l = new Link(n1, n2);
			l.setId(link.linkId);
			getLinkMap().put(link.linkId, l);
			routes.add(l);
		}

		attrMap = new HashMap<Link, LinkAttribute>();

		LinkAttributeCSV prev = null;

		List<LinkAttributeCSV> attrList = new CsvEntityManager(conf).load(
				LinkAttributeCSV.class).from(
				new FileReader(new File("data/links.csv")));
		for (LinkAttributeCSV la : attrList) {

			if (prev != null) {
				Field[] fields = LinkAttributeCSV.class.getFields();
				for (Field field : fields) {
					field.setAccessible(true);
					try {
						String val = (String) field.get(la);
						String valPrev = (String) field.get(prev);
						if (val.trim().isEmpty()) {
							field.set(la, valPrev);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			prev = la;

			// System.out.println(la.linkId);
			LinkAttribute a = new LinkAttribute();
			attrMap.put(getLinkMap().get(la.linkId), a);

			a.id = la.id;
			a.linkId = la.linkId;
			a.length = toD(la.length);
			a.minWidth = toD(la.minWidth);
			a.hasPedestrianRoad = toB(la.hasPedestrianRoad);
			a.numPedstriansWeekday = toI(la.numPedstriansWeekday);
			a.numPedstriansHoliday = toI(la.numPedstriansHoliday);
			a.roofCoverage = toD(la.roofCoverage);
			a.material = toI(la.material);
			a.numHoles700 = toI(la.numHoles700);
			a.numHoles1400 = toI(la.numHoles1400);
			a.numHolesMax = toI(la.numHolesMax);
			a.numGratings700 = toI(la.numGratings700);
			a.numGratings1400 = toI(la.numGratings1400);
			a.numGratingsMax = toI(la.numGratingsMax);
			a.numStuddedPavings = toI(la.numStuddedPavings);
			a.numLights700 = toI(la.numLights700);
			a.numLights1400 = toI(la.numLights1400);
			a.numLightsMax = toI(la.numLightsMax);
			a.numElectricityBoxes700 = toI(la.numElectricityBoxes700);
			a.numElectricityBoxes1400 = toI(la.numElectricityBoxes1400);
			a.numElectricityBoxesMax = toI(la.numElectricityBoxesMax);
			a.numTrees700 = toI(la.numTrees700);
			a.numTrees1400 = toI(la.numTrees1400);
			a.numTreesMax = toI(la.numTreesMax);
			a.numMovableObstacles700 = toI(la.numMovableObstacles700);
			a.numMovableObstacles1400 = toI(la.numMovableObstacles1400);
			a.numMovableObstaclesMax = toI(la.numMovableObstaclesMax);
			a.numStaticObstacles700 = toI(la.numStaticObstacles700);
			a.numStaticObstacles1400 = toI(la.numStaticObstacles1400);
			a.numStaticObstaclesMax = toI(la.numStaticObstaclesMax);
			a.numPowerPoles700 = toI(la.numPowerPoles700);
			a.numPowerPoles1400 = toI(la.numPowerPoles1400);
			a.numPowerPolesMax = toI(la.numPowerPolesMax);
			a.numSignPoles700 = toI(la.numSignPoles700);
			a.numSignPoles1400 = toI(la.numSignPoles1400);
			a.numSignPolesMax = toI(la.numSignPolesMax);
			a.numUnMaintenanced700 = toI(la.numUnMaintenanced700);
			a.numUnMaintenanced1400 = toI(la.numUnMaintenanced1400);
			a.numUnMaintenancedMax = toI(la.numUnMaintenancedMax);

			a.rightBicepsBrachii = toD(la.rightBicepsBrachii);
			a.rightTricepsBrachii = toD(la.rightTricepsBrachii);
			a.rightDeltoid = toD(la.rightDeltoid);
			a.rightBrachioradialis = toD(la.rightBrachioradialis);
			a.leftBicepsBrachii = toD(la.leftBicepsBrachii);
			a.leftTricepsBrachii = toD(la.leftTricepsBrachii);
			a.leftDeltoid = toD(la.leftDeltoid);
			a.leftBrachioradialis = toD(la.leftBrachioradialis);

			a.goProTime = toD(la.goProTime);
			// System.out.println(a);
			// a.vibrationRate = toD(la.vi)
		}

	}

	private static Integer toI(String f) {
		if (f.trim().isEmpty())
			return 0;

		int result = 0;
		try {
			Integer.parseInt(f);
		} catch (Exception e) {
			return Integer.MIN_VALUE;
		}
		return result;
	}

	private static Double toD(String f) {
		if (f.trim().isEmpty())
			return 0.0;

		Double result = 0.0;
		try {
			result = Double.parseDouble(f);
		} catch (Exception e) {
			return Double.MIN_VALUE;
		}
		return result;
	}

	private static Boolean toB(String f) {
		if (f.trim().isEmpty())
			return false;

		Boolean result = false;
		try {
			return Integer.parseInt(f) != 0;
		} catch (Exception e) {
		}
		return result;
	}

	public Node getNode(String id) {
		return nodeMap.get(id);
	}

	public Map<String, Node> getNodeMap() {
		return nodeMap;
	}

	public Map<String, Link> getLinkMap() {
		return linkMap;
	}

	public Node getClosest(final Double lat, final Double lon) {

		ArrayList<Node> list = new ArrayList<Node>(nodeMap.values());
		Collections.sort(list, new Comparator<Node>() {

			@Override
			public int compare(Node n1, Node n2) {
				Double lat1 = n1.getLat();
				Double lon1 = n1.getLon();

				Double lat2 = n2.getLat();
				Double lon2 = n2.getLon();

				double d1 = Point.distance(lat1, lon1, lat, lon);
				double d2 = Point.distance(lat2, lon2, lat, lon);
				if (d1 > d2)
					return 1;
				else if (d2 > d1)
					return -1;
				else
					return 0;

			}
		});

		return list.get(0);
	}
}
