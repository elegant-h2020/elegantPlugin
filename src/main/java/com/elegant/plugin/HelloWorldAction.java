package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 4/28/22
 */

/**
 * Simple example action
 */
public class HelloWorldAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        URL url = null;
        try {
            url = new URL("https://httpbin.org/ip");
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        URLConnection conn;
        try {
            conn = url.openConnection();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        InputStream is;
        try {
             is = conn.getInputStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        String str = is.toString();
        Messages.showInfoMessage(str, "info");
    }
}
