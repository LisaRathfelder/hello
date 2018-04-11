package com.oencue.client;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.oencue.shared.LoginInfo;

@RemoteServiceRelativePath("notemapper")
public interface noteMapper extends RemoteService{
	String access2Database(String name,int cmd) throws IllegalArgumentException;


}
