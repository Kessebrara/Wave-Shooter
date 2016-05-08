import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.stage.WindowEvent;
import java.util.Random;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.scene.input.MouseEvent;


public class Test extends Application
{
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    String test = "O.O";
    
    double distance;
    double updates;
    double fps;
    
    double avg;
    double total;
    
    double playerX = WIDTH/2;
    double playerY = HEIGHT/2;
    double playerSpeed = 5;
    double playerWeaponCounter;
    boolean playerMovingLeft, playerMovingRight, playerMovingUp, playerMovingDown;
    double playerMagazineCounter = 30;
    boolean playerReloading;
    double playerReloadingCounter;
    
    double stalkerX, stalkerY = 64;
    double stalkerCounter;
    boolean stalkerMovingLeft;
    AudioClip[] stalkersfx = new AudioClip[4];
    
    double[] bulletLife = new double[1024], bulletRotation = new double[1024], 
        bulletX = new double[1024], bulletY = new double[1024];
    
        
        
    Random random = new Random();
    
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    
    AudioClip[] aks = new AudioClip[5];
    AudioClip[] aksin = new AudioClip[5];
    URL resource = getClass().getResource("sounds/ak47_reload.wav");
    AudioClip akreload = new AudioClip(resource.toString());
    
    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) 
    {/**
        for(int i = 0; i < bulletLife.length; i++)
        {
            bulletX[i] = bulletY[i] = bulletLife[i] = bulletRotation[i] = 0;
        }**/
        
        stage.setTitle( "Collect the Money Bags!" );
        
        for(int i = 0; i < 5; i++)
        {
            URL resource = getClass().getResource("sounds/ak47_semi_"+i+".wav");
            aks[i] = new AudioClip(resource.toString());
            resource = getClass().getResource("sounds/ak47_semi_indoor_"+i+".wav");
            aksin[i] = new AudioClip(resource.toString());
        }
        
        for(int i = 0; i < 4; i++)
        {
            URL resource = getClass().getResource("sounds/stalker_idle_"+i+".wav");
            stalkersfx[i] = new AudioClip(resource.toString());
        }

        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        root.getChildren().add(canvas);

        ArrayList<String> input = new ArrayList<String>();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent e)
            {
                String key = e.getCode().toString();
                if (!input.contains(key))
                input.add(key);
                
                if(key == "W")
                {
                    playerMovingDown = false;
                    playerMovingUp = true;
                }
                if(key == "A")
                {
                    playerMovingLeft = true;
                    playerMovingRight = false;
                }
                if(key == "S")
                {
                    playerMovingDown = true;
                    playerMovingUp = false;
                }
                if(key == "D")
                {
                    playerMovingLeft = false;
                    playerMovingRight = true;
                }
                
            }
        });
        
        scene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent e)
            {
                String key = e.getCode().toString();
                input.remove(key);
                
                if(key == "W")
                playerMovingUp = false;
                if(key == "A")
                playerMovingLeft = false;
                if(key == "S")
                playerMovingDown = false;
                if(key == "D")
                playerMovingRight = false;
            }
        });
        
        scene.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e)
            {
                double mouseX = e.getX();
                double mouseY = e.getY();
                double rotation = Math.atan2(mouseY - playerY, mouseX - playerX);
                if(playerMagazineCounter > 0)
                {
                    if(playerWeaponCounter <= 0)
                    {
                        int r = random.nextInt(4);
                        
                        aks[r].setBalance( (playerX - WIDTH/2) / (WIDTH/2));
                        aksin[r].setBalance( (playerX / - WIDTH/2) / (WIDTH/2));
                        
                        playerWeaponCounter = .1;
                        if(playerX >= 256 && playerY >= 256 && 
                            playerX <= 352 && playerY <= 352)
                            aksin[r].play(1.0);
                        
                        else
                        aks[r].play(1.0);                        
                        
                        playerMagazineCounter--;
                        for(int i = 0; i < bulletLife.length; i++)
                        {
                            if(bulletLife[i] <= 0)
                            {
                                bulletLife[i] = 4;
                                bulletX[i] = playerX;
                                bulletY[i] = playerY;
                                bulletRotation[i] = rotation;
                                break;                            
                            }
                        }
                    }
                }
                else
                {
                    if(playerWeaponCounter <= 0 && !playerReloading)
                    {
                        playerReloading = true;
                        playerReloadingCounter = 3;
                        akreload.play(1.0);
                    }
                }
            }
        });
        
        
        
        scene.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e)
            {
                double mouseX = e.getX();
                double mouseY = e.getY();
                double rotation = Math.atan2(mouseY - playerY, mouseX - playerX);
                if(playerMagazineCounter > 0)
                {
                    if(playerWeaponCounter <= 0)
                    {
                        int r = random.nextInt(4);
                        
                        aks[r].setBalance( (playerX - WIDTH/2) / (WIDTH/2));
                        aksin[r].setBalance( (playerX / - WIDTH/2) / (WIDTH/2));
                        
                        playerWeaponCounter = .1;
                        if(playerX >= 256 && playerY >= 256 && 
                            playerX <= 352 && playerY <= 352)
                            aksin[r].play(1.0);
                        
                        else
                        aks[r].play(1.0);                        
                        
                        playerMagazineCounter--;
                        for(int i = 0; i < bulletLife.length; i++)
                        {
                            if(bulletLife[i] <= 0)
                            {
                                bulletLife[i] = 4;
                                bulletX[i] = playerX;
                                bulletY[i] = playerY;
                                bulletRotation[i] = rotation;
                                break;                            
                            }
                        }
                    }
                }
                else
                {
                    if(playerWeaponCounter <= 0 && !playerReloading)
                    {
                        playerReloading = true;
                        playerReloadingCounter = 3;
                        akreload.play(1.0);
                    }
                }
            }
        });
        
        
        
        GraphicsContext g = canvas.getGraphicsContext2D();

        Font font = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        g.setFont( font );
        g.setFill( Color.GREEN );
        g.setStroke( Color.BLACK );
        g.setLineWidth(1);
        g.fillText("Test = ", 16, 16);
        
        
        LongValue lastNanoTime = new LongValue(System.nanoTime());
        

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // calculate time since last update.
                double ET = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                total+= ET;
                updates++;
                fps = updates/total;

                // handle input
                
                
                // game logic
                
                if(playerMovingUp)
                {
                    playerY-= 128 * ET;
                    distance+= 128 * ET;
                }
                
                if(playerMovingDown)
                {
                    playerY+= 128 * ET;
                    distance+= 128 * ET;
                }
                
                if(playerMovingLeft)
                {
                    playerX-= 128 * ET;
                    distance+= 128 * ET;
                }
                
                if(playerMovingRight)
                {
                    playerX+= 128 * ET;
                    distance+= 128 * ET;
                }
                
                if(playerReloading)
                {
                    playerReloadingCounter-= 1 * ET;
                    if(playerReloadingCounter <= 0)
                    {
                        playerReloading = false;
                        playerMagazineCounter = 30;
                    }
                }
                
                avg = distance/total;
                
                playerWeaponCounter-= 1 * ET;
                
                for(int i = 0; i < bulletLife.length; i++)
                {
                    if(bulletLife[i] > 0)
                    {
                        bulletLife[i] -= 1 * ET;
                        bulletX[i]+= 512 * ET * Math.cos(bulletRotation[i]);
                        bulletY[i]+= 512 * ET * Math.sin(bulletRotation[i]);
                    }
                }
                
                
                
                
                
                // collision detection                
                
                if(stalkerMovingLeft)
                {
                    stalkerX-= 256 * ET;
                    if(stalkerX<= 0)
                    stalkerMovingLeft = false;
                }
                
                else
                {
                    stalkerX+= 256 * ET;
                    
                    if(stalkerX>= WIDTH)
                    stalkerMovingLeft = true;
                }
                
                stalkerCounter-= 1 * ET;
                
                if(stalkerCounter <= 0)
                {
                    int r = random.nextInt(4);
                    stalkerCounter = 1 + random.nextInt(5);
                    stalkersfx[r].setBalance( (stalkerX-WIDTH/2) / (WIDTH/2));
                    stalkersfx[r].play();
                }
                // render 
                
                g.setFill(Color.LIGHTGREY);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                
                g.setFill(Color.DARKGREY);
                for(int r = 0; r < HEIGHT / 32; r++)
                for(int c = r%2; c < WIDTH / 32; c+=2)
                g.fillRect(c * 32, r * 32, 32, 32);
                
                g.setFill(Color.BROWN);
                g.fillRect(8 * 32, 8 *32, 32, 32);
                g.fillRect(9 * 32, 8 *32, 32, 32);
                g.fillRect(10 * 32, 8 *32, 32, 32);
                g.fillRect(8 * 32, 9 *32, 32, 32);
                g.fillRect(10 * 32, 9 *32, 32, 32);
                g.fillRect(8 * 32, 10 *32, 32, 32);
                g.fillRect(9 * 32, 10 *32, 32, 32);
                g.fillRect(10 * 32, 10 *32, 32, 32);
                
                g.setStroke(Color.YELLOW);
                g.setLineWidth(3);
                for(int i = 0; i < 4; i++)
                if(stalkersfx[i].isPlaying())
                {
                    int radius = 32 + random.nextInt(32);
                    g.strokeOval(stalkerX - radius, stalkerY - radius, radius * 2, radius * 2);
                }
                
                g.setFill(Color.BLUE);
                g.fillOval(playerX-8, playerY-8, 16, 16);
                
                
                g.setStroke(Color.ORANGE);
                g.setLineWidth(1);
                
                for(int i = 0; i < 5; i++)
                if(aks[i].isPlaying() || aksin[i].isPlaying())
                {
                    int radius = 8 + random.nextInt(8);
                    g.strokeOval(playerX - radius, playerY - radius, radius * 2, radius * 2);
                }
                
                g.setFill(Color.RED);
                g.fillText("Magazine = "+playerMagazineCounter, 16, 16);
                g.fillText("FPS = "+fps, 16, 32);
                g.fillText("BulletLife 0 = "+bulletLife[0], 16, 48);
                
                g.fillOval(stalkerX - 16, stalkerY - 16, 32, 32);
                
                g.fillText("Balance = "+(playerX - WIDTH/2) / (WIDTH / 2), 16, 64);
                
                g.setFill(Color.BLACK);
                for(int i = 0; i < bulletLife.length; i++)
                if(bulletLife[i] > 0)
                g.fillRect(bulletX[i] - 2, bulletY[i] - 2, 4, 4);
                
                
                
                for(int i = 0; i < bulletLife.length; i++)
                if(bulletLife[i] > 0)
                g.fillRect(bulletX[i] - 2, bulletY[i] - 2, 4, 4);
                
                g.setFill(Color.WHITE);
                for(int i = 0; i < bulletLife.length; i++)
                if(bulletLife[i] > 0)
                g.fillRect(bulletX[i] - 1, bulletY[i] - 1, 2, 2);
            }
        }.start();

        stage.show();
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent e)
            {
                System.exit(1);
            }
        });
    }
}