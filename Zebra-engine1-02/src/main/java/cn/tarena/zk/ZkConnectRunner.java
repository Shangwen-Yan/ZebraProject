package cn.tarena.zk;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import cn.tarena.zebra.common.GlobalEnv;
import cn.tarena.zebra.common.OwnEnv;

public class ZkConnectRunner implements Runnable{
	private static ZooKeeper zk;
	@Override
	public void run() {
		try {
			zk=GlobalEnv.connectZkServer();
			if(zk.exists(GlobalEnv.getEngine1path(), null)==null){
				zk.create(GlobalEnv.getEngine1path(), null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			}
			//DESKTOP-KQ9M3EK/10.8.42.106/9991/
			String info=InetAddress.getLocalHost()+"/"+OwnEnv.getRpcport();
			zk.create(GlobalEnv.getEngine1path()+OwnEnv.getZnodepath(), info.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			System.out.println("engine1-02已连接，并创建一级引擎节点/node02，存入数据："+info);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public static void setBusy() throws KeeperException, InterruptedException, UnknownHostException {
		String info=InetAddress.getLocalHost()+"/"+OwnEnv.getRpcport()+"/busy";
		zk.setData(GlobalEnv.getEngine1path()+OwnEnv.getZnodepath(), info.getBytes(), -1);
		System.out.println("engine1-02已繁忙");
		
	}
	public static void setFree() throws UnknownHostException, KeeperException, InterruptedException {
		String info=InetAddress.getLocalHost()+"/"+OwnEnv.getRpcport()+"/free";
		
		zk.setData(GlobalEnv.getEngine1path()+OwnEnv.getZnodepath(), info.getBytes(), -1);
		System.out.println("engine1-02已空闲");
		
		
	}

}
