package com.VortexGames.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.VortexGames.entities.Player;
import com.VortexGames.main.Game;

public class UI {
	
	public void render(Graphics g)
	{		
		g.setColor(Color.red);
		g.fillRect(4, 9,50, 8);
		g.setColor(Color.green);
		g.fillRect(4, 9,(int)((Game.player.life/Game.player.maxLife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife,16,16);
	}
}
