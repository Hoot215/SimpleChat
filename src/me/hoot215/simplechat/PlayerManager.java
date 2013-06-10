/*
 * A simple chat plugin with channels.
 * Copyright (C) 2013 Andrew Stevanus (Hoot215) <hoot893@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.hoot215.simplechat;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

public class PlayerManager
  {
    private final SimpleChat plugin = SimpleChat.getInstance();
    private final Map<Player, Chatter> players = Collections
        .synchronizedMap(new HashMap<Player, Chatter>(plugin.getServer()
            .getMaxPlayers()));
    
    public Chatter getChatter (String playerName)
      {
        return this.getChatter(plugin.getServer().getPlayer(playerName));
      }
    
    public Chatter getChatter (Player player)
      {
        if (player == null)
          return null;
        if (players.containsKey(player))
          {
            return players.get(player);
          }
        return this.initializePlayer(player);
      }
    
    public Set<Chatter> getChatters ()
      {
        return new HashSet<Chatter>(players.values());
      }
    
    public Chatter initializePlayer (Player player)
      {
        Chatter chatter = new SimpleChatter(player);
        players.put(player, chatter);
        for (String channel : ((String) chatter
            .getGroupConfigValue("default-channels")).split(","))
          {
            chatter.joinChannel(channel);
          }
        chatter.setActiveChannel((String) chatter
            .getGroupConfigValue("default-active-channel"));
        return chatter;
      }
    
    public void destructPlayer (Player player)
      {
        Chatter chatter = players.get(player);
        if (chatter != null)
          {
            for (Channel ch : chatter.getChannels())
              {
                ch.removeMember(chatter);
              }
          }
        players.remove(player);
      }
  }
