package pl.poznan.put.TimeSeries.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import pl.poznan.put.TimeSeries.Constants.Limits;
import pl.poznan.put.TimeSeries.Constants.TimeLimit;
import pl.poznan.put.TimeSeries.Model.Characteristic;
import pl.poznan.put.TimeSeries.Model.Patient;

public class DataDivider {

	public static List<List<Characteristic>> dividePatientDataPeriodically(
			Patient patient) throws Exception {
		List<List<Characteristic>> listlist = new ArrayList<List<Characteristic>>();

		for (TimeLimit timeLimit : Limits.TimeLimits) {

			List<Characteristic> currList = new ArrayList<Characteristic>();

			DateTime lowerBound = ComputeTimeBound(timeLimit, patient, true);
			DateTime upperBound = ComputeTimeBound(timeLimit, patient, false);
			for (Characteristic characteristic : patient.getCharacteristics()) {
				DateTime time = characteristic.getExaminationTime();
				if (time.isAfter(lowerBound) && time.isBefore(upperBound)) {
					currList.add(characteristic);
				}
			}
			listlist.add(currList);
		}
		return listlist;
	}

	public static List<List<Characteristic>> dividePatientPeriodicallyThenRegularly(
			Patient patient, Integer parts) throws Exception {
		List<List<Characteristic>> listlist = dividePatientDataPeriodically(patient);
		List<List<Characteristic>> newlistlist = new ArrayList<List<Characteristic>>();
		for (List<Characteristic> list : listlist) {
			newlistlist.addAll(divideCollectionRegularly(list, parts));
		}
		return newlistlist;
	}

	public static <T> List<List<T>> divideCollectionRegularly(List<T> list,
			Integer parts) {
		parts = parts > list.size() ? list.size() : parts;

		int partLen = (int) Math.floor(list.size() / (double) parts);
		int additionals = list.size() % parts;
		List<List<T>> res = new ArrayList<List<T>>();
		Iterator<T> iterator = list.iterator();
		for (int i = 0; i < parts; i++) {
			List<T> currList = new ArrayList<T>();
			for (int j = 0; j < partLen; j++) {
				currList.add(iterator.next());
			}
			if (additionals-- > 0)
				currList.add(iterator.next());
			res.add(currList);
		}
		return res;
	}

	public static List<String> divideStringRegularly(String input, Integer parts) {
		List<String> res = new ArrayList<String>();

		int inputLen = input.length();
		int granula = (int) Math.ceil(inputLen / (float) parts);
		int i = 0;

		while (i < inputLen - granula) {
			res.add(input.substring(i, i + granula));
			i += granula;
		}

		if (i < inputLen) {
			res.add(input.substring(i, inputLen));
		}

		return res;
	}

	public static DateTime ComputeTimeBound(TimeLimit timeLimit,
			Patient patient, Boolean isLower) throws Exception {
		DateTime bound = null;

		char option = isLower ? timeLimit.getLowerOption() : timeLimit
				.getUpperOption();
		int mod = isLower ? timeLimit.getLowerMod() : timeLimit.getUpperMod();

		if (option == TimeLimit.Asleep)
			bound = patient.getAsleep();
		else if (option == TimeLimit.Awake)
			bound = patient.getAwake();

		if (bound == null) {
			throw new Exception("bound is null");
		}
		bound = bound.plusHours(mod);

		return bound;
	}
}