package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 5/3/22
 */

/**
 * Dimple example of Dialog Action
 */
public class DialogAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog(e.getProject(),"hello world","helloworld",Messages.getInformationIcon());
    }
}
