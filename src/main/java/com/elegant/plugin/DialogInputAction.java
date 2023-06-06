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
 * Simple example of Dialog action with
 *  a Dialog wrapper
 */
public class DialogInputAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        QueryPlanDialogWrapper qpdw = new QueryPlanDialogWrapper();
        if (qpdw.showAndGet()){
            Messages.showMessageDialog(e.getProject(),qpdw.querid,"helloworld",Messages.getInformationIcon());
        }
    }
}
