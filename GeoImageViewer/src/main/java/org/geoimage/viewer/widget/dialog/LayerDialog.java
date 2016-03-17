/*
 * LayerDialog.java
 *
 * Created on March 31, 2008, 12:44 PM
 */

package org.geoimage.viewer.widget.dialog;
import org.geoimage.viewer.core.api.IComplexVectorLayer;
import org.geoimage.viewer.core.api.IEditable;
import org.geoimage.viewer.core.api.ISave;
import org.geoimage.viewer.core.api.ISelect;
import org.geoimage.viewer.core.api.ilayer.ILayer;
import org.geoimage.viewer.core.layers.image.ImageLayer;
import org.geoimage.viewer.core.layers.visualization.GenericLayer;
import org.geoimage.viewer.core.layers.visualization.vectors.ComplexEditVDSVectorLayer;
import org.geoimage.viewer.widget.panels.EditorPanel;
import org.geoimage.viewer.widget.panels.GeometricInteractiveVDSLayerPanel;
import org.geoimage.viewer.widget.panels.ImagePanel;
import org.geoimage.viewer.widget.panels.LayerPanel;
import org.geoimage.viewer.widget.panels.SavePanel;
import org.geoimage.viewer.widget.panels.SelectPanel;
import org.geoimage.viewer.widget.panels.VectorPanel;

/**
 *
 * @author  thoorfr
 */
public class LayerDialog extends javax.swing.JDialog {
    
    /** Creates new form LayerDialog */
    public LayerDialog(java.awt.Frame parent, boolean modal, ILayer layer) {
        super(parent, modal);
        this.setAlwaysOnTop(false);
        initComponents();
        jTabbedPane1.add("Description",new LayerPanel(layer));
        
        if(layer instanceof ImageLayer) jTabbedPane1.add("Image parameters",new ImagePanel((ImageLayer)layer));
        
        if(layer instanceof GenericLayer) jTabbedPane1.add("Style",new VectorPanel((GenericLayer)layer));
        
        if(layer instanceof IComplexVectorLayer)
        {
            if(layer instanceof ComplexEditVDSVectorLayer)
                jTabbedPane1.add("Data", new GeometricInteractiveVDSLayerPanel(layer));
        } else {
           // jTabbedPane1.add("Data", new GeometricLayerPanel(((GenericLayer)layer).getGeometriclayer()));
        }
        if(layer instanceof IEditable) jTabbedPane1.add("Edit",new EditorPanel((IEditable)layer));
        
        if(layer instanceof ISave) jTabbedPane1.add("Save",new SavePanel((ISave)layer));
       
        if(layer instanceof ISelect) jTabbedPane1.add("Select",new SelectPanel((ISelect)layer));
        
        pack();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        setTitle("Layer Description");
        getContentPane().setLayout(new java.awt.GridLayout(1, 1));
        getContentPane().add(jTabbedPane1);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Image Settings");
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
    
}
