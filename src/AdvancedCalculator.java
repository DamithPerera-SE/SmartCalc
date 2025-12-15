import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AdvancedCalculator extends JFrame implements ActionListener {

    private JTextField display;
    private JTextArea historyArea;
    private String operator = "";
    private double num1 = 0;
    private boolean startNewNumber = true;
    private double memory = 0;
    private boolean degreeMode = true; // true=Degrees, false=Radians

    public AdvancedCalculator() {
        setTitle("Advanced Scientific Calculator");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // ===== Display Panel =====
        display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(display, BorderLayout.NORTH);

        // ===== History =====
        historyArea = new JTextArea(3, 20);
        historyArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyArea.setEditable(false);
        historyArea.setBackground(Color.BLACK);
        historyArea.setForeground(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.EAST);

        // ===== Buttons Panel =====
        JPanel panel = new JPanel(new GridLayout(8, 6, 5, 5));
        panel.setBackground(new Color(30, 30, 30));

        String[] buttons = {
                "MC", "MR", "M+", "M-", "C", "←",
                "sin", "cos", "tan", "sinh", "cosh", "tanh",
                "ln", "log", "x^y", "x²", "√", "1/x",
                "(", ")", "!", "π", "e", "+/-",
                "7", "8", "9", "/", "%", "DEG",
                "4", "5", "6", "*", "y√x", "^2", 
                "1", "2", "3", "-", "", "",
                "0", ".", "=", "+"
        };

        for (String text : buttons) {
            JButton btn;
            if (text.equals("")) btn = new JButton(); // empty placeholder
            else {
                btn = new JButton(text);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
                btn.setForeground(Color.WHITE);
                btn.setBackground(new Color(60, 60, 60));
                btn.setFocusPainted(false);

                // Color coding
                if ("+-*/=%".contains(text)) btn.setBackground(new Color(0, 120, 215));
                if (text.equals("C")) btn.setBackground(new Color(200, 60, 60));
                btn.addActionListener(this);
            }
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = e.getActionCommand();
        try {
            // Numbers & decimal
            if (input.matches("[0-9]") || input.equals(".")) {
                if (startNewNumber) display.setText(input.equals(".") ? "0." : input);
                else display.setText(display.getText() + input);
                startNewNumber = false;
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
                display.setText(text.length() > 1 ? text.substring(0, text.length() - 1) : "0");
            }

            // Toggle sign
            else if (input.equals("+/-")) display.setText(String.valueOf(-Double.parseDouble(display.getText())));

            // Memory
            else if (input.equals("MC")) memory = 0;
            else if (input.equals("MR")) display.setText(String.valueOf(memory));
            else if (input.equals("M+")) memory += Double.parseDouble(display.getText());
            else if (input.equals("M-")) memory -= Double.parseDouble(display.getText());

            // Degree/Radian
            else if (input.equals("DEG")) degreeMode = !degreeMode;

            // Operators
            else if ("+-*/%^".contains(input)) {
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

            // Factorial
            else if (input.equals("!")) {
                double val = Double.parseDouble(display.getText());
                display.setText(String.valueOf(factorial(val)));
            }

            // Constants
            else if (input.equals("π")) display.setText(String.valueOf(Math.PI));
            else if (input.equals("e")) display.setText(String.valueOf(Math.E));

            // Functions
            else if (input.equals("√")) display.setText(String.valueOf(Math.sqrt(Double.parseDouble(display.getText()))));
            else if (input.equals("x²")) display.setText(String.valueOf(Math.pow(Double.parseDouble(display.getText()), 2)));
            else if (input.equals("1/x")) display.setText(String.valueOf(1 / Double.parseDouble(display.getText())));
            else if (input.equals("x^y")) {
                operator = "^";
                num1 = Double.parseDouble(display.getText());
                startNewNumber = true;
            } else if (input.equals("y√x")) {
                operator = "y√x";
                num1 = Double.parseDouble(display.getText());
                startNewNumber = true;
            }

            // Trig & logs
            else if (input.equals("sin")) display.setText(String.valueOf(trig(Math::sin)));
            else if (input.equals("cos")) display.setText(String.valueOf(trig(Math::cos)));
            else if (input.equals("tan")) display.setText(String.valueOf(trig(Math::tan)));
            else if (input.equals("sinh")) display.setText(String.valueOf(Math.sinh(Double.parseDouble(display.getText()))));
            else if (input.equals("cosh")) display.setText(String.valueOf(Math.cosh(Double.parseDouble(display.getText()))));
            else if (input.equals("tanh")) display.setText(String.valueOf(Math.tanh(Double.parseDouble(display.getText()))));
            else if (input.equals("ln")) display.setText(String.valueOf(Math.log(Double.parseDouble(display.getText()))));
            else if (input.equals("log")) display.setText(String.valueOf(Math.log10(Double.parseDouble(display.getText()))));

        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    private double trig(Function func) {
        double val = Double.parseDouble(display.getText());
        if (degreeMode) val = Math.toRadians(val);
        return func.apply(val);
    }

    private interface Function { double apply(double v); }

    private double factorial(double n) {
        if (n < 0) return Double.NaN;
        double result = 1;
        for (int i = 2; i <= (int)n; i++) result *= i;
        return result;
    }

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
            case "y√x": result = Math.pow(num2, 1.0/num1); break;
        }

        display.setText(String.valueOf(round(result, 12)));
        num1 = result;
    }

    private double round(double value, int places) {
        if (places < 0) return value;
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdvancedCalculator::new);
    }
}
