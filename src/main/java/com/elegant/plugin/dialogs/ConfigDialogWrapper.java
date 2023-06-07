package com.elegant.plugin.dialogs;

import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author Ioannis Plakas
 * @email iplakas@ubitech.eu
 * @date 5/26/22
 */

/**
 * Dialog Wrapper to retrive
 * host and port
 */
public class ConfigDialogWrapper extends com.intellij.openapi.ui.DialogWrapper  {

    JPanel panel = new JPanel(new GridBagLayout());
    JTextField host = new JTextField();
    JTextField port = new JTextField();


    public String hosttext ;
    public String porttext ;

    public ConfigDialogWrapper() {
        super(true);
        setTitle("Config");
        init();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        GridBag gb = new GridBag();
        gb.setDefaultInsets(new Insets(0,0, AbstractLayout.DEFAULT_VGAP,AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400,200));
        panel.add(label("coordinator ip "),gb.nextLine().next().weightx(0.2));
        panel.add(host,gb.nextLine().next().weightx(0.8));

        panel.add(label("port"),gb.nextLine().next().weightx(0.2));
        panel.add(port,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    public void doOKAction(){
        hosttext = host.getText();
        porttext = port.getText();
        close(OK_EXIT_CODE);
    }

    private JComponent label(String str){
        JBLabel label = new JBLabel(str);
        label.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        label.setFontColor(UIUtil.FontColor.BRIGHTER);
        label.setBorder(JBUI.Borders.empty(0,5,2,0));
        return  label;
    }
}
