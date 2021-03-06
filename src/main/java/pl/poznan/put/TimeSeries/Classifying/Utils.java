package pl.poznan.put.TimeSeries.Classifying;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import weka.core.Instances;

public class Utils {

	public static Instances readInstances(String path) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		Instances instances = new Instances(reader);
		instances.setClassIndex(instances.numAttributes()-1);
		reader.close();
		return instances;
	}

	public static Pair<Instances, Instances> divideInstances(
			Instances instances, double trainToTestRatio) {

		instances.randomize(new Random());
		int limit = (int) ((1 - trainToTestRatio) * (double) instances
				.numInstances());
		Instances trainSet = new Instances(instances, 0, limit);
		Instances testSet = new Instances(instances, limit,
				instances.numInstances() - limit);

		Pair<Instances, Instances> res = new ImmutablePair<Instances, Instances>(
				trainSet, testSet);
		return res;
	}

}
