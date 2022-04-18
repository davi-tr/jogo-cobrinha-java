/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author tecpl
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_largura = 300;
    private final int B_altura = 300;
    private final int tam_pont = 10;
    private final int total_pont = 900;
    private final int rand_posi = 29;
    private final int atraso = 140;

    private final int x[] = new int[total_pont];
    private final int y[] = new int[total_pont];

    private int ponts;
    private int maca_x;
    private int maca_y;

    private boolean esquerda = false;
    private boolean direita = true;
    private boolean cima = false;
    private boolean baixo = false;
    private boolean inGame = true;

    private Timer tempo;
    private Image bola;
    private Image maca;
    private Image head;

    public Board() {
        initBoard();
    }
    
        private void initBoard(){
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_largura, B_altura));
        loadImages();
        initGame();
    }
        
        private void loadImages(){
            
            ImageIcon iid = new ImageIcon("src/resources/dot.png");
            bola = iid.getImage();
            
            ImageIcon iia = new ImageIcon("src/resources/apple.png");
            maca = iia.getImage();
            
            ImageIcon iih = new ImageIcon("src/resources/head.png");
            head = iih.getImage();
    }
        
        private void initGame(){
            
            ponts = 3;
            
            for (int z =0; z < ponts; z++){
                x[z] = 50 - z * 10;
                y[z] = 50;
            }
        
            locateMaca();
            
            tempo = new Timer(atraso, this);
            tempo.start();
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            
            doDrawing(g);
        }
        
        private void doDrawing(Graphics g){
            
            if(inGame){
                g.drawImage(maca, maca_x, maca_y, this);
                
                for(int z = 0; z < ponts; z++){
                   if(z==0){
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(bola, x[z], y[z], this);
                }
              }
                Toolkit.getDefaultToolkit().sync();
                
            }else{
                gameOver(g);
            }
        }
        private void gameOver(Graphics g){
            String msg = "Fim de Jogo";
            Font small = new Font("Helvetica", Font.BOLD,14);
            FontMetrics metr = getFontMetrics(small);
            
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, (B_largura - metr.stringWidth(msg)) / 2, B_altura/2);
        }
        
        private void checkMaca(){
            if ((x[0] == maca_x) && (y[0]== maca_y)){
                ponts++;
                locateMaca();
            }
        }
        
        private void move(){
            
            for(int z = ponts; z > 0; z--){
                x[z] = x[(z - 1)];
                y[z] = y[(z - 1)];
            }
            if(esquerda) {
                x[0] -= tam_pont;
            }
            if(direita) {
                x[0] += tam_pont;
            }
            if(cima){
                y[0] -= tam_pont;
            }
            if(baixo){
                y[0] += tam_pont;
            }
        }
        
       private void checkColisao() {

        for (int z = ponts; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_altura) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_largura) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            tempo.stop();
        }
    }
    private void locateMaca(){
        
        int r = (int) (Math.random()*rand_posi);
        maca_x = ((r * tam_pont));
        
        r = (int) (Math.random()*rand_posi);        
        maca_y = ((r * tam_pont));
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        
        if(inGame){
            checkMaca();
            checkColisao();
            move();
        }
        repaint();
    }
     private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!direita)) {
                esquerda = true;
                cima = false;
                baixo = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!esquerda)) {
                direita = true;
                cima = false;
                baixo = false;
            }

            if ((key == KeyEvent.VK_UP) && (!baixo)) {
                cima = true;
                direita = false;
                esquerda = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!cima)) {
                baixo = true;
                direita = false;
                esquerda = false;
            }
        }
    }
}

