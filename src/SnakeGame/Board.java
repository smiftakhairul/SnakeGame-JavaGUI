package SnakeGame;

import javax.swing.*;

import java.io.*;
import java.util.Scanner;

import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
	
	public final int B_WIDTH=400;
	public final int B_HEIGHT=400;
	public final int DOT_SIZE=10;
	public final int ALL_DOTS=10000;
	public final int RAND_POS_X=39;
	public final int RAND_POS_Y=36;
	public final int DELAY=140;
	public final int BONUS_DELAY=250;
	
	public final int[] x=new int[ALL_DOTS];
	public final int[] y=new int[ALL_DOTS];
	
	private int dots;
	private int apple_x;
	private int apple_y;
	private int bonus_apple_x;
	private int bonus_apple_y;
	private int count;
	private int btc;
	
	private int life;
	private int point;
	private int bPoint;
	private int highScore;
	private int totalScore;
	
	private boolean moveLeft = false;
    private boolean moveRight = true;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean inGame = true;
    
    private boolean drawApple=true;
    private boolean drawBonusApple=false;
    
    private Timer timer;
    private Timer bonusTimer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image bonusApple;
    private Image border;
    
    private Dimension dim;
    
    public Board()
    {
    	dim=new Dimension(B_WIDTH, B_HEIGHT);
    	
    	addKeyListener(new Controller());
    	setPreferredSize(dim);
    	setBackground(Color.black);
    	setFocusable(true);
    	
    	storeValueAndMethods();
    }
    
    public void imagesLoader()
    {
    	ImageIcon bii=new ImageIcon("images\\dot.png");
    	ball=bii.getImage();
    	
    	ImageIcon aii=new ImageIcon("images\\apple.png");
    	apple=aii.getImage();
    	
    	ImageIcon hii=new ImageIcon("images\\head.png");
    	head=hii.getImage();
    	
    	ImageIcon baii=new ImageIcon("images\\bonusApple.png");
    	bonusApple=baii.getImage();
    	
    	ImageIcon brii=new ImageIcon("images\\border.png");
    	border=brii.getImage();
    }
    
    public void storeValueAndMethods()
    {
    	dots=3;
    	count=0;
    	btc=0;
    	life=5;
    	point=0;
    	bPoint=0;
    	highScore=0;
    	totalScore=0;
    	
    	locateApple();
    	locateBonusApple();
    	imagesLoader();
    	initializeGame();
    }
    
    public void initializeGame()
    {
    	timer=new Timer(DELAY, this);
    	timer.start();
    }
    
    public void paint(Graphics g)
    {
    	super.paintComponent(g);
    	
    	draw(g);
    }
    
    public void draw(Graphics g)
    {
    	if(inGame)
    	{	
    		if(count!=0 && count%3==0)
    		{
    			drawApple=false;
    			drawBonusApple=true;
    			
    			bonusTimer=new Timer(BONUS_DELAY, bonusTimerInit());
    			bonusTimer.start();
    		}
    		
    		if(drawApple)
    			drawBonusApple=false;
    		
    		if(drawApple)
    		{
    			g.drawImage(apple, apple_x, apple_y, this);
    		}
    		
    		if(drawBonusApple)
    		{
    			if(btc<=45)
    			{
    				g.drawImage(bonusApple, bonus_apple_x, bonus_apple_y, this);
    			}
    			
    			else if(btc>45)
    			{
    				count=0;
    				drawApple=true;
    				btc=0;
    			}
    		}
    		
    		for(int i=0; i<dots; i++)
    		{
    			if(i==0)
    				g.drawImage(head, x[i], y[i], null);
    			
    			else
    				g.drawImage(ball, x[i], y[i], null);
    		}
    		
    		g.drawImage(border, 0, 370, null);
    		scoreInitialize(g);
    	}
    	
    	else
    		gameOverInitialize(g);
    }
    
    private ActionListener bonusTimerInit() {
    	
    	btc++;
    	
		return null;
	}

	public void gameOverInitialize(Graphics g)
	{	
    	String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 23);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.orange);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        
        totalScore=point+bPoint;
        
        String scrmsg = "Your Score " +totalScore;
        Font sm = new Font("Helvetica", Font.BOLD, 21);
        FontMetrics mtr = getFontMetrics(small);
        
        totalScoreWrite();

        g.setColor(Color.orange);
        g.setFont(sm);
        g.drawString(scrmsg, (B_WIDTH - mtr.stringWidth(scrmsg)) / 2, (B_HEIGHT / 2)+30);
        
        File f=new File("file.txt");
        
        try{
        	Scanner get=new Scanner(f);
        	
        	while(get.hasNextLine()==true)
        	{
        		String data=get.nextLine();
        		
        		highScore=Integer.parseInt(data);
        	}
        	get.close();
        }
        
        catch(Exception e) {
        	
        }
        
        String hsmsg="High Score " +highScore;
        Font hm = new Font("Helvetica", Font.BOLD, 21);
        FontMetrics mr = getFontMetrics(small);

        g.setColor(Color.orange);
        g.setFont(hm);
        g.drawString(hsmsg, (B_WIDTH - mr.stringWidth(hsmsg)) / 2, (B_HEIGHT / 2)+60);
    }
	
	public void totalScoreWrite()
	{
		File f=new File("file.txt");
		
		try{
			Scanner get=new Scanner(f);
			
			while(get.hasNextLine()==true)
			{
				String data=get.nextLine();
				
				int d=Integer.parseInt(data);
				
				if(totalScore>=d)
				{
					FileWriter fw=new FileWriter(f, false);
					
					fw.write(totalScore +"\n");
					fw.close();
				}
				
				else if(totalScore<d)
				{
					FileWriter fw=new FileWriter(f, false);
					
					fw.write(data);
					fw.close();
				}
			}
			get.close();
		}
		catch(Exception e) {
			
		}
	}
	
	public void scoreInitialize(Graphics g)
	{
		String lifemsg="Life: " +life;
		String bonusmsg="Bonus: " +bPoint;
		String pointmsg="Point: " +point;
		
		Font lf=new Font(null,Font.PLAIN, 18);
		
		g.setColor(Color.orange);
		g.setFont(lf);
		
		g.drawString(lifemsg, 10, 396);
		
		Font pt=new Font(null,Font.PLAIN, 18);
		
		g.setColor(Color.orange);
		g.setFont(pt);
		
		g.drawString(pointmsg, 300, 396);
		
		Font bp=new Font(null,Font.PLAIN, 18);
		FontMetrics metr = getFontMetrics(bp);
		
		g.setColor(Color.orange);
		g.setFont(bp);
		
		g.drawString(bonusmsg, ((B_WIDTH-metr.stringWidth(bonusmsg)) /2)-12, 396);
	}
    
    public void locateApple()
    {
    	int ran_x=(int)(Math.random()*RAND_POS_X);
    	apple_x=ran_x*DOT_SIZE;
    	
    	int ran_y=(int)(Math.random()*RAND_POS_Y);
    	apple_y=ran_y*DOT_SIZE;
    }
    
    public void locateBonusApple()
    {
    	int ran_x=(int)(Math.random()*RAND_POS_X);
    	bonus_apple_x=ran_x*DOT_SIZE;
    	
    	int ran_y=(int)(Math.random()*RAND_POS_Y);
    	bonus_apple_y=ran_y*DOT_SIZE;
    }
    
    public void eatApple()
    {
    	Rectangle headRect=new Rectangle(x[0], y[0], 8, 8);
    	Rectangle appleRect=new Rectangle(apple_x, apple_y, 8, 8);
    	
    	if(headRect.intersects(appleRect))
    	{
    		dots++;
    		locateApple();
    		locateBonusApple();
    		count++;
    		point+=5;
    	}
    }
    
    public void eatBonusApple()
    {
    	Rectangle headRect=new Rectangle(x[0], y[0], 8, 8);
    	Rectangle bonusAppleRect=new Rectangle(bonus_apple_x, bonus_apple_y, 13, 13);
    	
    	if(drawBonusApple)
    	{
	    	if(headRect.intersects(bonusAppleRect))
	    	{
	    		drawApple=true;
	    		count=0;
	    		
	    		if(btc>=30 && btc<=45)
	    			bPoint+=150;
	    		
	    		else if(btc>=15 && btc<30)
	    			bPoint+=250;
	    		
	    		else if(btc>0 && btc<15)
	    			bPoint+=400;
	    		
	    		else
	    			bPoint+=0;
	    		
	    		btc=0;
	    		
	    		locateBonusApple();
	    	}
    	}
    }
    
    public void move()
    {
    	for(int i=dots; i>0; i--)
    	{
    		x[i]=x[i-1];
    		y[i]=y[i-1];
    	}
    	
    	if(moveLeft) 
            x[0]-=DOT_SIZE;

        if(moveRight)
            x[0]+=DOT_SIZE;

        if(moveUp)
            y[0]-=DOT_SIZE;

        if(moveDown)
            y[0]+=DOT_SIZE;
    }
    
    public void checkCollision()
    {
    	for(int i=dots; i>0; i--) 
    	{
            if((i>4) && (x[0] == x[i]) && (y[0] == y[i]))
            {
                life--;
            }
        }

        if(y[0]>=(B_HEIGHT-40)+10)
        {
            y[0]=0;
            life--;
        }

        if(y[0]<0)
        {
        	
            y[0]=(B_HEIGHT-48)+10;
            life--;
        }

        if(x[0]>=B_WIDTH+10)
        {
            x[0]=0;
            life--;
        }

        if(x[0]<0)
        {
            x[0]=B_WIDTH+10;
            life--;
        }
        
        if(life==0)
        {
        	inGame=false;
        	timer.stop();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(inGame)
		{
			move();
			eatApple();
			eatBonusApple();
			checkCollision();
		}
		repaint();
	}
	
	public class Controller extends KeyAdapter {
		
		public void keyPressed(KeyEvent e)
		{
			int key=e.getKeyCode();

            if((key==KeyEvent.VK_LEFT) && (!moveRight)) {
                moveLeft=true;
                moveUp=false;
                moveDown=false;
            }

            if((key==KeyEvent.VK_RIGHT) && (!moveLeft)) {
                moveRight=true;
                moveUp=false;
                moveDown=false;
            }

            if((key==KeyEvent.VK_UP) && (!moveDown)) {
                moveUp=true;
                moveRight=false;
                moveLeft=false;
            }

            if((key==KeyEvent.VK_DOWN) && (!moveUp)) {
                moveDown=true;
                moveRight=false;
                moveLeft=false;
            }
            
            if(key==KeyEvent.VK_SPACE)
            {
            	timer.stop();
            }
            
            if(key==KeyEvent.VK_ENTER)
            {
            	timer.start();
            }
		}
		
	}

}
