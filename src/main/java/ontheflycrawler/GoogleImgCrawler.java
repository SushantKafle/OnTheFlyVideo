/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ontheflycrawler;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import ontheflyvideo.VideoCreator;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author sushant
 */
public class GoogleImgCrawler extends Imgcrawler {

    public GoogleImgCrawler(HashMap<String, Object> params) {
        super(params);
        
        if(DEBUG)
            Logger.getLogger(GoogleImgCrawler.class.getName()).log(Level.INFO, "Initialized on DEBUG MODE.");
    }
    
    @Override
    public void crawl(String keyword) {
        
        if(DEBUG)
            Logger.getLogger(GoogleImgCrawler.class.getName()).log(Level.INFO, "Crawl Started.");
        
        imageList = new ArrayList<Object[]>();
        int loops = (int)Math.ceil(((float)MAX_IMAGE)/8.0);
        try{
            for(int startFrom=0;startFrom<loops;startFrom++)
            {
                URL url = new URL(constructURL(keyword)+"&start="+Integer.toString(8*startFrom)+"&rsz=8");
                URLConnection connection = url.openConnection();

                String line;
                StringBuilder builder = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                JSONObject json = new JSONObject(builder.toString());
                JSONArray array = json.getJSONObject("responseData").getJSONArray("results");

                for(int i=0;i<array.length();i++)
                {
                    String imageUrl = array.getJSONObject(i).getString("url");
                    String fileType = imageUrl.substring(imageUrl.lastIndexOf('.') + 1);
                    try{
                    BufferedImage img = ImageIO.read(new URL(imageUrl));
                    if(img!=null)
                    {
                        if(DEBUG)
                            Logger.getLogger(GoogleImgCrawler.class.getName()).log(Level.INFO, "Pulling image from:"+imageUrl);
                        
                        imageList.add(new Object[]{img,fileType});
                    }
                    }catch(IOException e)
                    {
                        if(DEBUG)
                            Logger.getLogger(GoogleImgCrawler.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                
            }
        } catch(Exception e){
            if(DEBUG)
                Logger.getLogger(GoogleImgCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
        if(DEBUG)
            Logger.getLogger(GoogleImgCrawler.class.getName()).log(Level.INFO, "Crawl Complete.");
    }

    @Override
    public String process(String keyword){
        return "&q="+keyword.replace(" ", "+");
    }

    @Override
    public String constructURL(String keyword) {
        String baseURL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
        String query = process(keyword);
        
        baseURL +=query;
        baseURL += "&imgsz="+IMAGE_SIZE;
        
        if(!IMAGE_TYPE.equals("any"))
        {
            baseURL += "&tbs=ift:"+IMAGE_TYPE;
        }
        
        if(!RIGHTS.equals("any"))
        {
            baseURL +="&as_rights="+RIGHTS;
        }
        
        return baseURL;
    }
    
}
