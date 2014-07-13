package pl.poznan.put.TimeSeries.Util;

import pl.poznan.put.TimeSeries.Model.RegressionResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class RegressionCalculator {

	public static RegressionResult ComputeRegression(List<Float> xArray, List<Float> yArray){
		
		if(xArray.size()!=yArray.size())
			throw new IllegalArgumentException();
		
		SimpleRegression regression = new SimpleRegression();
		for(int i=0;i<xArray.size();i++){
			regression.addData(xArray.get(i),yArray.get(i));
		}
		
		RegressionResult res = new RegressionResult(regression.getSlope(), regression.getIntercept());
		
		return res;
	}
	
	public static RegressionResult ComputeRegression(List<Float> yArray){
		
		List<Float>	xArray = new ArrayList<Float>();
		for(int i =0;i<yArray.size();i++)
			xArray.add((float) i);
		return ComputeRegression(xArray, yArray);
	}
	
	public static RegressionResult ComputeRegression(Double[] data){
		SimpleRegression regression = new SimpleRegression();
		for(int i =0;i<data.length;i++)
			regression.addData((double) i, data[i]); 
		RegressionResult res = new RegressionResult(regression.getSlope(), regression.getIntercept());
		return res;
	}
	
}
