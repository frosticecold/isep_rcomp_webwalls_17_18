/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import server.udp.Protocol;
import client.Client;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Raúl Correia <1090657@isep.ipp.pt>
 */
public class GUIClient extends javax.swing.JFrame {

    private static Client client;
    private static String wallname;
    private static GUIClient instance;
    private static Thread refreshThread;
    private static String message = "";
    private static final int NUMBER_OF_CHARS = 155;

    /**
     * Creates new form GUIClient
     */
    public GUIClient() {
        initComponents();
        client = Client.getInstance();
        setVisible(true);
        instance = this;
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
        jScrollPane2 = new javax.swing.JScrollPane();
        chattxtarea = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        inputxtarea = new javax.swing.JTextPane();
        sendButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuitem_exit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wall Application");

        chattxtarea.setEditable(false);
        chattxtarea.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(chattxtarea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        inputxtarea.setBackground(new java.awt.Color(255, 255, 255));
        inputxtarea.setEnabled(false);
        jScrollPane3.setViewportView(inputxtarea);

        sendButton.setText("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settings_black_48x48.png"))); // NOI18N
        settingsButton.setBorderPainted(false);
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jMenu1.setText("Menu");

        menuitem_exit.setText("Exit");
        menuitem_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitem_exitActionPerformed(evt);
            }
        });
        jMenu1.add(menuitem_exit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        int numberofchars = inputxtarea.getText().length();
        if (numberofchars <= NUMBER_OF_CHARS) {
            message = inputxtarea.getText().trim();
            if (message.charAt(0) == Protocol.STARTING_COMMAND) {
                client.sendMessage(message);
            } else {
                client.sendMessageToWall(wallname, message);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error the limit is: " + NUMBER_OF_CHARS + " characters.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_sendButtonActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        SettingsUIDialog dialog = new SettingsUIDialog(this, true);
    }//GEN-LAST:event_settingsButtonActionPerformed

    private void menuitem_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitem_exitActionPerformed
        try {
            application.Application.exit();
            this.dispose();
        } catch (InterruptedException ex) {
            Logger.getLogger(GUIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuitem_exitActionPerformed

    public void newConnection(final String ip) {
        client.newConnection(ip);
    }

    public void setWallname(final String wallname) {
        this.wallname = wallname;
        Client.getInstance().sendGetWall(wallname);
    }

    public void enableChat() {
        this.sendButton.setEnabled(true);
        this.inputxtarea.setEnabled(true);
        refreshWallThread();
    }

    public static GUIClient getInstance() {
        return instance;

    }

    public String getCurrentWallName() {
        return wallname;
    }

    public String getMessage() {
        return message;
    }

    public void changeWallText(final String text) {
        this.chattxtarea.setText(text);
    }

    public void disableChat() {
        this.inputxtarea.setText(null);
        this.chattxtarea.setText(null);
        this.sendButton.setEnabled(false);
        this.inputxtarea.setEnabled(false);
    }

    public void showMessageSuccess(boolean reached) {
        if (reached) {
            JOptionPane.showMessageDialog(this, "Successfully sent message!", "Success!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error, message couldn't be sent entirely.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshWallThread() {
        if (refreshThread == null) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            client.sendGetWall(wallname);
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GUIClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            refreshThread = new Thread(run);
            refreshThread.start();
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane chattxtarea;
    private javax.swing.JTextPane inputxtarea;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuItem menuitem_exit;
    private javax.swing.JButton sendButton;
    private javax.swing.JButton settingsButton;
    // End of variables declaration//GEN-END:variables

}
