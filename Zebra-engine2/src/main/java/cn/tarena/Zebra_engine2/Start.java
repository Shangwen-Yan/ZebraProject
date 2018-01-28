package cn.tarena.Zebra_engine2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.tarena.rpc.RpcServer;
import cn.tarena.zk.ZkConnectRunner;

public class Start {
	public static void main(String[] args) {
		ExecutorService es=Executors.newCachedThreadPool();
		es.submit(new ZkConnectRunner());
		es.submit(new RpcServer());
	}
}
