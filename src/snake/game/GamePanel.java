package snake.game;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 2;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    static boolean gameOn = false;
    
    public GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    public void welcome(Graphics g){
        if(!running && !gameOn){
            gameOver(g);
        }
        else{
            g.setColor(Color.red);
            g.setFont(new Font("TimesRoman", Font.BOLD, 75));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("SNAKE", (SCREEN_WIDTH - metrics1.stringWidth("SNAKE")) / 2, SCREEN_HEIGHT / 2);
            g.setFont(new Font("TimesRoman", Font.ITALIC, 40));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Press space", (SCREEN_WIDTH - metrics2.stringWidth("Press space")) / 2, SCREEN_HEIGHT);
        }
    }
    
    public void pause(){
        gameOn = false;
        timer.stop();
    }
    
    public void resume(){
        gameOn = true;
        timer.start();
    }
    
    public void reset(){
        applesEaten = 0;
        bodyParts = 2;
        direction = 'R';
        running = false;
        gameOn = false;
        Arrays.fill(x, 0);
        Arrays.fill(y, 0);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(!gameOn){
            welcome(g);
        }
        else{
            draw(g);
        }
    }
    
    public void draw(Graphics g){
        if(running){
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("TimesRoman", Font.ITALIC, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    
    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    
    public void checkCollision(){
        // if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if(x[0] == x[i] && y[0] == y[i]){
                running = false;
            }
        }
        // if head touches left border
        if(x[0] < 0){
            running = false;
        }
        // if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // if head touches top border
        if(y[0] < 0){
            running = false;
        }
        // if head touches bottom border
        if(y[0] > SCREEN_WIDTH){
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }
    
    public void gameOver(Graphics g){
        // score card
        g.setColor(Color.red);
        g.setFont(new Font("TimesRoman", Font.ITALIC, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        // game over
        g.setFont(new Font("TimesRoman", Font.PLAIN, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        // press space
        g.setFont(new Font("TimesRoman", Font.ITALIC, 40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press space 2 times", (SCREEN_WIDTH - metrics3.stringWidth("Press space 2 times")) / 2, SCREEN_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        if(running && gameOn){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(gameOn){
                        pause();
                    }
                    else{
                        resume();
                    }
                    if(!gameOn && !running){
                        reset();
                        startGame();
                    }
                    break;
            }
        }
    }
    
}
