package pl.poznan.put.TimeSeries;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.poznan.put.TimeSeries.Classifying.Datasets;
import pl.poznan.put.TimeSeries.Classifying.Experiments;
import pl.poznan.put.TimeSeries.Util.Config;

public class GeneralExperimentTest {

	private static String folderPath;
	private static String path;

	@BeforeClass
	public static void beforeClass() throws Exception {
		path = Config.getInstance().getSingleDataPath();
		folderPath = Config.getInstance().getDataFolderPath();
		Config.getInstance().setSingleDataPath("SAMPLEDATASET");
		Config.getInstance().setDataFolderPath("testData/");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Config.getInstance().setSingleDataPath(path);
		Config.getInstance().setDataFolderPath(folderPath);
	}

	@Test
	public void directExperiments() {
		for (Experiments chosenExperiment : Experiments.values()) {
			try {
				Launcher.runExperiment(chosenExperiment, Datasets.SAMPLEUNITTEST);
			} catch (Exception e) {
				fail(e.getMessage());
			}
		}
	}

	@Test
	public void fileBasedExperiments() throws Exception {
		for (Experiments chosenExperiment : Experiments.values()) {
			String path = Launcher.exportArff(chosenExperiment, Datasets.SAMPLEUNITTEST);
			Launcher.processArff(chosenExperiment, path);
		}
	}
}
