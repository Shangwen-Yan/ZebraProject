package cn.tarena.file;

import java.io.File;

import cn.tarena.zebra.common.GlobalEnv;
import rpc.domain.FileSplit;

/**
 * 
 * 线程类，作用：
 * 1.日志队列取出日志文件
 * 2.做逻辑切块，按指定blockSize：3m来且，四块
 * 3.FileSplit（avro对象)封装任务描述信息：a.path b.start c. length 
 * 4.把文件切片存到队列里
 * @author CGB-yz
 *
 */
public class FileToBlock implements Runnable{
	public static long num;
	@Override
	public void run() {
		try {
			while(true){
				File file=GlobalEnv.getFileQueue().take(); //使用阻塞方法take
				long length=file.length();
				num=length%GlobalEnv.getBlocksize()==0?length/GlobalEnv.getBlocksize():length/GlobalEnv.getBlocksize()+1;
				for(int i=0;i<num;i++){
					FileSplit split=new FileSplit();
					split.setPath(file.getPath());
					split.setStart(i*GlobalEnv.getBlocksize());
					if(i==(num-1)){
						split.setLength(length-split.getStart());
					}else{
						split.setLength(GlobalEnv.getBlocksize());
					}
					GlobalEnv.getSplitQueue().add(split);
					System.out.println("jobtracker处理获得的第"+(i+1)+"个文件切片："+split);
					
				}
				System.out.println("jobtracker已将"+num+"个文件切片已存入GlobalEnv中的SplitQueue队列");
			
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

}
