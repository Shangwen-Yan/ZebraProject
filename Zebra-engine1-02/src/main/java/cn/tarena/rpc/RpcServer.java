package cn.tarena.rpc;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.specific.SpecificResponder;

import cn.tarena.zebra.common.OwnEnv;
import rpc.service.RpcFileSplit;

public class RpcServer implements Runnable{

	@Override
	public void run() {
		try {
			NettyServer Server=new NettyServer(new SpecificResponder(RpcFileSplit.class, new RpcFileSplitImpl()), new InetSocketAddress(OwnEnv.getRpcport()));
			System.out.println("engine1-02NettyServer已启动");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
