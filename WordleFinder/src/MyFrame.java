import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class MyFrame extends JFrame {

    private JPanel boardPanel;
    private JTextField[][] board;

    public MyFrame() {
        setTitle("Wordle Solver");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Kolory zgodnie z paletą Wordle
        Color backgroundColor = new Color(18, 18, 19); // --wordleBlack-3
        Color tileBackgroundColor = new Color(58, 58, 60); // --gray-25
        Color textColor = new Color(215, 218, 220); // --gray-27
        Color boardPanelColor = new Color(30, 30, 30); // --wordleBlack-2
        Color borderColor = new Color(64, 64, 64); // --gray-26

        // Tworzenie panelu głównego z marginesami
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);

        // Tworzenie panelu planszy
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(6, 5, 10, 10));
        boardPanel.setBackground(boardPanelColor);
        board = new JTextField[6][5];

        Dimension fieldSize = new Dimension(60, 60);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                board[row][col] = new JTextField();
                board[row][col].setFont(new Font("Arial", Font.PLAIN, 24));
                board[row][col].setHorizontalAlignment(JTextField.CENTER);
                board[row][col].setPreferredSize(fieldSize);
                board[row][col].setBackground(tileBackgroundColor);
                board[row][col].setForeground(textColor);
                board[row][col].setCaretColor(textColor);
                board[row][col].setBorder(BorderFactory.createLineBorder(borderColor));

                // Dodaj DocumentFilter aby ograniczyć wprowadzenie do jednej litery i konwertować na wielkie litery
                ((AbstractDocument) board[row][col].getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                        if ((fb.getDocument().getLength() + string.length()) <= 1) {
                            super.insertString(fb, offset, string.toUpperCase(), attr);
                        }
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        if ((fb.getDocument().getLength() + text.length() - length) <= 1) {
                            super.replace(fb, offset, length, text.toUpperCase(), attrs);
                        }
                    }
                });

                boardPanel.add(board[row][col]);
            }
        }

        mainPanel.add(boardPanel, BorderLayout.WEST);

        // Tworzenie panelu listy słów
        JPanel wordListPanel = new JPanel();
        wordListPanel.setLayout(new BorderLayout());
        wordListPanel.setBackground(backgroundColor);

        JTextArea wordListArea = new JTextArea();
        wordListArea.setEditable(false);
        wordListArea.setBackground(tileBackgroundColor);
        wordListArea.setForeground(textColor);
        wordListPanel.add(new JScrollPane(wordListArea), BorderLayout.CENTER);

        mainPanel.add(wordListPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }


}
