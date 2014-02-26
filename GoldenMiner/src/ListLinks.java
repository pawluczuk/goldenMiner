import java.util.List;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ListLinks {
	public static final String URL = "http://www.goldenline.pl/profile/mapa/";
    public static void main(String[] args) throws IOException {
    	List<String> urlList = new ArrayList<String>();
    	// linki do map dla kazdej litery
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
    		// jesli nie ma podstron, nie trzeba podawac /s/numerStrony
    		else urlList.add(webpage);
    	}
    	// debug
    	print(urlList.toString());
    	
    	//getProfileLinks
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

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}