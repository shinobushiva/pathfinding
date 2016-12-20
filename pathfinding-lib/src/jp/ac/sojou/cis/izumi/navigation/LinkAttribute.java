package jp.ac.sojou.cis.izumi.navigation;

import com.orangesignal.csv.annotation.CsvEntity;

@CsvEntity(header = false)
public class LinkAttribute {

	public String id;

	public String linkId;
	public Double length;
	public Double minWidth;
	public Boolean hasPedestrianRoad;
	public Integer numPedstriansWeekday;
	public Integer numPedstriansHoliday;
	// public Boolean hasRoof;
	public Double roofCoverage;
	public Integer material;
	public Integer numHoles700;
	public Integer numHoles1400;
	public Integer numHolesMax;
	public Integer numGratings700;
	public Integer numGratings1400;
	public Integer numGratingsMax;
	public Integer numStuddedPavings;
	public Integer numLights700;
	public Integer numLights1400;
	public Integer numLightsMax;
	public Integer numElectricityBoxes700;
	public Integer numElectricityBoxes1400;
	public Integer numElectricityBoxesMax;
	public Integer numTrees700;
	public Integer numTrees1400;
	public Integer numTreesMax;
	public Integer numMovableObstacles700;
	public Integer numMovableObstacles1400;
	public Integer numMovableObstaclesMax;
	public Integer numStaticObstacles700;
	public Integer numStaticObstacles1400;
	public Integer numStaticObstaclesMax;
	public Integer numPowerPoles700;
	public Integer numPowerPoles1400;
	public Integer numPowerPolesMax;
	public Integer numSignPoles700;
	public Integer numSignPoles1400;
	public Integer numSignPolesMax;
	public Integer numUnMaintenanced700;
	public Integer numUnMaintenanced1400;
	public Integer numUnMaintenancedMax;

	// // old things -these should be deleted soon
	// public Double bicepsBrachii;
	// public Double tricepsBrachii;
	// public Double deltoidAnterior;
	// public Double deltoidMiddle;
	// public Double deltoidPosterior;
	// public Double brachioradialis;

	public Double rightBicepsBrachii;
	public Double rightTricepsBrachii;
	public Double rightDeltoid;
	public Double rightBrachioradialis;

	public Double leftBicepsBrachii;
	public Double leftTricepsBrachii;
	public Double leftDeltoid;
	public Double leftBrachioradialis;

	public Double goProTime;
	public Double vibrationRate;

	// public String imageId;
	//
	// public Boolean crossGrade;
	// public Double crossDegree;
	// public Integer crossType;
	// public Integer inclineDirection;
	// public Boolean carRode;
	// public Boolean parkingOnStreet;
	// public Boolean vibration;

}
