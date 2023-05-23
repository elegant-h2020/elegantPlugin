package com.elegant.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ViewDashboard extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent a) {
        String url = "http://www.google.com";
        {
            Runtime runtime = Runtime.getRuntime();
            try {

                Process process = runtime.exec("xdg-open " + url);
                process.waitFor();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    }

