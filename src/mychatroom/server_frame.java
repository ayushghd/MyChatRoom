package chat_server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class server_frame extends javax.swing.JFrame 
{
   ArrayList clientOutputStreams;
   ArrayList<String> users;
   ServerSocket serversocket;  
   public class ClientHandler implements Runnable	
   {
       BufferedReader reader;
       Socket sock;
       //PrintWriter client;
       DataOutputStream client;
       DataOutputStream dout;
       DataInputStream din;
       
       public ClientHandler(Socket clientSocket, DataOutputStream user) 
       {
            client = user;
            try 
            {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
                din = new DataInputStream(sock.getInputStream());
                dout = new DataOutputStream(sock.getOutputStream());
            }
            catch (Exception ex) 
            {
                ta_chat.append("Unexpected error... \n");
            }

       }

       @Override
       public void run() 
       {
            String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat",file = "File" ;
            String[] data;

            try 
            {
                while ((message = din.readUTF()) != null) 
                {
                    ta_chat.append("Received: " + message + "\n");
                    data = message.split(":");
                    
                    if (data[2].equals(connect)) 
                    {
                        tellEveryone((data[0] + ":" + data[1] + ":" + chat));
                        userAdd(data[0]);
                    } 
                    else if (data[2].equals(disconnect)) 
                    {
                        tellEveryone((data[0] + ":has disconnected." + ":" + chat));
                        userRemove(data[0]);
                    } 
                    else if (data[2].equals(chat)) 
                    {
                        tellEveryone(message);
                    } 
                    else if(data[2].equals(file))
                    {
                        String filename="";
                        try{
                                filename=din.readUTF(); 
                                filename="server"+filename;
                                long sz=Long.parseLong(din.readUTF());
                                System.out.println ("File Size: "+(sz/(1024*1024))+" MB");
                                
                                byte b[]=new byte [1024];
                                System.out.println("Receving file..");
                                FileOutputStream fos=new FileOutputStream(new File(filename),true);
                                long bytesRead;
                                do
                                {
                                    bytesRead = din.read(b, 0, b.length);
                                    fos.write(b,0,b.length);
                                }while(!(bytesRead<1024));
                                fos.close(); 
                            }
                            catch(EOFException e)
                            {
                                    //do nothing
                            }
                            Iterator it = clientOutputStreams.iterator();

                            while (it.hasNext()) 
                            {
                                try 
                                {
                                    DataOutputStream writer = (DataOutputStream) it.next();
                                    try{
                                            writer.writeUTF(message);
                                            writer.flush();
                                            writer.writeUTF(filename);  
                                            writer.flush();  

                                            File f=new File(filename);
                                            FileInputStream fin=new FileInputStream(f);
                                            long sz=(int) f.length();

                                            byte b[]=new byte [1024];

                                            int read;

                                            writer.writeUTF(Long.toString(sz)); 
                                            writer.flush(); 

                                            while((read = fin.read(b)) != -1){
                                                writer.write(b, 0, read); 
                                                writer.flush(); 
                                            }
                                            fin.close();

                                        }
                                        catch(Exception e)
                                        {
                                                e.printStackTrace();
                                                System.out.println("An error occured");
                                        }

                                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

                                } 
                                catch (Exception ex) 
                                {
                                    ta_chat.append("Error telling everyone. \n");
                                }
                        } 
                    }
                    else 
                    {
                        ta_chat.append("No Conditions were met. \n");
                    }
                } 
             } 
             catch (Exception ex) 
             {
                ta_chat.append("Lost a connection. \n");
                ex.printStackTrace();
                clientOutputStreams.remove(client);
             } 
	} 
    }

    public server_frame() 
    {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        b_start = new javax.swing.JButton();
        b_end = new javax.swing.JButton();
        b_users = new javax.swing.JButton();
        b_clear = new javax.swing.JButton();
        tf_pwd = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setName("server"); // NOI18N
        setResizable(false);

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_start.setText("START");
        b_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_startActionPerformed(evt);
            }
        });

        b_end.setText("END");
        b_end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_endActionPerformed(evt);
            }
        });

        b_users.setText("Online Users");
        b_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_usersActionPerformed(evt);
            }
        });

        b_clear.setText("Clear");
        b_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_clearActionPerformed(evt);
            }
        });

        tf_pwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set_password(evt);
            }
        });

        jLabel1.setText("Server Password");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_end, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_start, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_pwd, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_users, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_start)
                    .addComponent(b_users)
                    .addComponent(tf_pwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_clear)
                    .addComponent(b_end))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>                        

    private void b_endActionPerformed(java.awt.event.ActionEvent evt) {                                      
        try 
        {
            Thread.sleep(2000);                 //5000 milliseconds is five second.
        } 
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}
        
        tellEveryone("Server:is stopping and all users will be disconnected.\n:Chat");
        ta_chat.append("Server stopping... \n");
        
        tf_pwd.setEditable(true);
        ta_chat.setText("");
       try {
           
           clientOutputStreams.clear();
           users.clear();
           serversocket.close();
           System.out.println(clientOutputStreams.size());
           b_end.setEnabled(false);
           b_start.setEnabled(true);
           
       } catch (IOException ex) {
                System.out.println("lllll");
               }
    }                                     

    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {                                        
        Thread starter = new Thread(new ServerStart());
        starter.start();
        tf_pwd.setEditable(false);
        ta_chat.append("Server started...\n");
        b_start.setEnabled(false);
        b_end.setEnabled(true);
        
    }                                       

    private void b_usersActionPerformed(java.awt.event.ActionEvent evt) {                                        
        ta_chat.append("\n Online users : \n");
        for (String current_user : users)
        {
            ta_chat.append(current_user);
            ta_chat.append("\n");
        }    
        
    }                                       

    private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {                                        
        ta_chat.setText("");
    }                                       

    private void set_password(java.awt.event.ActionEvent evt) {                              
        // TODO add your handling code here:
    }                             

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new server_frame().setVisible(true);
            }
        });
    }
    
    public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            clientOutputStreams = new ArrayList();
            users = new ArrayList();  
            try 
            {
                serversocket = new ServerSocket(2222);

                while (true) 
                {
                            Socket clientSock = serversocket.accept();
                            DataOutputStream dout=new DataOutputStream(clientSock.getOutputStream());
                            DataInputStream din = new DataInputStream(clientSock.getInputStream());
                            String s = din.readUTF();
                            System.out.println(s);
                            if(s.equals(tf_pwd.getText()))
                            {
                                dout.writeUTF("Connected");
                                dout.flush();
                                clientOutputStreams.add(dout);
                                Thread listener = new Thread(new ClientHandler(clientSock, dout));
                                listener.start();
                                ta_chat.append("Got a connection. \n");
                            }
                            else
                            {
                                dout.writeUTF("Password not matched");
                                dout.flush();
                            }
                }
            }
            catch (Exception ex)
            {
                ta_chat.append("Error making a connection. \n");
            }
        }
    }
    
    public void userAdd (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.add(name);
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void userRemove (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.remove(name);
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void tellEveryone(String message) 
    {
	Iterator it = clientOutputStreams.iterator();

        while (it.hasNext()) 
        {
            try 
            {
                DataOutputStream writer = (DataOutputStream) it.next();
                writer.writeUTF(message);
                writer.flush();
                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
            } 
            catch (Exception ex) 
            {
                System.out.println("inside");
		ta_chat.append("Error telling everyone. \n");
            }
        } 
    }
    
    // Variables declaration - do not modify                     
    private javax.swing.JButton b_clear;
    private javax.swing.JButton b_end;
    private javax.swing.JButton b_start;
    private javax.swing.JButton b_users;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField tf_pwd;
    // End of variables declaration                   
}
