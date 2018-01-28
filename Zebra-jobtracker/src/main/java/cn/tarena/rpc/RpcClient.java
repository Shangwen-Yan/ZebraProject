package cn.tarena.rpc;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;



import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

import cn.tarena.zebra.common.GlobalEnv;
import rpc.domain.FileSplit;
import rpc.service.RpcFileSplit;

public class RpcClient implements Runnable{
	private String path;
	private ZooKeeper zk;
	public RpcClient(String path, ZooKeeper zk) {
		this.path=path;
		this.zk=zk;
	}


	@Override
	public void run() {
		try {
			byte[] data=zk.getData(GlobalEnv.getEngine1path()+"/"+path, null, null);
			String ip=new String(data).split("/")[1];
			int port=Integer.parseInt(new String(data).split("/")[2]);
			NettyTransceiver client=new NettyTransceiver(new InetSocketAddress(ip, port));
			final RpcFileSplit proxy=SpecificRequestor.getClient(RpcFileSplit.class, client);
			
			
			proxy.sendFileSplit(GlobalEnv.getSplitQueue().take());
			for(;;){
				final CountDownLatch cdl=new CountDownLatch(1);
				zk.getData(GlobalEnv.getEngine1path()+"/"+path,new Watcher(){
					@Override
					public void process(WatchedEvent event) {
						if(event.getType()==EventType.NodeDataChanged){
							
							try {
								byte[] data = zk.getData(GlobalEnv.getEngine1path()+"/"+path, null, null);
								if(new String(data).contains("busy")){
									cdl.countDown();
								}else{
									FileSplit split=GlobalEnv.getSplitQueue().poll();
									if(split==null){	
									}else{
										proxy.sendFileSplit(split);
										cdl.countDown();
									}
								}
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				} ,null);
				cdl.await();
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}



