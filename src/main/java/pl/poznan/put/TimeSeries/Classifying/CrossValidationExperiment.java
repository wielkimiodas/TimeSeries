package pl.poznan.put.TimeSeries.Classifying;

import java.util.Random;

import pl.poznan.put.TimeSeries.Util.Config;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class CrossValidationExperiment extends ExperimentBase {

	private int folds = Config.getInstance().getCrossValidationFolds();

	public CrossValidationExperiment(Classifier classifier) {
		super(classifier);
	}

	public int getFolds() {
		return folds;
	}

	@Override
	public ExperimentResult runExperiment(Instances baseDataSet) throws Exception {
		// from
		// https://weka.wikispaces.com/Generating+classifier+evaluation+output+manually
		Evaluation eval = new Evaluation(baseDataSet);
		eval.crossValidateModel(classifier, baseDataSet, folds, new Random());
		return fillResult(eval);
	}

	@Override
	public ExperimentResult runFileExperiment(String pathToArff) throws Exception {
		Instances dataSet = Utils.readInstances(pathToArff);
		return runExperiment(dataSet);
	}

	public ExperimentResult runFileExperimentRepeatedly(String pathToArff, int times) throws Exception {
		Instances dataSet = Utils.readInstances(pathToArff);
		return runExperimentRepeatedly(dataSet, times);
	}

	public void setFolds(int folds) {
		this.folds = folds;
	}

	private ExperimentResult fillResult(Evaluation eval) throws Exception {
		double tp = 0, tn = 0, fp = 0, fn = 0;
		int numClasses = eval.getClassPriors().length;
		for (int i = 0; i < numClasses; i++) {
			tp += eval.numTruePositives(i);
			tn += eval.numTrueNegatives(i);
			fp += eval.numFalsePositives(i);
			fn += eval.numFalseNegatives(i);
		}

		double sensitivity = 0, specificity = 0, gMean = 0;

		if (numClasses == 2) {
			sensitivity = eval.weightedTruePositiveRate();
			specificity = eval.weightedTrueNegativeRate();
			gMean = Math.sqrt(specificity * sensitivity);
		}

		double accuracy = (tp + tn) / (tp + tn + fp + fn);

		double pr = eval.weightedPrecision();
		double rec = eval.weightedRecall();
		double f1 = 2 * ((pr * rec) / (pr + rec));
		double pcc = eval.correct() / eval.numInstances();
		ExperimentResult result = new ExperimentResult(accuracy, sensitivity, specificity, gMean, f1, pcc);
		return result;
	}
}
