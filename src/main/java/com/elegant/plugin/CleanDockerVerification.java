package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 2/24/23
 */
public class CleanDockerVerification extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            Process process = Runtime.getRuntime().exec("docker-compose down",null,new File(Configurations.docker_verif_file_path));
            process.waitFor();
            Runtime.getRuntime().exec("rm  -rf " + Configurations.temp_verific_directory  );
            Messages.showInfoMessage("Docker VerificationService Artifcats "+ " Deleted","Remove Artifacts");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

    }
}
