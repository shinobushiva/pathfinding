package jp.ac.sojou.cis.izumi.navigation.csv;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;

@CsvEntity(header = false)
public class LinkCSV {

	@CsvColumn(position = 0)
	public String id;

	@CsvColumn(position = 1)
	public String linkId;

}
