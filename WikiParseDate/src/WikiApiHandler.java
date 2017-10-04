import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class WikiApiHandler {
	
	final String []months=new String[]{"января","февраля","марта","апреля","мая","июня","июля","августа","сентября","октября","ноября","декабря"};
	final int []DaysInMonth=new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
	
	public static void main(String[] args) throws Exception {
		new WikiApiHandler().sendGET("1 января");
	}
	
	public void sendGET(String text) throws Exception {
		
		String result=URLEncoder.encode(text, "UTF-8");
		
		String url = "https://ru.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles="+result;		
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		// add request header
		// con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			for(int find=inputLine.indexOf("\\u");find!=-1;find=inputLine.indexOf("\\u",find)){
				inputLine=inputLine.substring(0, find)+(char)Integer.parseInt(inputLine.substring(find+2,find+6), 16)+inputLine.substring(find+6,inputLine.length());
			}
			
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());
		
		ArrayList<String>str=new ArrayList<>();
		System.out.println();
		int start=response.toString().indexOf("== Родились ==")+14;
		start=response.toString().indexOf("\\n*",start);
		int end=response.toString().indexOf("== Скончались");		
		
		String borned=response.toString().substring(start,end);
		
		for(String s:borned.split("\\\\n\\*")){
			System.out.println(s);
		}
		
		//System.out.println(borned);
		
	}
}
