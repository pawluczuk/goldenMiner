import java.util.List;
import java.io.*;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class ListLinks {
	// map of public profiles on goldenline
	public static final String URL = "http://www.goldenline.pl/profile/mapa/";
    public static void main(String[] args) throws IOException {
    	List<String> urlList = new ArrayList<String>();
    	// hyperlinks for maps for each letter in the alphabet
    	for(char c = 'a'; c <= 'z'; ++c) {
    		
    		String singleChar= String.valueOf(c);
    		String webpage = URL + singleChar;
    		int count = numberOfPages(singleChar,webpage);
    		if (count != 0)
    		{
    			for (int i=1; i<= count; i++)
        		{
            	    urlList.add(URL + singleChar + "/s/" + i);
        		}
    		}
    		// if there's only one page, we do not need to add /s/pageNo
    		else urlList.add(webpage);
    		
    	}
    	
    	// get profile hyperlinsk on each page
    	for (String hyperlink : urlList) {
            getProfileLinks(hyperlink);
        }
    }
    /**
     * Zwraca liczbe stron w mapie profili dla danej litery
     * @param url
     * @return liczba stron dla danej litery
     */
    private static int numberOfPages(String character, String url)
    {
    	Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Element link = doc.select("ul.pager:not(#contactLetters) a[href]:not(.next)").last();
        if (link == null ) return 0;
        else
        {
        	return Integer.parseInt(link.text());
        }

    }
    
    /**
     * Znajduje linki do profili na danej stronie mapy profili
     * @param url
     */
    private static void getProfileLinks(String url)
    {
    	Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Elements links = doc.select("div#people a[href]");

        
           try {
        	   Class.forName("org.sqlite.JDBC");
 			  

		        Connection conn2 = DriverManager.getConnection("jdbc:sqlite:goldenMiner.db");
		        conn2.setAutoCommit(false);
		       
		        PreparedStatement prepStmt2 = conn2
		                .prepareStatement("INSERT INTO hyperlinks values (?, ?);");
		        for (Element link : links) {
		        	prepStmt2.setString(1, link.attr("abs:href"));
		            prepStmt2.setString(2, "");
		            prepStmt2.addBatch();
		        }
		        prepStmt2.executeBatch();
		        conn2.commit();
		        
		        conn2.close();
           
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

}