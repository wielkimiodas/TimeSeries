package pl.poznan.put.TimeSeries.DataExporters;

import java.util.List;
import java.util.stream.Collectors;

import pl.poznan.put.TimeSeries.Model.EamonnRecord;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class BasicSaxArffBuilder extends ArffExporterBase {

	private List<EamonnRecord> input;

	public BasicSaxArffBuilder(List<EamonnRecord> records) {
		this.input = records;
		destClasses = input.stream().map(x -> x.getDestinationClass())
				.distinct().collect(Collectors.toList());
		setAttributes();
	}

	@Override
	protected void setAttributes() {
		attrInfo = new FastVector();

		// needed only to pass null value - to mark the attribute as string attr

		FastVector attrVals = null;

		attrInfo.addElement(new Attribute("saxString", attrVals));

		Attribute destClassAttribute = null;
		try {
			destClassAttribute = constructDestinationClassesNominalAttribute(destClasses);
		} catch (Exception e) {
			e.printStackTrace();
		}
		attrInfo.addElement(destClassAttribute);
	}

	@Override
	public Instances buildInstances() {
		instances = new Instances("Basic ngrams", attrInfo, input.size());
		instances.setClassIndex(instances.numAttributes() - 1);

		int saxAttrId = 0;
		int destClassAttrId = 1;
		for (EamonnRecord row : input) {
			Instance instance = new Instance(attrInfo.size());
			instance.setDataset(instances);
			instance.setValue(saxAttrId, row.getSaxString());
			int destClassIndex = getIndexOfDestinationClass(row
					.getDestinationClass());

			instance.setValue(destClassAttrId, destClassIndex);
			instances.add(instance);
		}
		return instances;
	}

}