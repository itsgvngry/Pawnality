import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;

// ============================================================
// GUI & EVENT-DRIVEN PROGRAMMING
// This is the face of our application! It builds the Swing UI,
// listens for button clicks, and ties together the QuizEngine 
// and FileManager to create the full user experience.
// ============================================================
public class MainFrame extends JFrame {

    // ═══════════════════════════════════════════════════════════════════════
    //  ENTRY POINT (main method)
    // ═══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }

    // --- Theme Colors ---
    static final Color BG       = new Color(10, 10, 10);
    static final Color SURFACE  = new Color(22, 22, 22);
    static final Color GOLD     = new Color(212, 175, 55);
    static final Color GOLD_DIM = new Color(140, 115, 35);
    static final Color WHITE    = Color.WHITE;
    static final Color GRAY     = new Color(120, 120, 120);
    static final Color BTN_BG   = new Color(32, 32, 32);
    static final Color BTN_HVR  = new Color(44, 36, 8);
    static final Color ERROR    = new Color(210, 60, 60);

    // --- Fonts ---
    static final Font F_TITLE  = new Font("SansSerif", Font.BOLD, 38);
    static final Font F_LARGE  = new Font("SansSerif", Font.BOLD, 28);
    static final Font F_MED    = new Font("SansSerif", Font.BOLD, 15);
    static final Font F_BODY   = new Font("SansSerif", Font.PLAIN, 13);
    static final Font F_SMALL  = new Font("SansSerif", Font.PLAIN, 11);
    static final Font F_PIECE  = new Font("Dialog",    Font.PLAIN, 80);
    static final Font F_BTN    = new Font("SansSerif", Font.BOLD,  12);
    static final Font F_TAG    = new Font("SansSerif", Font.ITALIC, 13);

    // --- Application State ---
    private CardLayout   cardLayout;
    private JPanel       mainPanel;
    private QuizEngine   engine;
    private String       userName = "";

    // References to UI components so we can update them dynamically.
    private JLabel    qCounterLabel;
    private JLabel    qTextLabel;
    private JButton[] answerBtns = new JButton[4];
    private JProgressBar progressBar;
    private JLabel    resPieceLabel, resCongLabel, resNameLabel, resTraitLabel;
    private JLabel    resDescLabel, resGmLabel, resGmTitleLabel, resGmStyleLabel;

    // ============================================================
    // CONSTRUCTOR
    // Sets up the window, creates the QuizEngine, and builds the 3 screens.
    // ============================================================
    public MainFrame() {
        setTitle("PAWNALITY");
        setSize(460, 680);
        setMinimumSize(new Dimension(400, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Just a little UI tweak to make it look cleaner.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        engine    = new QuizEngine(buildQuestions());
        cardLayout = new CardLayout();
        mainPanel  = dark();
        mainPanel.setLayout(cardLayout);

        // Building the 3 screens and adding them to the CardLayout.
        mainPanel.add(buildWelcomeScreen(),  "welcome");
        mainPanel.add(buildQuestionScreen(), "question");
        mainPanel.add(buildResultScreen(),   "result");

        add(mainPanel);
        cardLayout.show(mainPanel, "welcome");
        setVisible(true);
    }

    // ============================================================
    // SCREEN 1: WELCOME
    // Asks for the user's name. Contains our Exception Handling demo.
    // ============================================================
    private JPanel buildWelcomeScreen() {
        JPanel p = dark();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(pad(55, 48, 55, 48));

        JLabel piece = centered("\u265B", GOLD, F_PIECE);
        JLabel title = centered("PAWNALITY", WHITE, F_TITLE);
        JLabel tag = centered("What kind of player are you?", GRAY, F_TAG);
        JLabel nameLabel = centered("Enter your name to begin", GRAY, F_SMALL);
        nameLabel.setBorder(pad(0, 0, 6, 0));

        JTextField nameField = new JTextField();
        nameField.setBackground(new Color(28, 28, 28));
        nameField.setForeground(WHITE);
        nameField.setCaretColor(GOLD);
        nameField.setFont(F_BODY);
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 58, 15), 1),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel errLabel = centered(" ", ERROR, F_SMALL);

        JButton startBtn = goldBtn("START QUIZ");

        // ============================================================
        // EVENT HANDLING & EXCEPTION HANDLING
        // When the user clicks 'Start', we try to validate the name.
        // If it fails, we catch the exception and display the message 
        // on the screen WITHOUT crashing the app!
        // ============================================================
        ActionListener onStart = e -> {
            try {
                QuizEngine.validateName(nameField.getText()); // Validation happens here.
                userName = nameField.getText().trim();
                errLabel.setText(" ");
                engine = new QuizEngine(buildQuestions()); // Reset quiz.
                refreshQuestion();
                cardLayout.show(mainPanel, "question");
            } catch (IllegalArgumentException ex) {
                errLabel.setText(ex.getMessage()); // Friendly error message.
            }
        };
        startBtn.addActionListener(onStart);
        nameField.addActionListener(onStart); // Also works on Enter key.

        p.add(piece);
        p.add(Box.createVerticalStrut(2));
        p.add(title);
        p.add(Box.createVerticalStrut(6));
        p.add(tag);
        p.add(Box.createVerticalStrut(32));
        p.add(separator());
        p.add(Box.createVerticalStrut(28));
        p.add(nameLabel);
        p.add(Box.createVerticalStrut(10));
        p.add(nameField);
        p.add(Box.createVerticalStrut(8));
        p.add(errLabel);
        p.add(Box.createVerticalStrut(18));
        p.add(startBtn);
        p.add(Box.createVerticalGlue());
        return p;
    }

    // ============================================================
    // SCREEN 2: QUESTION
    // Displays the question, 4 answer buttons, and a progress bar.
    // ============================================================
    private JPanel buildQuestionScreen() {
        JPanel outer = dark();
        outer.setLayout(new BorderLayout());

        // Header
        JPanel header = dark();
        header.setLayout(new BorderLayout());
        header.setBorder(pad(20, 24, 0, 24));
        JLabel hTitle = new JLabel("PAWNALITY");
        hTitle.setForeground(GOLD);
        hTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        qCounterLabel = new JLabel("1 of 10");
        qCounterLabel.setForeground(GRAY);
        qCounterLabel.setFont(F_SMALL);
        header.add(hTitle, BorderLayout.WEST);
        header.add(qCounterLabel, BorderLayout.EAST);

        // Center Content
        JPanel center = dark();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(pad(16, 24, 16, 24));
        qTextLabel = new JLabel();
        qTextLabel.setForeground(WHITE);
        qTextLabel.setFont(F_MED);
        qTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        qTextLabel.setHorizontalAlignment(JLabel.CENTER);

        center.add(separator());
        center.add(Box.createVerticalStrut(22));
        center.add(qTextLabel);
        center.add(Box.createVerticalStrut(28));
        center.add(separator());
        center.add(Box.createVerticalStrut(18));

        // 4 answer buttons with A, B, C, D labels.
        String[] letters = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            answerBtns[i] = answerBtn(letters[i], i);
            center.add(answerBtns[i]);
            center.add(Box.createVerticalStrut(10));
        }

        // Footer Progress Bar
        JPanel footer = dark();
        footer.setLayout(new BorderLayout());
        footer.setBorder(pad(14, 24, 18, 24));
        progressBar = new JProgressBar(0, 10);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setBackground(new Color(35, 35, 35));
        progressBar.setForeground(GOLD);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 5));
        footer.add(progressBar, BorderLayout.CENTER);

        outer.add(header, BorderLayout.NORTH);
        outer.add(center, BorderLayout.CENTER);
        outer.add(footer, BorderLayout.SOUTH);
        return outer;
    }

    // ============================================================
    // SCREEN 3: RESULT
    // Shows the calculated playstyle, description, and GM match.
    // ============================================================
    private JPanel buildResultScreen() {
        JPanel outer = dark();
        outer.setLayout(new BorderLayout());

        JPanel inner = dark();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(pad(36, 44, 36, 44));

        resPieceLabel  = centered("", WHITE, F_PIECE);
        resCongLabel   = centered("", GRAY,  F_SMALL);
        resNameLabel   = centered("", WHITE, F_LARGE);
        resTraitLabel  = centered("", GRAY,  F_SMALL);
        resDescLabel   = centered("", new Color(200, 200, 200), F_BODY);
        resGmLabel     = centered("", GOLD,  F_MED);
        resGmTitleLabel= centered("", GRAY,  F_SMALL);
        resGmStyleLabel= centered("", new Color(175, 175, 175), F_SMALL);

        JLabel gmHeading = centered("GRANDMASTER MATCH", GOLD_DIM,
            new Font("SansSerif", Font.BOLD, 10));

        // Retake button to resets the quiz.
        JButton retakeBtn = goldBtn("RETAKE QUIZ");
        retakeBtn.addActionListener(e -> {
            engine = new QuizEngine(buildQuestions());
            cardLayout.show(mainPanel, "welcome");
        });

        // History button to triggers File I/O loading.
        JButton historyBtn = outlineBtn("VIEW HISTORY");
        historyBtn.addActionListener(e -> showHistory());

        JPanel btnRow = dark();
        btnRow.setLayout(new GridLayout(1, 2, 12, 0));
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRow.add(retakeBtn);
        btnRow.add(historyBtn);

        inner.add(resPieceLabel);
        inner.add(Box.createVerticalStrut(4));
        inner.add(resCongLabel);
        inner.add(Box.createVerticalStrut(4));
        inner.add(resNameLabel);
        inner.add(Box.createVerticalStrut(6));
        inner.add(resTraitLabel);
        inner.add(Box.createVerticalStrut(18));
        inner.add(separator());
        inner.add(Box.createVerticalStrut(16));
        inner.add(resDescLabel);
        inner.add(Box.createVerticalStrut(20));
        inner.add(separator());
        inner.add(Box.createVerticalStrut(18));
        inner.add(gmHeading);
        inner.add(Box.createVerticalStrut(10));
        inner.add(resGmLabel);
        inner.add(Box.createVerticalStrut(4));
        inner.add(resGmTitleLabel);
        inner.add(Box.createVerticalStrut(8));
        inner.add(resGmStyleLabel);
        inner.add(Box.createVerticalStrut(26));
        inner.add(separator());
        inner.add(Box.createVerticalStrut(22));
        inner.add(btnRow);

        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(null);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setBackground(BG);
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }

    // ============================================================
    // CORE LOGIC METHODS
    // ============================================================

    // Handles the user clicking an answer button.
    private void handleAnswer(int index) {
        Question q = engine.getCurrentQuestion();
        // Record the score based on which playstyle this answer maps to.
        engine.submitAnswer(q.getPlaystyleKey(index)); 

        if (engine.hasMoreQuestions()) {
            refreshQuestion(); // Load the next one.
        } else {
            progressBar.setValue(10);
            showResult(); // Quiz is done!
        }
    }

    // Updates the UI to display the next question.
    private void refreshQuestion() {
        Question q    = engine.getCurrentQuestion();
        int num       = engine.getCurrentIndex() + 1;
        int total     = engine.getTotalQuestions();
        qCounterLabel.setText(num + " of " + total);
        progressBar.setValue(num - 1);
        qTextLabel.setText(
            "<html><div style='text-align:center;width:310px'>"
            + q.getQuestionText() + "</div></html>"
        );
        String[] ans = q.getAnswers();
        for (int i = 0; i < 4; i++) {
            answerBtns[i].setText(ans[i]);
        }
    }

    // ============================================================
    // POLYMORPHISM & FILE HANDLING IN ACTION
    // 1. We call calculateResult() which uses RECURSION.
    // 2. We call createPlaystyle() which uses POLYMORPHISM to return
    //    the correct subclass.
    // 3. We save the result to a file using FILE HANDLING.
    // ============================================================
    private void showResult() {
        String key       = engine.calculateResult();        // Recursion returns the winner.
        Playstyle result = QuizEngine.createPlaystyle(key); // Polymorphism creates the object.

        // Fill the UI with the result data.
        resPieceLabel.setForeground(result.getColor());
        resPieceLabel.setText(result.getChessPiece());
        resCongLabel.setText("CONGRATULATIONS, " + userName.toUpperCase());
        resNameLabel.setText(result.getName().toUpperCase());
        resNameLabel.setForeground(result.getColor());
        resTraitLabel.setText(result.getTrait().toUpperCase() + " PLAYSTYLE");
        resDescLabel.setText(
            "<html><div style='text-align:center;width:320px'>"
            + result.getDescription() + "</div></html>"
        );
        resGmLabel.setText(result.getGrandmaster());
        resGmTitleLabel.setText(result.getGrandmasterTitle());
        resGmStyleLabel.setText(
            "<html><div style='text-align:center;width:320px'>"
            + result.getGrandmasterStyle() + "</div></html>"
        );

        // Saving to the text file.
        ResultFileManager.saveResult(userName, result.getName());

        cardLayout.show(mainPanel, "result");
    }

    // ============================================================
    // FILE HANDLING (READING)
    // Loads the history from the file and shows it in a popup.
    // ============================================================
    private void showHistory() {
        List<String> history = ResultFileManager.loadHistory(); 
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No history yet. Complete a quiz to save your first result.",
                "History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        int start = Math.max(0, history.size() - 10); // Show the last 10 entries.
        for (int i = start; i < history.size(); i++) {
            sb.append(history.get(i)).append("\n");
        }
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(F_SMALL);
        area.setBackground(SURFACE);
        area.setForeground(WHITE);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(360, 180));
        JOptionPane.showMessageDialog(this, sp, "Past Results", JOptionPane.PLAIN_MESSAGE);
    }

    // ============================================================
    // UI HELPER METHODS (Custom Painting & Styling)
    // These create our dark, gold-themed buttons and panels.
    // ============================================================
    private JPanel dark() {
        JPanel p = new JPanel();
        p.setBackground(BG);
        return p;
    }

    private JLabel centered(String text, Color color, Font font) {
        JLabel l = new JLabel(text, JLabel.CENTER);
        l.setForeground(color);
        l.setFont(font);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private Border pad(int t, int l, int b, int r) {
        return BorderFactory.createEmptyBorder(t, l, b, r);
    }

    private JSeparator separator() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(55, 45, 12));
        s.setBackground(new Color(55, 45, 12));
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    // Custom painted Gold button.
    private JButton goldBtn(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()   ? GOLD_DIM  :
                            getModel().isRollover()  ? GOLD.brighter() : GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setFont(F_BTN);
                g2.setColor(BG);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Custom painted Outlined button.
    private JButton outlineBtn(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? BTN_HVR :
                            getModel().isRollover() ? BTN_HVR : BTN_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(GOLD_DIM);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.setFont(F_BTN);
                g2.setColor(GOLD);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Custom answer button with a gold circle badge (A, B, C, D).
    private JButton answerBtn(final String letter, int index) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? BTN_HVR :
                            getModel().isRollover() ? BTN_HVR : BTN_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(getModel().isRollover() || getModel().isPressed()
                            ? GOLD : new Color(55, 55, 55));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                int cy = (getHeight() - 24) / 2;
                g2.setColor(GOLD);
                g2.fillOval(12, cy, 24, 24);
                g2.setColor(BG);
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(letter,
                    12 + (24 - fm.stringWidth(letter)) / 2,
                    cy + fm.getAscent() + (24 - fm.getAscent() - fm.getDescent()) / 2
                );
                g2.setColor(WHITE);
                g2.setFont(F_BODY);
                fm = g2.getFontMetrics();
                String t = getText();
                if (t == null) t = "";
                g2.drawString(t, 46,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 52));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // ============================================================
        // EVENT HANDLING
        // This is where we attach the listener. When this button is 
        // clicked, it calls handleAnswer() with its specific index.
        // ============================================================
        btn.addActionListener(e -> handleAnswer(index)); 
        return btn;
    }

    // ============================================================
    // THE QUESTION DATA (ARRAY OF 10 QUESTIONS) 
    // Each question has 4 answers, each mapped to a playstyle key.
    // ============================================================
private Question[] buildQuestions() {
    Question[] qs = new Question[10]; // ARRAY

    qs[0] = new Question(
        "Your team is losing badly with 10 minutes left. You...",
        new String[]{
            "Take charge and go all in immediately",
            "Reassess calmly and adjust the game plan",
            "Try something completely unexpected",
            "Hold your ground and prevent further damage"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[1] = new Question(
        "You are given a group project with no clear leader. You...",
        new String[]{
            "Step up and take charge immediately",
            "Suggest assigning roles based on each person's strengths",
            "Propose a completely different approach to the whole project",
            "Wait and see how things naturally develop"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[2] = new Question(
        "You get harsh criticism on work you are proud of. You...",
        new String[]{
            "Stand your ground and defend your work firmly",
            "Analyse the feedback carefully and revise with purpose",
            "Use it as fuel to produce something even bolder",
            "Accept it quietly, adjust, and move on"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[3] = new Question(
        "You spot a shortcut that might not work. You...",
        new String[]{
            "Take the risk — the reward is worth it",
            "Test it carefully in a small way before committing",
            "Try it just to see what happens",
            "Stick to the proven, safer route"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[4] = new Question(
        "You are given a surprise project with no instructions. You...",
        new String[]{
            "Start building immediately and figure it out as you go",
            "Plan the full structure carefully before touching anything",
            "Experiment with the most unusual approach you can think of",
            "Find an existing template or example to follow"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[5] = new Question(
        "Your friend group cannot agree on weekend plans. You...",
        new String[]{
            "Just make a decision for everyone and go",
            "Suggest the option you already researched beforehand",
            "Propose something completely new that nobody has tried",
            "Go along with whatever the majority decides"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[6] = new Question(
        "In an argument, you tend to...",
        new String[]{
            "Push hard and fight your corner to win the point",
            "Stay logical, gather evidence, and build your case slowly",
            "Shift the conversation in a direction nobody expected",
            "Stay calm, keep the peace, and avoid escalating"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[7] = new Question(
        "You have a big exam tomorrow and feel underprepared. You...",
        new String[]{
            "Pull an all-nighter and grind through everything",
            "Focus only on the highest-weighted topics strategically",
            "Try creative memorisation techniques you've never used before",
            "Sleep well and trust what you already know"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[8] = new Question(
        "Your approach to starting a brand new video game is...",
        new String[]{
            "Jump straight in and learn by failing repeatedly",
            "Read the guide thoroughly before touching anything",
            "Try the weirdest strategies just to see what happens",
            "Follow the tutorial step by step until you feel ready"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    qs[9] = new Question(
        "You are facing a tight deadline you might not hit. You...",
        new String[]{
            "Sprint as hard as possible and push to the very end",
            "Communicate early, prioritise ruthlessly, and adjust the plan",
            "Find an unconventional shortcut nobody thought of",
            "Deliver what is done, stay composed, and explain clearly"
        },
        new String[]{"Tactician", "Strategist", "Wildcard", "Fortress"}
    );

    return qs;
 }
}
