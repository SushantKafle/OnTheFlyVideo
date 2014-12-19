package ontheflysound;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;


 /**
 * This class is used to merge audio and video file.
 *
 * @author Arslaan Ejaz
 */
public class SoundHandler {
    
    String soundFile;
    
    public SoundHandler(String keyword)
    {
      soundFile = "sounds\\serious\\sound_0.mp3";
    }

    public void process(String[] args)
    {

        String filenamevideo = args[0];
        String filenameaudio = this.soundFile;


        IMediaWriter mWriter = ToolFactory.makeWriter(args[1]); //output file

        IContainer containerVideo = IContainer.make();
        IContainer containerAudio = IContainer.make();

        if (containerVideo.open(filenamevideo, IContainer.Type.READ, null) < 0)
            throw new IllegalArgumentException("Cant find " + filenamevideo);

        if (containerAudio.open(filenameaudio, IContainer.Type.READ, null) < 0)
            throw new IllegalArgumentException("Cant find " + filenameaudio);

        int numStreamVideo = containerVideo.getNumStreams();
        int numStreamAudio = containerAudio.getNumStreams();

        int videostreamt = -1; //this is the video stream id
        int audiostreamt = -1;

        IStreamCoder  videocoder = null;

        for(int i=0; i<numStreamVideo; i++){
            IStream stream = containerVideo.getStream(i);
            IStreamCoder code = stream.getStreamCoder();

            if(code.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
            {
                videostreamt = i;
                videocoder = code;
                break;
            }

        }

        for(int i=0; i<numStreamAudio; i++){
            IStream stream = containerAudio.getStream(i);
            IStreamCoder code = stream.getStreamCoder();

            if(code.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
            {
                audiostreamt = i;
                break;
            }

        }

        if (videostreamt == -1) throw new RuntimeException("No video steam found");
        if (audiostreamt == -1) throw new RuntimeException("No audio steam found");

        if(videocoder.open()<0 ) throw new RuntimeException("Cant open video coder");
        IPacket packetvideo = IPacket.make();

        IStreamCoder audioCoder = containerAudio.getStream(audiostreamt).getStreamCoder();

        if(audioCoder.open()<0 ) throw new RuntimeException("Cant open audio coder");
        
        mWriter.addAudioStream(0, 1, audioCoder.getChannels(), audioCoder.getSampleRate());
        mWriter.addVideoStream(1, 0,ICodec.ID.CODEC_ID_MPEG4, videocoder.getWidth(), videocoder.getHeight());

        IPacket packetaudio = IPacket.make();
        
        while(containerVideo.readNextPacket(packetvideo) >= 0 || containerAudio.readNextPacket(packetaudio) >= 0){

            if(packetvideo.getStreamIndex() == videostreamt){
            //video packet
                IVideoPicture picture = IVideoPicture.make(videocoder.getPixelType(),
                    videocoder.getWidth(),
                    videocoder.getHeight());
                int offset = 0;
                while (offset < packetvideo.getSize()){
                int bytesDecoded = videocoder.decodeVideo(picture, 
                        packetvideo, 
                        offset);
                if(bytesDecoded < 0) throw new RuntimeException("bytesDecoded not working");
                offset += bytesDecoded;

                    if(picture.isComplete()){
                        mWriter.encodeVideo(1, picture);
                    }
                }
             } 
            
        if(packetaudio.getStreamIndex() == audiostreamt){   
        //audio packet

            IAudioSamples samples = IAudioSamples.make(512, 
                    audioCoder.getChannels(),
                    IAudioSamples.Format.FMT_S32);  
            int offset = 0;
            while(offset<packetaudio.getSize())
            {
                int bytesDecodedaudio = audioCoder.decodeAudio(samples, 
                        packetaudio,
                        offset);
                if (bytesDecodedaudio < 0)
                    throw new RuntimeException("could not detect audio");
                offset += bytesDecodedaudio;

                if (samples.isComplete()){
                     mWriter.encodeAudio(0, samples);
                    }
                }

            }
        }
        mWriter.close();
    }
}