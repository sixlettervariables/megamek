/**
 * MegaMek - Copyright (C) 2000,2001,2002,2004 Ben Mazur (bmazur@sev.org)
 * 
 *  This program is free software; you can redistribute it and/or modify it 
 *  under the terms of the GNU General Public License as published by the Free 
 *  Software Foundation; either version 2 of the License, or (at your option) 
 *  any later version.
 * 
 *  This program is distributed in the hope that it will be useful, but 
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 *  for more details.
 */

package megamek.client.ui.swing.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.BreakIterator;
import java.util.Locale;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRelation;
import javax.accessibility.AccessibleRelationSet;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.swing.text.AttributeSet;

public class PMSimpleLabel implements PMLabel, Accessible {

    // The String to display.
    String string;
    // The position of the label
    int x = 0;
    int y = 0;
    // The width and height of the label
    int width;
    int height;
    // The descent of the label
    int descent;
    // Color to draw the label with.
    Color color;
    // Font and Fontmetrics for the label
    Font f;
    FontMetrics fm;

    boolean visible = true;

    protected AccessiblePMLabel accessibleContext;

    private PMElement labelFor;

    /*
     * Create the label with the specified string, font and color
     */
    public PMSimpleLabel(String s, FontMetrics fm, Color c) {
        string = s;
        this.fm = fm;
        width = fm.stringWidth(string);
        height = fm.getHeight();
        descent = fm.getMaxDescent();
        color = c;
    }

    public void setString(String s) {
        String oldAccessibleName = null;
        if (accessibleContext != null) {
            oldAccessibleName = accessibleContext.getAccessibleName();
        }

        string = s;
        // The width use to just be the stringWidth, but this
        // sometimes caused cropping when setString was called.
        // The value of 140% was chosen by trial and error, and
        // may be incorrect. In fact, this whole fix is
        // basically a kludge, since I don't know why it
        // is needed.
        width = (int) Math.ceil(fm.stringWidth(string) * 1.4);
        height = fm.getHeight();
        descent = fm.getMaxDescent();

        if ((accessibleContext != null)
            && (accessibleContext.getAccessibleName() != oldAccessibleName)) {
                accessibleContext.firePropertyChange(
                        AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY,
                        oldAccessibleName,
                        accessibleContext.getAccessibleName());
        }
    }

    /*
     * Set the color of the label of the font.
     */
    public void setColor(Color c) {
        color = c;
    }

    /*
     * translate the coordinates of the label.
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /*
     * Draw the label.
     */
    public void drawInto(Graphics g) {
        if (!visible)
            return;
        Font font = g.getFont();
        Color temp = g.getColor();
        g.setColor(color);
        g.setFont(fm.getFont());
        g.drawString(string, x, y);
        g.setColor(temp);
        g.setFont(font);
    }

    public void setVisible(boolean v) {
        visible = v;
        if (accessibleContext != null) {
            accessibleContext.firePropertyChange(
                AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                AccessibleState.VISIBLE,
                null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y - height + descent, width, height);
    }

    /*
     * Returns the size of the label
     */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /*
     * Returns the descent of the label.
     */
    public int getDescent() {
        return descent;
    }

    public PMElement getLabelFor() {
        return labelFor;
    }

    public void setLabelFor(PMElement element) {
        labelFor = element;
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessiblePMLabel();
        }
        return accessibleContext;
    }

    protected class AccessiblePMLabel extends AccessibleContext implements AccessibleText {

        /**
         * Get the accessible name of this object.
         *
         * @return the localized name of the object -- can be null if this
         * object does not have a name
         * @see AccessibleContext#setAccessibleName
         */
        @Override
        public String getAccessibleName() {
            String name = PMSimpleLabel.this.string;
            if (name == null) {
                name = super.getAccessibleName();
            }
            return name;
        }

        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.LABEL;
        }

        /**
         * Get the AccessibleRelationSet associated with this object if one
         * exists.  Otherwise return null.
         * @see AccessibleRelation
         */
        public AccessibleRelationSet getAccessibleRelationSet() {
            // Check where the AccessibleContext's relation
            // set already contains a LABEL_FOR relation.
            AccessibleRelationSet relationSet
                = super.getAccessibleRelationSet();

            if (!relationSet.contains(AccessibleRelation.LABEL_FOR)) {
                PMElement element = PMSimpleLabel.this.getLabelFor();
                if (element != null) {
                    AccessibleRelation relation
                        = new AccessibleRelation(AccessibleRelation.LABEL_FOR);
                    relation.setTarget(element);
                    relationSet.add(relation);
                }
            }
            return relationSet;
        }

        @Override
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet states = new AccessibleStateSet();
            states.add(AccessibleState.ENABLED);
            if (visible) {
                states.add(AccessibleState.VISIBLE);
            }
            return null;
        }

        @Override
        public int getAccessibleIndexInParent() {
            return 0;
        }

        @Override
        public int getAccessibleChildrenCount() {
            // No children.
            return 0;
        }

        @Override
        public Accessible getAccessibleChild(int i) {
            // No children.
            return null;
        }

        @Override
        public AccessibleText getAccessibleText() {
            return this;
        }

        @Override
        public Locale getLocale() throws IllegalComponentStateException {
            return Locale.getDefault();
        }

        @Override
        public int getIndexAtPoint(Point p) {
            return 0;
        }

        @Override
        public Rectangle getCharacterBounds(int i) {
            return null;
        }

        /**
         * Return the number of characters (valid indicies)
         *
         * @return the number of characters
         */
        @Override
        public int getCharCount() {
            return accessibleContext.getAccessibleName().length();
        }

        /**
         * Return the zero-based offset of the caret.
         *
         * Note: That to the right of the caret will have the same index
         * value as the offset (the caret is between two characters).
         * @return the zero-based offset of the caret.
         */
        @Override
        public int getCaretPosition() {
            // There is no caret.
            return -1;
        }
        
        /**
         * Returns the String at a given index.
         *
         * @param part the AccessibleText.CHARACTER, AccessibleText.WORD,
         * or AccessibleText.SENTENCE to retrieve
         * @param index an index within the text &gt;= 0
         * @return the letter, word, or sentence,
         *   null for an invalid index or part
         */
        public String getAtIndex(int part, int index) {
            if (index < 0 || index >= getCharCount()) {
                return null;
            }
            switch (part) {
            case AccessibleText.CHARACTER:
                return getText(index, 1);
            case AccessibleText.WORD:
                {
                    String s = getText(0, getCharCount());
                    BreakIterator words = BreakIterator.getWordInstance(getLocale());
                    words.setText(s);
                    int end = words.following(index);
                    return s.substring(words.previous(), end);
                }
            case AccessibleText.SENTENCE:
                {
                    String s = getText(0, getCharCount());
                    BreakIterator sentence =
                        BreakIterator.getSentenceInstance(getLocale());
                    sentence.setText(s);
                    int end = sentence.following(index);
                    return s.substring(sentence.previous(), end);
                }
            default:
                return null;
            }
        }

        /**
         * Returns the String after a given index.
         *
         * @param part the AccessibleText.CHARACTER, AccessibleText.WORD,
         * or AccessibleText.SENTENCE to retrieve
         * @param index an index within the text &gt;= 0
         * @return the letter, word, or sentence, null for an invalid
         *  index or part
         */
        public String getAfterIndex(int part, int index) {
            if (index < 0 || index >= getCharCount()) {
                return null;
            }
            switch (part) {
            case AccessibleText.CHARACTER:
                if (index+1 >= getCharCount()) {
                   return null;
                }
                return getText(index+1, 1);
            case AccessibleText.WORD:
                {
                    String s = getText(0, getCharCount());
                    BreakIterator words = BreakIterator.getWordInstance(getLocale());
                    words.setText(s);
                    int start = words.following(index);
                    if (start == BreakIterator.DONE || start >= s.length()) {
                        return null;
                    }
                    int end = words.following(start);
                    if (end == BreakIterator.DONE || end >= s.length()) {
                        return null;
                    }
                    return s.substring(start, end);
                }
            case AccessibleText.SENTENCE:
                {
                    String s = getText(0, getCharCount());
                    BreakIterator sentence =
                        BreakIterator.getSentenceInstance(getLocale());
                    sentence.setText(s);
                    int start = sentence.following(index);
                    if (start == BreakIterator.DONE || start > s.length()) {
                        return null;
                    }
                    int end = sentence.following(start);
                    if (end == BreakIterator.DONE || end > s.length()) {
                        return null;
                    }
                    return s.substring(start, end);
                }
            default:
                return null;
            }
        }

        /**
         * Returns the String before a given index.
         *
         * @param part the AccessibleText.CHARACTER, AccessibleText.WORD,
         *   or AccessibleText.SENTENCE to retrieve
         * @param index an index within the text &gt;= 0
         * @return the letter, word, or sentence, null for an invalid index
         *  or part
         */
        public String getBeforeIndex(int part, int index) {
            if (index < 0 || index > getCharCount()-1) {
                return null;
            }
            switch (part) {
            case AccessibleText.CHARACTER:
                if (index == 0) {
                    return null;
                }
                return getText(index-1, 1);
            case AccessibleText.WORD:
                {
                    String s = getText(0, getCharCount());
                    BreakIterator words = BreakIterator.getWordInstance(getLocale());
                    words.setText(s);
                    int end = words.following(index);
                    end = words.previous();
                    int start = words.previous();
                    if (start == BreakIterator.DONE) {
                        return null;
                    }
                    return s.substring(start, end);
                }
            case AccessibleText.SENTENCE:
                {
                    String s = getText(0, getCharCount());
                    BreakIterator sentence =
                        BreakIterator.getSentenceInstance(getLocale());
                    sentence.setText(s);
                    int end = sentence.following(index);
                    end = sentence.previous();
                    int start = sentence.previous();
                    if (start == BreakIterator.DONE) {
                        return null;
                    }
                    return s.substring(start, end);
                }
            default:
                return null;
            }
        }

        /**
         * Return the AttributeSet for a given character at a given index
         *
         * @param i the zero-based index into the text
         * @return the AttributeSet of the character
         */
        @Override
        public AttributeSet getCharacterAttribute(int i) {
            // No special formatting
            return null;
        }

        /**
         * Returns the start offset within the selected text.
         * If there is no selection, but there is
         * a caret, the start and end offsets will be the same.
         *
         * @return the index into the text of the start of the selection
         */
        public int getSelectionStart() {
            // Text cannot be selected.
            return -1;
        }

        /**
         * Returns the end offset within the selected text.
         * If there is no selection, but there is
         * a caret, the start and end offsets will be the same.
         *
         * @return the index into the text of the end of the selection
         */
        public int getSelectionEnd() {
            // Text cannot be selected.
            return -1;
        }

        /**
         * Returns the portion of the text that is selected.
         *
         * @return the String portion of the text that is selected
         */
        public String getSelectedText() {
            // Text cannot be selected.
            return null;
        }

        /*
         * Returns the text substring starting at the specified
         * offset with the specified length.
         */
        private String getText(int offset, int length) {
            return PMSimpleLabel.this.string.substring(offset, length);
        }
    }
}
