package jp.ac.sojou.cis.izumi.navigation.csv;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;

@CsvEntity(header = false)
public class LinkAttributeCSV {

	@CsvColumn(position = 0)
	public String id;
	@CsvColumn(position = 1)
	public String linkId;
	@CsvColumn(position = 2)
	public String length;
	@CsvColumn(position = 3)
	public String minWidth;
	@CsvColumn(position = 4)
	public String hasPedestrianRoad;
	@CsvColumn(position = 5)
	public String numPedstriansWeekday;
	@CsvColumn(position = 6)
	public String numPedstriansHoliday;
	@CsvColumn(position = 6)
	public String roofCoverage;
	@CsvColumn(position = 7)
	public String material;
	@CsvColumn(position = 8)
	public String numHoles700;
	@CsvColumn(position = 9)
	public String numHoles1400;
	@CsvColumn(position = 10)
	public String numHolesMax;
	@CsvColumn(position = 11)
	public String numGratings700;
	@CsvColumn(position = 12)
	public String numGratings1400;
	@CsvColumn(position = 13)
	public String numGratingsMax;
	@CsvColumn(position = 14)
	public String numStuddedPavings;
	@CsvColumn(position = 15)
	public String numLights700;
	@CsvColumn(position = 16)
	public String numLights1400;
	@CsvColumn(position = 17)
	public String numLightsMax;
	@CsvColumn(position = 18)
	public String numElectricityBoxes700;
	@CsvColumn(position = 19)
	public String numElectricityBoxes1400;
	@CsvColumn(position = 20)
	public String numElectricityBoxesMax;
	@CsvColumn(position = 21)
	public String numTrees700;
	@CsvColumn(position = 22)
	public String numTrees1400;
	@CsvColumn(position = 23)
	public String numTreesMax;
	@CsvColumn(position = 24)
	public String numMovableObstacles700;
	@CsvColumn(position = 25)
	public String numMovableObstacles1400;
	@CsvColumn(position = 27)
	public String numMovableObstaclesMax;
	@CsvColumn(position = 28)
	public String numStaticObstacles700;
	@CsvColumn(position = 29)
	public String numStaticObstacles1400;
	@CsvColumn(position = 30)
	public String numStaticObstaclesMax;
	@CsvColumn(position = 31)
	public String numPowerPoles700;
	@CsvColumn(position = 32)
	public String numPowerPoles1400;
	@CsvColumn(position = 33)
	public String numPowerPolesMax;
	@CsvColumn(position = 33)
	public String numSignPoles700;
	@CsvColumn(position = 34)
	public String numSignPoles1400;
	@CsvColumn(position = 35)
	public String numSignPolesMax;
	@CsvColumn(position = 37)
	public String numUnMaintenanced700;
	@CsvColumn(position = 38)
	public String numUnMaintenanced1400;
	@CsvColumn(position = 39)
	public String numUnMaintenancedMax;

	// // old things -these should be deleted soon
	// public String bicepsBrachii;
	// public String tricepsBrachii;
	// public String deltoidAnterior;
	// public String deltoidMiddle;
	// public String deltoidPosterior;
	// public String brachioradialis;

	@CsvColumn(position = 40)
	public String goProTime;

	@CsvColumn(position = 41)
	public String rightBicepsBrachii;
	@CsvColumn(position = 42)
	public String rightTricepsBrachii;
	@CsvColumn(position = 43)
	public String rightDeltoid;
	@CsvColumn(position = 44)
	public String rightBrachioradialis;
	@CsvColumn(position = 45)
	public String leftBicepsBrachii;
	@CsvColumn(position = 46)
	public String leftTricepsBrachii;
	@CsvColumn(position = 47)
	public String leftDeltoid;
	@CsvColumn(position = 48)
	public String leftBrachioradialis;

	// public String vibrationRate;

	// public String imageId;
	//
	// public String crossGrade;
	// public String crossDegree;
	// public String crossType;
	// public String inclineDirection;
	// public String carRode;
	// public String parkingOnStreet;
	// public String vibration;

}
