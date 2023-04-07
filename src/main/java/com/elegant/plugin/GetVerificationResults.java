package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 3/28/23
 */
public class GetVerificationResults extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        //GetEntries end of fucking story.. na
        //xtuphsw to api
        //kai na parw to response na dw ti einai
        getEntriesLoop();

        String volume_dir = Configurations.verif_volume_dir;
        File tmpFolder = new File(volume_dir);
        

        File[] filesInTmpFolder = tmpFolder.listFiles();
        if (filesInTmpFolder == null || filesInTmpFolder.length == 0) {
            Messages.showInfoMessage("No files in temporary folder.", "Success");
            return;
        }

        String[] fileNames = new String[filesInTmpFolder.length];
        String[] output_file_names = new String[filesInTmpFolder.length];
        for (int i = 0; i < filesInTmpFolder.length; i++) {
            fileNames[i] = filesInTmpFolder[i].getName();
            //output_file_name needs the last part
            //of the original file
            output_file_names[i] = "query_output_"+getLastPart(fileNames[i]);
        }

        int selectedFileIndex = Messages.showChooseDialog("Select a file to open:", "Files in temporary folder", output_file_names, output_file_names[0], null);
        if (selectedFileIndex >= 0 && selectedFileIndex < filesInTmpFolder.length) {
            Project project = e.getProject();
            if (project == null) {
                Messages.showErrorDialog("No project found.", "Error");
                return;
            }
            FileEditorManager editorManager = FileEditorManager.getInstance(project);

            VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filesInTmpFolder[selectedFileIndex].getAbsolutePath());
            if (virtualFile == null) {
                Messages.showErrorDialog("Error creating virtual file.", "Error");
                return;
            }
            editorManager.openFile(virtualFile, true);
        }
    }



    public void getEntriesLoop(){

        String host = Configurations.verif_service_ip;
        String port = Configurations.verif_service_port;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntries");
        int count =0 ;
        //HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntry?entryId="+entry_id);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 202) {
                String json_string = EntityUtils.toString(response.getEntity());

                JSONParser parser = new JSONParser();
                //JSONObject json = (JSONObject) parser.parse(json_string);
                JSONArray jsonArray = (JSONArray) parser.parse(json_string);
                count = jsonArray.size();
            }
            response.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (count== 0) {
            return;
        }

        for (int i=0 ; i<count; i++){
            getEnrty(i);
        }
    }

/*
    Call get entry to get
    the json files inside the volume
 */
    public void  getEnrty(int entry_id){

        String host = Configurations.verif_service_ip;
        String port = Configurations.verif_service_port;


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntry?entryId="+entry_id);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json_string = EntityUtils.toString(response.getEntity());
            }
            response.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                httpClient.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public static String getLastPart(String string) {
        String[] parts = string.split("_");
        parts = parts[parts.length-1].split(".json");
        return parts[parts.length - 1];
    }

}


