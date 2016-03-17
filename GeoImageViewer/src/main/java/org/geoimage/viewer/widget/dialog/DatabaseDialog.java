/*
 * DatabaseDialog.java
 *
 * Created on June 16, 2008, 5:20 PM
 */
package org.geoimage.viewer.widget.dialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.geoimage.viewer.core.GeometryCollection;
import org.geoimage.viewer.core.SumoPlatform;
import org.geoimage.viewer.core.api.ilayer.ILayer;
import org.geoimage.viewer.core.factory.FactoryLayer;
import org.geoimage.viewer.core.gui.manager.LayerManager;
import org.geoimage.viewer.core.layers.image.ImageLayer;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.slf4j.LoggerFactory;

/**
 *
 * @author  thoorfr
 */
public class DatabaseDialog extends javax.swing.JDialog {
	private static org.slf4j.Logger logger=LoggerFactory.getLogger(DatabaseDialog.class);

    private GeometryCollection layer;
    private ImageLayer ilayer;
    private String vtype;

    /** Creates new form DatabaseDialog */
    public DatabaseDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    public GeometryCollection getLayer() {
        return layer;
    }

    public void setConnection(Connection conn) {
        databaseQuery1.setConnection(conn);
    }

    public void setImageLayer(ImageLayer layer, String vectorType) {
        this.ilayer = layer;
        this.vtype = vectorType;
    }

    public void addLayerInThread(final String type, final GeometryCollection layer, final ImageLayer il) {
        new Thread(new Runnable() {

            public void run() {
                LayerManager.getIstanceManager().addLayer((ILayer)(FactoryLayer.createComplexLayer(layer)));
            }
        }).start();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        databaseQuery1 = new org.geoimage.viewer.widget.dialog.DatabaseQuery();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        databaseQuery1.setName("databaseQuery1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.geoimage.viewer.core.SumoPlatform.class).getContext().getActionMap(DatabaseDialog.class, this);
        jButton1.setAction(actionMap.get("addDataToImage")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.geoimage.viewer.core.SumoPlatform.class).getContext().getResourceMap(DatabaseDialog.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jCheckBox1.setText(resourceMap.getString("jCheckBox1.text")); // NOI18N
        jCheckBox1.setName("jCheckBox1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(databaseQuery1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(databaseQuery1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jCheckBox1)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DatabaseDialog dialog = new DatabaseDialog(new javax.swing.JFrame(), true);
                    Connection conn = DriverManager.getConnection("jdbc:h2:~/.sumo/SUMO_DB;AUTO_SERVER=TRUE", "sa", "");
                    dialog.setConnection(conn);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                	logger.error(ex.getMessage(),ex);
                }
            }
        });
    }

    @Action
    public Task addDataToImage() {
        return new AddDataToImageTask(org.jdesktop.application.Application.getInstance(org.geoimage.viewer.core.SumoPlatform.class));
    }

    private class AddDataToImageTask extends org.jdesktop.application.Task<GeometryCollection, Void> {
        AddDataToImageTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to AddDataToImageTask fields, here.
            super(app);
        }
        @Override protected GeometryCollection doInBackground() {
            try {
                layer = databaseQuery1.getLayer();
                if (jCheckBox1.isSelected() && ilayer != null) {
                    layer = GeometryCollection.createImageProjectedLayer(layer, ilayer.getImageReader().getGeoTransform(), "EPSG:4326");
                }
            } catch (Exception ex) {
            	logger.error(ex.getMessage(),ex);
            }
            return layer;
        }
        @Override protected void succeeded(GeometryCollection result) {
            if(result!=null){
                addLayerInThread(vtype, result, ilayer);
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.geoimage.viewer.widget.dialog.DatabaseQuery databaseQuery1;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    // End of variables declaration//GEN-END:variables

}
