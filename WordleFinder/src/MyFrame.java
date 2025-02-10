import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class MyFrame extends JFrame {

    protected static List<String> wordList;
    protected static JList<String> wordJList;

    protected static Color backgroundColor = new Color(18, 18, 19);
    protected static Color textColor = new Color(215, 218, 220);
    protected static Color boardPanelColor = new Color(30, 30, 30);
    protected static Color borderColor = new Color(64, 64, 64);

    protected static Color correctColor = new Color(83, 141, 78);
    protected static Color presentColor = new Color(181, 159, 59);
    protected static Color tileBackgroundColor = new Color(58, 58, 60);

    static {
        wordList = Utils.loadLanguage(Main.LANGUAGE);
    }

    private JPanel boardPanel;
    private JTextField[][] board;
    private JButton clearButton;
    private JButton languageButton;

    public MyFrame() {
        setTitle("Wordle Finder");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.setBackground(backgroundColor);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(6, 5, 10, 10));
        boardPanel.setBackground(boardPanelColor);
        board = new JTextField[6][5];

        Dimension fieldSize = new Dimension(60, 60);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                final int currentRow = row;
                final int currentCol = col;

                JTextField field = new JTextField();
                field.setFont(new Font("Arial", Font.PLAIN, 24));
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setPreferredSize(fieldSize);
                field.setBackground(tileBackgroundColor);
                field.setForeground(textColor);
                field.setCaretColor(textColor);
                field.setBorder(BorderFactory.createLineBorder(borderColor));

                ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                        if ((fb.getDocument().getLength() + string.length()) <= 1) {
                            super.insertString(fb, offset, string.toUpperCase(), attr);
                            updateWordList();
                        }
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        if ((fb.getDocument().getLength() + text.length() - length) <= 1) {
                            super.replace(fb, offset, length, text.toUpperCase(), attrs);
                            updateWordList();
                        }
                    }
                });

                field.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && field.getText().isEmpty()) {
                            field.setBackground(tileBackgroundColor);
                            transferFocusToPreviousField(currentRow, currentCol);
                            updateWordList();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE && field.getDocument().getLength() == 1) {
                            transferFocusToNextField(currentRow, currentCol);
                        }
                    }
                });

                field.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            field.setText("");
                            field.setBackground(tileBackgroundColor);
                            updateWordList();
                        } else if (!field.getText().isEmpty()) {
                            Color currentColor = field.getBackground();
                            if (currentColor.equals(tileBackgroundColor)) {
                                field.setBackground(presentColor);
                            } else if (currentColor.equals(presentColor)) {
                                field.setBackground(correctColor);
                            } else if (currentColor.equals(correctColor)) {
                                field.setBackground(tileBackgroundColor);
                            }
                            updateWordList();
                        }
                    }
                });

                board[row][col] = field;
                boardPanel.add(board[row][col]);
            }
        }

        mainPanel.add(boardPanel, BorderLayout.WEST);

        JPanel wordListPanel = new JPanel();
        wordListPanel.setLayout(new BorderLayout());
        wordListPanel.setBackground(backgroundColor);

        wordJList = new JList<>(new DefaultListModel<>());
        DefaultListModel<String> listModel = (DefaultListModel<String>) wordJList.getModel();
        listModel.addAll(wordList);

        wordJList.setBackground(tileBackgroundColor);
        wordJList.setForeground(textColor);

        wordJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Arial", Font.PLAIN, 24));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(wordJList);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        wordListPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(wordListPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(backgroundColor);

        clearButton = new JButton();
        clearButton.setBackground(tileBackgroundColor);
        clearButton.setForeground(textColor);
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setFocusPainted(false);
        clearButton.setBorder(BorderFactory.createLineBorder(borderColor));
        clearButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        clearButton.addActionListener(e -> clearBoard());

        languageButton = new JButton();
        languageButton.setBackground(tileBackgroundColor);
        languageButton.setForeground(textColor);
        languageButton.setFont(new Font("Arial", Font.BOLD, 16));
        languageButton.setFocusPainted(false);
        languageButton.setBorder(BorderFactory.createLineBorder(borderColor));
        languageButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        languageButton.addActionListener(e -> {
            if ("PL".equalsIgnoreCase(Main.LANGUAGE)) {
                Main.LANGUAGE = "EN";
            } else {
                Main.LANGUAGE = "PL";
            }
            wordList = Utils.loadLanguage(Main.LANGUAGE);
            clearBoard();
            updateButtonsText();
        });

        updateButtonsText();

        bottomPanel.add(clearButton);
        bottomPanel.add(languageButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void updateButtonsText() {
        if ("PL".equalsIgnoreCase(Main.LANGUAGE)) {
            clearButton.setText("Wyczyść");
            languageButton.setText("PL");
        } else {
            clearButton.setText("Clear");
            languageButton.setText("EN");
        }
    }

    private void transferFocusToNextField(int row, int col) {
        if (col < 4) {
            board[row][col + 1].requestFocus();
        } else if (row < 5) {
            board[row + 1][0].requestFocus();
        }
    }

    private void transferFocusToPreviousField(int row, int col) {
        if (col > 0) {
            board[row][col - 1].requestFocus();
        } else if (row > 0) {
            board[row - 1][4].requestFocus();
        }
    }

    private void clearBoard() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                board[row][col].setText("");
                board[row][col].setBackground(tileBackgroundColor);
            }
        }
        updateWordList();
    }

    private void updateWordList() {
        List<String> results = new ArrayList<>(wordList);

        for (int row = 0; row < 6; row++) {
            boolean anyFilled = false;
            for (int col = 0; col < 5; col++) {
                if (!board[row][col].getText().trim().isEmpty()) {
                    anyFilled = true;
                    break;
                }
            }
            if (!anyFilled) {
                continue;
            }

            StringBuilder greenPattern = new StringBuilder("_____");
            StringBuilder yellowPattern = new StringBuilder("_____");
            StringBuilder grayString = new StringBuilder();

            for (int col = 0; col < 5; col++) {
                String text = board[row][col].getText().trim().toLowerCase(Locale.ROOT);
                if (text.length() == 1) {
                    char c = text.charAt(0);
                    Color color = board[row][col].getBackground();

                    if (color.equals(correctColor)) {
                        greenPattern.setCharAt(col, c);
                    } else if (color.equals(presentColor)) {
                        yellowPattern.setCharAt(col, c);
                    } else {
                        grayString.append(c);
                    }
                }
            }

            results = Utils.findGreenLetters(results, greenPattern.toString().toCharArray());
            results = Utils.findYellow(results, yellowPattern.toString());
            String nonGray = (greenPattern.toString() + yellowPattern).replace("_", "");
            results = Utils.findGray(results, grayString.toString(), nonGray);
        }

        DefaultListModel<String> listModel = (DefaultListModel<String>) wordJList.getModel();
        listModel.clear();
        listModel.addAll(results);
    }
}
