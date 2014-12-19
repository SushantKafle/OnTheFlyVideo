package com.sushant.ontheflyvideo;

import java.util.HashMap;
import ontheflycrawler.GoogleImgCrawler;
import ontheflycrawler.Imgcrawler;
import ontheflysound.SoundHandler;
import ontheflyvideo.Mp4VideoCreator;
import ontheflyvideo.VideoCreator;

/**
 * Hello world!
 *
 */

public class App {

    public static void main(String[] args) {
        
        boolean DEBUG = true;
        String keyword = "beauty of nepal";
        
        HashMap<String,Object> crawlerParams = new HashMap<String, Object>();
        crawlerParams.put("DEBUG",DEBUG);
        crawlerParams.put("MAX_IMAGE", 16);
        crawlerParams.put("IMAGE_SIZE", "medium|large");
        crawlerParams.put("IMAGE_TYPE","any");
        crawlerParams.put("RIGHTS","any");
        
        Imgcrawler img = new GoogleImgCrawler(crawlerParams);
        img.crawl(keyword);
        
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("TRANSITION_DELAY",3);
        params.put("HEIGHT",300);
        params.put("WIDTH",400);
        params.put("DEBUG", DEBUG);
        
        VideoCreator vc = new Mp4VideoCreator(img.getImageObj(),params);
        vc.process();
        
        SoundHandler sh = new SoundHandler(keyword);
        sh.process(new String[]{vc.getVideoPath(),"video1.mp4"});
        
        
    }
}

