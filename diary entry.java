import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class PersonalDiaryWithLogin {
    public static void main(String[] args) {
        new LoginFrame();
    }
}

// ---------- LOGIN FRAME ----------
class LoginFrame extends JFrame implements ActionListener {
    JTextField userField;
    JPasswordField passField;
    JButton loginButton;

    HashMap<String, String> users = new HashMap<>();

    public LoginFrame() {
        // Hardcoded users
        users.put("sai", "1234");
        users.put("vedansh", "5678");

        setTitle("Login - java journal");
        setSize(300, 150);
        setLayout(new GridLayout(3, 2, 5, 5));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Username:"));
        userField = new JTextField();
        add(userField);

        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(new JLabel()); // empty space
        add(loginButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (users.containsKey(username) && users.get(username).equals(password)) {
            JOptionPane.showMessageDialog(this, "Welcome, " + username + "!");
            new DiaryFrame(username);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid login.");
        }
    }
}

// ---------- DIARY FRAME ----------
class DiaryFrame extends JFrame implements ActionListener {
    JTextArea textArea;
    JButton saveBtn, viewBtn, deleteBtn;
    String username;
    File diaryFile;

    public DiaryFrame(String username) {
        this.username = username;
        diaryFile = new File(username + "_diary.txt");

        setTitle(username + "'s Diary");
        setSize(500, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        saveBtn = new JButton("Save");
        viewBtn = new JButton("View");
        deleteBtn = new JButton("Delete");

        saveBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        deleteBtn.addActionListener(this);

        panel.add(saveBtn);
        panel.add(viewBtn);
        panel.add(deleteBtn);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            String content = textArea.getText();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Write something first!");
                return;
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(diaryFile, true))) {
                bw.write("----- Entry -----\n" + content + "\n-----------------\n\n");
                JOptionPane.showMessageDialog(this, "Saved!");
                textArea.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving entry.");
            }
        }

        if (e.getSource() == viewBtn) {
            if (!diaryFile.exists()) {
                JOptionPane.showMessageDialog(this, "No diary found.");
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(diaryFile))) {
                textArea.setText("");
                String line;
                while ((line = br.readLine()) != null) {
                    textArea.append(line + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading diary.");
            }
        }

        if (e.getSource() == deleteBtn) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete all entries?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && diaryFile.exists()) {
                if (diaryFile.delete()) {
                    textArea.setText("");
                    JOptionPane.showMessageDialog(this, "Diary deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Could not delete diary.");
                }
            }
        }
    }
}
