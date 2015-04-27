package utils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


public class CallFHIR {
	public HashMap<String,ArrayList<String>> getSpecialists() throws ClientProtocolException, IOException {
		String patientUrl = "https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Patient?_count=100&_format=json";
		int count = 0;
		int patientRecords = 100;
		HashMap<String, ArrayList<String>> conditionDoctorMap = new HashMap();
		ArrayList<String> patientIds = new ArrayList<>();
		while(count < patientRecords)
		{
			try {
				HttpGet request = new HttpGet(patientUrl);
				CloseableHttpClient client = HttpClientBuilder.create().build();
				HttpResponse response = client.execute(request);
				BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
				String line = rd.readLine();
				Map jsonData = PlayUtilities.getMapFromJson(line);
				if(count == 0) {
					patientUrl = (String) ((Map)((ArrayList)jsonData.get("link")).get(1)).get("href");
					//patientRecords = Integer.parseInt((String)jsonData.get("totalResults"));
				}
				else
					patientUrl = (String) ((Map)((ArrayList)jsonData.get("link")).get(2)).get("href");
				ArrayList entry=(ArrayList) jsonData.get("entry");
				for (int i =0; i<entry.size();i++)
				{
					count += 1;
					Map identifier =(Map) ((ArrayList) ((Map) ((Map)entry.get(i)).get("content")).get("identifier")).get(0);
					String patientId = (String)identifier.get("value");
					patientIds.add(patientId);
				}

			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		HashMap<String,String> patientConditions = new HashMap<>();

		for(String pid:patientIds)
		{
			String conditionSearchUrl = "https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Condition?subject:Patient=" + pid + "&_format=json"; 
			HttpGet request = new HttpGet(conditionSearchUrl);
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
			String line = rd.readLine();
			Map jsonData = PlayUtilities.getMapFromJson(line);
			if(jsonData != null) {
				ArrayList entry=(ArrayList) jsonData.get("entry");
				Map code =(Map)((Map) ((Map)entry.get(0)).get("content")).get("code");
				String condition = (String) ((Map)((ArrayList)code.get("coding")).get(0)).get("display");
				//System.out.println(pid + ": "+condition);
				patientConditions.put(pid, condition);
			}
		}
		
		for(String pid:patientIds)
		{
			String doctorSearchUrl = "https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/MedicationPrescription?subject:Patient=" + pid + "&_format=json";
			HttpGet request = new HttpGet(doctorSearchUrl);
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
			String line = rd.readLine();
			Map jsonData = PlayUtilities.getMapFromJson(line);
			if(jsonData != null) {
				ArrayList entry=(ArrayList) jsonData.get("entry");
				Map prescriber =(Map)((Map) ((Map)entry.get(0)).get("content")).get("prescriber");
				String dr = (String) prescriber.get("display");
				ArrayList<String> docs = null;
				docs = conditionDoctorMap.get(patientConditions.get(pid));
				if(docs != null && docs.size() < 6)
				{
					if(!docs.contains(dr))
					{
						docs.add(dr);
					}
				}
				else if(docs == null){
					docs = new ArrayList<>();
					docs.add(dr);
				}
				conditionDoctorMap.put(patientConditions.get(pid), docs);
//				System.out.println(patientConditions.get(pid)+" doctors are: ");
//				for(String d: docs)
//				{
//					System.out.println(d);
//				}
				
			}
		}

		return conditionDoctorMap;
		
	}
	
	public void writeToFile(HashMap<String,ArrayList<String>> conditionDoctorMap) throws IOException
	{
		String fname = "conditionDoctorMap.csv";
		FileWriter writer = new FileWriter(fname);
		for (Entry<String, ArrayList<String>> e : conditionDoctorMap.entrySet()) {
			String vals = "";
			for(String s: e.getValue())
			{
				vals += s+" ";
			}
			writer.append(e.getKey());
			writer.append(',');
			writer.append(vals);
			writer.append('\n');
		}
		writer.flush();
	    writer.close();
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException{
		CallFHIR fhirApi = new CallFHIR();
		HashMap<String,ArrayList<String>> conditionDoctorMap = fhirApi.getSpecialists();
		fhirApi.writeToFile(conditionDoctorMap);
	}

}
