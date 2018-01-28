package cn.tarena.file;

import java.io.File;

import javax.swing.plaf.synth.SynthSpinnerUI;

import cn.tarena.zebra.common.GlobalEnv;

/**
 * 
 * @author CGB-yz
 * 定期扫描日志文件
 */
public class FileHandle implements Runnable{
	
	@Override
	public void run() {
		try{
			while(true){
				File dir=new File(GlobalEnv.getDir());
				File[] files=dir.listFiles();
				for(File file:files){
					if(file.getName().endsWith(".ctr")){
						String csvName=file.getName().split(".ctr")[0]+".csv";
						File csvFile=new File(dir,csvName);
						file.delete();
						GlobalEnv.getFileQueue().add(csvFile);
						System.out.println("jobtracker已将"+csvFile.getName()+"存入待处理文件队列GlobalEnv.FileQueue");
						
					}
				}
				Thread.sleep(GlobalEnv.getScannningInterval());
			}
		}catch(Exception e){
			
		}
		
	}

}
