package com.oencue.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.oencue.shared.LoginInfo;

public interface noteMapperAsync {

	void access2Database(String name, int cmd, AsyncCallback<String> callback);



}
