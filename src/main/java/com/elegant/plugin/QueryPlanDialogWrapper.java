package com.elegant.plugin;

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
 * @date 5/3/22
 */

/**
 * Simple query plain dialog
 */
public class QueryPlanDialogWrapper extends com.intellij.openapi.ui.DialogWrapper {
    JPanel panel = new JPanel(new GridBagLayout());
    JTextField usernamme = new JTextField();
    JTextField id = new JTextField();

    public String querid ;

    protected QueryPlanDialogWrapper() {
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
        panel.add(label("username"),gb.nextLine().next().weightx(0.2));
        panel.add(usernamme,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    public void doOKAction(){
        querid = usernamme.getText();
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
