package pl.poznan.put.TimeSeries.Workflows;

import pl.poznan.put.TimeSeries.Util.Configuration;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;

public abstract class WorkflowBase {

	protected Classifier classifier = new JRip();
	
	protected int regularPartsForDivision = Integer.parseInt(Configuration.getProperty("regularPartsForDivision"));

	protected String tempTrainPath;
	protected String tempTestPath;

	protected abstract void importData();

	protected abstract void processData();

	protected abstract void runExperiment();

	protected abstract void reportResult();

	protected abstract void setTempPaths();

	public WorkflowBase() {
		super();
		setTempPaths();
	}

	public void runWorkflow() {
		System.out.println("Workflow has started.");
		importData();
		processData();
		runExperiment();
		reportResult();
		System.out.println("Workflow has ended.");
	}

}
