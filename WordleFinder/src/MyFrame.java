import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyFrame extends JFrame {

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

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
                board[currentRow][currentCol] = new JTextField();
                board[currentRow][currentCol].setFont(new Font("Arial", Font.PLAIN, 24));
                board[currentRow][currentCol].setHorizontalAlignment(JTextField.CENTER);
                board[currentRow][currentCol].setPreferredSize(fieldSize);
                board[currentRow][currentCol].setBackground(tileBackgroundColor);
                board[currentRow][currentCol].setForeground(textColor);
                board[currentRow][currentCol].setCaretColor(textColor);
                board[currentRow][currentCol].setBorder(BorderFactory.createLineBorder(borderColor));

                ((AbstractDocument) board[currentRow][currentCol].getDocument()).setDocumentFilter(new DocumentFilter() {
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

                board[currentRow][currentCol].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (board[currentRow][currentCol].getDocument().getLength() == 1) {
                            transferFocusToNextField(currentRow, currentCol);
                        }
                    }
                });

                boardPanel.add(board[currentRow][currentCol]);
            }
        }

        mainPanel.add(boardPanel, BorderLayout.WEST);

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

    private void transferFocusToNextField(int row, int col) {
        if (col < 4) {
            board[row][col + 1].requestFocus();
        } else if (row < 5) {
            board[row + 1][0].requestFocus();
        }
    }

}
