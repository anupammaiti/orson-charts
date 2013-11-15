/* ============
 * Orson Charts
 * ============
 * 
 * (C)opyright 2013, by Object Refinery Limited.
 * 
 */

package com.orsoncharts.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Serialization support methods.
 */
public class SerialUtils {
    
    private SerialUtils() {
        // no need to instantiate this class
    }
    
    /**
     * Reads a <code>Paint</code> object that has been serialized by the
     * {@link SerialUtils#writePaint(Paint, ObjectOutputStream)} method.
     *
     * @param stream  the input stream (<code>null</code> not permitted).
     *
     * @return The paint object (possibly <code>null</code>).
     *
     * @throws IOException  if there is an I/O problem.
     * @throws ClassNotFoundException  if there is a problem loading a class.
     */
    public static Paint readPaint(ObjectInputStream stream) throws IOException,
            ClassNotFoundException {

        ArgChecks.nullNotPermitted(stream, "stream");
        Paint result = null;
        boolean isNull = stream.readBoolean();
        if (!isNull) {
            Class c = (Class) stream.readObject();
            if (Serializable.class.isAssignableFrom(c)) {
                result = (Paint) stream.readObject();
            } else if (c.equals(GradientPaint.class)) {
                float x1 = stream.readFloat();
                float y1 = stream.readFloat();
                Color c1 = (Color) stream.readObject();
                float x2 = stream.readFloat();
                float y2 = stream.readFloat();
                Color c2 = (Color) stream.readObject();
                boolean isCyclic = stream.readBoolean();
                result = new GradientPaint(x1, y1, c1, x2, y2, c2, isCyclic);
            }
        }
        return result;

    }

    /**
     * Serializes a <code>Paint</code> object.
     *
     * @param paint  the paint object (<code>null</code> permitted).
     * @param stream  the output stream (<code>null</code> not permitted).
     *
     * @throws IOException if there is an I/O error.
     */
    public static void writePaint(Paint paint, ObjectOutputStream stream)
        throws IOException {

        ArgChecks.nullNotPermitted(stream, "stream");
        if (paint != null) {
            stream.writeBoolean(false);
            stream.writeObject(paint.getClass());
            if (paint instanceof Serializable) {
                stream.writeObject(paint);
            } else if (paint instanceof GradientPaint) {
                GradientPaint gp = (GradientPaint) paint;
                stream.writeFloat((float) gp.getPoint1().getX());
                stream.writeFloat((float) gp.getPoint1().getY());
                stream.writeObject(gp.getColor1());
                stream.writeFloat((float) gp.getPoint2().getX());
                stream.writeFloat((float) gp.getPoint2().getY());
                stream.writeObject(gp.getColor2());
                stream.writeBoolean(gp.isCyclic());
            }
        } else {
            stream.writeBoolean(true);
        }
    }

    /**
     * Reads a <code>Stroke</code> object that has been serialized by the
     * {@link SerialUtils#writeStroke(Stroke, ObjectOutputStream)} method.
     *
     * @param stream  the input stream (<code>null</code> not permitted).
     *
     * @return The stroke object (possibly <code>null</code>).
     *
     * @throws IOException  if there is an I/O problem.
     * @throws ClassNotFoundException  if there is a problem loading a class.
     */
    public static Stroke readStroke(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {

        ArgChecks.nullNotPermitted(stream, "stream");
        Stroke result = null;
        boolean isNull = stream.readBoolean();
        if (!isNull) {
            Class c = (Class) stream.readObject();
            if (c.equals(BasicStroke.class)) {
                float width = stream.readFloat();
                int cap = stream.readInt();
                int join = stream.readInt();
                float miterLimit = stream.readFloat();
                float[] dash = (float[]) stream.readObject();
                float dashPhase = stream.readFloat();
                result = new BasicStroke(width, cap, join, miterLimit, dash, 
                        dashPhase);
            } else {
                result = (Stroke) stream.readObject();
            }
        }
        return result;
    }

    /**
     * Serializes a <code>Stroke</code> object.  This code handles the
     * <code>BasicStroke</code> class which is the only <code>Stroke</code>
     * implementation provided by the JDK (and isn't directly
     * <code>Serializable</code>).
     *
     * @param stroke  the stroke object (<code>null</code> permitted).
     * @param stream  the output stream (<code>null</code> not permitted).
     *
     * @throws IOException if there is an I/O error.
     */
    public static void writeStroke(Stroke stroke, ObjectOutputStream stream)
            throws IOException {

        ArgChecks.nullNotPermitted(stream, "stream");
        if (stroke != null) {
            stream.writeBoolean(false);
            if (stroke instanceof BasicStroke) {
                BasicStroke s = (BasicStroke) stroke;
                stream.writeObject(BasicStroke.class);
                stream.writeFloat(s.getLineWidth());
                stream.writeInt(s.getEndCap());
                stream.writeInt(s.getLineJoin());
                stream.writeFloat(s.getMiterLimit());
                stream.writeObject(s.getDashArray());
                stream.writeFloat(s.getDashPhase());
            } else {
                stream.writeObject(stroke.getClass());
                stream.writeObject(stroke);
            }
        }
        else {
            stream.writeBoolean(true);
        }
    }
   
}