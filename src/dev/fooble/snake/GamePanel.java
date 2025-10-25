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
    private boolean started = false;
    private boolean paused = false;

    private Timer timer;
    private final Random random;
    private final UI ui;

    public GamePanel() {
        random = new Random();
        ui = new UI(SCREEN_WIDTH, SCREEN_HEIGHT);

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(127, 194, 155));
        this.addKeyListener(new SnakeKeyAdapter());
        this.setFocusable(true);
    }

    private void startGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        paused = false;

        for(int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        newApple();
        running = true;
        started = true;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void togglePause() {
        if(running && started) {
            paused = !paused;
            if(paused) {
                timer.stop();
            } else {
                timer.start();
            }
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    private void draw(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;

        if(running && started) {
            // Grid
            graphics.setColor(new Color(117, 189, 147));
            for(int row = 0; row < SCREEN_HEIGHT / TILE_SIZE; row++) {
                for(int col = 0; col < SCREEN_WIDTH / TILE_SIZE; col++) {
                    if((row + col) % 2 == 0) {
                        graphics.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }

            // Apple
            graphics.setColor(new Color(249, 57, 67));
            graphics.fillOval(appleX, appleY, TILE_SIZE, TILE_SIZE);
            graphics.setColor(new Color(255, 150, 150));
            graphics.fillOval(appleX + 5, appleY + 5, 8, 8);

            // Snake
            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) {
                    // Snake head
                    graphics.setColor(new Color(255, 187, 51));
                    graphics.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);

                    // Eyes based on direction
                    graphics.setColor(Color.BLACK);
                    int eyeSize = 4;
                    switch(direction) {
                        case 'R' -> {
                            graphics.fillOval(x[i] + 15, y[i] + 7, eyeSize, eyeSize);
                            graphics.fillOval(x[i] + 15, y[i] + 14, eyeSize, eyeSize);
                        }
                        case 'L' -> {
                            graphics.fillOval(x[i] + 6, y[i] + 7, eyeSize, eyeSize);
                            graphics.fillOval(x[i] + 6, y[i] + 14, eyeSize, eyeSize);
                        }
                        case 'U' -> {
                            graphics.fillOval(x[i] + 7, y[i] + 6, eyeSize, eyeSize);
                            graphics.fillOval(x[i] + 14, y[i] + 6, eyeSize, eyeSize);
                        }
                        case 'D' -> {
                            graphics.fillOval(x[i] + 7, y[i] + 15, eyeSize, eyeSize);
                            graphics.fillOval(x[i] + 14, y[i] + 15, eyeSize, eyeSize);
                        }
                    }
                } else {
                    // Snake body
                    graphics.setColor(new Color(255, 200, 87));
                    graphics.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);

                    // Border for body segments
                    graphics.setColor(new Color(255, 187, 51));
                    graphics.drawRect(x[i], y[i], TILE_SIZE - 1, TILE_SIZE - 1);
                }
            }

            // HUD
            ui.drawGameHUD(g2d, applesEaten);

            // Pause screen if paused
            if(paused) {
                ui.drawPauseScreen(g2d, applesEaten);
            }
        } else if(!started) {
            // Main menu
            ui.drawMainMenu(g2d);
        } else {
            // Game over screen
            ui.drawGameOver(g2d, applesEaten);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running && !paused) {
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    private class SnakeKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            // Start game from main menu
            if(!started && e.getKeyCode() == KeyEvent.VK_SPACE) {
                startGame();
                return;
            }

            // Restart game from game over screen
            if(!running && started && e.getKeyCode() == KeyEvent.VK_SPACE) {
                startGame();
                return;
            }

            // Toggle pause
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if(!started) {
                    System.exit(0);
                } else if(!running) {
                    System.exit(0);
                } else {
                    togglePause();
                }
                return;
            }

            if(!paused) {
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
}
