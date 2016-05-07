import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.geom.*;
import java.util.*;
import java.util.Random;
import javax.sound.sampled.*;

public class Rawr extends Applet
    implements Runnable, MouseListener, MouseMotionListener, KeyListener
{
    Thread thread;   
    Image doubleBuffer;
    Graphics bufferGraphics;
    AffineTransform currentTransform = new AffineTransform();
    Random random = new Random();
    
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final double TICKS_PER_SECOND = 30;
    
    
    double playerRotation;
    double playerSpeed = 5;
    
    double playerX, playerY;
    boolean playerMovingLeft, playerMovingRight, playerMovingForward, playerMovingBackward, playerShooting;    
    
    boolean mouseUpdating;
    double mouseX, mouseY;
    
    double[] bulletX = new double[1024], bulletY = new double[1024], bulletRotation = new double[1024], 
        bulletSpeed = new double[1024];
    double[] bulletLife = new double[1024];
    
    int screenX, screenY;
    
    int liveBullets;
    
    Weapon pistol = new Weapon(10, 30, 600, 12, .25);
    
    Weapon playerWeapon = new Weapon();
    boolean playerReloading;
    double playerAttackCounter, playerReloadCounter, playerMagazineCounter;
    
    public void init()
    {
        this.addKeyListener(this);            
        this.addMouseListener(this);           
        this.addMouseMotionListener(this);         
        this.resize(WIDTH, HEIGHT);        
        
        for(int i = 0; i < bulletLife.length; i++)
        {
            bulletX[i] = bulletY[i] = bulletLife[i] = 0;
        }
        
        playerWeapon = new Weapon(pistol);
       
        thread = new Thread(this);
        thread.start();
        
        doubleBuffer = createImage(this.getWidth(), this.getHeight());
        bufferGraphics = doubleBuffer.getGraphics();           
    }
    
    public void run()
    {
        while( true )
        {
            simulatePlayer();
            simulateBullets();
            
            if(playerShooting)
            shoot();
            
            liveBullets = 0;
            for(int i = 0; i < bulletLife.length; i++)
            if(bulletLife[i] > 0)
            liveBullets++;
            
            
            try
            {
                thread.sleep((long)(1000 / TICKS_PER_SECOND));
            }
            catch( Exception e ){}
            
            repaint();
            this.resize(WIDTH,HEIGHT);
        }
    }
    
    public void keyPressed( KeyEvent e )
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_W:
            playerMovingForward = true;
            playerMovingBackward = false;
            break;
            
            case KeyEvent.VK_A:
            playerMovingLeft = true;
            playerMovingRight = false;
            break;
            
            case KeyEvent.VK_S:
            playerMovingForward = false;
            playerMovingBackward= true;
            break;
            
            case KeyEvent.VK_D:
            playerMovingLeft = false;
            playerMovingRight = true;
            break;
        }
    }
    
    public void keyReleased( KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_W:
            playerMovingForward = false;
            break;
            
            case KeyEvent.VK_A:
            playerMovingLeft = false;
            break;
            
            case KeyEvent.VK_S:
            playerMovingBackward= false;
            break;
            
            case KeyEvent.VK_D:
            playerMovingRight = false;
            break;
        }
    } 
    public void keyTyped( KeyEvent e ){}
    
    public void mousePressed( MouseEvent e )
    {
        updateMouse(e);
        playerShooting = true;
        shoot();
    }
    public void mouseReleased( MouseEvent e )
    {
        playerShooting = false;
    }
    public void mouseEntered( MouseEvent e ){}
    public void mouseClicked( MouseEvent e ){}
    public void mouseExited( MouseEvent e ){}
    public void mouseMoved( MouseEvent e ){
        updateMouse(e);}
    public void mouseDragged( MouseEvent e )
    {
        updateMouse(e);
        shoot();
    }
        
    public void updateMouse(MouseEvent e)
    {
        mouseUpdating = true;
        mouseX = e.getX() + screenX;
        mouseY = e.getY() + screenY;  
    }
    
    public void shoot()
    {
        if(playerMagazineCounter > 0)
        {
            if(playerAttackCounter <= 0)
            {
                for(int i = 0; i < bulletLife.length; i++)
                {
                    if(bulletLife[i] <= 0)
                    {
                        bulletLife[i] = playerWeapon.getProjectileRange() / playerWeapon.getProjectileSpeed();
                        bulletX[i] = playerX;
                        bulletY[i] = playerY;
                        bulletSpeed[i] = playerWeapon.getProjectileSpeed();
                        bulletRotation[i] = playerRotation;
                        break;
                    }
                }
                
                java.awt.Toolkit.getDefaultToolkit().beep();
                
                
                
                playerAttackCounter = (1.0 / playerWeapon.getAttackSpeed() * TICKS_PER_SECOND);
                playerMagazineCounter--;
            }
        }
        
        else if(!playerReloading)
        {
            playerReloading = true;
            playerReloadCounter = (1.0 / playerWeapon.getReloadSpeed() * TICKS_PER_SECOND);            
        }
    }
    
    public void simulatePlayer()
    {
        if(mouseUpdating)
        {
            mouseUpdating = false;
            playerRotation = Math.atan2( (mouseY - playerY), (mouseX - playerX));   
        }        
        
        if(playerMovingForward)
        {
            playerX += playerSpeed * Math.cos(playerRotation);
            playerY += playerSpeed * Math.sin(playerRotation);            
        }
        
        if(playerMovingBackward)
        {
            playerX -= playerSpeed * Math.cos(playerRotation);
            playerY -= playerSpeed * Math.sin(playerRotation);      
        }
        
        if(playerMovingLeft)
        {
            playerX += playerSpeed*Math.cos(playerRotation - Math.toRadians(90));
            playerY += playerSpeed*Math.sin(playerRotation - Math.toRadians(90));
        }
        
        if(playerMovingRight)
        {
            playerX -= playerSpeed*Math.cos(playerRotation - Math.toRadians(90));
            playerY -= playerSpeed*Math.sin(playerRotation - Math.toRadians(90));
        }
        
        if(playerReloading)
        {
            playerReloadCounter--;
            if(playerReloadCounter <= 0)
            {
                playerMagazineCounter = playerWeapon.getMagazineSize();
                playerReloading = false;
            }
        }
        
        if(playerAttackCounter > 0)
        playerAttackCounter--;      
        
        
        screenX = (int)playerX - 800/2;
        screenY = (int)playerY - 600/2;
    }
    
    public void simulateBullets()
    {
        for(int i = 0; i < bulletLife.length; i++)
        {
            if(bulletLife[i] > 0)
            {
                bulletLife[i]--;
                bulletX[i]+= bulletSpeed[i] * Math.cos(bulletRotation[i]);
                bulletY[i]+= bulletSpeed[i] * Math.sin(bulletRotation[i]);                
            }
        }
    }
    
    public void paint( Graphics g )
    {  
        Graphics2D g2D = (Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform initialTransform = g2D.getTransform();    
        g2D.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        g2D.setColor(Color.lightGray);
        g2D.fillRect(0, 0, WIDTH, HEIGHT);
        
        drawPlayer(g2D);
        drawMouse(g2D);
        drawBullets(g2D);
        
        g2D.setColor(Color.white);
        g2D.drawString("BULLETS : "+playerMagazineCounter, 16, 32);
        g2D.drawString("LIVE BULLETS : "+liveBullets, 16, 48);
        if(playerReloadCounter > 0)
        g2D.drawString("RELOADING : "+playerReloadCounter, 16, 64);
    }   
    
    public void drawPlayer(Graphics2D g)
    {
        g.setColor(Color.darkGray);
        g.fillOval((int)playerX - 8 - screenX, (int)playerY - 8 - screenY, 16, 16);
        
        g.setColor(Color.black);
        g.drawString("Player located at "+(int)playerX + ", "+(int)playerY, 16, 16);
    }
    
    public void drawMouse(Graphics2D g)
    {        
        g.setColor(Color.red);
        g.drawOval((int)mouseX - 4 - screenX, (int)mouseY - 4 - screenY, 8, 8);
    }
    
    public void drawBullets(Graphics2D g)
    {
        g.setColor(Color.orange);
        for(int i = 0; i < bulletLife.length; i++)
        {
            if(bulletLife[i] > 0)
            {
                g.fillOval((int)bulletX[i] - 2 - screenX, (int)bulletY[i] - 2 - screenY, 4, 4);
            }
        }
    }
   
    public void update( Graphics g )
    {
        paint(bufferGraphics);
        g.drawImage(doubleBuffer, 0, 0, this);
    }
}

