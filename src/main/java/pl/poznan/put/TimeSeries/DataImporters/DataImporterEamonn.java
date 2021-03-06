package pl.poznan.put.TimeSeries.DataImporters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pl.poznan.put.TimeSeries.Model.EamonnRecord;
import pl.poznan.put.TimeSeries.Util.Config;
import pl.poznan.put.TimeSeries.Util.SaxPerformer;

public class DataImporterEamonn implements IDataImporter {

	private String folderPath;
	private List<EamonnRecord> records;

	public DataImporterEamonn(String path) {
		this.folderPath = path;
		records = new ArrayList<EamonnRecord>();
	}

	private void readData(String file) throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader(folderPath + file));
		StringTokenizer tokenizer;

		String currLine = br.readLine();

		while (currLine != null) {
			tokenizer = new StringTokenizer(currLine);
			double destClass = Double.parseDouble(tokenizer.nextToken());
			int valsCount = tokenizer.countTokens();
			List<Float> vals = new ArrayList<Float>();
			for (int i = 0; i < valsCount; i++) {
				vals.add(Float.parseFloat(tokenizer.nextToken()));
			}
			if(destClass==-1) 
				destClass = 2;
			EamonnRecord currRecord = new EamonnRecord(destClass, vals);
			records.add(currRecord);
			currLine = br.readLine();
		}
		br.close();
	}

	public List<EamonnRecord> importData() throws IOException {
		String datasetName = folderPath.substring(
				folderPath.lastIndexOf('/') + 1, folderPath.length());
		readData(String.format("/%s_TRAIN", datasetName));
		readData(String.format("/%s_TEST", datasetName));

		int alphabeatSize = Config.getInstance().getSaxAlphabeatSize();
		int outputLength = Config.getInstance().getSaxOutputLength();
		
		try {
			SaxPerformer.applyNormalizedSax(records, outputLength, alphabeatSize);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(String.format("Dataset %s - %d records.",
				datasetName, records.size()));
		return records;
	}

}
