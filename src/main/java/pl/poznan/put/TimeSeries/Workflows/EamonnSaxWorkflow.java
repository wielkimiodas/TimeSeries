package pl.poznan.put.TimeSeries.Workflows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import pl.poznan.put.TimeSeries.DataExporters.NewSaxArffBuilder;
import pl.poznan.put.TimeSeries.DataExporters.SaxArffExporter;
import pl.poznan.put.TimeSeries.DataProcessors.DataDivider;
import pl.poznan.put.TimeSeries.DataProcessors.NgramProcessor;
import pl.poznan.put.TimeSeries.Model.Patient;
import pl.poznan.put.TimeSeries.Model.SaxArffCandidateRow;
import pl.poznan.put.TimeSeries.Model.UnifiedRecord;
import pl.poznan.put.TimeSeries.Util.Configuration;
import weka.core.Instances;

public class EamonnSaxWorkflow extends EamonnWorkflowBase {

	@Override
	protected void processData() {
		SaxArffExporter translator = new SaxArffExporter("EamonnToSax");
		int attrLength = Integer.parseInt(Configuration
				.getProperty("ngramSize"));

		int windowLen = Integer
				.parseInt(Configuration.getProperty("ngramSize"));

		List<SaxArffCandidateRow> nestedList = new ArrayList<SaxArffCandidateRow>();

		for (UnifiedRecord record : records) {
			LinkedList<HashMap<String, AtomicInteger>> listHashMap = new LinkedList<HashMap<String, AtomicInteger>>();
			try {

				List<String> dividedSax = DataDivider.DivideStringRegularly(
						record.getSaxString(),
						regularPartsForDivision);

				for (String string : dividedSax) {
					HashMap<String, AtomicInteger> ngramCountMap = NgramProcessor
							.slashString(string, windowLen);
					listHashMap.add(ngramCountMap);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SaxArffCandidateRow row = new SaxArffCandidateRow(listHashMap, record.getDestinationClass());
			nestedList.add(row);
		}

		Instances insts = NewSaxArffBuilder.buildInstancesFromStats(nestedList);
		NewSaxArffBuilder.saveArff(insts,tempCVpath);
	}

	@Override
	protected void reportResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setTempPaths() {
		tempCVpath = "output/saxEamonn4p3g.arff";
		
	}

}
