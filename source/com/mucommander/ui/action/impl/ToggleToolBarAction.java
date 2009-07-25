/*
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2009 Maxence Bernard
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mucommander.ui.action.impl;

import com.mucommander.conf.impl.MuConfiguration;
import com.mucommander.text.Translator;
import com.mucommander.ui.action.AbstractActionDescriptor;
import com.mucommander.ui.action.ActionCategories;
import com.mucommander.ui.action.ActionCategory;
import com.mucommander.ui.action.ActionFactory;
import com.mucommander.ui.action.MuAction;
import com.mucommander.ui.main.MainFrame;

import javax.swing.*;

import java.util.Hashtable;

/**
 * This action shows/hides the current MainFrame's {@link com.mucommander.ui.main.toolbar.ToolBar} depending on its
 * current visible state: if it is visible, hides it, if not shows it.
 *
 * <p>This action's label will be updated to reflect the current visible state.
 *
 * <p>Each time this action is executed, the new current visible state is stored in the configuration so that
 * new MainFrame windows will use it to determine whether the ToolBar has to be made visible or not.
 *
 * @author Maxence Bernard
 */
public class ToggleToolBarAction extends MuAction {

    public ToggleToolBarAction(MainFrame mainFrame, Hashtable properties) {
        super(mainFrame, properties, false);
        setLabel(Translator.get(MuConfiguration.getVariable(MuConfiguration.TOOLBAR_VISIBLE,
                                                                 MuConfiguration.DEFAULT_TOOLBAR_VISIBLE)
                                ? com.mucommander.ui.action.impl.ToggleToolBarAction.class.getName()+".hide":com.mucommander.ui.action.impl.ToggleToolBarAction.class.getName()+".show"));
    }

    public void performAction() {
        JPanel toolBarPanel = mainFrame.getToolBarPanel();
        boolean visible = !toolBarPanel.isVisible();
        // Save the last toolbar visible state in the configuration, this will become the default for new MainFrame windows.
        MuConfiguration.setVariable(MuConfiguration.TOOLBAR_VISIBLE, visible);
        // Change the label to reflect the new toolbar state
        setLabel(Translator.get(visible?com.mucommander.ui.action.impl.ToggleToolBarAction.class.getName()+".hide":com.mucommander.ui.action.impl.ToggleToolBarAction.class.getName()+".show"));
        // Show/hide the toolbar
        toolBarPanel.setVisible(visible);
        mainFrame.validate();
    }
    
    public static class Factory implements ActionFactory {

		public MuAction createAction(MainFrame mainFrame, Hashtable properties) {
			return new ToggleToolBarAction(mainFrame, properties);
		}
    }
    
    public static class Descriptor extends AbstractActionDescriptor {
    	public static final String ACTION_ID = "ToggleToolBar";
    	
		public String getId() { return ACTION_ID; }

		public ActionCategory getCategory() { return ActionCategories.View; }

		public KeyStroke getDefaultAltKeyStroke() { return null; }

		public KeyStroke getDefaultKeyStroke() { return null; }
    }
}
