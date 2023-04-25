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
            Process process = Runtime.getRuntime().exec("docker stop elegant-code-verification-container ",null,new File(Configurations.docker_verif_file_path));

            process.waitFor();

            Process process2 = Runtime.getRuntime().exec("docker rm elegant-code-verification-container ",null,new File(Configurations.docker_verif_file_path));

            process2.waitFor();

            Process clean_vol = Runtime.getRuntime().exec("docker volume rm service_files");

            clean_vol.waitFor();
            Process rm_tmp_verif_folder = Runtime.getRuntime().exec("rm -rf "+Configurations.temp_verific_directory);

            rm_tmp_verif_folder.waitFor();

            Process rm_tmp_storage = Runtime.getRuntime().exec("rm -rf "+Configurations.verif_volume_dir);

            rm_tmp_storage.waitFor();

            Messages.showInfoMessage("Docker VerificationService Artifcats "+ " Deleted","Remove Artifacts");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
