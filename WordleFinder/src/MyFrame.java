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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyFrame extends JFrame {

    protected static List<String> wordList;
    protected static JList<String> wordJList;

    static {
        wordList = Utils.loadLanguage(Main.LANGUAGE);
    }

    private JPanel boardPanel;
    private JTextField[][] board;

    public MyFrame() {
        setTitle("Wordle Solver");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color backgroundColor = new Color(18, 18, 19);
        Color tileBackgroundColor = new Color(58, 58, 60);
        Color textColor = new Color(215, 218, 220);
        Color boardPanelColor = new Color(30, 30, 30);
        Color borderColor = new Color(64, 64, 64);
        Color correctColor = new Color(83, 141, 78);
        Color presentColor = new Color(181, 159, 59);

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

                board[currentRow][currentCol] = field;
                boardPanel.add(board[currentRow][currentCol]);
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
        wordListPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(wordListPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
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

    private void updateWordList() {
        StringBuilder greenPatternBuilder = new StringBuilder("_____");
        StringBuilder yellowPatternBuilder = new StringBuilder("_____");
        Set<Character> grayChars = new HashSet<>();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                String text = board[row][col].getText().trim().toLowerCase();
                if (text.length() == 1) {
                    char c = text.charAt(0);
                    Color color = board[row][col].getBackground();
                    if (color.equals(new Color(83, 141, 78))) {
                        greenPatternBuilder.setCharAt(col, c);
                    } else if (color.equals(new Color(181, 159, 59))) {
                        yellowPatternBuilder.setCharAt(col, c);
                    } else if (color.equals(new Color(58, 58, 60))) {
                        grayChars.add(c);
                    }
                }
            }
        }

        String greenPattern = greenPatternBuilder.toString();
        String yellowPattern = yellowPatternBuilder.toString();
        String grayPattern = grayChars.stream()
                .map(String::valueOf)
                .reduce((s1, s2) -> s1 + s2)
                .orElse("");

        for (char c : greenPattern.toCharArray()) {
            if (c != '_') {
                grayPattern = grayPattern.replace(String.valueOf(c), "");
            }
        }

        List<String> filteredWords = new ArrayList<>(wordList);

        filteredWords = Utils.findGray(filteredWords, grayPattern);
        filteredWords = Utils.findYellow(filteredWords, yellowPattern);
        filteredWords = Utils.findGreen(filteredWords, greenPattern);

        DefaultListModel<String> listModel = (DefaultListModel<String>) wordJList.getModel();
        listModel.clear();
        listModel.addAll(filteredWords);
    }

}
