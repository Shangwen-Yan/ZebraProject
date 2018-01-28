package cn.tarena.mapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import cn.tarena.zebra.common.OwnEnv;
import cn.tarena.zk.ZkConnectRunner;
import rpc.domain.FileSplit;
import rpc.domain.HttpAppHost;
/**
 * 处理FileSplit，
 * 用|切割并封装
 * 用户key合并数据
 * @author CGB-yz
 *
 */
public class MapperRunner implements Runnable{

	@Override
	public void run() {
		try {
			while(true){
				FileSplit split=OwnEnv.getSpiltQueue().take();
				System.out.println("engine1-01已取出FileSplit"+split);
				//设置繁忙状态
				ZkConnectRunner.setBusy();
				Map<CharSequence,HttpAppHost> map=new HashMap<CharSequence,HttpAppHost>();
				File file=new File(split.getPath().toString());
				long start=split.getStart();
				long end=start+split.getLength();
				FileChannel fc=new FileInputStream(file).getChannel();
				if(start==0){	
				}else{
					long headPosition=start;
					while(true){
						fc.position(headPosition);
						ByteBuffer buffer=ByteBuffer.allocate(1);
						fc.read(buffer);
						if(new String(buffer.array()).equals("\n")){
							start=headPosition+1;
									break;
						}else{
							headPosition=headPosition-1;
						}
					}
				}
				if(end==file.length()){
					//读完了
				}else{
					long tailPosition=end;
					while(true){
						fc.position(tailPosition);
						ByteBuffer buffer=ByteBuffer.allocate(1);
						
						fc.read(buffer);
						if(new String(buffer.array()).equals("\n")){
							end=tailPosition;
							break;
						}else{
							tailPosition=tailPosition-1;
						}
					}
				}
				fc.position(start);
				ByteBuffer fileData=ByteBuffer.allocate((int) (end-start));
				fc.read(fileData);
				BufferedReader br=new BufferedReader(
						new InputStreamReader(
								new ByteArrayInputStream(fileData.array())));

				String line=null;
				while((line=br.readLine())!=null){
					String[] data=line.split("\\|");
					
					HttpAppHost hah=new HttpAppHost();
					hah.setReportTime(file.getName().split("_")[1]);
					//上网小区的id
					hah.setCellid(data[16]);
					//应用类
					hah.setAppType(Integer.parseInt(data[22]));
					//应用子类
					hah.setAppSubtype(Integer.parseInt(data[23]));
					//用户ip
					hah.setUserIP(data[26]);
					//用户port
					hah.setUserPort(Integer.parseInt(data[28]));
					//访问的服务ip
					hah.setAppServerIP(data[30]);
					//访问的服务port
					hah.setAppServerPort(Integer.parseInt(data[32]));
					//域名
					hah.setHost(data[58]);

					int appTypeCode=Integer.parseInt(data[18]);
					String transStatus=data[54];
					//业务逻辑处理
					if(hah.getCellid()==null||hah.getCellid().equals("")){
					hah.setCellid("000000000");
					}
					if(appTypeCode==103){
					hah.setAttempts(1);
					}
					if(appTypeCode==103 &&"10,11,12,13,14,15,32,33,34,35,36,37,38,48,49,50,51,52,53,54,55,199,200,201,202,203,204,205 ,206,302,304,306".contains(transStatus)){
					hah.setAccepts(1);
					}else{
					hah.setAccepts(0);
					}
					if(appTypeCode == 103){
					hah.setTrafficUL(Long.parseLong(data[33]));
					}
					if(appTypeCode == 103){
					hah.setTrafficDL(Long.parseLong(data[34]));
					}
					if(appTypeCode == 103){
					hah.setRetranUL(Long.parseLong(data[39]));
					}

					if(appTypeCode == 103){
					hah.setRetranDL(Long.parseLong(data[40]));
					}
					if(appTypeCode==103){
					hah.setTransDelay(Long.parseLong(data[20]) -Long.parseLong(data[19]));
					}
					CharSequence key=hah.getReportTime() + "|" + hah.getAppType() + "|" + hah.getAppSubtype() + "|" + hah.getUserIP() + "|" + hah.getUserPort() + "|" + hah.getAppServerIP() + "|" + hah.getAppServerPort() +"|" + hah.getHost() + "|" + hah.getCellid();
					if(map.containsKey(key)){
						HttpAppHost mapHah=map.get(key);
						mapHah.setAccepts(mapHah.getAccepts()+hah.getAccepts());
						mapHah.setAttempts(mapHah.getAttempts()+hah.getAttempts());
						mapHah.setTrafficUL(mapHah.getTrafficUL()+hah.getTrafficUL());
						mapHah.setTrafficDL(mapHah.getTrafficDL()+hah.getTrafficDL());
						mapHah.setRetranUL(mapHah.getRetranUL()+hah.getRetranUL());
						mapHah.setRetranDL(mapHah.getRetranDL()+hah.getRetranDL());
						mapHah.setTransDelay(mapHah.getTransDelay()+hah.getTransDelay());
						map.put(key, mapHah);
					}else{
						map.put(key,hah);
					}
				}
				OwnEnv.getMapQueue().add(map);
				ZkConnectRunner.setFree();
				System.out.println("engine1-01已将合并完map存入OwnEnv.MapQue,map大小"+map.size());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
