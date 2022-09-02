package com.elegant.plugin;

import com.elegant.plugin.dialogs.SubmitQueryDialogWrapper;
import com.elegant.plugin.dialogs.SubmitQueryDialogWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import net.minidev.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 5/4/22
 */
public class SubmitQueryAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String host = Configurations.coordinator_ip;
        String port = Configurations.coordinator_port;

        SubmitQueryDialogWrapper sqdw = new SubmitQueryDialogWrapper();
        if (sqdw.showAndGet()){
            //if ok is pressed
            String userQuery = sqdw.userQuerytext;
            String placement = sqdw.placementQuerytext;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userQuery",userQuery);
            jsonObject.put("strategyName",placement);


            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("http://"+host+":"+port+"/v1/nes/query/execute-query");
            httpPost.addHeader("content-type", "application/json");
            StringEntity stringEntity;
            stringEntity = new StringEntity(jsonObject.toString().replace("\\\\\\","\\"), ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);

            try {
                CloseableHttpResponse response = httpClient.execute(httpPost);
                Messages.showMessageDialog(e.getProject(),userQuery+ " " + placement ,"UserQuery and Placement",Messages.getInformationIcon());
                Messages.showMessageDialog(e.getProject(), String.valueOf(response.getStatusLine().getStatusCode()),"Status_code",Messages.getInformationIcon());
                if(response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201 ) {
                    String json = EntityUtils.toString(response.getEntity());
                    Messages.showMessageDialog(e.getProject(), json, "ID", Messages.getInformationIcon());
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            try {
                httpClient.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }

    }
}
