/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ontheflyvideo;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
        final IMediaWriter writer = ToolFactory.makeWriter(this.outFile);
        
        Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(screenBounds.height);
        System.out.println(screenBounds.width);
        
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,screenBounds.width/2, screenBounds.height/2);
        
        long startTime = System.nanoTime();
        
        for (Object[] obj:this.imageObj) {
                
                BufferedImage screen = resize((BufferedImage)obj[0],1366,768);
                BufferedImage bgrScreen = convertToType(screen, BufferedImage.TYPE_3BYTE_BGR);
                
                writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime,
                        TimeUnit.NANOSECONDS);
                
                try {
                    Thread.sleep((long)(1000 * TRANSITION_DELAY));
                }
                catch (InterruptedException e) {
                    // ignore
                }
                
            } 
        
        writer.close();

    }
    
    
    
}
