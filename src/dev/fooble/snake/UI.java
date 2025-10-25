package dev.fooble.snake;

import javax.swing.*;
import java.awt.*;

public class UI {

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    // Colors
    private final Color PRIMARY_COLOR = new Color(122, 40, 203);
    private final Color SECONDARY_COLOR = new Color(255, 187, 51);
    private final Color ACCENT_COLOR = new Color(249, 57, 67);
    private final Color BACKGROUND_OVERLAY = new Color(0, 0, 0, 180);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color SHADOW_COLOR = new Color(0, 0, 0, 100);

    // Fonts
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 80);
    private final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 40);
    private final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 24);
    private final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 18);
    private final Font SCORE_FONT = new Font("Arial", Font.BOLD, 28);

    public UI(int screenWidth, int screenHeight) {
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;
    }

    public void drawMainMenu(Graphics2D g2d) {
        enableAntiAliasing(g2d);

        // Overlay
        g2d.setColor(BACKGROUND_OVERLAY);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Title with shadow
        g2d.setFont(TITLE_FONT);
        final String title = "SNAKE";
        drawTextWithShadow(g2d, title, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 3, PRIMARY_COLOR, 4);

        // Subtitle
        g2d.setFont(NORMAL_FONT);
        final String subtitle = "Press SPACE to Start";
        drawTextWithShadow(g2d, subtitle, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SECONDARY_COLOR, 2);

        // Controls
        g2d.setFont(SMALL_FONT);
        g2d.setColor(TEXT_COLOR);
        final String[] controls = {
                "Controls:",
                "Arrow Keys or WASD to move",
                "ESC to pause"
        };

        final int startY = SCREEN_HEIGHT * 2 / 3;
        for(int i = 0; i < controls.length; i++) {
            drawCenteredText(g2d, controls[i], SCREEN_WIDTH / 2, startY + (i * 30));
        }

        drawDecorations(g2d);
    }

    public void drawGameHUD(Graphics2D g2d, int score) {
        enableAntiAliasing(g2d);

        drawScorePanel(g2d, score);
        drawControlsHint(g2d);
    }

    public void drawGameOver(Graphics2D g2d, int finalScore) {
        enableAntiAliasing(g2d);

        // Overlay
        g2d.setColor(BACKGROUND_OVERLAY);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Title
        g2d.setFont(SUBTITLE_FONT);
        final String gameOverText = "GAME OVER";
        drawTextWithShadow(g2d, gameOverText, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 3, ACCENT_COLOR, 3);

        // Score panel
        final int panelY = SCREEN_HEIGHT / 2 - 60;
        drawEndGameScorePanel(g2d, finalScore, panelY);

        // Restart prompt
        g2d.setFont(NORMAL_FONT);
        final String restartText = "Press SPACE to Restart";
        drawTextWithShadow(g2d, restartText, SCREEN_WIDTH / 2, SCREEN_HEIGHT * 3 / 4, SECONDARY_COLOR, 2);

        // Quit prompt
        g2d.setFont(SMALL_FONT);
        g2d.setColor(TEXT_COLOR);
        final String quitText = "Press ESC to Exit";
        drawCenteredText(g2d, quitText, SCREEN_WIDTH / 2, SCREEN_HEIGHT * 3 / 4 + 50);
    }

    public void drawPauseScreen(Graphics2D g2d, int currentScore) {
        enableAntiAliasing(g2d);

        // Overlay
        g2d.setColor(BACKGROUND_OVERLAY);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Title
        g2d.setFont(SUBTITLE_FONT);
        final String pausedText = "PAUSED";
        drawTextWithShadow(g2d, pausedText, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 50, PRIMARY_COLOR, 3);

        // Current score
        g2d.setFont(NORMAL_FONT);
        final String scoreText = "Score: " + currentScore;
        drawTextWithShadow(g2d, scoreText, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 20, TEXT_COLOR, 2);

        // Resume prompt
        g2d.setFont(SMALL_FONT);
        g2d.setColor(TEXT_COLOR);
        final String resumeText = "Press ESC to Resume";
        drawCenteredText(g2d, resumeText, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 70);
    }

    private void drawScorePanel(Graphics2D g2d, int score) {
        final int panelWidth = 200;
        final int panelHeight = 60;
        final int y = 20;
        final int x = SCREEN_WIDTH - panelWidth - y;

        // Panel background
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(x, y, panelWidth, panelHeight, 15, 15);

        // Border
        g2d.setColor(PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(x, y, panelWidth, panelHeight, 15, 15);

        // Score
        g2d.setFont(SCORE_FONT);
        g2d.setColor(SECONDARY_COLOR);
        final String scoreText = "Score: " + score;
        g2d.drawString(scoreText, x + 15, y + 40);
    }

    private void drawEndGameScorePanel(Graphics2D g2d, int finalScore, int y) {
        final int panelWidth = 300;
        final int panelHeight = 100;
        final int x = (SCREEN_WIDTH - panelWidth) / 2;

        // Panel background
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(x, y, panelWidth, panelHeight, 20, 20);

        // Border
        g2d.setColor(PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRoundRect(x, y, panelWidth, panelHeight, 20, 20);

        // Final score label
        g2d.setFont(SCORE_FONT);
        g2d.setColor(SECONDARY_COLOR);
        final String scoreLabel = "Final Score";
        drawCenteredText(g2d, scoreLabel, SCREEN_WIDTH / 2, y + 40);

        // Final score value
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        g2d.setColor(TEXT_COLOR);
        final String scoreValue = String.valueOf(finalScore);
        drawCenteredText(g2d, scoreValue, SCREEN_WIDTH / 2, y + 85);
    }

    private void drawControlsHint(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(new Color(255, 255, 255, 150));
        final String hint = "ESC: Pause";
        g2d.drawString(hint, 20, SCREEN_HEIGHT - 20);
    }

    private void drawDecorations(Graphics2D g2d) {
        // Decorative circles
        g2d.setColor(new Color(122, 40, 203, 50));
        g2d.fillOval(-50, -50, 150, 150);
        g2d.fillOval(SCREEN_WIDTH - 100, SCREEN_HEIGHT - 100, 150, 150);

        g2d.setColor(new Color(255, 187, 51, 30));
        g2d.fillOval(SCREEN_WIDTH - 80, -30, 120, 120);
        g2d.fillOval(-30, SCREEN_HEIGHT - 80, 120, 120);
    }

    private void drawTextWithShadow(Graphics2D g2d, String text, int centerX, int y, Color color, int shadowOffset) {
        final FontMetrics metrics = g2d.getFontMetrics();
        final int x = centerX - metrics.stringWidth(text) / 2;

        // Shadow
        g2d.setColor(SHADOW_COLOR);
        g2d.drawString(text, x + shadowOffset, y + shadowOffset);

        // Main text
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }

    private void drawCenteredText(Graphics2D g2d, String text, int centerX, int y) {
        final FontMetrics metrics = g2d.getFontMetrics();
        final int x = centerX - metrics.stringWidth(text) / 2;
        g2d.drawString(text, x, y);
    }

    private void enableAntiAliasing(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}