package com.elegant.plugin;

import com.elegant.plugin.dialogs.ConfigDialogWrapper;
import com.elegant.plugin.dialogs.ConfigVerifDialogWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 3/27/23
 */

/**
 * Config Verification service port and IP
 */
public class ConfigVerificationAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ConfigVerifDialogWrapper cdw = new ConfigVerifDialogWrapper();
        if (cdw.showAndGet()) {
            Configurations.verif_service_port = cdw.porttext;
            Configurations.verif_service_ip = cdw.hosttext;
            Messages.showMessageDialog(e.getProject(),"Host IP set to : "+cdw.hosttext+
                    " Port set to : "+ cdw.porttext,"Config Management",Messages.getInformationIcon());
        }
    }
}