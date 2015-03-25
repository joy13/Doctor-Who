package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import utils.PlayUtilities;
import utils.SQLiteJDBC;

public class DiagnosisDecisionAlgorithm {
	
	public HashMap<Integer,Float> getScoredDiagnosis(Integer[] inputSymptoms) {
		SQLiteJDBC sqliteJDBC = new SQLiteJDBC();
		//indices correspond to disease id's, values are number of symptoms related to diagnosis
		int [] diagnosisList = sqliteJDBC.getdiseaseListByID();
		//similar to diagnosis array 
		int [] symptomList = sqliteJDBC.getSymptomListByID();
		
		//array values are filtered disease id's
		int [] filteredDiagnosisList = sqliteJDBC.getFilteredDiseaseList(inputSymptoms);
		
		HashMap<Integer,Integer[]> diagnosisSymptomMap = sqliteJDBC.getDiagnosisSymptomMap();
		
		float [][] diagnosisSymptomLikelihood = new float[diagnosisList.length][symptomList.length];
		float [][] symptomDiagnosisLikelihood = new float[symptomList.length][diagnosisList.length];
		
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
				diagnosisSymptomLikelihood[i][j] = 1/(float)(diagnosisList[i]*symptomList[j]);
			}
		}
		
		HashMap<Integer,Float> DiagnosisScoreMap = new HashMap<Integer,Float>();
		
		 // list1 - observed symptoms consistent with diagnosis
		//list2 - observed symptoms not associated with diagnosis
		//list3 - symptoms associated with diagnosis but not observed in patient
		
		for(int i = 0; i < filteredDiagnosisList.length; i++)
		{
			int diagnosisId = filteredDiagnosisList[i];
			Integer[] list1 =  diagnosisSymptomMap.get(diagnosisId);
			Set<Integer> inputSymptomSet = new HashSet<Integer>(Arrays.asList(inputSymptoms));
			Set<Integer> copy = new HashSet<Integer>(inputSymptomSet.size()); 
			for(Integer sym: inputSymptomSet) {
				copy.add(sym);
			}
			Set<Integer> diagnosisSymptomSet = new HashSet<Integer>(Arrays.asList(diagnosisSymptomMap.get(filteredDiagnosisList[i])));
			inputSymptomSet.removeAll(diagnosisSymptomSet);
			Integer[] list2 = inputSymptomSet.toArray(new Integer[inputSymptomSet.size()]);
			diagnosisSymptomSet.removeAll(copy);
			Integer[] list3 = diagnosisSymptomSet.toArray(new Integer[diagnosisSymptomSet.size()]);
			float score1 = 0;
			float score2 = 0;
			float score3 = 0;
			
			for(int l : list1) {
				score1 += diagnosisSymptomLikelihood[diagnosisId][l]/symptomDiagnosisLikelihood[l][filteredDiagnosisList[i]];				
			}
			
			for(int l : list2) {
				score2 += diagnosisSymptomLikelihood[diagnosisId][l]/symptomDiagnosisLikelihood[l][filteredDiagnosisList[i]];				
			}
			
			for(int l : list3) {
				score3 += diagnosisSymptomLikelihood[diagnosisId][l]/symptomDiagnosisLikelihood[l][filteredDiagnosisList[i]];				
			}
			
			float weight1 = 1;
			float weight2 = (float) 0.1;
			float weight3 = 1;
			
			float finalScore = weight1*score1 + weight2*score2 + weight3*score3;
			DiagnosisScoreMap.put(diagnosisId,finalScore);
			
		}
		
		return (HashMap<Integer, Float>) PlayUtilities.entriesSortedByValues(DiagnosisScoreMap);
		
	}


}