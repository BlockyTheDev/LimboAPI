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

package net.elytrium.limboapi.server.world.chunk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.elytrium.limboapi.api.chunk.VirtualBiome;
import net.elytrium.limboapi.api.chunk.VirtualBlock;
import net.elytrium.limboapi.api.chunk.data.ChunkSnapshot;
import net.elytrium.limboapi.api.chunk.data.LightSection;
import net.elytrium.limboapi.server.world.SimpleBlock;

@RequiredArgsConstructor
@Getter
public class SimpleChunkSnapshot implements ChunkSnapshot {

  private final int x;
  private final int z;
  private final boolean fullChunk;
  private final SimpleSection[] sections;
  private final LightSection[] light;
  private final VirtualBiome[] biomes;

  public VirtualBlock getBlock(int x, int y, int z) {
    SimpleSection section = sections[y / 16];
    return section == null ? SimpleBlock.AIR : section.getBlockAt(x, y % 16, z);
  }
}