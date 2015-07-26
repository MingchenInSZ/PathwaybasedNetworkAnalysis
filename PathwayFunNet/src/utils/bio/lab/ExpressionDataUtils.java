package utils.bio.lab;

import inoutput.bio.lab.ExpressionData;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;

import logging.bio.lab.LogUtils;
import serialization.bio.lab.Serialization;

public class ExpressionDataUtils {

	public void dumpExpressionData(String normFile, String tumFile) {
		ExpressionData ed = new ExpressionData();
		HashMap<String, List<Double>> norm = ed.readExpressionData(normFile);
		Serialization.save(norm, "normexp.out");
		HashMap<String, List<Double>> tum = ed.readExpressionData(tumFile);
		Serialization.save(tum, "tumexp.out");
		LogUtils.info("All dump done!" + new Time(System.currentTimeMillis()));
	}

	public static void main(String[] args) {
		ExpressionDataUtils edu = new ExpressionDataUtils();
		edu.dumpExpressionData("normexpress.txt", "tumexpress.txt");
	}
}
