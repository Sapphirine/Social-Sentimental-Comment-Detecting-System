import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;

import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
public class AnalysisText {
	private static String mysqlurl="jdbc:mysql://localhost:3306/tweets";
	private static String alchemyApiKey="7c2634fb5a4b7b53d53f939f399c164c8e155f08";
	private static String alchemyUrlString="http://gateway-a.watsonplatform.net/calls/text/TextGetTextSentiment?apikey="+alchemyApiKey+"&outputMode=json&text=";
	public static void main(String[] args) throws JSONException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedReader br =null;
		try{
			String line;
			URL alchemyURL;
			BufferedReader reader;
			JSONObject jsonresult=null;
			br=new BufferedReader(new FileReader("battery.txt"));
			int i=0;
			while((line=br.readLine())!=null){
				try {
					int index=line.indexOf(',');
					if(index==-1){
						System.out.println("all data into database");
						break;
					}
					String date=line.substring(0, index);
					String text1=line.substring(index+1);
					String text=java.net.URLEncoder.encode(text1,"UTF-8");
					alchemyURL=new URL(alchemyUrlString+text);
					reader=new BufferedReader(new InputStreamReader(alchemyURL.openStream()));
					StringBuffer buffer=new StringBuffer();
					int read;
					char[] chars=new char[1024];
					while((read=reader.read(chars))!=-1){
						buffer.append(chars, 0, read);
					}
					jsonresult=new JSONObject(buffer.toString());
					//System.out.println(chars.toString());
					String sentiString=null;
					if(jsonresult.isNull("docSentiment")) continue;
					else{
						String temp=jsonresult.getJSONObject("docSentiment").toString();
						JSONObject senti=new JSONObject(temp);
						sentiString=senti.getString("type");
						Connection conn=DriverManager.getConnection(mysqlurl,"root","finalproject");
						Statement stmt=conn.createStatement();
						String category="battery";
						stmt.executeUpdate("INSERT INTO tweets "+"VALUES ('"+i+"', '"+date+"', '"+text+"', '"+ category+"', '"+sentiString+"')");
						System.out.println(sentiString);
						i++;
					}
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
