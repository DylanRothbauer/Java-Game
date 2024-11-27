package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Entity.Player;
import Tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    // Screen Settings
    final int originalTileSize = 16; // 16x16 title
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxScreenCol;
    public final int worldHeight = tileSize * maxScreenRow;

    //FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this,keyH);

public GamePanel() {
    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    this.setBackground(Color.black);
    this.setDoubleBuffered(true);
    this.addKeyListener(keyH);
    this.setFocusable(true);
}

public void startGameThread() {
    gameThread = new Thread(this);
    gameThread.start();
}

@Override
public void run() {

    // There are two methods to do this,
    // Sleep Method OR Delta/Accumulator Method

    // Delta/Accumulator Method
    double drawInterval = 1000000000/FPS; // nanoseconds/60 fps = 0.016666 seconds
    //double nextDrawTime = System.nanoTime() + drawInterval;
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;
    long timer = 0;
    int drawCount = 0;

    while(gameThread != null) {
        //System.out.println("The game loop is running");
        currentTime = System.nanoTime();
        delta += (currentTime - lastTime) / drawInterval;
        timer+= (currentTime - lastTime);
        lastTime = currentTime;

        if (delta >= 1) {
            // 1) Update: Update information such as character positions
            update();
            // 2) Draw: Draw the screen with the updated information
            repaint();
            delta--;
            drawCount++;
        }

        if (timer >= 1000000000) {
            System.out.println("FPS: " + drawCount);
            drawCount = 0;
            timer = 0;
        }

        // Sleep Method 

        // try {
        //     double remainingTime = nextDrawTime - System.nanoTime();
        //     remainingTime = remainingTime / 1000000; // Divide by 1 million because sleep() takes a millis

        //     if (remainingTime < 0) {
        //         remainingTime = 0;
        //     }

        //     Thread.sleep((long)remainingTime);
        //     nextDrawTime += drawInterval;

        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
    }
}

public void update() {
    player.update();
}

public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    tileM.draw(g2);
    player.draw(g2);
    g2.dispose();
}

}