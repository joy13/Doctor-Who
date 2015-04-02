import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import utils.DiagnosisDecisionAlgorithm;
import utils.SQLiteJDBC;


public class UnitTest {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		SQLiteJDBC sql = new SQLiteJDBC();
//		Integer[] syms = {27,80}; 
//		HashMap<Integer, Integer[]> mp = sql.getDiagnosisSymptomMap();
//		Iterator it = mp.entrySet().iterator();
//	    while (it.hasNext()) {
//	        Map.Entry pair = (Map.Entry)it.next();
//	        System.out.println("Diag "+pair.getKey());
//	        Integer[] ar = (Integer[])pair.getValue(); 
//	        for(int i=0;i<ar.length;i++)
//	        {
//	        	System.out.println(ar[i]);
//	        }
//	        it.remove(); // avoids a ConcurrentModificationException
//	    }
		/*
		DiagnosisDecisionAlgorithm algo = new DiagnosisDecisionAlgorithm();
		String[] inputSymptoms = {"hiccup","cough","headache","watery eyes","nasal congestion","sore throat","sneeze","mouth breathing"};
		Integer[] inputSymptomIds = sql.getSymptomIdsFromNames(inputSymptoms);
		HashMap<String, Float> map = algo.getScoredDiagnosis(inputSymptoms);
		Iterator it = map.entrySet().iterator();
		System.out.println("Diagnosis");
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey()+ " score: " + pair.getValue());
	    }*/
		
	}

}
