package com.elegant.plugin;

import com.elegant.plugin.dialogs.SubmitClassVerfictationDialogWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 3/14/23
 */
public class EsbmcVerificationAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String host = Configurations.verif_service_ip;
        String port = Configurations.verif_service_port;

        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();

        // The update method below is only called periodically so need
        // to be careful to check for selected text
        if (caretModel.getCurrentCaret().hasSelection()) {
            //Code-block selected
            String codeString = caretModel.getCurrentCaret().getSelectedText();

            //Create files:code,request
            //For Post method
            try {

                //The path of the created code-file
                Path temp = Files.createTempFile("code",".c");
                //Write the "copied" contents
                Files.write(temp,codeString.getBytes(StandardCharsets.UTF_8));

                //Creating a JSONObject object
                JSONObject jsonObject = new JSONObject();
                //Inserting key-value pairs into the json object
                jsonObject.put("tool", "ESBMC");
                jsonObject.put("fileName", temp.toFile().getName());

                //Escape "special" char
                String escaped_str = jsonObject.toString().replace("\\/", "/");

                //Create request file
                Path request_file = Files.createTempFile("request",".json");
                Files.write(request_file,escaped_str.getBytes(StandardCharsets.UTF_8));

                //POST METHOD CREATION


                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost("http://" + host + ":" + port + "/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry");
                HttpEntity entity = MultipartEntityBuilder.create()
                        .addPart("file", new FileBody(temp.toFile()))
                        .addPart("request", new FileBody(request_file.toFile()))
                        .build();
                httpPost.setEntity(entity);


                try {
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    //Show response message
                    Messages.showMessageDialog(e.getProject(), String.valueOf(response.getStatusLine().getStatusCode()), "Status_code", Messages.getInformationIcon());

                    if (response.getStatusLine().getStatusCode() == 202) {
                        String json = EntityUtils.toString(response.getEntity());
                        Messages.showMessageDialog(e.getProject(), json, "Class Submitted for verification", Messages.getInformationIcon());
                    }

                } catch (FileNotFoundException ex){
                    Messages.showMessageDialog(e.getProject(),"Files not found","Something's worng",Messages.getInformationIcon());
                } catch (IOException ex) {
                    Messages.showErrorDialog("Exception :"+ex.toString(),"Exception");
                    throw new RuntimeException(ex);
                }

                //Delete code file
                Files.delete(temp);
                //Delete Request File
                Files.delete(request_file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else{
            Messages.showErrorDialog("You need to pre-select a code block","Select Code-block");
        }

    }
}