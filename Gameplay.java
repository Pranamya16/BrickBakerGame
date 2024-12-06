import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.Color;
//import java.awt.Graphics2D;

/**
 * Main
 */

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    boolean play = false;//game starts when its true
    int score = 0;
    int totalbricks = 21;
    Timer timer;
    int delay = 0;
    int playerX = 310;
    int ballposX = 120;
    int ballposY = 350;
    int ballXdir = -1;
    int ballYdir = -2;
    MapGenerator map;                                                       

    Gameplay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
        setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // draw map
        map.draw((Graphics2D) g);

        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(" " + score, 590, 30);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(692, 0, 3, 592);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        // won
        if (totalbricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You won....Scores:" + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press enter to restart", 230, 350);

        }

        if (ballposY >= 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game over....Scores:" + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press enter to restart", 230, 350);
            // repaint();

        }
        g.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {

                play = false;
                score = 0;
                totalbricks = 21;

                delay = 0;
                playerX = 310;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }

    }

    void moveRight() {
        play = true;
        playerX += 20;
    }

    void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickx = j * map.brickWidth + 80;
                        int bricky = i * map.brickHeigtht + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeigtht = map.brickHeigtht;

                        Rectangle rect = new Rectangle(brickx, bricky, brickWidth, brickHeigtht);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalbricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }

                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }
        }
        repaint();

    }

    public static void main(String[] args) {
        JFrame j = new JFrame();
        Gameplay gameplay = new Gameplay();
        j.setBounds(10, 10, 700, 600);
        j.setTitle("BreakoutBall");
        j.setResizable(false);
        j.setVisible(true);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.add(gameplay);
    }

}

class MapGenerator {
    int map[][];
    int brickWidth;
    int brickHeigtht;

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;// '1'will detect that this particular brick has not been hit by the ball yet
            }
        }
        brickWidth = 540 / col;
        brickHeigtht = 150 / row;

    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeigtht + 50, brickWidth, brickHeigtht);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeigtht + 50, brickWidth, brickHeigtht);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}


