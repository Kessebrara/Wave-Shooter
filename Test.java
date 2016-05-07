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
    boolean playerMovingLeft, playerMovingRight, playerMovingUp, playerMovingDown;
    
    Random random = new Random();
    
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    
    AudioClip[] aks = new AudioClip[4];
    
    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) 
    {
        stage.setTitle( "Collect the Money Bags!" );
        
        for(int i = 0; i < 4; i++)
        {
            URL resource = getClass().getResource("sounds/ak_semi_"+i+".wav");
            aks[i] = new AudioClip(resource.toString());
        }

        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        root.getChildren().add(canvas);

        ArrayList<String> input = new ArrayList<String>();

        scene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
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
                    if(key == "SPACE")
                    {
                        aks[random.nextInt(4)].play(1.0);
                    }
                    
                }
            });

        scene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
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
                
                avg = distance/total;
                
                
                
                
                // collision detection
                
                
                // render 
                
                g.setFill(Color.WHITE);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                g.setFill(Color.GREEN);
                                
                g.setFill(Color.BLACK);
                g.fillOval(playerX-8, playerY-8, 16, 16);
                
                g.setFill(Color.RED);
                g.fillText("Total elapsed time = "+(int)total, 16, 32);
                g.fillText("Time since last calc = "+ET, 16, 48);
                g.fillText("Pixels traveled = "+distance, 16, 64);
                g.fillText("Avg distance per second = "+avg, 16, 80);
                g.fillText("FPS = "+fps, 16, 96);
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