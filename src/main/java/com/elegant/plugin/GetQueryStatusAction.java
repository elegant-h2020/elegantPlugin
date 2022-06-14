package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GetQueryStatusAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        CloseableHttpClient httpClient = httpClient = HttpClients.createDefault();
        String host = Configurations.coordinator_ip;
        String port = Configurations.coordinator_port;
        String str = Messages.showInputDialog(e.getProject(), "Enter queryID", "Get Status Query", Messages.getInformationIcon());

        HttpGet httpGet = new HttpGet("http://"+host+":"+port+"/v1/nes/queryCatalog/status?queryId=" + str);
        httpGet.setHeader(new BasicHeader("cache-control", "no-cache"));

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Messages.showMessageDialog(e.getProject(),result,"QueryStatus: "+str,Messages.getInformationIcon());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

}
