package pl.poznan.put.TimeSeries.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import pl.poznan.put.TimeSeries.Classifying.Datasets;
import pl.poznan.put.TimeSeries.Classifying.Experiments;

public class Config {

	private static Config _instance = null;
	private static Object mutex = new Object();

	static final String basePath = "src/main/resources/config.properties";
	static final String sparePath = "C:/TimeSeriesConfig/config.properties";
	


	public static Config getInstance() {
		if (_instance == null)
			synchronized (mutex) {
				if (_instance == null)
					_instance = new Config();
			}
		return _instance;
	}

	private static float getFloatProperty(String propertyName) {
		return Float.parseFloat(getProperty(propertyName));
	}

	private static int getIntProperty(String propertyName) {
		int res = -1;
		try {
			res = Integer.parseInt(getProperty(propertyName));
		} catch (Exception e) {
			System.out.println("Failed to import property '" + propertyName + "' from config file.");
		}
		return res;
	}

	private static String getProperty(String propertyName) {
		Properties prop = new Properties();
		InputStream input = null;
		String res = null;
		try {
			File f = new File(basePath);
			if(!f.exists() || f.isDirectory()){
				f = new File(sparePath);
			}
			
			if(!f.exists() || f.isDirectory())
				throw new FileNotFoundException("Config file doesnt exist in any valid location");
			
			input = new FileInputStream(f.getAbsolutePath());
			prop.load(input);
			res = prop.getProperty(propertyName);
		} catch (IOException ex) {
			System.out.println("Cannot get property: " + propertyName);
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	private float attributesToCutRatio = getFloatProperty("attributesToCutRatio");
	private int crossValidationFolds = getIntProperty("crossValidationFolds");
	private int crossValidationRepetitions = getIntProperty("crossValidationRepetitions");;
	private Datasets currentDataset;
	private Experiments currentExperiment;
	private String dataFolderPath = getProperty("dataFolderPath");
	private int divisionPartsAmount = getIntProperty("divisionPartsAmount");
	private int dtwSearchRadius = getIntProperty("dtwSearchRadius");
	private int experimentId = getIntProperty("experimentId");
	private String glaucomaDataSet = getProperty("glaucomaDataSet");
	private String inputArff = getProperty("inputArffPath");
	private boolean isFolderDataUsed = getIntProperty("isFolderDataUsed") == 1;
	private int k = getIntProperty("k");
	private int ngramSize = getIntProperty("ngramSize");
	private int saxAlphabeatSize = getIntProperty("saxAlphabeatSize");
	private int saxOutputLength = getIntProperty("saxOutputLength");
	private String singleDataPath;// = getProperty("singleDataPath");
	private int targetDataset = getIntProperty("targetDataset");
	private int variant = getIntProperty("variant");
	private String xlsReportPath = getProperty("xlsReportPath");

	private Config() {
	}

	public float getAttributesToCutRatio() {
		return attributesToCutRatio;
	}

	public int getCrossValidationFolds() {
		return crossValidationFolds;
	}

	public int getCrossValidationRepetitions() {
		return crossValidationRepetitions;
	}

	public Datasets getCurrentDataset() {
		return currentDataset;
	}

	public Experiments getCurrentExperiment() {
		return currentExperiment;
	}

	public String getDataFolderPath() {
		return dataFolderPath;
	}

	public int getDivisionPartsAmount() {
		return divisionPartsAmount;
	}

	public int getDtwSearchRadius() {
		return dtwSearchRadius;
	}

	public int getExperimentId() {
		return experimentId;
	}

	public String getGlaucomaDataSet() {
		return dataFolderPath + glaucomaDataSet;
	}

	public String getInputArff() {
		return inputArff;
	}

	public int getK() {
		return k;
	}

	public int getNgramSize() {
		return ngramSize;
	}

	public int getSaxAlphabeatSize() {
		return saxAlphabeatSize;
	}

	public int getSaxOutputLength() {
		return saxOutputLength;
	}

	public String getSingleDataPath() {
		return dataFolderPath + singleDataPath;
	}

	public int getTargetDataset() {
		return targetDataset;
	}

	public int getVariant() {
		return variant;
	}

	public String getXlsReportPath() {
		return xlsReportPath;
	}

	public boolean isFolderDataUsed() {
		return isFolderDataUsed;
	}

	public void setAttributesToCutRatio(float attributesToCutRatio) {
		this.attributesToCutRatio = attributesToCutRatio;
	}

	public void setCrossValidationFolds(int crossValidationFolds) {
		this.crossValidationFolds = crossValidationFolds;
	}

	public void setCrossValidationRepetitions(int crossValidationRepetitions) {
		this.crossValidationRepetitions = crossValidationRepetitions;
	}

	public void setCurrentDataset(Datasets currentDataset) {
		this.currentDataset = currentDataset;
	}

	public void setCurrentExperiment(Experiments currentExperiment) {
		this.currentExperiment = currentExperiment;
	}

	public void setDataFolderPath(String dataFolderPath) {
		this.dataFolderPath = dataFolderPath;
	}

	public void setDivisionPartsAmount(int divisionPartsAmount) {
		this.divisionPartsAmount = divisionPartsAmount;
	}

	public void setDtwSearchRadius(int dtwSearchRadius) {
		this.dtwSearchRadius = dtwSearchRadius;
	}

	public void setFolderDataUsed(boolean isFolderDataUsed) {
		this.isFolderDataUsed = isFolderDataUsed;
	}

	public void setK(int k) {
		this.k = k;
	}

	public void setNgramSize(int ngramSize) {
		this.ngramSize = ngramSize;
	}

	public void setSaxAlphabeatSize(int saxAlphabeatSize) {
		this.saxAlphabeatSize = saxAlphabeatSize;
	}

	public void setSaxOutputLength(int saxOutputLength) {
		this.saxOutputLength = saxOutputLength;
	}

	public void setSingleDataPath(String singleDataPath) {
		this.singleDataPath = singleDataPath;
	}

	public void setXlsReportPath(String xlsReportPath) {
		this.xlsReportPath = xlsReportPath;
	}

}
