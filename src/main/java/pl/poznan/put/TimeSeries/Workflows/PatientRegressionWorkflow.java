package pl.poznan.put.TimeSeries.Workflows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import pl.poznan.put.TimeSeries.Classifying.Experiment;
import pl.poznan.put.TimeSeries.DataExporters.RegressionArffExporter;
import pl.poznan.put.TimeSeries.DataImporters.DataImporterBase;
import pl.poznan.put.TimeSeries.DataImporters.DataImporterCsv;
import pl.poznan.put.TimeSeries.DataImporters.PureDataImporter;
import pl.poznan.put.TimeSeries.DataProcessors.PatientDataDivider;
import pl.poznan.put.TimeSeries.Model.Patient;
import pl.poznan.put.TimeSeries.Model.UnifiedArffRow;
import pl.poznan.put.TimeSeries.Util.Configuration;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;

public class PatientRegressionWorkflow extends PatientWorkflowBase{

	public PatientRegressionWorkflow() {
		super();
	}

	@Override
	protected void processData() {
		PatientDataDivider patientDivider = new PatientDataDivider();
		List<UnifiedArffRow> rows = new ArrayList<UnifiedArffRow>();

		for (Patient patient : patients) {

			try {
				UnifiedArffRow arffRow = patientDivider
						.ComputeRegression(patient);
				rows.add(arffRow);
			} catch (Exception e) {
				System.out.println("patient missed");
				e.printStackTrace();
			}
		}
		Pair<List<UnifiedArffRow>, List<UnifiedArffRow>> pair = divideRowsToTrainAndTest(rows);
		List<UnifiedArffRow> trainSet = pair.getLeft();
		List<UnifiedArffRow> testSet = pair.getRight();
		
		ReportInputStatistics(trainSet, testSet);
		
		RegressionArffExporter exporter = new RegressionArffExporter("UnifiedData");
		try {
			exporter.saveUnifiedRecordsToArffData(trainSet, tempTrainPath);
			exporter.saveUnifiedRecordsToArffData(testSet, tempTestPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void runExperiment() {		
		try {
			double res = Experiment.runExperiment(classifier, tempTrainPath, tempTestPath);
			System.out.println("The result for " + this.getClass().getSimpleName() +" is: " + res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void reportResult() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void setTempPaths() {
		tempTrainPath = "output/tempRegressionArffTrain.arff";
		tempTestPath = "output/tempRegressionArffTest.arff";
		
	}

}
