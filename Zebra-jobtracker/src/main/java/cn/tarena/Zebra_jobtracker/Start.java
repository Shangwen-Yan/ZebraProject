package cn.tarena.Zebra_jobtracker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.tarena.file.FileHandle;
import cn.tarena.file.FileToBlock;
import cn.tarena.zk.ZkConnectRunner;

public class Start {
	public static void main(String[] args) {
		System.out.println("jobtracker启动");
		ExecutorService es=Executors.newCachedThreadPool();
		es.submit(new FileHandle());
		es.submit(new FileToBlock());
		es.submit(new ZkConnectRunner());
	}
}
