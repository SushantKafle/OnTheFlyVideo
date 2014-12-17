package com.sushant.ontheflyvideo;

import java.util.HashMap;
import java.util.Map;
import ontheflycrawler.GoogleImgCrawler;
import ontheflycrawler.Imgcrawler;
import ontheflyvideo.Mp4VideoCreator;
import ontheflyvideo.VideoCreator;

/**
 * Hello world!
 *
 */

public class App {

    public static void main(String[] args) {
        
        HashMap<String,Object> crawlerParams = new HashMap<String, Object>();
        crawlerParams.put("DEBUG",true);
        crawlerParams.put("MAX_IMAGE", 20);
        crawlerParams.put("IMAGE_SIZE", "medium|large");
        crawlerParams.put("IMAGE_TYPE","any");
        crawlerParams.put("RIGHTS","any");
        
        Imgcrawler img = new GoogleImgCrawler(crawlerParams);
        img.crawl("nepalese culture");
        
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("TRANSITION_DELAY",4);
        
        VideoCreator vc = new Mp4VideoCreator(img.getImageObj(),params);
        vc.process();
        
    }
}

