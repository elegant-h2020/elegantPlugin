package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class StartSelectedDockerCompose extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile selectedFile = e.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE);


        // Perform your desired action on the selected file
        if (selectedFile != null) {
            // TODO: Implement your logic here
            System.out.println(selectedFile.getName());
        }
    }
}
