package com.elegant.plugin;

import com.elegant.plugin.dialogs.GetVerificationEnrtryDialogWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 3/21/23
 */
public class GetVerificationStatusAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String host = Configurations.verif_service_ip;
        String port = Configurations.verif_service_port;

        GetVerificationEnrtryDialogWrapper gqvw = new GetVerificationEnrtryDialogWrapper();
        if (gqvw.showAndGet()) {
            //if ok is pressed
            String entry_id = gqvw.entry_id;

            if (entry_id.isEmpty()) {
                Messages.showErrorDialog(e.getProject(),"Please provide entry id","Entry is empty");
                throw new RuntimeException("Entry runtime");
            }

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntry?entryId="+entry_id);
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                Messages.showMessageDialog(e.getProject(), String.valueOf(response.getStatusLine().getStatusCode()), "Status_code", Messages.getInformationIcon());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String json_string = EntityUtils.toString(response.getEntity());

                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(json_string);

                    Messages.showMessageDialog(e.getProject(), json.getAsString("status"), "Entry Status", Messages.getInformationIcon());
                }
                response.close();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ParseException ex) {
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
