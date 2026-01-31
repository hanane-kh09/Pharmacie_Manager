package com.pharmacie.setup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import com.pharmacie.view.MainFrame;

public class SetupFrame extends JFrame {

    // --- Modern UI Constants ---
    private static final Color COLOR_PRIMARY = new Color(25, 118, 210); // Chrome Blue / Modern Blue
    private static final Color COLOR_HOVER = new Color(21, 101, 192);
    private static final Color COLOR_BG = Color.WHITE;
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    private static final Color COLOR_TEXT_SECONDARY = new Color(97, 97, 97);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.PLAIN, 24);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 15);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton btnNext, btnPrev, btnCancel;
    private int currentStep = 1;

    // Data
    private String selectedLanguage = "Français";
    private boolean licenseAccepted = false;
    private boolean launchAtEnd = true;

    // Components Step 1
    private JComboBox<String> comboLang;

    // Components Step 2
    private JRadioButton radioAccept, radioRefuse;

    // Components Step 3
    // Just labels

    // Components Step 4
    private JProgressBar progressBar;
    private JLabel lblStatus;

    // Components Step 5
    private JCheckBox chkLaunch;

    public SetupFrame() {
        setTitle("Installation de Pharmacie Manager");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(false); // Garder la décoration OS standard pour "Chrome style" (souvent standard mais
                               // propre)

        // Appliquer un style global minimal
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        setContentPane(mainPanel);

        // --- LEFT SIDEBAR (Optionnel, ou Header comme Chrome - Chrome est souvent
        // simple) ---
        // On va faire un header simple clean et tout le contenu blanc.

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BG);
        headerPanel.setBorder(new EmptyBorder(20, 30, 0, 30));

        JLabel lblAppTitle = new JLabel("Pharmacie Manager");
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAppTitle.setForeground(COLOR_TEXT_SECONDARY);
        headerPanel.add(lblAppTitle, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- CONTENT ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        createStep1Language();
        createStep2License();
        createStep3Ready();
        createStep4Install(); // The heavy lifting
        createStep5Finish();

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        footerPanel.setBackground(new Color(240, 240, 240)); // Gris très clair en bas
        footerPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

        btnPrev = createStyledButton("Précédent", false);
        btnNext = createStyledButton("Suivant", true); // Primary action
        btnCancel = createStyledButton("Annuler", false);

        btnPrev.setEnabled(false); // First step

        btnPrev.addActionListener(e -> navigate(-1));
        btnNext.addActionListener(e -> navigate(1));
        btnCancel.addActionListener(e -> System.exit(0));

        footerPanel.add(btnPrev);
        footerPanel.add(btnNext);
        footerPanel.add(btnCancel);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Set Icon
        try {
            ImageIcon icon = new ImageIcon("resources/images/icon.ico");
            setIconImage(icon.getImage());
        } catch (Exception e) {
        }
    }

    private JButton createStyledButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (primary) {
            btn.setBackground(COLOR_PRIMARY);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

            // Hover effect simple
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(COLOR_HOVER);
                }

                public void mouseExited(MouseEvent e) {
                    btn.setBackground(COLOR_PRIMARY);
                }
            });
            // Pour enlever le border par defaut moche sur certains OS
            btn.setUI(new BasicButtonUI());
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            btn.setPreferredSize(new Dimension(100, 32));
        }
        return btn;
    }

    // --- STEPS ---

    private void createStep1Language() {
        JPanel panel = new JPanel(null); // Absolute layout for precise centering or Box
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BG);

        panel.add(Box.createVerticalGlue());

        JLabel lblTitle = new JLabel("Bienvenue");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(10));

        JLabel lblMsg = new JLabel("Veuillez sélectionner la langue qui sera utilisée par l'assistant d'installation.");
        lblMsg.setFont(FONT_BODY);
        lblMsg.setForeground(COLOR_TEXT);
        lblMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblMsg);

        panel.add(Box.createVerticalStrut(30));

        String[] langs = { "Français", "English" };
        comboLang = new JComboBox<>(langs);
        comboLang.setMaximumSize(new Dimension(200, 30));
        comboLang.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(comboLang);

        panel.add(Box.createVerticalGlue());

        contentPanel.add(panel, "1");
    }

    private void createStep2License() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(COLOR_BG);

        JLabel lblTitle = new JLabel("Accord de licence");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT);
        panel.add(lblTitle, BorderLayout.NORTH);

        JTextArea txtLicence = new JTextArea();
        txtLicence.setText("LICENCE D'UTILISATION - PHARMACIE MANAGER\n\n" +
                "1. OBJET\nCe logiciel est destiné à la gestion pédagogique d'une pharmacie.\n\n" +
                "2. DROITS\nL'utilisation est libre pour des fins académiques.\n\n" +
                "3. RESPONSABILITÉ\nL'auteur décline toute responsabilité en cas de perte de données.\n\n" +
                "VEUILLEZ LIRE ATTENTIVEMENT CES CONDITIONS AVANT D'INSTALLER.\n" +
                "(Scroll pour lire la suite...)\n\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        txtLicence.setEditable(false);
        txtLicence.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtLicence.setLineWrap(true);
        txtLicence.setWrapStyleWord(true);
        txtLicence.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(txtLicence);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel radioPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        radioPanel.setBackground(COLOR_BG);

        ButtonGroup group = new ButtonGroup();
        radioAccept = new JRadioButton("Je comprends et j'accepte les termes du contrat de licence");
        radioRefuse = new JRadioButton("Je refuse les termes du contrat de licence");

        radioAccept.setBackground(COLOR_BG);
        radioRefuse.setBackground(COLOR_BG);
        radioRefuse.setSelected(true);

        radioAccept.setFont(FONT_BODY);
        radioRefuse.setFont(FONT_BODY);

        group.add(radioAccept);
        group.add(radioRefuse);

        radioPanel.add(radioAccept);
        radioPanel.add(radioRefuse);

        radioAccept.addActionListener(e -> {
            licenseAccepted = true;
            updateButtons();
        });
        radioRefuse.addActionListener(e -> {
            licenseAccepted = false;
            updateButtons();
        });

        panel.add(radioPanel, BorderLayout.SOUTH);

        contentPanel.add(panel, "2");
    }

    private void createStep3Ready() {
        JPanel panel = new JPanel(new GridBagLayout()); // Center everything
        panel.setBackground(COLOR_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel lblTitle = new JLabel("Prêt à installer");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT);
        panel.add(lblTitle, gbc);

        gbc.gridy++;
        JTextArea txtInfo = new JTextArea(
                "L'assistant est prêt à installer Pharmacie Manager sur votre ordinateur.\n\n" +
                        "Cliquez sur \"Installer\" pour démarrer la copie des fichiers et la configuration.\n" +
                        "Une icône sera créée sur votre Bureau.");
        txtInfo.setEditable(false);
        txtInfo.setOpaque(false);
        txtInfo.setFont(FONT_SUBTITLE);
        txtInfo.setSize(new Dimension(400, 100));
        txtInfo.setLineWrap(true);
        txtInfo.setWrapStyleWord(true);
        // Hack to center text in JTextArea
        txtInfo.setAlignmentX(JTextArea.CENTER_ALIGNMENT);

        panel.add(txtInfo, gbc);

        contentPanel.add(panel, "3");
    }

    private void createStep4Install() {
        JPanel panel = new JPanel(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BG);

        panel.add(Box.createVerticalGlue());

        JLabel lblTitle = new JLabel("Installation en cours...");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(20));

        lblStatus = new JLabel("Préparation...");
        lblStatus.setFont(FONT_BODY);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblStatus);

        panel.add(Box.createVerticalStrut(10));

        progressBar = new JProgressBar(0, 100);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(2000, 25)); // Full width
        progressBar.setForeground(new Color(34, 139, 34)); // Green
        panel.add(progressBar);

        panel.add(Box.createVerticalGlue());

        contentPanel.add(panel, "4");
    }

    private void createStep5Finish() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitle = new JLabel("Installation terminée");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(new Color(56, 142, 60)); // Success Green
        panel.add(lblTitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 20, 0);
        JLabel lblMsg = new JLabel("Pharmacie Manager a été installé avec succès.");
        lblMsg.setFont(FONT_SUBTITLE);
        panel.add(lblMsg, gbc);

        gbc.gridy++;
        chkLaunch = new JCheckBox("Lancer l'application maintenant");
        chkLaunch.setSelected(true);
        chkLaunch.setBackground(COLOR_BG);
        chkLaunch.setFont(FONT_BODY);
        panel.add(chkLaunch, gbc);

        contentPanel.add(panel, "5");
    }

    // --- LOGIC ---

    private void updateButtons() {
        if (currentStep == 1) { // Language
            btnPrev.setEnabled(false);
            btnNext.setEnabled(true);
            btnNext.setText("Suivant");
        } else if (currentStep == 2) { // License
            btnPrev.setEnabled(true);
            btnNext.setEnabled(licenseAccepted);
            btnNext.setText("Suivant");
        } else if (currentStep == 3) { // Ready
            btnPrev.setEnabled(true);
            btnNext.setEnabled(true);
            btnNext.setText("Installer");
        } else if (currentStep == 4) { // Install (Progress)
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            btnCancel.setEnabled(false);
        } else if (currentStep == 5) { // Finish
            btnPrev.setEnabled(false);
            btnNext.setEnabled(true);
            btnNext.setText("Terminer");
            btnCancel.setEnabled(false);
        }
    }

    private void navigate(int dir) {
        if (dir == 1 && currentStep == 3) {
            // GO TO INSTALL
            currentStep++;
            cardLayout.show(contentPanel, String.valueOf(currentStep));
            updateButtons();

            // Run Worker
            new Thread(this::runInstallation).start();
        } else if (dir == 1 && currentStep == 5) {
            // FINISH
            dispose();
            if (chkLaunch.isSelected()) {
                // Launch MainFrame in the same VM (Bypassing LoginFrame)
                SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
            } else {
                System.exit(0);
            }
        } else {
            currentStep += dir;
            cardLayout.show(contentPanel, String.valueOf(currentStep));
            updateButtons();
        }
    }

    // --- INSTALLATION PROCESS ---
    private void runInstallation() {
        try {
            updateStatus(10, "Création du dossier d'installation...");

            // 1. Define Paths
            String userHome = System.getProperty("user.home");
            File desktopDir = new File(userHome, "Desktop");
            File appDir = new File(desktopDir, "PharmacieManager");

            if (!appDir.exists()) {
                appDir.mkdir();
            }

            Thread.sleep(500); // UI visual wait

            updateStatus(25, "Copie des librairies...");
            // 2. Copy Libs
            File libsSource = new File("libs");
            if (libsSource.exists()) {
                copyDirectory(libsSource, new File(appDir, "libs"));
            }

            updateStatus(40, "Copie des ressources...");
            // 3. Copy Resources
            File resSource = new File("resources");
            if (resSource.exists()) {
                copyDirectory(resSource, new File(appDir, "resources"));
            }

            updateStatus(55, "Copie des exécutables...");
            // 4. Copy JARs or Classes (Simulation: Assuming we are running from root
            // project or bin)
            // For a robust installer, usually we copy a simplified jar.
            // Here we will create a simple .bat launcher on the desktop.
            createShortcuts(appDir);

            updateStatus(70, "Configuration de la base de données...");
            // 5. Database Setup (Keep existing logic)
            setupDatabase();

            updateStatus(90, "Finalisation...");
            Thread.sleep(1000);

            updateStatus(100, "Terminé !");

            SwingUtilities.invokeLater(() -> {
                currentStep++;
                cardLayout.show(contentPanel, String.valueOf(currentStep));
                updateButtons();
            });

        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Erreur d'installation: " + e.getMessage(), "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            });
        }
    }

    private void setupDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/"; // Default local
        try (Connection conn = DriverManager.getConnection(url, "root", "")) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS db_pharmacie");
            stmt.execute("USE db_pharmacie");

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS T_Fournisseur (id INT AUTO_INCREMENT PRIMARY KEY, nom VARCHAR(100), ville VARCHAR(100), contact VARCHAR(100))");
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS T_Medicament (id INT AUTO_INCREMENT PRIMARY KEY, nom VARCHAR(100), famille VARCHAR(100), prix DOUBLE, stock INT, id_fournisseur INT)");
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS T_Utilisateur (id INT AUTO_INCREMENT PRIMARY KEY, login VARCHAR(50), password VARCHAR(100))");
            stmt.executeUpdate("INSERT IGNORE INTO T_Utilisateur (login, password) VALUES ('admin', 'admin')");
            stmt.executeUpdate("INSERT IGNORE INTO T_Utilisateur (login, password) VALUES ('ytr', 'ytr')");

            // Create db.properties in AppDir
            // Note: In a real installer we would write this to appDir. Currently writing to
            // CWD for immediate usage
            Properties props = new Properties();
            props.setProperty("db.url", "jdbc:mysql://localhost:3306/db_pharmacie");
            props.setProperty("db.user", "root");
            props.setProperty("db.password", "");
            try (FileOutputStream fos = new FileOutputStream("db.properties")) {
                props.store(fos, "Pharmacie Config");
            }
        }
    }

    private void createShortcuts(File appDir) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (!os.contains("win"))
                return; // Windows only

            String userHome = System.getProperty("user.home");

            // Critical: Copy BIN folder to appDir because we removed it from previous
            // method
            try {
                File binSrc = new File("bin");
                File binDest = new File(appDir, "bin");
                if (binSrc.exists()) {
                    copyDirectory(binSrc, binDest);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Copy custom icon from Downloads to app resources
            try {
                File iconSrc = new File(userHome, "Downloads\\image.ico");
                File iconDest = new File(appDir, "resources\\images\\icon.ico");
                if (iconSrc.exists()) {
                    iconDest.getParentFile().mkdirs();
                    Files.copy(iconSrc.toPath(), iconDest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 1. Target (The Lancer_Installation_Pro.vbs we will copy there or simple Bat?
            // Better: Create a clean BAT that runs hidden, OR point LNK to javaw directly.
            // Best for "Pro" feel: Point LNK to javaw command.

            // We need absolute paths
            String javaHome = System.getProperty("java.home");
            String javawPath = javaHome + File.separator + "bin" + File.separator + "javaw.exe";

            // Classpath needs to be absolute for the Shortcut to work reliably from
            // anywhere
            String libPath = new File(appDir, "libs").getAbsolutePath() + File.separator + "*";
            String binPath = new File(appDir, "bin").getAbsolutePath();
            String cp = "\"" + libPath + ";" + binPath + "\"";

            String mainClass = "com.pharmacie.view.MainFrame"; // Direct launch

            // Arguments for shortcut
            String args = "-cp " + cp + " " + mainClass;

            // Icon Path - Use custom icon if available, otherwise fallback to logo.png
            File customIcon = new File(appDir, "resources\\images\\icon.ico");
            String iconPath = customIcon.exists() ? customIcon.getAbsolutePath() 
                    : new File(appDir, "resources/images/logo.png").getAbsolutePath();

            // Define Shortcut Paths
            String startMenuDir = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs";
            String desktopDir = userHome + "\\Desktop";

            // Create Start Menu Shortcut
            createWindowsShortcut(startMenuDir + "\\Pharmacie Manager.lnk", javawPath, args, appDir.getAbsolutePath(),
                    iconPath);

            // Create Desktop Shortcut
            createWindowsShortcut(desktopDir + "\\Pharmacie Manager.lnk", javawPath, args, appDir.getAbsolutePath(),
                    iconPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createWindowsShortcut(String linkPath, String targetPath, String args, String workingDir,
            String iconPath) {
        try {
            File vbs = File.createTempFile("shortcut", ".vbs");
            vbs.deleteOnExit();
            try (PrintWriter writer = new PrintWriter(vbs)) {
                writer.println("Set oWS = WScript.CreateObject(\"WScript.Shell\")");
                writer.println("sLinkFile = \"" + linkPath + "\"");
                writer.println("Set oLink = oWS.CreateShortcut(sLinkFile)");
                writer.println("oLink.TargetPath = \"" + targetPath + "\"");
                writer.println("oLink.Arguments = \"" + args.replace("\"", "\"\"") + "\"");
                writer.println("oLink.WorkingDirectory = \"" + workingDir + "\"");
                writer.println("oLink.IconLocation = \"" + iconPath + "\"");
                writer.println("oLink.Save");
            }
            Runtime.getRuntime().exec("wscript " + vbs.getAbsolutePath()).waitFor();
            vbs.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyDirectory(File source, File target) throws IOException {
        if (!target.exists())
            target.mkdirs();

        Files.walkFileTree(source.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.toPath().resolve(source.toPath().relativize(dir));
                if (!Files.exists(targetDir))
                    Files.createDirectory(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.toPath().resolve(source.toPath().relativize(file)),
                        StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void updateStatus(int progress, String msg) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(progress);
            lblStatus.setText(msg);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SetupFrame().setVisible(true));
    }
}
