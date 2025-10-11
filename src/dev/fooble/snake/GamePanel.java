package dev.fooble.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 600;
    private final int TILE_SIZE = 25;
    private final int GAME_TILES = (SCREEN_WIDTH * SCREEN_HEIGHT) / TILE_SIZE;
    private final int DELAY = 100;

    private final int[] x = new int[GAME_TILES];
    private final int[] y = new int[GAME_TILES];

    private int bodyParts = 6;

    private int applesEaten = 0;
    private int appleX;
    private int appleY;

    private char direction = 'R';

    private boolean running = false;

    private Timer timer;
    private final Random random;

    public GamePanel() {
        random = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(127, 194, 155));
        this.addKeyListener(new SnakeKeyAdapter());
        this.setFocusable(true);

        startGame();
    }

    private void startGame() {
        newApple();
        running = true;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    private void draw(Graphics graphics) {
        if(running) {
            // Grid
            graphics.setColor(new Color(90, 175, 125));
            for(int i = 0; i < SCREEN_HEIGHT / TILE_SIZE; i++) {
                graphics.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i * TILE_SIZE, SCREEN_WIDTH, i * TILE_SIZE);
            }

            // Aple
            graphics.setColor(new Color(249, 57, 67));
            graphics.fillOval(appleX, appleY, TILE_SIZE, TILE_SIZE);

            // Snake
            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) graphics.setColor(new Color(255, 187, 51));
                else graphics.setColor(new Color(255, 200, 87));

                graphics.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }
        }

        // Text
        final Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setColor(new Color(122, 40, 203));
        graphics2D.setFont(new Font("Calibri", Font.BOLD, 35));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final FontMetrics metrics = getFontMetrics(graphics2D.getFont());
        graphics2D.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, graphics2D.getFont().getSize());

        if(!running) {
            gameOver((Graphics2D) graphics);
        }
    }

    private void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / TILE_SIZE) * TILE_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    private void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction) {
            case 'U' -> y[0] = y[0] - TILE_SIZE;
            case 'D' -> y[0] = y[0] + TILE_SIZE;
            case 'L' -> x[0] = x[0] - TILE_SIZE;
            case 'R' -> x[0] = x[0] + TILE_SIZE;
        }
    }

    private void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    private void checkCollisions() {
        // Collision with body
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        // Collision with borders
        if(x[0] < 0) running = false;
        if(x[0] >= SCREEN_WIDTH) running = false;
        if(y[0] < 0) running = false;
        if(y[0] >= SCREEN_HEIGHT) running = false;

        // Stop timer if game over
        if(!running) timer.stop();
    }

    private void gameOver(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(122, 40, 203));
        graphics2D.setFont(new Font("Calibri", Font.BOLD, 75));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final FontMetrics metrics = getFontMetrics(graphics2D.getFont());
        graphics2D.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    private class SnakeKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                    if(direction != 'R') direction = 'L';
                }
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                    if(direction != 'L') direction = 'R';
                }
                case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    if(direction != 'D') direction = 'U';
                }
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    if(direction != 'U') direction = 'D';
                }
            }
        }
    }
}
