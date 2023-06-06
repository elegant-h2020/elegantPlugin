package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 6/14/22
 */

/**
 * Clean docker artifacts related
 * to NES
 */
public class CleanDockerAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            Process process = Runtime.getRuntime().exec("docker-compose -f " + Configurations.docker_file_path + " down");
            process.waitFor();
            Runtime.getRuntime().exec("rm  -rf " + Configurations.temp_nes_directory  );
            Messages.showInfoMessage("NES-Docker Services Stopped","Delete NES-Docker Artifacts");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

    }
}
