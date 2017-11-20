package org.lff.handwriting;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainDialog extends JDialog {

    private static ExecutorService pool = Executors.newCachedThreadPool();

    private static final long serialVersionUID = 2401381172141590048L;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;
    private JPanel configPanel;
    private JTabbedPane tabbedPane;
    private JPanel previewPanel;
    private JScrollPane scrollPane;

    public MainDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane s = (JTabbedPane) (e.getSource());
                int index = s.getSelectedIndex();   
                if (index == 1) {
                    SwingUtilities.invokeLater(() -> {
                        preview();
                    });
                }
            }

            private void preview() {
                String text = textArea1.getText();
                PreviewUtil.preview(scrollPane, previewPanel, text);
            }
        });
    }

    private void onOK() {
        SwingUtilities.invokeLater(() -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
            jfc.setDialogTitle("Set Filename");
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
            jfc.addChoosableFileFilter(filter);

            int returnValue = jfc.showOpenDialog(null);
            if (returnValue != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String file = jfc.getSelectedFile().getPath();
            pool.submit(() -> {
                createPDF(file);
            });
        });
    }

    private void createPDF(String file) {
        try {
            String fileName = !file.endsWith(".pdf") ? file + ".pdf" : file;
            byte[] data = PDFUtil.getContent(this.textArea1.getText(), new Option());
            FileOutputStream fw = new FileOutputStream(fileName);
            fw.write(data);
            fw.close();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "File " + fileName + " created.");
            });
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "File not created : " + e.getMessage());
            });
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private PreviewPanel getPreviewPanel() {
        return (PreviewPanel) previewPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        scrollPane = new JScrollPane();
        previewPanel = new PreviewPanel(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        configPanel = new ConfigPanel();
    }
}
