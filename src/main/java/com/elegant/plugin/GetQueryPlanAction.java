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

public class GetQueryPlanAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String host = Configurations.coordinator_ip;
        String port = Configurations.coordinator_port;
        CloseableHttpClient httpClient = httpClient = HttpClients.createDefault();
        String str = Messages.showInputDialog(e.getProject(), "Enter queryID", "Get Query Plan", Messages.getInformationIcon());

        HttpGet httpGet = new HttpGet("http://"+host+":"+port+"/v1/nes/query/query-plan?queryId=" + str);
        httpGet.setHeader(new BasicHeader("cache-control", "no-cache"));

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Messages.showMessageDialog(e.getProject(),result,"Query Plan of queryID: "+str,Messages.getInformationIcon());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
}
