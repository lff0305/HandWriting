package org.lff.handwriting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainDialog extends JDialog {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
    private JMenuBar menuBar;

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


        this.setTitle("Handing Writing Generator " + getVersion());

        logger.info("MainDialog inited.");
    }

    private void onOK() {

        Option option = Option.getInstance();
        setOptions(option);

        SwingUtilities.invokeLater(() -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
            jfc.setDialogTitle("Set Filename");
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
            jfc.addChoosableFileFilter(filter);

            int returnValue = jfc.showOpenDialog(contentPane);
            if (returnValue != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String file = jfc.getSelectedFile().getPath();
            pool.submit(() -> {
                createPDF(file, option);
            });
        });
    }

    private void createPDF(String file, Option option) {
        try {
            String fileName = !file.endsWith(".pdf") ? file + ".pdf" : file;
            File diskFile = new File(fileName);
            if (diskFile.isDirectory()) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(contentPane, fileName + " is a directory");
                });
                return;
            }
            if (diskFile.exists()) {
                int r = JOptionPane.showConfirmDialog(contentPane, fileName + " exists. Overwrite?", "Warning", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            byte[] data = PDFUtil.getContent(this.textArea1.getText(), option);
            FileOutputStream fw = new FileOutputStream(fileName);
            fw.write(data);
            fw.close();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(contentPane, "File " + fileName + " created.");
            });
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(contentPane, "File not created : " + e.getMessage());
            });
        }

    }

    private void onCancel() {
        this.dispose();
        logger.info("Program Exited.");
        System.exit(0);
    }

    private void onAbout() {
        AboutDialog d = new AboutDialog();
        d.init(this);
    }

    private PreviewPanel getPreviewPanel() {
        return (PreviewPanel) previewPanel;
    }

    private void createUIComponents() {
        scrollPane = new JScrollPane();
        previewPanel = new PreviewPanel(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        configPanel = new ConfigPanel();

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menuFile = new JMenu("File");
        JMenu menuAbout = new JMenu("Help");

        JMenuItem menuItemExit = new JMenuItem("Exit", null);
        JMenuItem menuItemAbout = new JMenuItem("About", null);
        menuAbout.add(menuItemAbout);
        menuFile.add(menuItemExit);
        menuBar.add(menuFile);
        menuBar.add(menuAbout);

        menuItemExit.addActionListener( l-> {
            onCancel();
        });
        menuItemAbout.addActionListener( l -> {
            onAbout();
        });
    }

    private String getVersion() {
        Properties p = new Properties();
        try {
            p.load(this.getClass().getResourceAsStream("/org/lff/handwriting/version.properties"));
        } catch (IOException e) {
            logger.error("Cannot load version.properties", e);
            return "NA";
        }
        String major = p.getProperty("major");
        String minor = p.getProperty("minor");
        String build = p.getProperty("buildNo");
        String timestamp = p.getProperty("timestamp");
        return major + "." + minor + " build " + build + " " + timestamp;
    }

    public void setOptions(Option options) {
        ConfigPanel panel = (ConfigPanel) configPanel;
        options.setAddEmptyLineAfter(panel.isAddEmptyLine());
        options.setSkipEmptyLine(panel.isSkipEmptyLine());
    }
}
