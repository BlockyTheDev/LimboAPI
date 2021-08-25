/*
 * Copyright (C) 2021 Elytrium
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

package net.elytrium.limboapi.injection;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import lombok.RequiredArgsConstructor;
import net.elytrium.limboapi.LimboAPI;

@RequiredArgsConstructor
public class DisconnectListener {
  private final LimboAPI limboAPI;

  public void onDisconnect(DisconnectEvent e) {
    limboAPI.unsetVirtualServerJoined(e.getPlayer());
  }
}