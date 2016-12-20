package jp.ac.sojou.cis.izumi.navigation;

/**
 * 経路ネットワーク構成要素である事を示すクラスです。
 * 
 * @author Shinobu Izumi (Sojo University)
 */
public abstract class Element {

	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}