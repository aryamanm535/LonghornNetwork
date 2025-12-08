
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * Swing UI for the Longhorn Network lab
 */
public class NetworkLabUI extends JFrame {

    private static final Color UT_ORANGE = new Color(0xBF5700);
    private static final Color GRAPH_BG = new Color(0xF0E6D9);
    private static final String CARD_LOGO = "logo";
    private static final String CARD_GRAPH = "graph";

    private final List<List<UniversityStudent>> testCases;

    private JComboBox<String> testCaseSelector;
    private JTextArea infoArea;
    private GraphPanel graphPanel;

    private JPanel centerPanel;
    private CardLayout centerLayout;

    private JButton btnRoommates, btnReferral, btnChat;

    private JComboBox<String> referralStartCombo;
    private JTextField referralCompanyField;

    private boolean roommateModeActive = false;

    private final Image logoImage;

    public NetworkLabUI() {
        super("Longhorn Network Lab UI");

        testCases = Arrays.asList(
                Main.generateTestCase1(),
                Main.generateTestCase2(),
                Main.generateTestCase3()
        );

        ImageIcon icon = new ImageIcon(NetworkLabUI.class.getResource("/assets/longhorn.png"));
        logoImage = icon.getImage();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);

        showLogoCard();
    }

    //SIDEBAR
    //used AI help for alignment issues
    private JPanel buildSidebar() {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        box.setBackground(UT_ORANGE);

        Font headerFont = new Font("Segoe UI", Font.BOLD, 26);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);

        JLabel title = new JLabel("<html>Longhorn<br>Network UI</html>");
        title.setFont(headerFont);
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(title);
        box.add(Box.createVerticalStrut(35));

        JLabel tcLabel = new JLabel("Test Case:");
        tcLabel.setFont(labelFont);
        tcLabel.setForeground(Color.WHITE);
        tcLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(tcLabel);

        box.add(Box.createVerticalStrut(10));

        testCaseSelector = new JComboBox<>(new String[]{
            "Test Case 1", "Test Case 2", "Test Case 3"
        });
        testCaseSelector.setMaximumSize(new Dimension(180, 32));
        testCaseSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        testCaseSelector.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        box.add(testCaseSelector);

        box.add(Box.createVerticalStrut(25));

        JButton loadBtn = makeSidebarButton("Load Graph");
        loadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadBtn.addActionListener(e -> loadGraph());
        box.add(loadBtn);

        box.add(Box.createVerticalStrut(20));

        JButton testsBtn = makeSidebarButton("Run Tests");
        testsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        testsBtn.addActionListener(e -> runTests());
        box.add(testsBtn);

        box.add(Box.createVerticalGlue());
        return box;
    }

    private JButton makeSidebarButton(String text) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setBackground(Color.WHITE);
        b.setForeground(UT_ORANGE);
        b.setFocusPainted(false);
        b.setMaximumSize(new Dimension(160, 28));
        return b;
    }

    //used AI help for colors/some layout changes
    //MAIN AREA
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());

        infoArea = new JTextArea(6, 40);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBackground(new Color(0xFAF7F2));
        infoArea.setMargin(new Insets(10, 10, 10, 10));
        infoArea.setForeground(new Color(0x333333));
        infoArea.setCaretPosition(0);
        DefaultCaret caret = (DefaultCaret) infoArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        infoArea.setText("Welcome to the Longhorn Network!\n"
                + "Select a test case and click on the \"Load Graph\" button to explore,\n"
                + "or run tests to see the score.");

        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xC2763F), 2),
                "Details / Output",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(0xC2763F)
        ));

        main.add(infoScroll, BorderLayout.NORTH);

        centerLayout = new CardLayout();
        centerPanel = new JPanel(centerLayout);

        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setBackground(GRAPH_BG);
        if (logoImage != null) {
            Image scaled = logoImage.getScaledInstance(260, 160, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaled));
            logoPanel.add(logoLabel);
        }

        JPanel graphContainer = new JPanel(new BorderLayout());
        graphPanel = new GraphPanel();
        graphContainer.add(graphPanel, BorderLayout.CENTER);

        JPanel graphButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        graphButtons.setBackground(UT_ORANGE);

        btnRoommates = new JButton("Roommate Matching");
        btnReferral = new JButton("Referral Path Finder");
        btnChat = new JButton("Chat & Friends");

        btnRoommates.addActionListener(e -> toggleRoommateMode());
        btnReferral.addActionListener(e -> showReferralDialog());
        btnChat.addActionListener(e -> showChatDialog());

        graphButtons.add(btnRoommates);
        graphButtons.add(btnReferral);
        graphButtons.add(btnChat);
        graphContainer.add(graphButtons, BorderLayout.SOUTH);

        centerPanel.add(logoPanel, CARD_LOGO);
        centerPanel.add(graphContainer, CARD_GRAPH);

        main.add(centerPanel, BorderLayout.CENTER);
        return main;
    }

    private void showLogoCard() {
        centerLayout.show(centerPanel, CARD_LOGO);
    }

    private void showGraphCard() {
        centerLayout.show(centerPanel, CARD_GRAPH);
    }

    //DATA HELPERS
    private List<UniversityStudent> currentData() {
        int idx = testCaseSelector.getSelectedIndex();
        if (idx < 0 || idx >= testCases.size()) {
            return Collections.emptyList();
        }
        return testCases.get(idx);
    }

    private void loadGraph() {
        runTests();
        List<UniversityStudent> data = currentData();
        if (data.isEmpty()) {
            infoArea.setText("No students available for this test case.");
            return;
        }

        roommateModeActive = false;
        btnRoommates.setText("Roommate Matching");
        graphPanel.clearRoommateMode();

        StudentGraph g = new StudentGraph(data);
        graphPanel.setGraph(g, data);
        showGraphCard();
        infoArea.setText("Graph loaded for Test Case " + (testCaseSelector.getSelectedIndex() + 1) + ".\n"
                + "Use the buttons below to compute roommates,\n"
                + "find referral paths, or test chat & friends.");
    }

    //ROOMMATE MODE
    private void toggleRoommateMode() {
        List<UniversityStudent> data = currentData();
        if (data.isEmpty()) {
            infoArea.setText("No students loaded.");
            return;
        }

        if (!roommateModeActive) {
            loadGraph();
            data.forEach(s -> s.setRoommate(null));
            GaleShapley.assignRoommates(data);

            StringBuilder sb = new StringBuilder("Roommate Assignment:\n");
            for (UniversityStudent s : data) {
                UniversityStudent r = s.getRoommate();
                sb.append("  ").append(s.getName())
                        .append(" is roommates with ")
                        .append(r == null ? "no one" : r.getName())
                        .append("\n");
            }
            infoArea.setText(sb.toString());

            graphPanel.colorRoommates(data);

            JOptionPane.showMessageDialog(
                    this,
                    "Roommates have been matched using Gale–Shapley.\n\n"
                    + "Click on a student's node in the graph.\n"
                    + "Their roommate and the edge between them will be outlined in yellow.\n\n"
                    + "Click \"Close Roommate Matching\" to return to the normal view.",
                    "Roommate Matching",
                    JOptionPane.INFORMATION_MESSAGE
            );

            roommateModeActive = true;
            btnRoommates.setText("Close Roommate Matching");
        } else {
            roommateModeActive = false;
            btnRoommates.setText("Roommate Matching");
            graphPanel.clearRoommateMode();
            infoArea.setText("Roommate matching view closed. Graph colors reset.");
        }
    }

    private void onNodeClickedInGraph(UniversityStudent s) {
        if (!roommateModeActive || s == null) {
            return;
        }
        graphPanel.highlightRoommatePair(s);
        UniversityStudent r = s.getRoommate();
        String line = s.getName() + " is roommates with " + (r == null ? "none" : r.getName());
        infoArea.setText("Roommate highlight:\n  " + line);
    }

    //REFERRAL PATH
    private void showReferralDialog() {
        loadGraph();
        List<UniversityStudent> data = currentData();
        if (data.isEmpty()) {
            infoArea.setText("No students loaded.");
            return;
        }

        JPanel panel = new JPanel(new FlowLayout());
        referralStartCombo = new JComboBox<>();
        data.forEach(s -> referralStartCombo.addItem(s.getName()));
        referralCompanyField = new JTextField(10);
        JButton btnFind = new JButton("Find Path");
        btnFind.addActionListener(e -> findReferralPath());

        panel.add(new JLabel("Start:"));
        panel.add(referralStartCombo);
        panel.add(new JLabel("Company:"));
        panel.add(referralCompanyField);
        panel.add(btnFind);

        JOptionPane.showMessageDialog(this, panel,
                "Referral Path Finder", JOptionPane.PLAIN_MESSAGE);

        graphPanel.highlightReferral(Collections.emptyList());
    }

    private void findReferralPath() {
        String startName = (String) referralStartCombo.getSelectedItem();
        String company = referralCompanyField.getText().trim();
        if (startName == null || company.isEmpty()) {
            infoArea.setText("Please choose a student and enter a company name.");
            return;
        }

        List<UniversityStudent> data = currentData();
        UniversityStudent start = data.stream()
                .filter(s -> s.getName().equals(startName))
                .findFirst().orElse(null);
        if (start == null) {
            infoArea.setText("Starting student not found.");
            return;
        }

        ReferralPathFinder finder = new ReferralPathFinder(new StudentGraph(data));
        List<UniversityStudent> path = finder.findReferralPath(start, company);

        if (path.isEmpty()) {
            infoArea.setText("No referral path found for " + start.getName()
                    + " to \"" + company + "\".");
            graphPanel.highlightReferral(Collections.emptyList());
        } else {
            infoArea.setText("Referral path for " + start.getName()
                    + " to \"" + company + " is highlighted.");
            graphPanel.highlightReferral(path);
        }
    }

    //CHAT AND FRIENDS
    //used AI help for some data retrieval errors and minor help with making the chat menu
    private void showChatDialog() {
        loadGraph();
        List<UniversityStudent> data = currentData();
        if (data.isEmpty()) {
            infoArea.setText("No students loaded.");
            return;
        }

        String[] names = data.stream().map(UniversityStudent::getName).toArray(String[]::new);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        DefaultComboBoxModel<String> senderModel = new DefaultComboBoxModel<>(names);
        DefaultComboBoxModel<String> receiverModel = new DefaultComboBoxModel<>(names);

        JComboBox<String> senderCombo = new JComboBox<>(senderModel);
        JComboBox<String> receiverCombo = new JComboBox<>(receiverModel);

        senderCombo.addActionListener(e -> {
            String selected = (String) senderCombo.getSelectedItem();
            receiverModel.removeAllElements();
            for (String n : names) {
                if (!n.equals(selected)) {
                    receiverModel.addElement(n);
                }
            }
        });
        if (senderCombo.getItemCount() > 0) {
            senderCombo.setSelectedIndex(0);
            for (ActionListener al : senderCombo.getActionListeners()) {
                al.actionPerformed(null);
            }
        }

        JTextField messageField = new JTextField(15);

        JButton btnSendChat = new JButton("Send Chat Message");
        JButton btnSendRequest = new JButton("Send Friend Request");
        JButton btnShowHistory = new JButton("Show Chat & Friend History");

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Sender:"), c);
        c.gridx = 1;
        panel.add(senderCombo, c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Receiver:"), c);
        c.gridx = 1;
        panel.add(receiverCombo, c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Message:"), c);
        c.gridx = 1;
        panel.add(messageField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        JPanel buttonsRow = new JPanel(new FlowLayout());
        buttonsRow.add(btnSendChat);
        buttonsRow.add(btnSendRequest);
        buttonsRow.add(btnShowHistory);
        panel.add(buttonsRow, c);

        btnSendChat.addActionListener(e -> {
            UniversityStudent sender = findByName((String) senderCombo.getSelectedItem());
            UniversityStudent receiver = findByName((String) receiverCombo.getSelectedItem());
            if (sender == null || receiver == null) {
                infoArea.setText("Choose valid students for chat.");
                return;
            }
            String msg = messageField.getText().trim();
            new Thread(new ChatThread(sender, receiver, msg)).start();
            infoArea.setText("Chat sent from " + sender.getName() + " to "
                    + receiver.getName() + ":\n  \"" + msg + "\"");
        });

        btnSendRequest.addActionListener(e -> {
            UniversityStudent sender = findByName((String) senderCombo.getSelectedItem());
            UniversityStudent receiver = findByName((String) receiverCombo.getSelectedItem());
            if (sender == null || receiver == null) {
                infoArea.setText("Choose valid students for friend request.");
                return;
            }
            new Thread(new FriendRequestThread(sender, receiver)).start();
            infoArea.setText("Friend request sent from " + sender.getName()
                    + " to " + receiver.getName() + ".");
        });

        btnShowHistory.addActionListener(e -> {
            UniversityStudent sender = findByName((String) senderCombo.getSelectedItem());
            if (sender == null) {
                infoArea.setText("Sender not found.");
                return;
            }
            showHistoryFor(sender);
        });

        JOptionPane.showMessageDialog(this, panel,
                "Chat & Friends", JOptionPane.PLAIN_MESSAGE);
    }

    private UniversityStudent findByName(String name) {
        if (name == null) {
            return null;
        }
        return currentData().stream()
                .filter(s -> name.equals(s.getName()))
                .findFirst().orElse(null);
    }

    //display chat history
    private void showHistoryFor(UniversityStudent s) {
        StringBuilder sb = new StringBuilder();
        sb.append("History for ").append(s.getName()).append(":\n\n");
        sb.append("Friends:\n");
        if (s.getFriends().isEmpty()) {
            sb.append("  None\n");
        } else {
            for (UniversityStudent f : s.getFriends()) {
                sb.append("  ").append(f.getName()).append("\n");
            }
        }
        boolean anyInteraction = false;
        sb.append("\n");
        sb.append("Chat History:\n");
        // Loop through everyone in the same test case
        for (UniversityStudent other : currentData()) {
            if (other == s) {
                continue;
            }

            List<String> conv = s.getChatHistoryWith(other);

            if (!conv.isEmpty()) {
                anyInteraction = true;
                sb.append("- With ").append(other.getName()).append(":\n");

                for (String msg : conv) {
                    sb.append("  ").append(msg).append("\n");
                }
                sb.append("\n");
            }
        }

        if (!anyInteraction) {
            sb.append("No chat history.\n");
        }

        infoArea.setText(sb.toString());
    }

    //RUN TESTS
    private void runTests() {
        int caseNum = testCaseSelector.getSelectedIndex() + 1;
        int score = Main.gradeLab(currentData(), caseNum);
        infoArea.setText("Test Case " + caseNum + " Score: " + score);
    }

    //GRAPH PANEL
    //used AI help to create the hover chart for students and some coloring issues
    private class GraphPanel extends JPanel {

        private StudentGraph graph;
        private List<UniversityStudent> nodes = Collections.emptyList();

        private List<UniversityStudent> referralPath = Collections.emptyList();
        private Set<String> referralEdges = Collections.emptySet();

        private Map<UniversityStudent, Color> roommateColors = new HashMap<>();
        private Set<UniversityStudent> highlightedRoommatePair = new HashSet<>();
        private Set<String> highlightedRoommateEdges = new HashSet<>();

        public GraphPanel() {
            setBackground(GRAPH_BG);
            ToolTipManager.sharedInstance().registerComponent(this);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    UniversityStudent hit = findNodeAt(e.getX(), e.getY());
                    if (hit != null) {
                        NetworkLabUI.this.onNodeClickedInGraph(hit);
                    }
                }
            });
        }

        @Override
        public String getToolTipText(MouseEvent e) {
            UniversityStudent s = findNodeAt(e.getX(), e.getY());
            if (s == null) {
                return null;
            }

            return "<html><b>" + s.getName() + "</b><br>"
                    + "Age: " + s.getAge() + "<br>"
                    + "Major: " + s.getMajor() + "<br>"
                    + "Year: " + s.getYear() + "<br>"
                    + "GPA: " + s.getGpa() + "<br>"
                    + "Internships: " + s.getPreviousInternships() + "</html>";
        }

        public void setGraph(StudentGraph g, List<UniversityStudent> data) {
            graph = g;
            nodes = new ArrayList<>(data);
            referralPath = Collections.emptyList();
            referralEdges = Collections.emptySet();
            roommateColors.clear();
            highlightedRoommatePair.clear();
            highlightedRoommateEdges.clear();
            repaint();
        }

        public void highlightReferral(List<UniversityStudent> path) {
            referralPath = path;
            Set<String> edges = new HashSet<>();
            for (int i = 0; i + 1 < path.size(); i++) {
                UniversityStudent a = path.get(i), b = path.get(i + 1);
                edges.add(edgeKey(a, b));
                edges.add(edgeKey(b, a));
            }
            referralEdges = edges;
            repaint();
        }

        public void colorRoommates(List<UniversityStudent> data) {
            roommateColors.clear();
            highlightedRoommatePair.clear();
            highlightedRoommateEdges.clear();

            Color[] palette = {
                new Color(0xF4A261), new Color(0x2A9D8F),
                new Color(0xE76F51), new Color(0x264653),
                new Color(0x8AB17D), new Color(0x577590)
            };

            int idx = 0;
            Set<UniversityStudent> assigned = new HashSet<>();

            for (UniversityStudent s : data) {
                if (assigned.contains(s)) {
                    continue;
                }
                UniversityStudent r = s.getRoommate();
                if (r != null && !assigned.contains(r)) {
                    Color c = palette[idx % palette.length];
                    roommateColors.put(s, c);
                    roommateColors.put(r, c);
                    assigned.add(s);
                    assigned.add(r);
                    idx++;
                }
            }

            for (UniversityStudent s : data) {
                roommateColors.putIfAbsent(s, new Color(255, 150, 80));
            }

            repaint();
        }

        public void clearRoommateMode() {
            roommateColors.clear();
            highlightedRoommatePair.clear();
            highlightedRoommateEdges.clear();
            repaint();
        }

        public void highlightRoommatePair(UniversityStudent s) {
            highlightedRoommatePair.clear();
            highlightedRoommateEdges.clear();
            if (s == null) {
                repaint();
                return;
            }

            highlightedRoommatePair.add(s);
            UniversityStudent r = s.getRoommate();
            if (r != null && nodes.contains(r)) {
                highlightedRoommatePair.add(r);
                highlightedRoommateEdges.add(edgeKey(s, r));
                highlightedRoommateEdges.add(edgeKey(r, s));
            }
            repaint();
        }

        private UniversityStudent findNodeAt(int mx, int my) {
            Map<UniversityStudent, Point> pos = computePositions();
            int radius = 15;
            for (UniversityStudent s : nodes) {
                Point p = pos.get(s);
                int dx = mx - p.x, dy = my - p.y;
                if (dx * dx + dy * dy <= radius * radius) {
                    return s;
                }
            }
            return null;
        }

        private Map<UniversityStudent, Point> computePositions() {
            Map<UniversityStudent, Point> map = new HashMap<>();
            if (nodes.isEmpty()) {
                return map;
            }

            int w = getWidth(), h = getHeight();
            int r = Math.min(w, h) / 3;
            int cx = w / 2, cy = h / 2;

            for (int i = 0; i < nodes.size(); i++) {
                double ang = 2 * Math.PI * i / nodes.size();
                map.put(nodes.get(i),
                        new Point(cx + (int) (r * Math.cos(ang)),
                                cy + (int) (r * Math.sin(ang))));
            }
            return map;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (graph == null || nodes.isEmpty()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Map<UniversityStudent, Point> coords = computePositions();

            int radius = 22;   // bigger nodes
            Font nameFont = new Font("Arial", Font.BOLD, 13);
            Font weightFont = new Font("Arial", Font.BOLD, 14);

            //Draw Edges
            for (UniversityStudent s : nodes) {
                for (StudentGraph.Edge e : graph.getNeighbors(s)) {
                    UniversityStudent t = e.neighbor;
                    if (nodes.indexOf(t) <= nodes.indexOf(s)) {
                        continue;
                    }
                    Point p1 = coords.get(s);
                    Point p2 = coords.get(t);

                    String key = edgeKey(s, t);
                    boolean highlight
                            = referralEdges.contains(key)
                            || highlightedRoommateEdges.contains(key);

                    g2.setColor(highlight ? Color.YELLOW : Color.BLACK);
                    g2.setStroke(new BasicStroke(1f + 0.6f * e.weight));
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                    g2.setFont(weightFont);
                    g2.setColor(Color.BLACK);
                    int mx = (p1.x + p2.x) / 2;
                    int my = (p1.y + p2.y) / 2;

                    g2.drawString(String.valueOf(e.weight), mx - 4, my - 6);
                }
            }

            //Draw nodes
            for (UniversityStudent s : nodes) {
                Point p = coords.get(s);

                Color fill = roommateColors.getOrDefault(s, new Color(255, 150, 80));

                // referral colors
                if (referralPath.contains(s)) {
                    int index = referralPath.indexOf(s);
                    fill = (index == 0) ? new Color(0x2A9D8F)
                            : (index == referralPath.size() - 1) ? new Color(0xE76F51)
                            : new Color(0xF4A261);
                }

                g2.setColor(fill);
                g2.fillOval(p.x - radius, p.y - radius, radius * 2, radius * 2);

                // roommate pair highlight
                if (highlightedRoommatePair.contains(s)) {
                    g2.setColor(Color.YELLOW);
                    g2.setStroke(new BasicStroke(4f));
                    g2.drawOval(p.x - radius - 3, p.y - radius - 3,
                            radius * 2 + 6, radius * 2 + 6);
                }
                g2.setFont(nameFont);
                g2.setColor(Color.BLACK);
                String label = s.getName();
                //used AI for this:
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, p.x - fm.stringWidth(label) / 2, p.y + fm.getAscent() / 2 - 2);
            }
        }

        private String edgeKey(UniversityStudent a, UniversityStudent b) {
            return a.getName() + "->" + b.getName();
        }
    }

    //Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkLabUI().setVisible(true));
    }
}
