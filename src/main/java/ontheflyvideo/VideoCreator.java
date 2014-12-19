/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ontheflyvideo;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

/**
 *
 * @author sushant
 */
public abstract class VideoCreator {
    
    protected boolean DEBUG;
    protected String outFile;
    protected int TRANSITION_DELAY;
    protected int DURATION;
    protected Dimension screen;
    protected List<BufferedImage> images;
    protected ArrayList<Object[]> imageObj;
    
    public VideoCreator(ArrayList<Object[]> imageList, HashMap<String,Object> params)
    {
        this.imageObj = imageList;
        
        initCreator();
        
        if(params.containsKey("TRANSITION_DELAY"))
            this.TRANSITION_DELAY = (Integer)params.get("TRANSITION_DELAY");
        
        if(params.containsKey("WIDTH") && params.containsKey("HEIGHT"))
            screen.setSize((Integer)params.get("WIDTH"),(Integer)params.get("HEIGHT"));
        
        if(params.containsKey("DEBUG"))
            DEBUG = (Boolean)params.get("DEBUG");
    }
    
    private void initCreator()
    {
        this.TRANSITION_DELAY = 1;
        screen = new Dimension(400,300);
        DEBUG = true;
        
        Collections.shuffle(imageObj);
    }
    
    /*
    public void getAllImages()
    {
        try {
            for(int i=0;i<17;i++)
            {
                BufferedImage img = ImageIO.read(new File("saved_"+Integer.toString(i)+".jpg"));
                images.add(img);
            }
        } catch (IOException ex) {
            Logger.getLogger(VideoCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Collections.shuffle(images);
    }
    */
    
    public abstract void process();
    
    public String getVideoPath()
    {
        return this.outFile;
    }
    
    public BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        
        BufferedImage image;
        
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        }else {
            image = new BufferedImage(sourceImage.getWidth(), 
                 sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;
    }
    
    protected BufferedImage resize(BufferedImage img,int width, int height)
    {
        return Scalr.resize(img,Scalr.Method.SPEED,Scalr.Mode.FIT_EXACT,width,height,Scalr.OP_ANTIALIAS);
    }
    
}
