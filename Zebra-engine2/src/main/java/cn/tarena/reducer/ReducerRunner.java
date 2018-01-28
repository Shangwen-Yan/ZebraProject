package cn.tarena.reducer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.tarena.rpc.RpcSendHttpAppHostImpl;
import cn.tarena.zebra.db.ZebraDB;
import rpc.domain.HttpAppHost;

/**
 * 是一个线程类，用于将4个map合成一个map，（用户数据的合并）
 * 此外，将最后合并的map做数据落地（落地到数据库里）
 * @author ysq
 *
 */
public class ReducerRunner implements Runnable{

	 //创建结果集map
		private Map<String,HttpAppHost> map=new HashMap<>();

		@Override
		public void run() {
			while(true){
				Map<CharSequence,HttpAppHost> reduceMap=
							RpcSendHttpAppHostImpl.mapQueue.poll();
				
					if(reduceMap==null){
							//证明已经所有任务都归并完，跳出循环
							System.out.println("跳出循环");
							break;
					}else{
						
						for(Entry<CharSequence,HttpAppHost> entry:reduceMap.entrySet()){
						//用户标识
						String key=entry.getKey().toString();
								
						HttpAppHost hah=entry.getValue();
						if(map.containsKey(key)){
									//做数据累加
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
								map.put(key, hah);
						}
								
							}
						}
					
					}
					System.out.println("经过engine2处理，最后结果map的大小"+map.size());
					//map已经处理完数据，进行落地
					ZebraDB.toDb(map);
				
				}

}
