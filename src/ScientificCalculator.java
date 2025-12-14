import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScientificCalculator extends JFrame implements ActionListener, KeyListener {

    private JTextField display;
    private JTextArea historyArea;
    private String operator = "";
    private double num1 = 0;
    private boolean startNewNumber = true;
    private double memory = 0;

    public ScientificCalculator() {
        setTitle("Scientific Java Swing Calculator");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Display
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // History area
        historyArea = new JTextArea(5, 20);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.EAST);

        // Buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 5, 5, 5));

        String[] buttons = {
                "MC", "MR", "M+", "M-", "C",
                "←", "√", "x²", "x^y", "/",
                "7", "8", "9", "*", "%",
                "4", "5", "6", "-", "1/x",
                "1", "2", "3", "+", "+/-",
                "0", ".", "=", "sin", "cos",
                "tan", "log", "ln", "(", ")"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.addActionListener(this);
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);

        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = e.getActionCommand();

        try {
            // Numbers and decimal
            if (input.matches("[0-9]") || input.equals(".")) {
                if (startNewNumber) {
                    display.setText(input.equals(".") ? "0." : input);
                    startNewNumber = false;
                } else {
                    if (input.equals(".") && display.getText().contains(".")) return;
                    display.setText(display.getText() + input);
                }
            }

            // Clear
            else if (input.equals("C")) {
                display.setText("0");
                num1 = 0;
                operator = "";
                startNewNumber = true;
            }

            // Backspace
            else if (input.equals("←")) {
                String text = display.getText();
                if (text.length() > 1) display.setText(text.substring(0, text.length() - 1));
                else display.setText("0");
            }

            // Toggle sign
            else if (input.equals("+/-")) {
                display.setText(String.valueOf(-Double.parseDouble(display.getText())));
            }

            // Memory
            else if (input.equals("MC")) memory = 0;
            else if (input.equals("MR")) display.setText(String.valueOf(memory));
            else if (input.equals("M+")) memory += Double.parseDouble(display.getText());
            else if (input.equals("M-")) memory -= Double.parseDouble(display.getText());

            // Operators
            else if (input.matches("[+\\-*/%]")) {
                calculate();
                operator = input;
                num1 = Double.parseDouble(display.getText());
                startNewNumber = true;
            }

            // Equals
            else if (input.equals("=")) {
                calculate();
                operator = "";
                startNewNumber = true;
            }

            // Square root
            else if (input.equals("√")) {
                double value = Double.parseDouble(display.getText());
                double result = Math.sqrt(value);
                display.setText(String.valueOf(result));
                addToHistory("√" + value + " = " + result);
            }

            // Square
            else if (input.equals("x²")) {
                double value = Double.parseDouble(display.getText());
                double result = value * value;
                display.setText(String.valueOf(result));
                addToHistory(value + "² = " + result);
            }

            // Reciprocal
            else if (input.equals("1/x")) {
                double value = Double.parseDouble(display.getText());
                double result = 1 / value;
                display.setText(String.valueOf(result));
                addToHistory("1/" + value + " = " + result);
            }

            // Exponent
            else if (input.equals("x^y")) {
                operator = "^";
                num1 = Double.parseDouble(display.getText());
                startNewNumber = true;
            }

            // Trig & logs
            else if (input.equals("sin")) applyFunction(Math::sin, "sin");
            else if (input.equals("cos")) applyFunction(Math::cos, "cos");
            else if (input.equals("tan")) applyFunction(Math::tan, "tan");
            else if (input.equals("log")) applyFunction(Math::log10, "log");
            else if (input.equals("ln")) applyFunction(Math::log, "ln");
        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    private void applyFunction(Function func, String name) {
        double value = Double.parseDouble(display.getText());
        double result = func.apply(value);
        display.setText(String.valueOf(result));
        addToHistory(name + "(" + value + ") = " + result);
        startNewNumber = true;
    }

    private interface Function { double apply(double value); }

    private void calculate() {
        double num2 = Double.parseDouble(display.getText());
        double result = num2;

        switch (operator) {
            case "+": result = num1 + num2; break;
            case "-": result = num1 - num2; break;
            case "*": result = num1 * num2; break;
            case "/": result = num2 != 0 ? num1 / num2 : 0; break;
            case "%": result = num1 % num2; break;
            case "^": result = Math.pow(num1, num2); break;
        }

        display.setText(String.valueOf(result));
        addToHistory(num1 + " " + operator + " " + num2 + " = " + result);
        num1 = result;
    }

    private void addToHistory(String entry) {
        historyArea.append(entry + "\n");
    }

    // Keyboard support
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if (Character.isDigit(key) || key == '.') actionPerformed(new ActionEvent(e.getSource(), 0, String.valueOf(key)));
        else if (key == '+') actionPerformed(new ActionEvent(e.getSource(), 0, "+"));
        else if (key == '-') actionPerformed(new ActionEvent(e.getSource(), 0, "-"));
        else if (key == '*') actionPerformed(new ActionEvent(e.getSource(), 0, "*"));
        else if (key == '/') actionPerformed(new ActionEvent(e.getSource(), 0, "/"));
        else if (key == '\n') actionPerformed(new ActionEvent(e.getSource(), 0, "="));
    }

    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        new ScientificCalculator();
    }
}
