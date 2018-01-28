package cn.tarena.rpc;

import org.apache.avro.AvroRemoteException;

import cn.tarena.zebra.common.OwnEnv;
import rpc.domain.FileSplit;
import rpc.service.RpcFileSplit;

public class RpcFileSplitImpl implements RpcFileSplit {

	@Override
	public Void sendFileSplit(FileSplit fileSplit) throws AvroRemoteException {
		OwnEnv.getSpiltQueue().add(fileSplit);
		System.out.println("一级引擎engine1-02已将数据"+fileSplit+"存入OwnEnv.SplitQueue");
		return null;
	}

}
