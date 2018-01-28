package cn.tarena.Zebra_engine1_01;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.tarena.mapper.MapperRunner;
import cn.tarena.rpc.RpcClient;
import cn.tarena.rpc.RpcServer;
import cn.tarena.zk.ZkConnectRunner;

public class Start {
	public static void main(String[] args) {
		
		ExecutorService es=Executors.newCachedThreadPool();
		es.submit(new ZkConnectRunner());
		es.submit(new RpcServer());
		es.submit(new MapperRunner());
		es.submit(new RpcClient());
	}

}
