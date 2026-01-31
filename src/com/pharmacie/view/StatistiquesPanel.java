package com.pharmacie.view;

import javax.swing.*;

import com.pharmacie.dao.VenteDAO;

import java.awt.*;
import java.util.Map;

public class StatistiquesPanel extends JPanel {
    private VenteDAO venteDAO;
    private Map<String, Double> stats;

    public StatistiquesPanel() {
        venteDAO = new VenteDAO();
        setLayout(new BorderLayout());
        JButton btnRafraichir = new JButton("Rafraîchir les données");
        btnRafraichir.addActionListener(e -> rafraichir());
        add(btnRafraichir, BorderLayout.NORTH);
        
        rafraichir();
    }

    public void rafraichir() {
        stats = venteDAO.getVentesParFamille();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stats == null || stats.isEmpty()) {
            g.drawString("Aucune vente enregistrée pour le moment.", 50, 50);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        
        // Afficher le titre du graphique
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Ventes par Famille (en €)", width / 2 - 100, 50);

        // Trouver la valeur maximale pour ajuster l'échelle du graphique
        int barWidth = 50;
        int x = padding;
        int maxVal = 0;
        for (Double v : stats.values()) {
            if (v > maxVal) maxVal = v.intValue();
        }
        
        if (maxVal == 0) maxVal = 1;

        int chartHeight = height - 2 * padding;
        
        // Dessiner chaque barre du graphique avec une couleur différente
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        int i = 0;
        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA};

        for (Map.Entry<String, Double> entry : stats.entrySet()) {
            String famille = entry.getKey();
            double valeur = entry.getValue();
            
            int barHeight = (int) ((valeur / maxVal) * chartHeight);
            
            g2.setColor(colors[i % colors.length]);
            g2.fillRect(x, height - padding - barHeight, barWidth, barHeight);
            
            g2.setColor(Color.BLACK);
            g2.drawRect(x, height - padding - barHeight, barWidth, barHeight);
            
            // Afficher le nom de la famille et le montant
            g2.drawString(famille, x, height - padding + 15);
            g2.drawString(String.format("%.1f€", valeur), x, height - padding - barHeight - 5);
            
            x += barWidth + 50;
            i++;
        }
    }
}
