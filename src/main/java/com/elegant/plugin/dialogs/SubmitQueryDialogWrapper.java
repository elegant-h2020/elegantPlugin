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
 * @date 5/4/22
 */

public class SubmitQueryDialogWrapper extends com.intellij.openapi.ui.DialogWrapper {
    JPanel panel = new JPanel(new GridBagLayout());
    JTextField userQuery = new JTextField();
    JTextField placement = new JTextField();

    public String userQuerytext ;
    public String placementQuerytext ;

    public SubmitQueryDialogWrapper() {
        super(true);
        setTitle("Query Plan");
        init();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        GridBag gb = new GridBag();
        gb.setDefaultInsets(new Insets(0,0, AbstractLayout.DEFAULT_VGAP,AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400,200));
        panel.add(label("userQuery"),gb.nextLine().next().weightx(0.2));
        panel.add(userQuery,gb.nextLine().next().weightx(0.8));

        panel.add(label("Placement(BottomUp or TopDown)"),gb.nextLine().next().weightx(0.2));
        panel.add(placement,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    public void doOKAction(){
        userQuerytext = userQuery.getText();
        placementQuerytext = placement.getText();

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
