package cn.tarena.rpc;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.commons.collections.bag.SynchronizedSortedBag;

import rpc.service.RpcSendHttpAppHost;

public class RpcServer implements Runnable{

	@Override
	public void run() {
		NettyServer server=new NettyServer(
				new SpecificResponder(RpcSendHttpAppHost.class, new RpcSendHttpAppHostImpl()), 
				new InetSocketAddress(7777));
		System.out.println("engine2NettyServer已启动");
		
	}
	

}
