package client;

import java.rmi.RemoteException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Component;
import javax.swing.SwingConstants;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

public class ChatUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel textPanel, inputPanel;
    private JTextField textField;
    private String name, message;
    private Font meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
    private Border blankBorder = BorderFactory.createEmptyBorder(10, 10, 20, 10);
    private ChatClient chatClient;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    protected JTextArea textArea, userArea;
    protected JFrame frame;
    protected JButton privateMsgButton, startButton, sendButton;
    protected JPanel clientPanel, userPanel;

    public static void main(String args[]) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ChatUI();
    }

    public ChatUI() {
        frame = new JFrame("Salle de discussion");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (chatClient != null) {
                    try {
                        sendMessage("Au revoir à tous, je pars");
                        chatClient.IServer.quitChat(name);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });

        Container c = getContentPane();
        JPanel outerPanel = new JPanel(new BorderLayout());

        outerPanel.add(getInputPanel(), BorderLayout.CENTER);
        outerPanel.add(getTextPanel(), BorderLayout.NORTH);

        c.setLayout(new BorderLayout());
        c.add(outerPanel, BorderLayout.CENTER);
        c.add(getUsersPanel(), BorderLayout.WEST);

        frame.add(c);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setLocation(150, 150);
        textField.requestFocus();

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public JPanel getTextPanel() {
        String welcome = "Bienvenue, entrez votre nom et appuyez sur Démarrer pour commencer\n";
        textArea = new JTextArea(welcome, 15, 38);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setFont(meiryoFont);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel = new JPanel();
        textPanel.add(scrollPane);

        textPanel.setFont(new Font("Meiryo", Font.PLAIN, 14));
        return textPanel;
    }

    public JPanel getInputPanel() {
        inputPanel = new JPanel(new GridLayout(1, 1, 5, 5));
        inputPanel.setBorder(blankBorder);
        textField = new JTextField();
        textField.setFont(meiryoFont);
        inputPanel.add(textField);
        return inputPanel;
    }

    public JPanel getUsersPanel() {
        userPanel = new JPanel(new BorderLayout());
        String userStr = "Utilisateurs";

        JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
        userPanel.add(userLabel, BorderLayout.NORTH);
        userLabel.setFont(new Font("Meiryo", Font.PLAIN, 16));

        String[] noClientsYet = {"Aucun utilisateur"};
        setClientPanel(noClientsYet);

        clientPanel.setFont(meiryoFont);
        userPanel.add(makeButtonPanel(), BorderLayout.SOUTH);
        userPanel.setBorder(blankBorder);

        return userPanel;
    }

    public void setClientPanel(String[] currClients) {
        clientPanel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<String>();

        for (String s : currClients) {
            listModel.addElement(s);
        }
        if (currClients.length > 1) {
            privateMsgButton.setEnabled(true);
        }

        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(8);
        list.setFont(meiryoFont);
        list.setCellRenderer(new UserListCellRenderer());

        JScrollPane listScrollPane = new JScrollPane(list);

        clientPanel.add(listScrollPane, BorderLayout.CENTER);
        userPanel.add(clientPanel, BorderLayout.CENTER);
    }

    public JPanel makeButtonPanel() {
        sendButton = new JButton("Envoyer ");
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);
        sendButton.setBackground(Color.ORANGE);
        sendButton.setForeground(Color.BLACK);

        privateMsgButton = new JButton("Msg prv");
        privateMsgButton.addActionListener(this);
        privateMsgButton.setEnabled(false);
        privateMsgButton.setBackground(Color.CYAN);
        privateMsgButton.setForeground(Color.BLACK);

        startButton = new JButton("Démarrer ");
        startButton.addActionListener(this);
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.BLACK);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(privateMsgButton);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(startButton);
        buttonPanel.add(sendButton);

        return buttonPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == startButton) {
                name = textField.getText();
                if (name.length() != 0) {
                    frame.setTitle(name + "'salle de discussion ");
                    textField.setText("");
                    textArea.append("utilisateur : " + name + " connexion à...\n");
                    getConnected(name);
                    if (!chatClient.connectionProblem) {
                        startButton.setEnabled(false);
                        sendButton.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Entrer votre nom");
                }
            }

            if (e.getSource() == sendButton) {
                message = textField.getText();
                textField.setText("");
                sendMessage(message);
                System.out.println("message en cours : " + message);
            }

            if (e.getSource() == privateMsgButton) {
                int[] privateList = list.getSelectedIndices();
                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("index selectionné :" + privateList[i]);
                }
                message = textField.getText();
                textField.setText("");
                sendPrivate(privateList);
            }

        } catch (RemoteException remoteExc) {
            remoteExc.printStackTrace();
        }
    }

    private void sendMessage(String message) throws RemoteException {
        chatClient.IServer.updateChat(name, message);
    }

    private void sendPrivate(int[] privateList) throws RemoteException {
        String privateMessage = "[Privé de " + name + "] :" + message + "\n";
        chatClient.IServer.privateDM(privateList, privateMessage);
    }

    private void getConnected(String userName) throws RemoteException {
        String cleanedUserName = userName.replaceAll("\\s+", "_");
        cleanedUserName = cleanedUserName.replaceAll("\\W+", "_");
        try {
            chatClient = new ChatClient(this, cleanedUserName);
            chatClient.startClient();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateClientList(String[] clients) {
        if (listModel != null) {
            listModel.removeAllElements();
            for (String client : clients) {
                listModel.addElement(client);
            }
        }
    }
}

class UserListCellRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;
    private static final Color DARK_GREEN = new Color(0, 100, 0);
    private static final Color RED = new Color(255, 0, 0);
    private static final int DOT_SIZE = 10;

    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setHorizontalAlignment(SwingConstants.LEFT); 
        label.setFont(new Font("Meiryo", Font.BOLD, 14)); 
        label.setForeground(Color.BLACK);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().equals("Aucun utilisateur")) {
            g.setColor(RED);
        } else {
            g.setColor(DARK_GREEN);
        }
        int x = getWidth() - DOT_SIZE - 5;
        int y = (getHeight() - DOT_SIZE) / 2;
        g.fillOval(x, y, DOT_SIZE, DOT_SIZE);
    }
}
