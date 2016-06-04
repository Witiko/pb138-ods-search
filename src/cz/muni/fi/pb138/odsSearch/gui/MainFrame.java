package cz.muni.fi.pb138.odsSearch.gui;

import cz.muni.fi.pb138.odsSearch.common.Cell;
import cz.muni.fi.pb138.odsSearch.common.SpreadsheetImpl;
import cz.muni.fi.pb138.odsSearch.common.Queriable;
import cz.muni.fi.pb138.odsSearch.common.SpreadsheetImplException;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class represents the main frame of the GUI of ods-search.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public class MainFrame extends javax.swing.JFrame {

    private final JFileChooser fileChooser = new JFileChooser();
    private Set<Queriable<Cell>> spreadsheets = new HashSet<>();
    private final ResultTableModel results = new ResultTableModel();
    private final ContextTableModel context = new ContextTableModel();
    private final ResourceBundle bundle = ResourceBundle.getBundle("cz/muni/fi/"
            + "pb138/odsSearch/gui/MainFrame");
    private final JComponent[] interactables;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        // Initialize the frame.
        init();
        interactables = new JComponent[] {
            fileChooserButton, submitButton, caseSensitiveCheckBox,
            exactMatchCheckBox, resultTable, contextTable
        };
    }
    
    /**
     * Initialize the frame and its components.
     */
    private void init() {
        // Initialize the Netbeans components.
        initComponents();
        // Set up the file chooser.
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                bundle.getString("fileExtensionODS"), "ods"));
        fileChooser.setDialogTitle(bundle.getString("fileChooserTitle"));
        // Set up the context table.
        ListSelectionModel cellSelectionModel = resultTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent e) {
            int[] selectedRow = resultTable.getSelectedRows();
            if (selectedRow.length == 1)
                context.switchFocus(results.getRow(resultTable.
                        convertRowIndexToModel(selectedRow[0])));
          }
        });
    }
    
    /**
     * Displays an error message.
     * @param message the error message.
     */
    private void error(String message) {    
        JOptionPane.showMessageDialog(null,message,
                bundle.getString("errorDialogTitle"),
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Disable the GUI while a Swing worker is running.
     */
    private void disableGUI() {
        for (JComponent c : interactables) {
            c.setEnabled(false);
        }
    }
    
    /**
     * Enable the GUI while after a Swing worker is done running.
     */
    private void enableGUI() {
        for (JComponent c : interactables)
            c.setEnabled(true);
    }
    
    /**
     * This swing worker retrieves the results for a query out of the spreadsheets.
     */
    private class QuerySwingWorker extends SwingWorker<Void,Void> {
        
        private final Set<Cell> result = new HashSet<>();
        private final String query;
        private final boolean caseSensitive;
        private final boolean exactMatch;
        
        public QuerySwingWorker(String query, boolean caseSensitive,
                boolean exactMatch) {
            // Disable the GUI.
            submitButton.setText(bundle.getString("submitButtonLabelActive"));
            disableGUI();
            // Set up the private members.
            this.query = query;
            this.caseSensitive = caseSensitive;
            this.exactMatch = exactMatch;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            for (Queriable<Cell> finder : spreadsheets)
                result.addAll(finder.queryFixedString(query,
                        caseSensitive, exactMatch));
            return null;
        }
        
        @Override
        protected void done() {
            // Display the number of results.
            resultTableLabel.setText(bundle.getString("resultTableLabel") +
                    (result.size() > 0 ? " (" + result.size() + "):" : ":"));
            // Swap the result table data and change the context table focus.
            results.swapList(result);
            context.switchFocus(null);
            // Reenable the GUI.
            submitButton.setText(bundle.getString("submitButtonLabel"));
            enableGUI();
        }
    
    }
    
    /**
     * This swing worker constructs spreadsheets out of the supplied files.
     */
    private class ConstructorSwingWorker extends SwingWorker<Void,Void> {

        private final File[] files;
        private final List<File> processedFiles = new ArrayList<>();
        private final Map<File,SpreadsheetImplException> failedFiles =
                new HashMap<>();
        
        public ConstructorSwingWorker(File[] files) {
            // Disable the GUI.
            fileChooserButton.setText(bundle.getString(
                    "fileChooserButtonLabelActive"));
            disableGUI();
            // Set up the private members.
            this.files = files;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            spreadsheets = new HashSet<>();
            for (File file : files) {
                try {
                    spreadsheets.add(new SpreadsheetImpl(file));
                    processedFiles.add(file);
                } catch (SpreadsheetImplException e) {
                    failedFiles.put(file, e);
                }
            }
            return null;
        }   
        
        @Override
        protected void done() {
            // Set the content of the file chooser textfield.
            filesTextField.setText(processedFiles.toString());
            // Handle the failed files.
            if (!failedFiles.isEmpty()) {
                StringBuilder buffer = new StringBuilder();
                buffer.append("<html>").
                        append(bundle.getString("errorDialogTextOpen")).
                        append("<ul>");
                for (Entry<File, SpreadsheetImplException> entry :
                        failedFiles.entrySet()) {
                    buffer.append("<li>").append(entry.getKey()).append(" : ").
                            append(entry.getValue()).append("</li>");
                }
                buffer.append("</ul></html>");
                error(buffer.toString());
            }
            // Reenable the GUI.
            fileChooserButton.setText(bundle.getString(
                    "fileChooserButtonLabel"));
            enableGUI();
        }
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        filesTextField = new javax.swing.JTextField();
        fileChooserButton = new javax.swing.JButton();
        filesTextFieldLabel = new javax.swing.JLabel();
        queryTextAreaLabel = new javax.swing.JLabel();
        caseSensitiveCheckBox = new javax.swing.JCheckBox();
        exactMatchCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        queryTextArea = new javax.swing.JTextArea();
        submitButton = new javax.swing.JButton();
        resultTableLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        contextTable = new javax.swing.JTable();
        contextTableLabel = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(bundle.getString("mainTitle")
        );

        filesTextField.setEditable(false);
        filesTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filesTextFieldActionPerformed(evt);
            }
        });

        fileChooserButton.setText(bundle.getString("fileChooserButtonLabel"));
        fileChooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserButtonActionPerformed(evt);
            }
        });

        filesTextFieldLabel.setText(bundle.getString("filesTextFieldLabel"));

        queryTextAreaLabel.setText(bundle.getString("queryTextAreaLabel"));

        caseSensitiveCheckBox.setText(bundle.getString("caseSensitiveLabel"));
        caseSensitiveCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                caseSensitiveCheckBoxActionPerformed(evt);
            }
        });

        exactMatchCheckBox.setText(bundle.getString("exactMatchLabel"));

        queryTextArea.setColumns(20);
        queryTextArea.setRows(5);
        queryTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(queryTextArea);

        submitButton.setText(bundle.getString("submitButtonLabel"));
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        resultTableLabel.setText(bundle.getString("resultTableLabel") + ":");

        resultTable.setAutoCreateRowSorter(true);
        resultTable.setModel(results);
        jScrollPane4.setViewportView(resultTable);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        contextTable.setModel(context);
        jScrollPane3.setViewportView(contextTable);

        contextTableLabel.setText(bundle.getString("contextTableLabel"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(filesTextFieldLabel)
                                .addGap(14, 14, 14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(queryTextAreaLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(filesTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileChooserButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(exactMatchCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(caseSensitiveCheckBox)
                                    .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resultTableLabel)
                            .addComponent(contextTableLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(filesTextFieldLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fileChooserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(queryTextAreaLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(caseSensitiveCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(exactMatchCheckBox)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(submitButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultTableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addComponent(contextTableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void caseSensitiveCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_caseSensitiveCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_caseSensitiveCheckBoxActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        String query = queryTextArea.getText();
        boolean caseSensitive = caseSensitiveCheckBox.isSelected();
        boolean exactMatch = exactMatchCheckBox.isSelected();
        new QuerySwingWorker(query, caseSensitive, exactMatch).execute();
    }//GEN-LAST:event_submitButtonActionPerformed

    private void fileChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserButtonActionPerformed
        int retval = fileChooser.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            new ConstructorSwingWorker(fileChooser.getSelectedFiles()).execute();
        }
    }//GEN-LAST:event_fileChooserButtonActionPerformed

    private void filesTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filesTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filesTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox caseSensitiveCheckBox;
    private javax.swing.JTable contextTable;
    private javax.swing.JLabel contextTableLabel;
    private javax.swing.JCheckBox exactMatchCheckBox;
    private javax.swing.JButton fileChooserButton;
    private javax.swing.JTextField filesTextField;
    private javax.swing.JLabel filesTextFieldLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea queryTextArea;
    private javax.swing.JLabel queryTextAreaLabel;
    private javax.swing.JTable resultTable;
    private javax.swing.JLabel resultTableLabel;
    private javax.swing.JButton submitButton;
    // End of variables declaration//GEN-END:variables
}
