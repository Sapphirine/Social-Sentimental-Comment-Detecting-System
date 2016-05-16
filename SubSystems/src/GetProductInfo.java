import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class GetProductInfo {
	public static void main(String[] args) {
		try {
			System.setOut(new PrintStream(new FileOutputStream("output.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//链接数据库
		//twitter configuration
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("Jj01lVEbPn8fupbiXphhmOxON")
          .setOAuthConsumerSecret("iVYTRTZ4hbgh0zIvdxzzMMLFZbha5MPnD61qfgZm0koOvanbYL")
          .setOAuthAccessToken("704740943539339264-P6c1QHN7L5F5pku9cwUvmtIisuza7HO")
          .setOAuthAccessTokenSecret("dX327UZ65ofhGB3UIhQGJMJPGw7UQ2233q9hnGkuYVirL");
        
       TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
       StatusListener listener=new StatusListener(){
    	   public void onStatus(Status status){
    		   String result="";
    		   result=result+status.getCreatedAt().toString()+","+status.getText();
    		   System.out.println(result);
    	   	}
    	   @Override
           public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
               //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
           }

           @Override
           public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
               //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
           }

           @Override
           public void onScrubGeo(long userId, long upToStatusId) {
               //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
           }

           @Override
           public void onStallWarning(StallWarning warning) {
               //System.out.println("Got stall warning:" + warning);
           }

           @Override
           public void onException(Exception ex) {
               ex.printStackTrace();
           }
//           @Override
//           public void onStatus(Status arg0) {
//        	   // TODO Auto-generated method stub
//			
//           }
       };
       
       twitterStream.addListener(listener);
       FilterQuery filtre = new FilterQuery();
       String[] keywordsArray = { "iphone6", "macbook" };
       filtre.track(keywordsArray);
       twitterStream.filter(filtre);
       //twitterStream.sample();
	} 
	
}
