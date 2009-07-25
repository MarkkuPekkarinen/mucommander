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

import com.mucommander.file.AbstractFile;
import com.mucommander.ui.action.AbstractActionDescriptor;
import com.mucommander.ui.action.ActionCategories;
import com.mucommander.ui.action.ActionCategory;
import com.mucommander.ui.action.MuAction;
import com.mucommander.ui.action.ActionFactory;
import com.mucommander.ui.main.MainFrame;
import com.mucommander.ui.main.table.FileTable;
import com.mucommander.ui.main.table.FileTableModel;

import java.util.Hashtable;

import javax.swing.KeyStroke;

/**
 * This action compares the content of the 2 MainFrame's file tables and marks the files that are different.
 *
 * @author Maxence Bernard
 */
public class CompareFoldersAction extends MuAction {

    public CompareFoldersAction(MainFrame mainFrame, Hashtable properties) {
        super(mainFrame, properties);
    }

    public void performAction() {
        FileTable leftTable = mainFrame.getLeftPanel().getFileTable();
        FileTable rightTable = mainFrame.getRightPanel().getFileTable();

        FileTableModel leftTableModel = leftTable.getFileTableModel();
        FileTableModel rightTableModel = rightTable.getFileTableModel();

        int nbFilesLeft = leftTableModel.getFileCount();
        int nbFilesRight = rightTableModel.getFileCount();
        int fileIndex;
        String tempFileName;
        AbstractFile tempFile;
        for(int i=0; i<nbFilesLeft; i++) {
            tempFile = leftTableModel.getFileAt(i);
            if(tempFile.isDirectory())
                continue;

            tempFileName = tempFile.getName();
            fileIndex = -1;
            for(int j=0; j<nbFilesRight; j++)
                if (rightTableModel.getFileAt(j).getName().equals(tempFileName)) {
                    fileIndex = j;
                    break;
                }
            if (fileIndex==-1 || rightTableModel.getFileAt(fileIndex).getDate()<tempFile.getDate()) {
                leftTableModel.setFileMarked(tempFile, true);
                leftTable.repaint();
            }
        }

        for(int i=0; i<nbFilesRight; i++) {
            tempFile = rightTableModel.getFileAt(i);
            if(tempFile.isDirectory())
                continue;

            tempFileName = tempFile.getName();
            fileIndex = -1;
            for(int j=0; j<nbFilesLeft; j++)
                if (leftTableModel.getFileAt(j).getName().equals(tempFileName)) {
                    fileIndex = j;
                    break;
                }
            if (fileIndex==-1 || leftTableModel.getFileAt(fileIndex).getDate()<tempFile.getDate()) {
                rightTableModel.setFileMarked(tempFile, true);
                rightTable.repaint();
            }
        }

        // Notify registered listeners that currently marked files have changed on the file tables
        leftTable.fireMarkedFilesChangedEvent();
        rightTable.fireMarkedFilesChangedEvent();
    }
    
    public static class Factory implements ActionFactory {

		public MuAction createAction(MainFrame mainFrame, Hashtable properties) {
			return new CompareFoldersAction(mainFrame, properties);
		}
    }
    
    public static class Descriptor extends AbstractActionDescriptor {
    	public static final String ACTION_ID = "CompareFolders";
    	
		public String getId() { return ACTION_ID; }

		public ActionCategory getCategory() { return ActionCategories.Selection; }

		public KeyStroke getDefaultAltKeyStroke() { return null; }

		public KeyStroke getDefaultKeyStroke() { return KeyStroke.getKeyStroke("control M"); }
    }
}
