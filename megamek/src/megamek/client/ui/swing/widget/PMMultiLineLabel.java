package megamek.client.ui.swing.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is a label that can stretch across multiple lines
 * @author NickAragua
 *
 */
public class PMMultiLineLabel extends JPanel {
    private List<String> labels = new ArrayList<>();

    /**
     * Constructs a new multi-line label
     * @param fm Font metrics object
     * @param c Color for the text on this label
     */
    public PMMultiLineLabel(Font f, Color c) {
        setFont(f);
        setForeground(c);

        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);
    }
    
    /**
     * Clear the contents of this multi-line label
     */
    public void clear() {
        labels.clear();
        super.removeAll();
    }
    
    /**
     * Add a string to this multi-line label
     * @param s The string to add
     */
    public void addString(String s) {
        labels.add(s);
        add(new JLabel(s));
    }
}
