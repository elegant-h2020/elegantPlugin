package com.elegant.plugin;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.Message;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class StartSelectedDockerCompose extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile selectedFile = e.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE);


        // Perform your desired action on the selected file
        if (selectedFile != null) {

            String path = selectedFile.getPath();
            Process process = null;

            if(!isDockerComposeFile(selectedFile.getName())){
                Messages.showErrorDialog("Need *docker-compose* file to start the services","Error Loading Docker-Compose");
                return;
            }

            try {
                process = Runtime.getRuntime().exec("docker-compose  -f " + path  + " up --force-recreate");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            if(!process.isAlive() && process.exitValue() !=0  ){
                Messages.showErrorDialog( e.getProject(), "Error running docker-compose exit value: "+String.valueOf(process.exitValue()),"Error Running docker-compose");
                return;
            }

            Messages.showInfoMessage("Check Docker-Services running","");
        } else Messages.showErrorDialog("No file selected","Error Loading Docker-Compose");
    }

    /*
Check if is Docker-File string
 */
    public boolean isDockerComposeFile(String str){
        if (str == null) {
            return false;
        }

        return str.contains("docker-compose");
    }
}
