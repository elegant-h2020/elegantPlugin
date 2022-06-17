package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import kotlin.text.Charsets;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.testcontainers.containers.DockerComposeContainer;

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
public class SetDockerEnviroment extends AnAction {
    @Override
    @ClassRule
    public void actionPerformed(@NotNull AnActionEvent e) {
       /*  DockerComposeContainer environment =
                new DockerComposeContainer(new File("src/main/resources/compose-test.yml"));*/
        try {

            //Copy zip to temp Folder
            InputStream nesFolder = getClass().getClassLoader().getResourceAsStream("nes.zip");
            Path tempNESDirectory = Files.createTempDirectory("nes");

            if (nesFolder != null) {
                try (ZipInputStream zipInputStream = new ZipInputStream(nesFolder)) {
                    // Extract the zip contents and keep in temp directory
                    extract(zipInputStream, tempNESDirectory.toFile());
                }
            }



            System.out.println(tempNESDirectory);
            String path = tempNESDirectory.toFile().getPath()+"/nebulastream-tutorial/docker-compose-local.yml";
            Process process = Runtime.getRuntime().exec("docker-compose  -f " + path  + " up --force-recreate");
            Configurations.docker_file_path = path ;
            Configurations.temp_nes_directory = tempNESDirectory.toFile().getPath();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

// Read the output from the commandmthis was bloacking the app
/*
            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
*/

// Read any errors from the attempted command.this was also blocking
            //keep for debug
/*
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
*/


            Messages.showMessageDialog(e.getProject(),"Visit localhost:3000","Enviroment Set Up",Messages.getInformationIcon());

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
