import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class AIChatbotGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private HashMap<String, String> responses;

    public AIChatbotGUI() {
        setTitle("Artificial Intelligence Chatbot");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        responses = new HashMap<>();
        trainBot();

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 15));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        chatArea.append("Bot: Hello! I am your AI Chatbot. Ask me anything.\n\n");

        sendButton.addActionListener(e -> sendMessage());

        inputField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        setVisible(true);
    }

    private void trainBot() {
        responses.put("hello", "Hello! How can I help you?");
        responses.put("hi", "Hi there! How may I assist you?");
        responses.put("hey", "Hey! Nice to meet you.");

        responses.put("java", "Java is an object-oriented programming language used to build applications.");
        responses.put("oop", "OOP means Object-Oriented Programming. It uses classes and objects.");
        responses.put("class", "A class is a blueprint for creating objects in Java.");
        responses.put("object", "An object is an instance of a class.");

        responses.put("internship", "An internship helps students gain practical industry experience.");
        responses.put("codealpha", "CodeAlpha provides internship tasks to improve practical skills.");

        responses.put("ai", "Artificial Intelligence means making machines think and respond like humans.");
        responses.put("nlp", "NLP stands for Natural Language Processing. It helps computers understand human language.");
        responses.put("chatbot", "A chatbot is a program that communicates with users using text or speech.");

        responses.put("bye", "Goodbye! Have a great day.");
        responses.put("thanks", "You're welcome!");
        responses.put("thank you", "You're welcome!");
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();

        if (userInput.isEmpty()) {
            return;
        }

        chatArea.append("You: " + userInput + "\n");

        String botResponse = getBotResponse(userInput);

        chatArea.append("Bot: " + botResponse + "\n\n");

        inputField.setText("");
    }

    private String getBotResponse(String input) {
        String processedInput = input.toLowerCase();
        processedInput = processedInput.replaceAll("[^a-zA-Z ]", "");

        for (String keyword : responses.keySet()) {
            if (processedInput.contains(keyword)) {
                return responses.get(keyword);
            }
        }

        if (processedInput.contains("how are you")) {
            return "I am just a chatbot, but I am working perfectly!";
        }

        if (processedInput.contains("your name")) {
            return "My name is Java AI Chatbot.";
        }

        return "Sorry, I don't understand that yet. Please ask something related to Java, AI, NLP, or internship.";
    }

    public static void main(String[] args) {
        new AIChatbotGUI();
    }
}