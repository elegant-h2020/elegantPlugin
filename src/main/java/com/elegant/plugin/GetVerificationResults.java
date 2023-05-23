package com.elegant.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 3/28/23
 */
public class GetVerificationResults extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String volume_dir = Configurations.verif_volume_dir;
        File tmpFolder = new File(volume_dir);


        //Used to get the result Files and metadata for each file
        ArrayList<HashMap> list_metadata = getEntriesLoop();

        //List result files
        File[] filesInTmpFolder = tmpFolder.listFiles();

        if (filesInTmpFolder == null || filesInTmpFolder.length == 0) {
            Messages.showInfoMessage("No files in temporary folder.", "Success");
            return;
        }

        //Create struct for the real file names
        String[] fileNames = new String[filesInTmpFolder.length];
        //Create struct for the virtual file names
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
            //Get entry_id of the selected file
            String entr_id_selct_file = getLastPart(filesInTmpFolder[selectedFileIndex].getName());
            //Get metadata for the file 
            HashMap metadata = list_metadata.get(Integer.parseInt((entr_id_selct_file)));
            String metadata_str = formatHashMaptoString(metadata);
            Messages.showInfoMessage(metadata_str,"Metadata-Info");
        }
    }

/*
    Iterate over the results of VerificationService
 */

    public ArrayList<HashMap> getEntriesLoop(){

        String host = Configurations.verif_service_ip;
        String port = Configurations.verif_service_port;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntries");
        int count =0 ;

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 202) {
                String json_string = EntityUtils.toString(response.getEntity());
                JSONParser parser = new JSONParser();
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
            return null;
        }

        /*
            List of HashMaps to store metada
            of each entry.
         */
        ArrayList<HashMap>  list_map = new ArrayList<HashMap>();
        HashMap<String, String> metadata_map;
        //i corressponds to each
        //entryId, results retrived
        //inorder
        for (int i=0 ; i<count; i++){
             metadata_map = getEnrty(i);
             list_map.add(metadata_map);
        }

        return list_map;
    }

/*
    Call get entry to retrive
    the json files inside the volume
 */
    public HashMap<String, String> getEnrty(int entry_id){

        String host = Configurations.verif_service_ip;
        String port = Configurations.verif_service_port;

        HashMap<String, String>  metadata_map= new HashMap<String, String>();

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //Need the Get request to get the file
        HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntry?entryId="+entry_id);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {

                String json_string = EntityUtils.toString(response.getEntity());
                metadata_map = buildMetadataMap(json_string);

            }
            response.close();
            return metadata_map;

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

    /*
        A method returning String
        containing Metadata Info
        easily-interpreted by user
     */
    public static HashMap<String, String> buildMetadataMap(String json_response) throws JsonProcessingException {

        HashMap<String, String>  metadata_map= new HashMap<String, String>();

        //EntryUtils won't show response in debug
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json_response);
        String task_id_key="taskId";
        if (jsonNode.has(task_id_key)) {
            String value = jsonNode.get(task_id_key).asText();
            System.out.println("Value for key '" + task_id_key + "' is: " + value);
            metadata_map.put(task_id_key,value);
        } else {
            System.out.println("JSON does not contain key '" + task_id_key + "'");
        }
        String request_key = "request";
        String className_key = "className";
        if (jsonNode.has(request_key) && jsonNode.get(request_key).has(className_key)) {
            String value = jsonNode.get(request_key).get(className_key).asText();
            System.out.println("Value for key '" + className_key + "' is: " + value);
            metadata_map.put(className_key,value);
        }
        String methodName_key = "methodName";
        if (jsonNode.has(request_key) && jsonNode.get(request_key).has(methodName_key)) {
            String value = jsonNode.get(request_key).get(methodName_key).asText();
            System.out.println("Value for key '" + methodName_key + "' is: " + value);
            metadata_map.put(methodName_key,value);
        }
        String verificTool_key = "verificationTool";
        if (jsonNode.has(verificTool_key)) {
            String value = jsonNode.get(verificTool_key).asText();
            System.out.println("Value for key '" + value + "' is: " + value);
            metadata_map.put(verificTool_key,value);
        }

        return metadata_map;
    }

    public static String getLastPart(String string) {
        String[] parts = string.split("_");
        parts = parts[parts.length-1].split(".json");
        //enrtryId of file
        return parts[parts.length - 1];
    }

    /*
     A function for pretty printing the
     Hash-Map
     */
    public static String formatHashMap(HashMap<?, ?> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            builder.append("\"").append(entry.getKey()).append("\":\"");
            builder.append(entry.getValue() == null ? "null" : entry.getValue()).append("\",");
        }
        if (builder.charAt(builder.length() - 1) == ',') {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("}");
        return builder.toString();
    }

    /*
    Return standarized text
     */

    public static String formatHashMaptoString(HashMap<String, String> map) {
        StringBuilder builder = new StringBuilder();

        String taskid = map.get("taskId");

       String className = map.get("className");

        String methodName = map.get("methodName");

        String verificTool = map.get("verificationTool");

        String result_text = "Metadata for query with  taskId :"+taskid+ "\n"+
                "VerificationTool used: "+verificTool +".\n";
        if(!className.isEmpty()){
            result_text = result_text + "Class name : "+className+"\n";
        }

        if(!methodName.isEmpty()){
            result_text = result_text + "Method name : "+methodName;
        }


        return result_text;
    }

}

