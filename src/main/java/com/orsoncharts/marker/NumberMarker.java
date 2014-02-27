/* ============
 * Orson Charts
 * ============
 * 
 * (C)opyright 2013, 2014, by Object Refinery Limited.
 * 
 * http://www.object-refinery.com/orsoncharts/index.html
 * 
 * Redistribution of this source file is prohibited.
 * 
 */

package com.orsoncharts.marker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.orsoncharts.Range;
import com.orsoncharts.graphics3d.Utils2D;
import com.orsoncharts.util.Anchor2D;
import com.orsoncharts.util.ArgChecks;
import com.orsoncharts.util.ObjectUtils;
import com.orsoncharts.util.SerialUtils;
import com.orsoncharts.util.TextAnchor;
import com.orsoncharts.util.TextUtils;

/**
 * A marker used to mark one value on an axis.
 * 
 * @since 1.2
 */
public class NumberMarker extends AbstractMarker implements ValueMarker,
        Serializable {

    /** The data value to be marked. */
    private double value;
    
    /** The label for the marker (optional). */
    private String label;
    
    /** The font for the label. */
    private Font font;
    
    /** The color for the label. */
    private Color labelColor;
    
    /** The anchor for the label. */
    private Anchor2D labelAnchor;
    
    /** The stroke for the marker line. */
    private transient Stroke stroke;
    
    /** The color for the marker line. */
    private Color lineColor;
    
    /**
     * Creates a new marker.
     * 
     * @param value  the value. 
     */
    public NumberMarker(double value) {
        super();
        this.value = value;
        this.label = null;
        this.font = DEFAULT_MARKER_FONT;
        this.labelColor = DEFAULT_LABEL_COLOR;
        this.stroke = DEFAULT_LINE_STROKE;
        this.lineColor = DEFAULT_LINE_COLOR;
        this.labelAnchor = Anchor2D.CENTER;
    }
    
    /**
     * Returns the value for the marker (the initial value comes from the 
     * constructor).
     * 
     * @return The value. 
     */
    public double getValue() {
        return this.value;
    }
    
    /**
     * Sets the value for the marker and sends a change event to all registered
     * listeners.
     * 
     * @param value  the value. 
     */
    public void setValue(double value) {
        this.value = value;
        fireChangeEvent();
    }
    
    /**
     * Returns the range for the marker (in this case, a single value range).
     * This method is used by the axis to filter out markers that do not touch 
     * the current axis range.
     * 
     * @return The range for the marker (never <code>null</code>). 
     */
    @Override
    public Range getRange() {
        return new Range(this.value, this.value);
    }

    /**
     * Returns the label for the marker (if this is <code>null</code> then no
     * label is displayed).  The default value is <code>null</code>.
     * 
     * @return The label (possibly <code>null</code>). 
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Sets the label and sends a change event to all registered listeners.
     * 
     * @param label  the label (<code>null</code> permitted).
     */
    public void setLabel(String label) {
        this.label = label;
        fireChangeEvent();
    }
    
    /**
     * Returns the font for the label.  The default value is 
     * {@link Marker#DEFAULT_MARKER_FONT}.
     * 
     * @return The font (never <code>null</code>). 
     */
    public Font getFont() {
        return this.font;
    }
    
    /**
     * Sets the font for the marker label and sends a change event to all 
     * registered listeners.
     * 
     * @param font  the font (<code>null</code> not permitted). 
     */
    public void setFont(Font font) {
        ArgChecks.nullNotPermitted(font, "font");
        this.font = font;
        fireChangeEvent();
    }
    
    /**
     * Returns the label color.  The default value is 
     * {@link Marker#DEFAULT_LABEL_COLOR}.
     * 
     * @return The label color (never <code>null</code>).
     */
    public Color getLabelColor() {
        return this.labelColor;
    }
    
    /**
     * Sets the label color and sends a change event to all registered
     * listeners.
     * 
     * @param color  the color (<code>null</code> not permitted). 
     */
    public void setLabelColor(Color color) {
        ArgChecks.nullNotPermitted(color, "color");
        this.labelColor = color;
        fireChangeEvent();
    }
    
    /**
     * Returns the anchor for the label.  The default value is 
     * {@link Anchor2D#CENTER}.
     * 
     * @return The anchor for the label. 
     */
    public Anchor2D getLabelAnchor() {
        return this.labelAnchor;
    }
    
    /**
     * Sets the anchor for the label and sends a change event to all registered
     * listeners.
     * 
     * @param anchor  the anchor (<code>null</code> not permitted). 
     */
    public void setLabelAnchor(Anchor2D anchor) {
        ArgChecks.nullNotPermitted(anchor, "anchor");
        this.labelAnchor = anchor;
        fireChangeEvent();
    }
     
    /**
     * Returns the stroke for the marker line.  The default value is
     * {@link Marker#DEFAULT_LINE_STROKE}.
     * 
     * @return The stroke for the marker line (never <code>null</code>).
     */
    public Stroke getLineStroke() {
        return this.stroke;    
    }
    
    /**
     * Sets the stroke for the marker line and sends a change event to all
     * registered listeners.
     * 
     * @param stroke  the stroke (<code>null</code> not permitted). 
     */
    public void setLineStroke(Stroke stroke) {
        ArgChecks.nullNotPermitted(stroke, "stroke");
        this.stroke = stroke;
        fireChangeEvent();
    }
    
    /**
     * Returns the color for the marker line.  The default value is 
     * {@link Marker#DEFAULT_LINE_COLOR}.
     * 
     * @return The color for the marker line (never <code>null</code>). 
     */
    public Color getLineColor() {
        return this.lineColor;
    }
    
    /**
     * Sets the color for the marker line and sends a change event to all 
     * registered listeners.
     * 
     * @param color  the color (<code>null</code> not permitted). 
     */
    public void setLineColor(Color color) {
        ArgChecks.nullNotPermitted(color, "color");
        this.lineColor = color;
        fireChangeEvent();
    }
    
    /**
     * Draws the marker.  This method is called by the library, you won't 
     * normally call it directly.
     * 
     * @param g2  the graphics target (<code>null</code> not permitted).
     * @param markerData  transient marker data (<code>null</code> not 
     *     permitted).
     */
    @Override
    public void draw(Graphics2D g2, MarkerData markerData, boolean reverse) {
        MarkerLine line = markerData.getValueLine();
        g2.setPaint(this.lineColor);
        g2.setStroke(this.stroke);
        Line2D l = new Line2D.Double(line.getStartPoint(), line.getEndPoint());
        g2.draw(l);
        double angle = Utils2D.calculateTheta(l);
        boolean vflip = false;
        if (angle > Math.PI / 2) {
            angle -= Math.PI;
            vflip = true;
        }
        if (angle < -Math.PI / 2) {
            angle += Math.PI;
            vflip = true;
        }
        if (reverse) {
            vflip = !vflip;
        }
        Point2D labelPoint = markerData.getLabelPoint(); 
        if (labelPoint != null) {
            double lineLength = Utils2D.length(l);
            Rectangle2D bounds = g2.getFontMetrics().getStringBounds(this.label, g2);
            if (bounds.getWidth() < lineLength) {
                g2.setFont(this.font);
                g2.setPaint(this.labelColor);
                TextAnchor textAnchor = deriveTextAnchorForLine(
                        this.labelAnchor.getRefPt(), !vflip);
                TextUtils.drawRotatedString(this.label, g2, 
                        (float) labelPoint.getX(), (float) labelPoint.getY(), 
                        textAnchor, angle, textAnchor);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.value) 
                ^ (Double.doubleToLongBits(this.value) >>> 32));
        hash = 19 * hash + ObjectUtils.hashCode(this.label);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NumberMarker other = (NumberMarker) obj;
        if (Double.doubleToLongBits(this.value) 
                != Double.doubleToLongBits(other.value)) {
            return false;
        }
        if (!ObjectUtils.equals(this.label, other.label)) {
            return false;
        }
        if (!ObjectUtils.equals(this.font, other.font)) {
            return false;
        }
        if (!ObjectUtils.equals(this.labelColor, other.labelColor)) {
            return false;
        }
        if (!ObjectUtils.equals(this.labelAnchor, other.labelAnchor)) {
            return false;
        }
        if (!ObjectUtils.equals(this.stroke, other.stroke)) {
            return false;
        }
        if (!ObjectUtils.equals(this.lineColor, other.lineColor)) {
            return false;
        }
        return true;
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtils.writeStroke(this.stroke, stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.stroke = SerialUtils.readStroke(stream);
    }
   
}