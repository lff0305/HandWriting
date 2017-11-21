package org.lff.handwriting;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;

/**
 * @author Feifei Liu
 * @datetime 2017-11-21  11:31
 */
public class AboutDialog {
    public AboutDialog() {

    }

    public void init(MainDialog parent) {
        // create jeditorpane
        JEditorPane jEditorPane = new JEditorPane();

        // make it read-only
        jEditorPane.setEditable(false);

        // create a scrollpane; modify its attributes as desired
        JScrollPane scrollPane = new JScrollPane(jEditorPane);

        // add an html editor kit
        HTMLEditorKit kit = new HTMLEditorKit();
        jEditorPane.setEditorKit(kit);

        // add some styles to the html
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
        styleSheet.addRule("h1 {color: blue;}");
        styleSheet.addRule("h2 {color: #ff0000;}");
        styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");

        // create some simple html as a string
        String htmlString = "<html>\n"
                + "<body>\n"
                + "<p>By Liu Feifei, 2017</p>\n"
                + "<p>This project, is based on OpenSource projects, including iText, slf4j, MigLayout, etc.</p>\n"
                + "<p>And font resources from JARDOTTY, etc.</p>\n"
                + "<p>Any bugs, or suggestions, please <a href=\"lff0305@gmail.com\">email me</a></p>\n"
                + "<p>Or submit <a href=\"https://github.com/lff0305\">an issue</a> at GitHub.</p>\n"
                + "</body>\n";

        // create a document, set it on the jeditorpane, then add the html
        Document doc = kit.createDefaultDocument();
        jEditorPane.setDocument(doc);
        jEditorPane.setText(htmlString);

        // now add it all to a frame
        JDialog j = new JDialog(parent);
        j.getContentPane().add(scrollPane, BorderLayout.CENTER);
        j.setTitle("About");
        // make it easy to close the application
        j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // display the frame
        j.setSize(new Dimension(400, 350));

        // pack it, if you prefer
        j.pack();

        j.setAlwaysOnTop(true);
        // center the jframe, then make it visible
        j.setLocationRelativeTo(parent);
        j.setVisible(true);
    }
}
