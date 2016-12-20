package jp.ac.sojou.cis.izumi.navigation.csv;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;

@CsvEntity(header = false)
public class NodeCSV {

	@CsvColumn(position = 0)
	public String nodeId;

	@CsvColumn(position = 1)
	public Double x;

	@CsvColumn(position = 2)
	public Double y;

	@CsvColumn(position = 3)
	public Double z;
	
	@CsvColumn(position = 4)
	public Double lat;
	
	@CsvColumn(position = 5)
	public Double lon;

}
