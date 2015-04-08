package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;



public class CallFHIR {
	public HashMap<String,String[]> getSpecialists() throws ClientProtocolException, IOException {
		String patientUrl = "https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Patient?_count=100&_format=json";
		int count = 0;
		int patientRecords = 1;
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
					patientRecords = Integer.parseInt((String)jsonData.get("totalResults"));
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
		ArrayList<String> conditions = new ArrayList<>();

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
				System.out.println(pid + ": "+condition);
				conditions.add(condition);
			}
		}

		return null;
	}


}
