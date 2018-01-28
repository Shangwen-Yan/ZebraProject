package cn.tarena.zk;

import java.net.InetAddress;
import java.util.concurrent.Callable;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

import cn.tarena.zebra.common.GlobalEnv;

/**
 * 负责注册二级引擎的服务信息，ip，rpc端口号
 * @author ysq
 *
 */
public class ZkConnectRunner implements Runnable{
	public static int num;
	private ZooKeeper zk;
	@Override
	public void run() {
		try {
			zk=GlobalEnv.connectZkServer();
			/*byte[] data=zk.getData(GlobalEnv.getEngine1path(), null, null);
			if(data!=null){
				num=Integer.parseInt(new String(data));
			}*/
			
			String info=InetAddress.getLocalHost()+"/"+"7777";
			zk.create(GlobalEnv.getEngine2path(),info.getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			
			System.out.println("engine2已连接，并创建二级引擎节点/engine2，存入数据："+info);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}