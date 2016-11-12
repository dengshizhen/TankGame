package com.test5;
import java.io.*;
import javax.sound.sampled.*;
public class AePlayWave extends Thread{

	String filename;
	public AePlayWave(String wavfile) {
		// TODO �Զ����ɵĹ��캯�����
		filename=wavfile;
	}
	public void run()
	{
		File soundFile=new File(filename);
		AudioInputStream audio=null;
		try {
			audio=AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return;
		}
		AudioFormat format=audio.getFormat();
		SourceDataLine auline=null;
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, format);
		
		try {
			auline=(SourceDataLine)AudioSystem.getLine(info);
			auline.open(format);
		} catch (LineUnavailableException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		
		auline.start();
		int nByteRead=0;
		byte[] abData=new byte[1024];
		while(nByteRead!=-1)
		{
			try {
				nByteRead=audio.read(abData, 0, abData.length);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			if(nByteRead>=0)
			{
				auline.write(abData, 0, nByteRead);
			}
		}
	}

}
