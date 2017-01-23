/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package parsers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import persistentdatabase.model.Aneurysm;

public class CSVparser {
	
	private String csvFile;
	private Aneurysm aneurysm;
	
	public CSVparser(String filePath){
		csvFile = filePath;
		aneurysm = new Aneurysm();
	}
	
	public Aneurysm getAneurysm() { return aneurysm; }
	
	public void parse() throws IOException{
		File csvData = new File(csvFile);
		CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(),  CSVFormat.EXCEL);
		List<CSVRecord> records = parser.getRecords();
		CSVRecord record = records.get(1); // reading the second line that contains the information
		
		aneurysm.setAge(record.get(3));
		aneurysm.setAneurysmLocation(record.get(6));
		aneurysm.setAneurysmType(record.get(5));
		aneurysm.setId(record.get(0));
		aneurysm.setinstitution(record.get(1));
		aneurysm.setModality(record.get(2));
		aneurysm.setMultipleAneurysm(record.get(8));
		aneurysm.setRaptureStatus(record.get(7));
		aneurysm.setSex(record.get(4));
		
	}

}
