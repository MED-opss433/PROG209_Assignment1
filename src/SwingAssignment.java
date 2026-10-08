import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PROG209 - Enterprise-Grade Event-Driven Architecture Assignment
 * Luxury Gold Edition with Custom Painted Components
 *
 * @author Medicia Conteh
 * @version 2.2.0
 * @since 2026-10-08
 */
public class SwingAssignment extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(SwingAssignment.class.getName());

    // UI Dimensions & Constants
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final String APP_TITLE = "PROG209 - Luxury Edition Interface";
    private static final String ASSET_ICON_PATH = "icon.png";

    // Theme Palette: Luxury White & Gold
    private static final Color COLOR_BG_PRIMARY = new Color(252, 252, 250);     // Soft Pure White
    private static final Color COLOR_GOLD_PRIMARY = new Color(212, 175, 55);    // Metallic Gold
    private static final Color COLOR_GOLD_HOVER = new Color(230, 195, 80);      // Lighter Gold for Hover
    private static final Color COLOR_TEXT_DARK = new Color(40, 40, 40);         // Deep Charcoal
    private static final Color COLOR_TEXT_MUTED = new Color(120, 120, 120);     // Muted Gray

    // Domain Model Components
    private JPanel rootPanel;
    private JLabel bannerLabel;
    private JLabel creditLabel;
    private JButton actionButtonOne;
    private JButton actionButtonTwo;
    private Image logoImage;

    // State Management Enums
    private enum ApplicationState {
        DEFAULT("Welcome to Java Swing First Individual Assignment by Medicia Conteh", COLOR_TEXT_DARK),
        MODIFIED("System State Updated: Event Stream Executed Successfully", COLOR_GOLD_PRIMARY);

        private final String message;
        private final Color textColor;

        ApplicationState(String message, Color textColor) {
            this.message = message;
            this.textColor = textColor;
        }

        public String getMessage() { return message; }
        public Color getTextColor() { return textColor; }
    }

    private ApplicationState currentState = ApplicationState.DEFAULT;

    public SwingAssignment() {
        configureNativeSystemEnvironment();
        loadAssets();
        initializeCoreComponents();
        assembleLayoutHierarchy();
        registerEventHandlers();
        renderWindow();
    }

    private void configureNativeSystemEnvironment() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Failed to initialize native system look and feel.", ex);
        }
    }

    private void loadAssets() {
        File iconFile = new File(ASSET_ICON_PATH);
        if (iconFile.exists()) {
            ImageIcon appIcon = new ImageIcon(ASSET_ICON_PATH);
            setIconImage(appIcon.getImage());
            logoImage = appIcon.getImage();
            LOGGER.info("Application assets loaded successfully from root path.");
        } else {
            LOGGER.warning("Target asset 'icon.png' missing from root directory. Using fallback generator.");
        }
    }

    private void initializeCoreComponents() {
        setTitle(APP_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Root container with pure luxury white background
        rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(COLOR_BG_PRIMARY);
        rootPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Typography setup
        bannerLabel = new JLabel(currentState.getMessage(), SwingConstants.CENTER);
        bannerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bannerLabel.setForeground(currentState.getTextColor());

        creditLabel = new JLabel("Architected & Developed by Medicia Conteh", SwingConstants.CENTER);
        creditLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        creditLabel.setForeground(COLOR_TEXT_MUTED);

        // Styled Custom Action Buttons
        actionButtonOne = createCustomStyledButton("Trigger Event State", COLOR_GOLD_PRIMARY, COLOR_GOLD_HOVER, Color.WHITE);
        actionButtonTwo = createCustomStyledButton("Toggle Canvas Theme", COLOR_TEXT_DARK, new Color(70, 70, 70), Color.WHITE);
    }

    /**
     * Creates a custom-painted button to bypass OS native LaF background overrides.
     */
    private JButton createCustomStyledButton(String text, Color baseColor, Color hoverColor, Color textColor) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fill background shape with rounded corners
                g2d.setColor(isHovered ? hoverColor : baseColor);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));

                // Paint text
                g2d.setColor(textColor);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }
        };

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(220, 45));

        return button;
    }

    private void assembleLayoutHierarchy() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 25, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // 1. Header Title Label
        rootPanel.add(bannerLabel, gbc);

        // 2. Central Circular Logo Component
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        rootPanel.add(createCircularLogoPanel(), gbc);

        // 3. Subtitle / Credit Label
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 35, 0);
        rootPanel.add(creditLabel, gbc);

        // 4. Control Panel Sub-grid for Action Buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        controlPanel.setOpaque(false);
        controlPanel.add(actionButtonOne);
        controlPanel.add(actionButtonTwo);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        rootPanel.add(controlPanel, gbc);

        add(rootPanel);
    }

    /**
     * Creates a custom component that clips and renders the logo image
     * inside a centered circle with a luxury metallic gold border ring.
     */
    private JPanel createCircularLogoPanel() {
        JPanel circlePanel = new JPanel() {
            private final int diameter = 160;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;

                // Create circular clipping mask
                Ellipse2D.Double circle = new Ellipse2D.Double(x, y, diameter, diameter);

                // Draw background white base
                g2d.setColor(Color.WHITE);
                g2d.fill(circle);

                if (logoImage != null) {
                    g2d.setClip(circle);
                    g2d.drawImage(logoImage, x, y, diameter, diameter, this);
                    g2d.setClip(null); // Reset clip for border painting
                } else {
                    // Fallback monogram if image asset is unavailable
                    g2d.setClip(circle);
                    g2d.setColor(new Color(245, 245, 245));
                    g2d.fill(circle);
                    g2d.setClip(null);
                    g2d.setColor(COLOR_TEXT_DARK);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
                    FontMetrics fm = g2d.getFontMetrics();
                    String fallback = "MC";
                    g2d.drawString(fallback, x + (diameter - fm.stringWidth(fallback)) / 2, y + (diameter + fm.getAscent()) / 2 - 4);
                }

                // Render luxurious double gold ring border
                g2d.setColor(COLOR_GOLD_PRIMARY);
                g2d.setStroke(new BasicStroke(3.5f));
                g2d.draw(new Ellipse2D.Double(x + 1, y + 1, diameter - 2, diameter - 2));

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        circlePanel.setOpaque(false);
        return circlePanel;
    }

    private void registerEventHandlers() {
        // Event Handler 1: State switching mechanism with gold accents
        actionButtonOne.addActionListener(e -> {
            currentState = (currentState == ApplicationState.DEFAULT)
                    ? ApplicationState.MODIFIED
                    : ApplicationState.DEFAULT;

            bannerLabel.setText(currentState.getMessage());
            bannerLabel.setForeground(currentState.getTextColor());
            LOGGER.info("UI Event Handled: State transitioned successfully.");
        });

        // Event Handler 2: Dynamic luxury background cycling
        actionButtonTwo.addActionListener(e -> {
            Color[] luxuryPalette = {
                    new Color(252, 252, 250), // Pure Alabaster White
                    new Color(250, 247, 240), // Warm Champagne Cream
                    new Color(245, 242, 235)  // Soft Ivory Tone
            };
            Color selectedColor = luxuryPalette[(int)(Math.random() * luxuryPalette.length)];
            rootPanel.setBackground(selectedColor);
            LOGGER.info("UI Event Handled: Luxury theme canvas updated.");
        });
    }

    private void renderWindow() {
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new SwingAssignment();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Critical runtime failure during application initialization.", ex);
            }
        });
    }
}