package com.elegant.plugin;

import com.elegant.plugin.dialogs.SubmitClassVerfictationDialogWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 3/13/23
 */
public class VerifyClassAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String host = Configurations.verif_service_ip;
        String port = Configurations.verif_service_port;


        SubmitClassVerfictationDialogWrapper vqdw = new SubmitClassVerfictationDialogWrapper();
        if (vqdw.showAndGet()) {
            //if ok is pressed
            String class_path = vqdw.class_pathText;
            String request = vqdw.requestText;

            if (class_path.isEmpty()) {
                class_path = "/tmp/Simple.class";
            }

            if (request.isEmpty()) {
                request = "/tmp/request-class.json";
            }


            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("http://" + host + ":" + port + "/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry");
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addPart("file", new FileBody(new File(class_path)))
                    .addPart("request", new FileBody(new File(request)))
                    .build();
            httpPost.setEntity(entity);

            try {
                CloseableHttpResponse response = httpClient.execute(httpPost);
                Messages.showMessageDialog(e.getProject(), String.valueOf(response.getStatusLine().getStatusCode()), "Status_code", Messages.getInformationIcon());
                if (response.getStatusLine().getStatusCode() == 202) {
                    String json = EntityUtils.toString(response.getEntity());
                    Messages.showMessageDialog(e.getProject(), json, "Class Submitted for verification", Messages.getInformationIcon());
                }
                response.close();

            } catch (FileNotFoundException ex){
                Messages.showMessageDialog(e.getProject(),"Files not found","Something's worng",Messages.getInformationIcon());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    httpClient.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }


            try {
                httpClient.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
