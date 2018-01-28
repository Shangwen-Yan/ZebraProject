package cn.tarena.rpc;

import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.zookeeper.ZooKeeper;

import cn.tarena.zebra.common.GlobalEnv;
import cn.tarena.zebra.common.OwnEnv;
import rpc.service.RpcSendHttpAppHost;

public class RpcClient implements Runnable{
	private ZooKeeper zk;
	@Override
	public void run() {
		zk=GlobalEnv.connectZkServer();
		try {
			byte[] data=zk.getData(GlobalEnv.getEngine2path(), null, null);
			String ip=new String(data).split("/")[1];
			int port=Integer.parseInt(new String(data).split("/")[2]);
			NettyTransceiver client=new NettyTransceiver(new InetSocketAddress(ip, port));
			RpcSendHttpAppHost proxy=SpecificRequestor.getClient(RpcSendHttpAppHost.class, client);
			while(true)
			{
				proxy.sendHahMap(OwnEnv.getMapQueue().take());
				System.out.println("以及引擎engine1-01已从mapQueue取出一个map并发送到二级引擎");
			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
