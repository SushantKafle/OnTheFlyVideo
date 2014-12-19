/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ontheflycrawler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author sushant
 */
public abstract class Imgcrawler {
    
    protected ArrayList<Object[]> imageList;
    protected int MAX_IMAGE;
    protected boolean DEBUG;
    protected String IMAGE_SIZE;
    protected String IMAGE_TYPE;
    protected String RIGHTS;
    
    public Imgcrawler(HashMap<String, Object> params)
    {
        initCrawler();
        
        if(params.containsKey("MAX_IMAGE"))
            MAX_IMAGE = (Integer)params.get("MAX_IMAGE");
        
        if(params.containsKey("DEBUG"))
            DEBUG = (Boolean)params.get("DEBUG");
        
        if(params.containsKey("IMAGE_SIZE"))
            IMAGE_SIZE = (String)params.get("IMAGE_SIZE");
        
        if(params.containsKey("IMAGE_TYPE"))
            IMAGE_TYPE = (String)params.get("IMAGE_TYPE");
        
        if(params.containsKey("RIGHTS"))
            RIGHTS = (String)params.get("RIGHTS");
    }
    
    private void initCrawler()
    {
        MAX_IMAGE = 20;
        DEBUG = true;
        IMAGE_SIZE = "medium|large";
        IMAGE_TYPE = "all";
        RIGHTS = "all";
    }
    
    public ArrayList<Object[]> getImageObj()
    {
        return this.imageList;
    }
    
    public abstract void crawl(String keyword);
    public abstract String process(String keyword);
    public abstract String constructURL(String keyword);
}
