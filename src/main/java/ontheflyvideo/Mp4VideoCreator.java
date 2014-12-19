/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ontheflyvideo;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sushant
 */
public class Mp4VideoCreator extends VideoCreator {

    public Mp4VideoCreator(ArrayList<Object[]> imageList, HashMap<String, Object> params) {
        super(imageList, params);
        this.outFile = "video.mp4";
    }
    
    @Override
    public void process() {
        
        if(DEBUG)
            Logger.getLogger(Mp4VideoCreator.class.getName()).log(Level.INFO, "Video Encoding Started");
            
        final IMediaWriter writer = ToolFactory.makeWriter(this.outFile);
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,screen.width, screen.height);
        
        long startTime = System.nanoTime();
        
        for (Object[] obj:this.imageObj) {
            
            BufferedImage screenImg = resize((BufferedImage)obj[0],screen.width,screen.height);
            BufferedImage bgrScreen = convertToType(screenImg, BufferedImage.TYPE_3BYTE_BGR);

            writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime,TimeUnit.NANOSECONDS);

            try {
                Thread.sleep((long)(1000 * TRANSITION_DELAY));
            }
            catch (InterruptedException e) {
                // ignore
            }
        }

        writer.close();
        
        if(DEBUG)
            Logger.getLogger(Mp4VideoCreator.class.getName()).log(Level.INFO, "Video Encoding Complete.");
    }
    
    
    
}
