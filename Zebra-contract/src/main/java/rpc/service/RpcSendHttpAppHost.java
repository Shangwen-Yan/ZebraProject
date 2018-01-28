/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package rpc.service;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface RpcSendHttpAppHost {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"RpcSendHttpAppHost\",\"namespace\":\"rpc.service\",\"types\":[{\"type\":\"record\",\"name\":\"HttpAppHost\",\"namespace\":\"rpc.domain\",\"fields\":[{\"name\":\"reportTime\",\"type\":[\"string\",\"null\"]},{\"name\":\"cellid\",\"type\":[\"string\",\"null\"]},{\"name\":\"appType\",\"type\":[\"int\",\"null\"]},{\"name\":\"appSubtype\",\"type\":[\"int\",\"null\"]},{\"name\":\"userIP\",\"type\":[\"string\",\"null\"]},{\"name\":\"userPort\",\"type\":[\"int\",\"null\"]},{\"name\":\"appServerIP\",\"type\":[\"string\",\"null\"]},{\"name\":\"appServerPort\",\"type\":[\"int\",\"null\"]},{\"name\":\"host\",\"type\":[\"string\",\"null\"]},{\"name\":\"attempts\",\"type\":[\"int\",\"null\"]},{\"name\":\"accepts\",\"type\":[\"int\",\"null\"]},{\"name\":\"trafficUL\",\"type\":[\"long\",\"null\"]},{\"name\":\"trafficDL\",\"type\":[\"long\",\"null\"]},{\"name\":\"retranUL\",\"type\":[\"long\",\"null\"]},{\"name\":\"retranDL\",\"type\":[\"long\",\"null\"]},{\"name\":\"transDelay\",\"type\":[\"long\",\"null\"]}]}],\"messages\":{\"sendHttpAppHost\":{\"request\":[{\"name\":\"httpAppHost\",\"type\":\"rpc.domain.HttpAppHost\"}],\"response\":\"null\"},\"sendHahMap\":{\"request\":[{\"name\":\"hahMap\",\"type\":{\"type\":\"map\",\"values\":\"rpc.domain.HttpAppHost\"}}],\"response\":\"null\"}}}");
  java.lang.Void sendHttpAppHost(rpc.domain.HttpAppHost httpAppHost) throws org.apache.avro.AvroRemoteException;
  java.lang.Void sendHahMap(java.util.Map<java.lang.CharSequence,rpc.domain.HttpAppHost> hahMap) throws org.apache.avro.AvroRemoteException;

  @SuppressWarnings("all")
  public interface Callback extends RpcSendHttpAppHost {
    public static final org.apache.avro.Protocol PROTOCOL = rpc.service.RpcSendHttpAppHost.PROTOCOL;
    void sendHttpAppHost(rpc.domain.HttpAppHost httpAppHost, org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
    void sendHahMap(java.util.Map<java.lang.CharSequence,rpc.domain.HttpAppHost> hahMap, org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
  }
}