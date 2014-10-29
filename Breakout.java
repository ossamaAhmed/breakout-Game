/*
 * File: Breakout.java
 * -------------------
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 600;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
//delay definition
	private static final int DELAY = 50;
/**instance private members*/
	private GRect paddle;
	private double vx,vy;
	private GOval ball;
	//generate random generator
	private RandomGenerator rgen = RandomGenerator.getInstance();
	//flag variable for gameover
	private boolean Running;
	//number of bricks 
	private int num_bricks;
	//load sounds
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run()
	{
	for(int i=0;i<NTURNS;i++)
	 {
	 setup();
	 labelTurns(i);
	 play();
	 }
	}
    private void play()
    {
    createBall();
     initVelocities();
     while(Running)
      {  
    	 ballMotion();
    	 bounceOfWalls();
    	 collision();
    	 pause(DELAY);
    	 check_for_terminate();
      }
     
    }
  private void labelTurns(int i)
  {
	  GLabel Turns=new GLabel("NUMBER OF TURNS LEFT IS :"+(NTURNS-(i+1)));
	  add(Turns,40,HEIGHT-20);
  }
  
   
  private void check_for_terminate()
  {
	  bottomWall();
	  bricksOver();
	  
  }
  private void bricksOver()
  {
	  if(num_bricks==0)
	  {
		  Running=false;
		  removeAll();
		  GLabel temp=new GLabel("YOU WIN");
		  add(temp,WIDTH/2,HEIGHT/2);
		  pause(2000);
	  }
  }
  private void collision()
  {
	  GObject collider = getCollidingObject();
	  if(collider!=null)
	  {
		  act_with_collider(collider);
	  }
	  
  }
  private void act_with_collider(GObject collider)
  {
	  if(collider==paddle)
	  {
		  vy=-vy;
		  bounceClip.play();
	  }
	  else
	  {
		  remove(collider);
		  vy=-vy;
		  num_bricks--;
		  bounceClip.play();
	  }
  }
  
    
  //get the colliding object if there any
   private GObject getCollidingObject()
   { 
	   if(getElementAt(ball.getX(),ball.getY())!=null)
	   {
		return (getElementAt(ball.getX(),ball.getY()));   
	   }
	   else if(getElementAt(ball.getX()+(2*BALL_RADIUS),ball.getY())!=null)
	   {
		   return (getElementAt(ball.getX()+(2*BALL_RADIUS),ball.getY()));
	   }
	   else if(getElementAt(ball.getX(),ball.getY()+(2*BALL_RADIUS))!=null)
	   {
		   return (getElementAt(ball.getX(),ball.getY()+(2*BALL_RADIUS)));
	   }
	   else if (getElementAt(ball.getX()+(2*BALL_RADIUS),ball.getY()+(2*BALL_RADIUS))!=null)
	   {
		   return (getElementAt(ball.getX()+(2*BALL_RADIUS),ball.getY()+(2*BALL_RADIUS)));
	   }
	   else 
	   {
		   return null;
	   }
   }

   private void ballMotion()
   {
	   ball.move(vx,vy);
   }
    //check for bouncing walls and take actions
   private void bounceOfWalls()
   {rightWall();
    leftWall();
    upperWall();
   }
   private void rightWall()
   {
	   if((ball.getX()+(BALL_RADIUS*2))>=WIDTH)
	   {
		   vx=-vx;
	   }
   }
   private void leftWall()
   {
	   if(ball.getX()<=0)	   
	   {  
		   vx=-vx;
	   }
   }
   private void bottomWall()
   {//the 4 below is just wierd :D i don't know why did it work :D
	   if(ball.getY()>=HEIGHT-(BALL_RADIUS*4))	   
	   {  
		   Running=false;
		   removeAll();
		   GLabel temp=new GLabel("YOU lose");
		   add(temp,WIDTH/2,HEIGHT/2);
		   pause(2000);
	   }
   }
   private void upperWall()
   {
	   if(ball.getY()<=0)	   
	   {  
		   vy=-vy;
	   }
   }
   
    //initializing.....
    private void initVelocities()
    {
    	vx = rgen.nextDouble(1.0,9);
        if (rgen.nextBoolean(0.5)) 
    	 {vx = -vx;}
          vy=9; 
    }
    private void createBall()
    {
    	ball=new GOval(BALL_RADIUS*2,BALL_RADIUS*2);
    	ball.setFilled(true);
    	ball.setFillColor(Color.BLACK);
    	ball.setColor(Color.BLACK);
    	add(ball,(WIDTH/2)-BALL_RADIUS,(HEIGHT/2)-BALL_RADIUS);
    }
    
    //setting up the play scene including bricks and padel.
	private void setup()
	{   setBackground(Color.MAGENTA);
		Running=true;
	    num_bricks=NBRICK_ROWS*NBRICKS_PER_ROW;
		bricksSetUp();
		paddleSetUp();
	}
	
    /*this is the part that we set 
     * up the bricks at the top of the application 
     * step 1 considered
     */
    private void bricksSetUp()
    {
     /*calculation of the initial position of x
      * as follows in xLoc variable
      */
     int xLoc=(WIDTH-(BRICK_WIDTH*NBRICKS_PER_ROW)-(BRICK_SEP*(NBRICKS_PER_ROW-1)))/2;
     Color c=Color.BLACK;
     int changeC=1;
     for(int i=0;i< NBRICK_ROWS;i++)
     {int rainbow=i+1;
       /*changing and switching colours
       */
	   if(rainbow%2!=0)
	   { switch(changeC)
		 { case 1: c=Color.RED;
		           break;
		   case 2: c=Color.ORANGE;
                   break;
		   case 3: c=Color.YELLOW;
                   break;
		   case 4: c=Color.GREEN;
                   break;
		   case 5: c=Color.CYAN;
                   break;
          }
	     if(changeC==5)
	     {changeC=0;}
	     changeC++;
	   }
	   //end changing colours
    	 for (int j=0; j<NBRICKS_PER_ROW;j++)
    	 {   
    		 GRect temp=new GRect(BRICK_WIDTH,BRICK_HEIGHT);
    		 temp.setFilled(true);
    		 temp.setFillColor(c);
    		 temp.setColor(c);
    		 add(temp,xLoc+(BRICK_WIDTH*j)+(BRICK_SEP*j),BRICK_Y_OFFSET+(BRICK_HEIGHT*i)+(BRICK_SEP*i));
    	 }
     }
    }
    //setting up the paddle to track the mouse.
    private void paddleSetUp()
    { 
    	paddle=new GRect(PADDLE_WIDTH,PADDLE_HEIGHT);
    	paddle.setColor(Color.BLACK);
    	paddle.setFilled(true);
    	paddle.setFillColor(Color.BLACK);
    	add(paddle,(WIDTH/2)-(PADDLE_WIDTH/2),HEIGHT-(PADDLE_HEIGHT+PADDLE_Y_OFFSET) );
    	addMouseListeners();
    }
   
    public void mouseMoved(MouseEvent e) 
    {   if(e.getX()<(WIDTH-PADDLE_WIDTH))
    	{paddle.setLocation(e.getX(), HEIGHT-(PADDLE_HEIGHT+PADDLE_Y_OFFSET) );}
       else
       {paddle.setLocation((WIDTH-PADDLE_WIDTH), HEIGHT-(PADDLE_HEIGHT+PADDLE_Y_OFFSET) );}
    	
    }

}
