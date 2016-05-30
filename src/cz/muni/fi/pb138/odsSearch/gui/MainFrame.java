package cz.muni.fi.pb138.odsSearch.gui;

import cz.muni.fi.pb138.odsSearch.common.Cell;
import cz.muni.fi.pb138.odsSearch.common.DummySpreadsheetImpl;
import cz.muni.fi.pb138.odsSearch.common.Queriable;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class represents the main frame of the GUI of ods-search.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public class MainFrame extends javax.swing.JFrame {

    private final JFileChooser fileChooser = new JFileChooser();
    private Set<Queriable<Cell>> spreadsheets = new HashSet<>();
    private final ResultListModel results = new ResultListModel();
    private final ResourceBundle bundle = ResourceBundle.getBundle("cz/muni/fi/"
            + "pb138/odsSearch/gui/MainFrame");
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        // Initialize the frame.
        init();
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
    }
    
    /**
     * This swing worker retrieves the results out of 
     */
    private class QuerySwingWorker extends SwingWorker<Void,Void> {
        
        private final Set<Cell> result = new HashSet<>();
        private final String query;
        private final boolean caseSensitive;
        private final boolean exactMatch;
        
        public QuerySwingWorker(String query, boolean caseSensitive, boolean exactMatch) {
            this.query = query;
            this.caseSensitive = caseSensitive;
            this.exactMatch = exactMatch;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            for (Queriable<Cell> finder : spreadsheets) {
                result.addAll(finder.queryFixedString(query,
                        caseSensitive, exactMatch));
            }
            return null;
        }
        
        @Override
        protected void done() {
            // Swap the result sets.
            results.swapList(result);
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

        filesTextField = new javax.swing.JTextField();
        fileChooserButton = new javax.swing.JButton();
        filesTextFieldLabel = new javax.swing.JLabel();
        queryTextAreaLabel = new javax.swing.JLabel();
        caseSensitiveCheckBox = new javax.swing.JCheckBox();
        exactMatchCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        queryTextArea = new javax.swing.JTextArea();
        submitButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        resultList = new javax.swing.JList<>();
        resultListLabel = new javax.swing.JLabel();

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
        jScrollPane1.setViewportView(queryTextArea);

        submitButton.setText(bundle.getString("submitButtonLabel"));
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        resultList.setModel(results);
        jScrollPane3.setViewportView(resultList);

        resultListLabel.setText(bundle.getString("resultListLabel"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(exactMatchCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(caseSensitiveCheckBox)
                                    .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(resultListLabel)
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
                        .addComponent(filesTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
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
            File[] files = fileChooser.getSelectedFiles();
            filesTextField.setText(Arrays.toString(files));
            spreadsheets = new HashSet<>();
            for (File file : files) {
                spreadsheets.add(new DummySpreadsheetImpl(file));
            }
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
    private javax.swing.JCheckBox exactMatchCheckBox;
    private javax.swing.JButton fileChooserButton;
    private javax.swing.JTextField filesTextField;
    private javax.swing.JLabel filesTextFieldLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea queryTextArea;
    private javax.swing.JLabel queryTextAreaLabel;
    private javax.swing.JList<String> resultList;
    private javax.swing.JLabel resultListLabel;
    private javax.swing.JButton submitButton;
    // End of variables declaration//GEN-END:variables
}