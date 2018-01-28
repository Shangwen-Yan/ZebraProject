package cn.tarena.zk;

import java.util.List;

import org.apache.zookeeper.ZooKeeper;

import cn.tarena.file.FileToBlock;
import cn.tarena.rpc.RpcClient;
import cn.tarena.zebra.common.GlobalEnv;
/**
 * 
 * 根据节点的data，从zookeeper中获取以及引擎的ip地址和端口号
 * @author CGB-yz
 *
 */
public class ZkConnectRunner implements Runnable{
	private ZooKeeper zk;
	@Override
	public void run() {
		try {
			zk=GlobalEnv.connectZkServer();
			zk.setData(GlobalEnv.getEngine1path(),(FileToBlock.num+"").getBytes(), -1);
			List<String> paths=	zk.getChildren(GlobalEnv.getEngine1path(), null);
			for(String path:paths){
				
				new Thread(new RpcClient(path,zk)).start();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
