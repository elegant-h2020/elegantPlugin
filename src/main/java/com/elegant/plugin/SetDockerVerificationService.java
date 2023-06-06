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

/**
 * Set Docker Artifacts for Verification Service
 */
public class SetDockerVerificationService extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {


        try {

            //Create temp for volume
            Path verif_vol_storage = Files.createTempDirectory("verif_storage");
            Configurations.verif_volume_dir = verif_vol_storage.toString();

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

            /*
               If image not prebuild it may be slow
             */
//            Process process = Runtime.getRuntime().exec(
//                    "docker build -t code-verification-service-container .",null,f_path);

            //process.waitFor();

            //Delete docker volume
            Process process_cr_volume = Runtime.getRuntime().exec(
                    "docker volume create service_files"
            );
            //Wait for process to finish
            process_cr_volume.waitFor();

            //Run docker container
            Process process2 = Runtime.getRuntime().exec(
                    "docker run --name elegant-code-verification-container -v "+Configurations.verif_volume_dir+":/service/files -i -p 8080:8080 code-verification-service-container",null,f_path);


            Configurations.temp_verific_directory = tempVERIFDirectory.toFile().getPath();

            Configurations.docker_verif_file_path = path ;

            Configurations.verif_service_ip = "localhost";

            Configurations.verif_service_port = "8080";

            //printResults(process2);

            Messages.showMessageDialog(e.getProject(),"Docker Verification Service is running on port 8080 \n","Enviroment Set Up",Messages.getInformationIcon());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }



    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}


