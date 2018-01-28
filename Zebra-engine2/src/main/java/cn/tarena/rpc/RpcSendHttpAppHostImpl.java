package cn.tarena.rpc;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.avro.AvroRemoteException;

import cn.tarena.reducer.ReducerRunner;
import cn.tarena.zk.ZkConnectRunner;
import rpc.domain.HttpAppHost;
import rpc.service.RpcSendHttpAppHost;

public class RpcSendHttpAppHostImpl implements RpcSendHttpAppHost{
	public static BlockingQueue<Map<CharSequence,HttpAppHost>> mapQueue=new LinkedBlockingQueue<>();
	@Override
	public Void sendHttpAppHost(HttpAppHost httpAppHost) throws AvroRemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void sendHahMap(Map<CharSequence, HttpAppHost> hahMap) throws AvroRemoteException {
		System.out.println("二级引擎收到map数据："+hahMap.size());
		mapQueue.add(hahMap);
		//4不能写死：自己改
		
		/*if(mapQueue.size()==ZkConnectRunner.num){
			//启动合并线程
			new Thread(new ReducerRunner());
		}*/
		if(mapQueue.size()==4){
			//启动合并线程
			new Thread(new ReducerRunner()).start();
		}
		return null;
	}

}
