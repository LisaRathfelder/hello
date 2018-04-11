package com.oencue.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.oencue.shared.LoginInfo;

public interface LoginServiceAsync {

	void login(String requestUri, AsyncCallback<LoginInfo> callback);

}
