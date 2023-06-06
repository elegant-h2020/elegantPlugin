package com.elegant.plugin;

import com.elegant.plugin.dialogs.ConfigDialogWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 5/26/22
 */

/**
 * Configure port an IP to
 * send data to Coordinator
 */
public class ConfigAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ConfigDialogWrapper cdw = new ConfigDialogWrapper();
        if (cdw.showAndGet()) {
            Configurations.coordinator_port = cdw.porttext;
            Configurations.coordinator_ip = cdw.hosttext;
            Messages.showMessageDialog(e.getProject(),"Host IP set to : "+cdw.hosttext+
                    " Port set to : "+ cdw.porttext,"Config Management",Messages.getInformationIcon());
        }
    }
}
