/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package license.key.internals;

/**
 *
 * @author saliya
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * License Key Demo — Java 8 Swing Application Demonstrates offline license key
 * generation and self-validation.
 */
public class LicenseKeyInternals extends JFrame {

    // ── UI Components ──────────────────────────────────────────────
    private final JTextField[] blockFields = new JTextField[5];
    private final JLabel statusLabel = new JLabel("Ready", SwingConstants.CENTER);
    private final JLabel statusIcon = new JLabel("🔑", SwingConstants.CENTER);
    private final JButton generateBtn = new JButton("Generate Key");
    private final JButton validateBtn = new JButton("Validate Key");

    // ── Colors ────────────────────────────────────────────────────
    private static final Color BG = new Color(0x1B2A3B);
    private static final Color PANEL_BG = new Color(0x243447);
    private static final Color FIELD_BG = new Color(0x0F1923);
    private static final Color ACCENT_BLUE = new Color(0x2E75B6);
    private static final Color ACCENT_ORG = new Color(0xC55A11);
    private static final Color TEXT_MAIN = new Color(0xE8EFF7);
    private static final Color TEXT_DIM = new Color(0x7A93AE);
    private static final Color SUCCESS = new Color(0x27AE60);
    private static final Color DANGER = new Color(0xE74C3C);
    private static final Color SEPARATOR = new Color(0x2E75B6);

    // ── Fonts ─────────────────────────────────────────────────────
    private static final Font MONO_LARGE = new Font("Courier New", Font.BOLD, 26);
    private static final Font MONO_SMALL = new Font("Courier New", Font.PLAIN, 13);
    private static final Font UI_BOLD = new Font("SansSerif", Font.BOLD, 13);
    private static final Font UI_PLAIN = new Font("SansSerif", Font.PLAIN, 12);

    // ── Algorithm ─────────────────────────────────────────────────
    private final LicenseKeyAlgorithm algorithm = new LicenseKeyAlgorithm();

    // ══════════════════════════════════════════════════════════════
    public LicenseKeyInternals() {
        setTitle("License Key Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(700, 460));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL_BG);
        p.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, SEPARATOR),
                new EmptyBorder(18, 24, 14, 24)
        ));

        JLabel title = new JLabel("🔐  License Key Demo");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(TEXT_MAIN);

        JLabel sub = new JLabel("Offline self-validating keys · No database required");
        sub.setFont(UI_PLAIN);
        sub.setForeground(TEXT_DIM);

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 3));
        text.setOpaque(false);
        text.add(title);
        text.add(sub);

        p.add(text, BorderLayout.WEST);
        return p;
    }

    // ── Center ────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(28, 32, 12, 32));

        // Label row
        JLabel lbl = new JLabel("LICENSE KEY");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(TEXT_DIM);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(8));

        // Key fields row
        p.add(buildKeyFieldsRow());
        p.add(Box.createVerticalStrut(6));

        // Helper text
        JLabel hint = new JLabel("↑  Fields are editable — modify any block then click Validate");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(TEXT_DIM);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(hint);

        p.add(Box.createVerticalStrut(28));
        p.add(buildStatusPanel());
        p.add(Box.createVerticalStrut(28));
        p.add(buildButtonRow());

        return p;
    }

    // ── Key Fields ────────────────────────────────────────────────
    private JPanel buildKeyFieldsRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < 5; i++) {
            JTextField tf = new JTextField(4);
            tf.setFont(MONO_LARGE);
            tf.setForeground(TEXT_MAIN);
            tf.setBackground(FIELD_BG);
            tf.setCaretColor(TEXT_MAIN);
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_BLUE, 1, true),
                    new EmptyBorder(10, 8, 10, 8)
            ));
            tf.setColumns(4);
            tf.setPreferredSize(new Dimension(100, 58));

            // Auto-uppercase and max 4 chars
            tf.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    JTextField src = (JTextField) e.getSource();
                    String text = src.getText();
                    char c = Character.toUpperCase(e.getKeyChar());
                    e.setKeyChar(c);
                    if (text.length() >= 4 && src.getSelectedText() == null) {
                        e.consume();
                    }
                    // Reset status on edit
                    setStatus("neutral", "🔑", "Edit fields then click Validate");
                }
            });

            blockFields[i] = tf;
            row.add(tf);

            // Dash separator
            if (i < 4) {
                JLabel dash = new JLabel(" – ");
                dash.setFont(new Font("SansSerif", Font.BOLD, 22));
                dash.setForeground(TEXT_DIM);
                row.add(dash);
            }
        }
        return row;
    }

    // ── Status Panel ──────────────────────────────────────────────
    private JPanel buildStatusPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(PANEL_BG);
        outer.setBorder(new CompoundBorder(
                new LineBorder(new Color(0x2E4A63), 1, true),
                new EmptyBorder(14, 20, 14, 20)
        ));
        outer.setAlignmentX(Component.LEFT_ALIGNMENT);
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        statusIcon.setFont(new Font("SansSerif", Font.PLAIN, 22));
        statusIcon.setForeground(TEXT_DIM);
        statusIcon.setPreferredSize(new Dimension(36, 36));

        statusLabel.setFont(UI_BOLD);
        statusLabel.setForeground(TEXT_DIM);
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

        outer.add(statusIcon, BorderLayout.WEST);
        outer.add(statusLabel, BorderLayout.CENTER);
        return outer;
    }

    // ── Button Row ────────────────────────────────────────────────
    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        styleButton(generateBtn, ACCENT_BLUE, Color.WHITE);
        styleButton(validateBtn, ACCENT_ORG, Color.WHITE);

        generateBtn.addActionListener(e -> onGenerate());
        validateBtn.addActionListener(e -> onValidate());

        row.add(generateBtn);
        row.add(validateBtn);

        // Clear button
        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(UI_BOLD);
        clearBtn.setForeground(TEXT_DIM);
        clearBtn.setBackground(PANEL_BG);
        clearBtn.setBorder(new CompoundBorder(
                new LineBorder(new Color(0x2E4A63), 1, true),
                new EmptyBorder(9, 18, 9, 18)
        ));
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearBtn.setFocusPainted(false);
        clearBtn.addActionListener(e -> onClear());
        clearBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                clearBtn.setForeground(TEXT_MAIN);
            }

            public void mouseExited(MouseEvent e) {
                clearBtn.setForeground(TEXT_DIM);
            }
        });
        row.add(clearBtn);
        return row;
    }

    // ── Footer ────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL_BG);
        p.setBorder(new CompoundBorder(
                new MatteBorder(2, 0, 0, 0, new Color(0x1A2D40)),
                new EmptyBorder(10, 24, 10, 24)
        ));

        JLabel left = new JLabel("Java 8  ·  Adler-style Hash  ·  CRC-32  ·  5-bit Encoding");
        left.setFont(MONO_SMALL);
        left.setForeground(new Color(0x3D5A73));

        JLabel right = new JLabel("license-key-internals");
        right.setFont(MONO_SMALL);
        right.setForeground(new Color(0x3D5A73));

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // ── Button Styling ────────────────────────────────────────────
    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(UI_BOLD);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setOpaque(true);

        Color hover = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
    }

    // ── Actions ───────────────────────────────────────────────────
    private void onGenerate() {
        String[] blocks = algorithm.generateLicenseKey();
        for (int i = 0; i < 5; i++) {
            blockFields[i].setText(blocks[i]);
            blockFields[i].setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_BLUE, 1, true),
                    new EmptyBorder(10, 8, 10, 8)
            ));
        }
        setStatus("success", "✅",
                "Key generated — " + String.join("-", blocks));
    }

    private void onValidate() {
        // Read the 5 fields
        String[] blocks = new String[5];
        for (int i = 0; i < 5; i++) {
            blocks[i] = blockFields[i].getText().trim().toUpperCase();
        }

        // Basic format check
        for (int i = 0; i < 5; i++) {
            if (blocks[i].length() != 4) {
                highlightInvalid(i);
                setStatus("error", "❌",
                        "Block " + (i + 1) + " must be exactly 4 characters");
                return;
            }
        }

        boolean valid = algorithm.validateKey(blocks);

        // Reset all field borders first
        for (int i = 0; i < 5; i++) {
            blockFields[i].setBorder(new CompoundBorder(
                    new LineBorder(valid ? SUCCESS : DANGER, 1, true),
                    new EmptyBorder(10, 8, 10, 8)
            ));
        }

        if (valid) {
            setStatus("success", "✅",
                    "Valid key — all checksums match");
        } else {
            setStatus("error", "❌",
                    "Invalid key — checksum mismatch detected");
        }
    }

    private void onClear() {
        for (JTextField tf : blockFields) {
            tf.setText("");
            tf.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_BLUE, 1, true),
                    new EmptyBorder(10, 8, 10, 8)
            ));
        }
        setStatus("neutral", "🔑", "Ready");
        blockFields[0].requestFocus();
    }

    private void highlightInvalid(int index) {
        for (int i = 0; i < 5; i++) {
            Color border = (i == index) ? DANGER : ACCENT_BLUE;
            blockFields[i].setBorder(new CompoundBorder(
                    new LineBorder(border, i == index ? 2 : 1, true),
                    new EmptyBorder(10, 8, 10, 8)
            ));
        }
    }

    private void setStatus(String type, String icon, String message) {
        statusIcon.setText(icon);
        statusLabel.setText(message);
        switch (type) {
            case "success":
                statusLabel.setForeground(SUCCESS);
                break;
            case "error":
                statusLabel.setForeground(DANGER);
                break;
            default:
                statusLabel.setForeground(TEXT_DIM);
                break;
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  ALGORITHM (self-contained)
    // ══════════════════════════════════════════════════════════════
    static class LicenseKeyAlgorithm {

        private final Random random = new Random();

        private final int[] crc32Table = {
            0x00000000, 0x77073096, 0xEE0E612C, 0x990951BA,
            0x076DC419, 0x706AF48F, 0xE963A535, 0x9E6495A3,
            0x0EDB8832, 0x79DCB8A4, 0xE0D5E91E, 0x97D2D988,
            0x09B64C2B, 0x7EB17CBD, 0xE7B82D07, 0x90BF1D91,
            0x1DB71064, 0x6AB020F2, 0xF3B97148, 0x84BE41DE,
            0x1ADAD47D, 0x6DDDE4EB, 0xF4D4B551, 0x83D385C7,
            0x136C9856, 0x646BA8C0, 0xFD62F97A, 0x8A65C9EC,
            0x14015C4F, 0x63066CD9, 0xFA0F3D63, 0x8D080DF5,
            0x3B6E20C8, 0x4C69105E, 0xD56041E4, 0xA2677172,
            0x3C03E4D1, 0x4B04D447, 0xD20D85FD, 0xA50AB56B,
            0x35B5A8FA, 0x42B2986C, 0xDBBBC9D6, 0xACBCF940,
            0x32D86CE3, 0x45DF5C75, 0xDCD60DCF, 0xABD13D59,
            0x26D930AC, 0x51DE003A, 0xC8D75180, 0xBFD06116,
            0x21B4F4B5, 0x56B3C423, 0xCFBA9599, 0xB8BDA50F,
            0x2802B89E, 0x5F058808, 0xC60CD9B2, 0xB10BE924,
            0x2F6F7C87, 0x58684C11, 0xC1611DAB, 0xB6662D3D,
            0x76DC4190, 0x01DB7106, 0x98D220BC, 0xEFD5102A,
            0x71B18589, 0x06B6B51F, 0x9FBFE4A5, 0xE8B8D433,
            0x7807C9A2, 0x0F00F934, 0x9609A88E, 0xE10E9818,
            0x7F6A0DBB, 0x086D3D2D, 0x91646C97, 0xE6635C01,
            0x6B6B51F4, 0x1C6C6162, 0x856530D8, 0xF262004E,
            0x6C0695ED, 0x1B01A57B, 0x8208F4C1, 0xF50FC457,
            0x65B0D9C6, 0x12B7E950, 0x8BBEB8EA, 0xFCB9887C,
            0x62DD1DDF, 0x15DA2D49, 0x8CD37CF3, 0xFBD44C65,
            0x4DB26158, 0x3AB551CE, 0xA3BC0074, 0xD4BB30E2,
            0x4ADFA541, 0x3DD895D7, 0xA4D1C46D, 0xD3D6F4FB,
            0x4369E96A, 0x346ED9FC, 0xAD678846, 0xDA60B8D0,
            0x44042D73, 0x33031DE5, 0xAA0A4C5F, 0xDD0D7CC9,
            0x5005713C, 0x270241AA, 0xBE0B1010, 0xC90C2086,
            0x5768B525, 0x206F85B3, 0xB966D409, 0xCE61E49F,
            0x5EDEF90E, 0x29D9C998, 0xB0D09822, 0xC7D7A8B4,
            0x59B33D17, 0x2EB40D81, 0xB7BD5C3B, 0xC0BA6CAD,
            0xEDB88320, 0x9ABFB3B6, 0x03B6E20C, 0x74B1D29A,
            0xEAD54739, 0x9DD277AF, 0x04DB2615, 0x73DC1683,
            0xE3630B12, 0x94643B84, 0x0D6D6A3E, 0x7A6A5AA8,
            0xE40ECF0B, 0x9309FF9D, 0x0A00AE27, 0x7D079EB1,
            0xF00F9344, 0x8708A3D2, 0x1E01F268, 0x6906C2FE,
            0xF762575D, 0x806567CB, 0x196C3671, 0x6E6B06E7,
            0xFED41B76, 0x89D32BE0, 0x10DA7A5A, 0x67DD4ACC,
            0xF9B9DF6F, 0x8EBEEFF9, 0x17B7BE43, 0x60B08ED5,
            0xD6D6A3E8, 0xA1D1937E, 0x38D8C2C4, 0x4FDFF252,
            0xD1BB67F1, 0xA6BC5767, 0x3FB506DD, 0x48B2364B,
            0xD80D2BDA, 0xAF0A1B4C, 0x36034AF6, 0x41047A60,
            0xDF60EFC3, 0xA867DF55, 0x316E8EEF, 0x4669BE79,
            0xCB61B38C, 0xBC66831A, 0x256FD2A0, 0x5268E236,
            0xCC0C7795, 0xBB0B4703, 0x220216B9, 0x5505262F,
            0xC5BA3BBE, 0xB2BD0B28, 0x2BB45A92, 0x5CB36A04,
            0xC2D7FFA7, 0xB5D0CF31, 0x2CD99E8B, 0x5BDEAE1D,
            0x9B64C2B0, 0xEC63F226, 0x756AA39C, 0x026D930A,
            0x9C0906A9, 0xEB0E363F, 0x72076785, 0x05005713,
            0x95BF4A82, 0xE2B87A14, 0x7BB12BAE, 0x0CB61B38,
            0x92D28E9B, 0xE5D5BE0D, 0x7CDCEFB7, 0x0BDBDF21,
            0x86D3D2D4, 0xF1D4E242, 0x68DDB3F8, 0x1FDA836E,
            0x81BE16CD, 0xF6B9265B, 0x6FB077E1, 0x18B74777,
            0x88085AE6, 0xFF0F6A70, 0x66063BCA, 0x11010B5C,
            0x8F659EFF, 0xF862AE69, 0x616BFFD3, 0x166CCF45,
            0xA00AE278, 0xD70DD2EE, 0x4E048354, 0x3903B3C2,
            0xA7672661, 0xD06016F7, 0x4969474D, 0x3E6E77DB,
            0xAED16A4A, 0xD9D65ADC, 0x40DF0B66, 0x37D83BF0,
            0xA9BCAE53, 0xDEBB9EC5, 0x47B2CF7F, 0x30B5FFE9,
            0xBDBDF21C, 0xCABAC28A, 0x53B39330, 0x24B4A3A6,
            0xBAD03605, 0xCDD70693, 0x54DE5729, 0x23D967BF,
            0xB3667A2E, 0xC4614AB8, 0x5D681B02, 0x2A6F2B94,
            0xB40BBE37, 0xC30C8EA1, 0x5A05DF1B, 0x2D02EF8D
        };

        // ── Generate ──────────────────────────────────────────────
        public String[] generateLicenseKey() {
            String baseChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 20; i++) {
                sb.append(baseChars.charAt(random.nextInt(baseChars.length())));
            }

            char[] serial = sb.toString().toCharArray();
            serial = mixCharacters(serial);

            int calc = initialHash(serial) ^ 0x1D8DF;
            System.arraycopy(encodeToSevenChars(calc).toCharArray(), 0, serial, 13, 7);

            calc = calculateCRC(serial);
            System.arraycopy(encodeToSevenCharsV2(calc).toCharArray(), 0, serial, 13, 7);

            serial = mixCharacters(serial);

            String[] blocks = new String[5];
            for (int i = 0; i < 5; i++) {
                blocks[i] = new String(serial, i * 4, 4);
            }
            return blocks;
        }

        // ── Validate ──────────────────────────────────────────────
        public boolean validateKey(String[] blocks) {
            // Reassemble the 20-char array
            StringBuilder sb = new StringBuilder();
            for (String b : blocks) {
                sb.append(b);
            }
            if (sb.length() != 20) {
                return false;
            }

            char[] serial = sb.toString().toCharArray();

            // Undo final permutation (mixCharacters is self-inverse over 2 calls)
            serial = mixCharacters(serial);

            // Save the embedded verification region (positions 13–19)
            char[] embedded = new char[7];
            System.arraycopy(serial, 13, embedded, 0, 7);

            // Recompute pass 1
            int calc = initialHash(serial) ^ 0x1D8DF;
            System.arraycopy(encodeToSevenChars(calc).toCharArray(), 0, serial, 13, 7);

            // Recompute CRC
            calc = calculateCRC(serial);

            // Recompute pass 2 expected string
            String expected = encodeToSevenCharsV2(calc);

            // Compare expected vs embedded
            return expected.equals(new String(embedded));
        }

        // ── Helpers ───────────────────────────────────────────────
        private char[] mixCharacters(char[] s) {
            char t;
            t = s[2];
            s[2] = s[13];
            s[13] = t;
            t = s[4];
            s[4] = s[14];
            s[14] = t;
            t = s[5];
            s[5] = s[15];
            s[15] = t;
            t = s[7];
            s[7] = s[16];
            s[16] = t;
            t = s[12];
            s[12] = s[17];
            s[17] = t;
            t = s[1];
            s[1] = s[18];
            s[18] = t;
            t = s[3];
            s[3] = s[19];
            s[19] = t;
            return s;
        }

        private int initialHash(char[] s) {
            int acc = 0, prev = 1, cur = 0;
            for (int i = 0; i < 13; i++) {
                cur = s[i] + prev;
                prev = cur % 0xfff1;
                acc = (prev + acc) % 0xfff1;
            }
            return (acc << 16) + cur;
        }

        private String encodeToSevenChars(int value) {
            String alpha = "64382957JKLMNPQRSTUVWXYZABCDEFGH";
            int[] idx = new int[7];
            idx[6] = value & 0x1f;
            idx[5] = (value >> 5) & 0x1f;
            idx[4] = (value >> 10) & 0x1f;
            idx[3] = (value >> 15) & 0x1f;
            idx[2] = (value >> 20) & 0x1f;
            idx[1] = (value >> 25) & 0x1f;
            idx[0] = (idx[6] & 0x07) << 2;
            StringBuilder r = new StringBuilder();
            for (int i : idx) {
                r.append(alpha.charAt(i));
            }
            return r.toString();
        }

        private String encodeToSevenCharsV2(int value) {
            String alpha = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ64382957JKLMNPQRSTU";
            int[] idx = new int[7];
            idx[6] = ((value % 0x100) & 0x03) << 3;
            value >>= 2;
            idx[5] = value & 0x1f;
            idx[4] = (value >> 5) & 0x1f;
            idx[3] = (value >> 10) & 0x1f;
            idx[2] = (value >> 15) & 0x1f;
            idx[1] = (value >> 20) & 0x1f;
            idx[0] = (value >> 25) & 0x1f;
            idx[6] = idx[6] | (idx[0] & 0x07);
            StringBuilder r = new StringBuilder();
            for (int i : idx) {
                r.append(alpha.charAt(i));
            }
            return r.toString();
        }

        private int calculateCRC(char[] s) {
            int crc = 0x1d8df;
            for (int g = 0; g < 4; g++) {
                int base = 5 * g;
                for (int i = 0; i < 5; i++) {
                    int idx = (s[base + i] ^ crc) & 0xff;
                    int tv = crc32Table[idx];
                    crc = (crc >> 8) ^ tv;
                }
            }
            return crc;
        }
    }

    // ── Entry Point ───────────────────────────────────────────────
    public static void main(String[] args) {
        // Use system look and feel as base, then override
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Global UI defaults
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        UIManager.put("TextField.selectionBackground", new Color(0x2E75B6));
        UIManager.put("TextField.selectionForeground", Color.WHITE);

        SwingUtilities.invokeLater(LicenseKeyInternals::new);
    }
}
