package utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import utils.PlayUtilities;
import utils.SQLiteJDBC;

public class DiagnosisDecisionAlgorithm {
	
	public static HashMap<String,Float> getScoredDiagnosis(Integer[] inputSymptoms) throws SQLException, ClassNotFoundException {
		SQLiteJDBC sqliteJDBC = new SQLiteJDBC();
		//indices correspond to disease id's, values are number of symptoms related to diagnosis
		ArrayList<Integer> diagnosisList = sqliteJDBC.getDiagnosisListByID();
		//similar to diagnosis array 
		ArrayList<Integer> symptomList = sqliteJDBC.getSymptomListByID();
		
		//array values are filtered disease id's
		ArrayList<Integer> filteredDiagnosisList = sqliteJDBC.getFilteredDiagnosisList(inputSymptoms);
		
		HashMap<Integer,Integer[]> diagnosisSymptomMap = sqliteJDBC.getDiagnosisSymptomMap();
		
		float [][] diagnosisSymptomLikelihood = new float[diagnosisList.size()][symptomList.size()];
		float [][] symptomDiagnosisLikelihood = new float[symptomList.size()][diagnosisList.size()];
		
		//populate 2nd value - likelihood of disease given symptom - set to 1 
		for(int i = 0; i < symptomDiagnosisLikelihood.length; i++)
		{
			for(int j = 0; j < symptomDiagnosisLikelihood[0].length; j++)
			{
				symptomDiagnosisLikelihood[i][j] = 1;
			}
		}
		
		//populate 1st value - likelihood of symptom given disease
		for(int i=0; i < diagnosisSymptomLikelihood.length; i++)
		{
			for(int j = 0; j < diagnosisSymptomLikelihood[0].length; j++)
			{
				diagnosisSymptomLikelihood[i][j] = 1/(float)(diagnosisList.get(i)*symptomList.get(j));
			}
		}
		
		HashMap<String,Float> DiagnosisScoreMap = new HashMap<String,Float>();
		
		 // list1 - observed symptoms consistent with diagnosis
		//list2 - observed symptoms not associated with diagnosis
		//list3 - symptoms associated with diagnosis but not observed in patient
		
		SQLiteJDBC sql = new SQLiteJDBC();
		ArrayList<String> diagnosisNames = sql.getDiagnosisNamesFromIds(filteredDiagnosisList.toArray(new Integer[filteredDiagnosisList.size()]));
		
		for(int i = 0; i < filteredDiagnosisList.size(); i++)
		{
			int diagnosisId = filteredDiagnosisList.get(i);
			Integer[] symptomList1 =  diagnosisSymptomMap.get(diagnosisId);
			Set<Integer> inputSymptomSet = new HashSet<Integer>(Arrays.asList(inputSymptoms));
			Set<Integer> copy = new HashSet<Integer>(inputSymptomSet.size()); 
			for(Integer sym: inputSymptomSet) {
				copy.add(sym);
			}
			Set<Integer> diagnosisSymptomSet = new HashSet<Integer>(Arrays.asList(diagnosisSymptomMap.get(diagnosisId)));
			inputSymptomSet.removeAll(diagnosisSymptomSet);
			Integer[] symptomList2 = inputSymptomSet.toArray(new Integer[inputSymptomSet.size()]);
			diagnosisSymptomSet.removeAll(copy);
			Integer[] symptomList3 = diagnosisSymptomSet.toArray(new Integer[diagnosisSymptomSet.size()]);
			float score1 = 0;
			float score2 = 0;
			float score3 = 0;
			
			for(int l : symptomList1) {
				score1 += diagnosisSymptomLikelihood[diagnosisId-1][l-1]/symptomDiagnosisLikelihood[l-1][diagnosisId-1];				
			}
			
			for(int l : symptomList2) {
				score2 += diagnosisSymptomLikelihood[diagnosisId-1][l-1]/symptomDiagnosisLikelihood[l-1][diagnosisId-1];				
			}
			
			for(int l : symptomList3) {
				score3 += diagnosisSymptomLikelihood[diagnosisId-1][l-1]/symptomDiagnosisLikelihood[l-1][diagnosisId-1];				
			}
			
			float weight1 = 1;
			float weight2 = (float) 0.1;
			float weight3 = (float) 1;
			
			float finalScore = weight1*score1 - weight2*score2 - weight3*score3;
			DiagnosisScoreMap.put(diagnosisNames.get(i),finalScore);
			
		}
		
		return (HashMap<String, Float>) PlayUtilities.entriesSortedByValues(DiagnosisScoreMap);
		
	}


}