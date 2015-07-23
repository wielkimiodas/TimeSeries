package pl.poznan.put.TimeSeries.Workflows;

import pl.poznan.put.TimeSeries.Constants.DivisionOptions;
import pl.poznan.put.TimeSeries.DataExporters.OriginalSignalNormalizedArffBuilder;
import weka.core.Instances;

public class EamonnOriginalSignalNormalizedBySaxWorkflow extends WorkflowBase {

	public EamonnOriginalSignalNormalizedBySaxWorkflow(
			DivisionOptions divisionOption, boolean glaucoma) {
		super(divisionOption, glaucoma);
	}

	private OriginalSignalNormalizedArffBuilder exporter;

	@Override
	protected Instances buildInstances() {
		exporter = new OriginalSignalNormalizedArffBuilder(records);
		return exporter.buildInstances();
	}

	@Override
	protected void exportArff() throws Exception {
		exporter.saveArff(arffPath);
	}

	@Override
	protected void processData() throws Exception {
		// nothing to do here - passing clean input further

	}
}
