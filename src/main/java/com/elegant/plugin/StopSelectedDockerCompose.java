package com.elegant.plugin;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.Message;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Stop Selected Docker compose
 */
public class StopSelectedDockerCompose extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //Get slected file
        VirtualFile selectedFile = e.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE);


        // Perform your desired action on the selected file
        if (selectedFile != null) {

            String path = selectedFile.getPath();
            Process process = null;

            if(!isDockerComposeFile(selectedFile.getName())){
                Messages.showErrorDialog("Need *docker-compose* file ","Error Loading Docker-Compose");
                return;
            }

            try {
                process = Runtime.getRuntime().exec("docker-compose  -f " + path  + " down");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            if(!process.isAlive() && process.exitValue() !=0  ){
                Messages.showErrorDialog( e.getProject(), "Error running docker-compose exit value: "+String.valueOf(process.exitValue()),"Error Running docker-compose");
                return;
            }

            Messages.showInfoMessage("Check Docker-Services ","");
        } else Messages.showErrorDialog("No file selected","Error Loading Docker-Compose");
    }

    /*
Check if is Docker-File contains valid substring
 */
    public boolean isDockerComposeFile(String str){
        if (str == null) {
            return false;
        }

        return str.contains("docker-compose");
    }
}
