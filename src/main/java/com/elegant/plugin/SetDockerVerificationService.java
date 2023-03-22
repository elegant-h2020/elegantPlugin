package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipInputStream;

import static com.elegant.plugin.SetDockerEnviroment.extract;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 2/23/23
 */
public class SetDockerVerificationService extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        try {

            //Copy zip to temp Folder
            InputStream verifFolder = getClass().getClassLoader().getResourceAsStream("VerificationService.zip");
            Path tempVERIFDirectory = Files.createTempDirectory("verification-service");

            if (verifFolder != null) {
                try (ZipInputStream zipInputStream = new ZipInputStream(verifFolder)) {
                    // Extract the zip contents and keep in temp directory
                    extract(zipInputStream, tempVERIFDirectory.toFile());
                }
            }
            String path = tempVERIFDirectory.toFile().getPath()+"/Elegant-Code-Verification-Service-main/";
            File f_path = new File(path);

            //Delete pre-built containers just in case
            Process deleteprocess = Runtime.getRuntime().exec("docker rm elegant-code-verification-container", null, f_path);

            deleteprocess.waitFor();

            Process process = Runtime.getRuntime().exec(
                    "docker build -t code-verification-service-container .",null,f_path);

            process.waitFor();

            Process process2 = Runtime.getRuntime().exec(
                    "docker run --name elegant-code-verification-container -i -p 8080:8080 code-verification-service-container",null,f_path);

            Configurations.temp_verific_directory = tempVERIFDirectory.toFile().getPath();

            Configurations.docker_verif_file_path = path ;

            Configurations.verif_service_ip = "localhost";

            Configurations.verif_service_port = "8080";

            Messages.showMessageDialog(e.getProject(),"Docker Verification Service is running on port 8080 \n","Enviroment Set Up",Messages.getInformationIcon());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
