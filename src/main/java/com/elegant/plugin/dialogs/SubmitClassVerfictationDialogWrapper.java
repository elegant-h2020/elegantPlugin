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
 * @date 3/13/23
 */

/**
 * Dialog wrapper to set path
 * of class to be verified along with
 * request
 */
public class SubmitClassVerfictationDialogWrapper extends com.intellij.openapi.ui.DialogWrapper  {
    JPanel panel = new JPanel(new GridBagLayout());
    JTextField class_path = new JTextField();
    JTextField request= new JTextField();

    public String class_pathText;
    public String requestText;

    public SubmitClassVerfictationDialogWrapper() {
        super(true);
        setTitle("Verification Parameters");
        init();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        GridBag gb = new GridBag();
        gb.setDefaultInsets(new Insets(0,0, AbstractLayout.DEFAULT_VGAP,AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400,200));
        panel.add(label("Code File (Java Class or C File to be verified, eg. /tmp/Simple.class)"),gb.nextLine().next().weightx(0.2));
        panel.add(class_path,gb.nextLine().next().weightx(0.8));

        panel.add(label("Request File (.json File specifying Verification method, eg. /tmp/req.json)"),gb.nextLine().next().weightx(0.2));
        panel.add(request,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    public void doOKAction(){
        class_pathText = class_path.getText();
        requestText = request.getText();

        //send post
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
