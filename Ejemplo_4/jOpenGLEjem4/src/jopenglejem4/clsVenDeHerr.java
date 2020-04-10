/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem4;

/**
 *
 * @author PC-MAX
 */
public class clsVenDeHerr extends javax.swing.JFrame {

    /**
     * Creates new form clsVenDeHerr
     */
    int posVenX, posVenY;
    float valGrosor;
    
    public clsVenDeHerr() {
        initComponents();
        setLocation(275, 155); //pos. temporal... !!!mejorar de forma dinámica 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        sprGrosor = new javax.swing.JSpinner();
        lblZonMov = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        jLabel2.setText("Grosor");

        sprGrosor.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        sprGrosor.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9, 1));
        sprGrosor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sprGrosorStateChanged(evt);
            }
        });

        lblZonMov.setBackground(new java.awt.Color(255, 255, 204));
        lblZonMov.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        lblZonMov.setForeground(new java.awt.Color(204, 204, 204));
        lblZonMov.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblZonMov.setText("Herramientas ");
        lblZonMov.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        lblZonMov.setOpaque(true);
        lblZonMov.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lblZonMovMouseDragged(evt);
            }
        });
        lblZonMov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblZonMovMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sprGrosor, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(lblZonMov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblZonMov)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sprGrosor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblZonMovMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblZonMovMousePressed
        //Se obtiene la coord. x;y al presionar botones del mouse
        posVenX = evt.getX(); 
        posVenY = evt.getY();
    }//GEN-LAST:event_lblZonMovMousePressed

    private void lblZonMovMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblZonMovMouseDragged
        //Se ejecuta el desplazamiento de la Ventana según las coord. del mouse
        this.setLocation(this.getLocation().x + evt.getX() - posVenX, this.getLocation().y + evt.getY() - posVenY);
    }//GEN-LAST:event_lblZonMovMouseDragged

    private void sprGrosorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sprGrosorStateChanged
        //Se obtiene el valor de tipo entero a tipo Float en la variable valGrosor (grosor de línea)
        valGrosor = ((Integer)sprGrosor.getValue()).floatValue();
    }//GEN-LAST:event_sprGrosorStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(clsVenDeHerr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(clsVenDeHerr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(clsVenDeHerr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(clsVenDeHerr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new clsVenDeHerr().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblZonMov;
    private javax.swing.JSpinner sprGrosor;
    // End of variables declaration//GEN-END:variables
}
