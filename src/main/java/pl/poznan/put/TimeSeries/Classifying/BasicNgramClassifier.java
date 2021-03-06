package pl.poznan.put.TimeSeries.Classifying;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.poznan.put.TimeSeries.Util.Config;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class BasicNgramClassifier extends Classifier {

	private static final long serialVersionUID = 8495869910146343292L;

	// works much better when ngram Size is about half of output length
	private static int ngramSize = Config.getInstance().getNgramSize();;

	private List<HashMap<String, Double>> ngramsInClasses;
	private Attribute saxAttribute = null;
	private int numClasses;

	@Override
	public void buildClassifier(Instances data) throws Exception {
		data.deleteWithMissingClass();

		numClasses = data.numClasses();
		ngramsInClasses = new ArrayList<HashMap<String, Double>>();
		int[] classInstancesCounter = new int[numClasses];

		for (int i = 0; i < numClasses; i++) {
			ngramsInClasses.add(new HashMap<String, Double>());
		}

		saxAttribute = data.attribute("saxString");
		if (saxAttribute == null)
			throw new Exception(
					"Instances object hasn't got the sax attribute.");

		for (int i = 0; i < data.numInstances(); i++) {
			Instance instance = data.instance(i);
			int classIndex = (int) instance.classValue();
			classInstancesCounter[classIndex]++;

			List<String> ngrams = createNgramList(instance
					.toString(saxAttribute));

			for (String ngram : ngrams) {
				HashMap<String, Double> currentMap = ngramsInClasses
						.get(classIndex);
				if (!currentMap.containsKey(ngram)) {
					currentMap.put(ngram, 1.0);
				} else {
					currentMap.put(ngram, currentMap.get(ngram) + 1);
				}
			}
		}

		for (int i = 0; i < numClasses; i++) {
			HashMap<String, Double> map = ngramsInClasses.get(i);
			for (String key : map.keySet()) {
				map.put(key, map.get(key) / (double) classInstancesCounter[i]);
			}
		}
	}

	private static HashMap<String, Integer> countNgrams(List<String> ngrams) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		for (String ngram : ngrams) {
			if (!map.containsKey(ngram)) {
				map.put(ngram, 1);
			} else {
				map.put(ngram, map.get(ngram) + 1);
			}
		}
		return map;
	}

	@Override
	public double classifyInstance(Instance instance) throws Exception {

		String saxValue = instance.toString(saxAttribute);

		List<String> patientNgrams = createNgramList(saxValue);
		HashMap<String, Integer> patientCountedNgrams = countNgrams(patientNgrams);

		double[] distances = new double[numClasses];

		for (String ngram : patientNgrams) {
			for (int i = 0; i < numClasses; i++) {
				double patternValue = 0;
				if (ngramsInClasses.get(i).containsKey(ngram)) {
					patternValue = ngramsInClasses.get(i).get(ngram);
				}
				double currNgramDist = patternValue
						- patientCountedNgrams.get(ngram);
				distances[i] += Math.pow(currNgramDist, 2);
			}
		}

		for (int i = 0; i < numClasses; i++) {
			distances[i] = Math.sqrt(distances[i]);
		}

		double minDist = Double.MAX_VALUE;
		int bestClassIndex = -1;

		for (int i = 0; i < numClasses; i++) {
			if (distances[i] < minDist) {
				minDist = distances[i];
				bestClassIndex = i;
			}
		}

		return bestClassIndex;
	}

	private static List<String> createNgramList(String stringToDivide) {
		List<String> ngrams = new ArrayList<String>();
		if (stringToDivide == null) {
			System.out.println("null in ngram creator");
			return new ArrayList<String>();
		}
		for (int i = 0; i < stringToDivide.length() - ngramSize + 1; i++) {
			ngrams.add(stringToDivide.substring(i, i + ngramSize));
		}
		return ngrams;
	}

}
