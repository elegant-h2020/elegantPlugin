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
 * @date 3/21/23
 */
public class GetVerificationEnrtryDialogWrapper extends com.intellij.openapi.ui.DialogWrapper{
    JPanel panel = new JPanel(new GridBagLayout());
    JTextField entry= new JTextField();

    public String entry_id ;

    public GetVerificationEnrtryDialogWrapper() {
        super(true);
        setTitle("Entry ID");
        init();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        GridBag gb = new GridBag();
        gb.setDefaultInsets(new Insets(0,0, AbstractLayout.DEFAULT_VGAP,AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400,200));
        panel.add(label("Entry Number"),gb.nextLine().next().weightx(0.2));
        panel.add(entry,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    public void doOKAction(){
        entry_id = entry.getText();
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
