package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import kotlin.text.Charsets;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.testcontainers.containers.DockerComposeContainer;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 6/7/22
 */

/**
 * Set Docker-artifacts for NES
 */
public class SetDockerEnviroment extends AnAction {
    @Override
    @ClassRule
    public void actionPerformed(@NotNull AnActionEvent e) {

        try {
            //Copy zip to temp Folder
            InputStream nesFolder = getClass().getClassLoader().getResourceAsStream("nes.zip");
            //Create tempDir
            Path tempNESDirectory = Files.createTempDirectory("nes");

            if (nesFolder != null) {
                try (ZipInputStream zipInputStream = new ZipInputStream(nesFolder)) {
                    // Extract the zip contents and keep in temp directory
                    extract(zipInputStream, tempNESDirectory.toFile());
                }
            }

            System.out.println(tempNESDirectory);
            //Get path of tempNESDir
            String path = tempNESDirectory.toFile().getPath()+"/nebulastream-tutorial/docker-compose-local.yml";
            //Execute docker-compose command
            Process process = Runtime.getRuntime().exec("docker-compose  -f " + path  + " up --force-recreate");

            if(!process.isAlive() && process.exitValue() !=0  ){
                Messages.showErrorDialog( e.getProject(), "Error running docker-compose exit value: "+String.valueOf(process.exitValue()),"Error Running docker-compose");
                return;
            }

            System.out.println(process.pid());
            //Retrive Paths
            Configurations.docker_file_path = path ;
            Configurations.temp_nes_directory = tempNESDirectory.toFile().getPath();
            //Default values of Configuration
            Configurations.coordinator_ip="localhost";
            Configurations.coordinator_port="8081";

            //Show Dialog
            Messages.showMessageDialog(e.getProject(),"NES-Docker Services are running \n" +
                    "Visit localhost:3000 for ELEGANT UI\n" +
                    "ELEGANT Coordinator set on port 8081\n" +
                    "Instructions for Keyckloack user administration on \n" +
                    "https://github.com/elegant-h2020/elegantPlugin/blob/main/keyckloack_info/Keycloack_Info.md","Enviroment Set Up",Messages.getInformationIcon());

            //printResults(process);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }


    public static void extract(ZipInputStream zip, File target) throws IOException {
        try {
            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                File file = new File(target, entry.getName());

                if (!file.toPath().normalize().startsWith(target.toPath())) {
                    throw new IOException("Bad zip entry");
                }

                if (entry.isDirectory()) {
                    file.mkdirs();
                    continue;
                }

                //5 MB
                byte[] buffer = new byte[5 * 1000000];
                file.getParentFile().mkdirs();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                int count;

                while ((count = zip.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }

                out.close();
            }
        } finally {
            zip.close();
        }
    }

}
