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

package net.elytrium.limboapi.server.world;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.velocitypowered.api.network.ProtocolVersion;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.elytrium.limboapi.api.material.Item;
import net.elytrium.limboapi.api.material.VirtualItem;

@NoArgsConstructor
public class SimpleItem implements VirtualItem {

  private static final Gson gson = new Gson();

  @Getter
  private static final Map<Item, SimpleItem> legacyIdMap = new EnumMap<>(Item.class);

  @Getter
  private final Map<Version, Short> versionIDs = new EnumMap<>(Version.class);

  public short getId(ProtocolVersion version) {
    return getId(Version.map(version));
  }

  public short getId(Version version) {
    return versionIDs.get(version);
  }

  static {
    try {
      URL file = ClassLoader.getSystemResource("mapping/items.json");
      LinkedTreeMap<String, LinkedTreeMap<String, String>> map = gson.fromJson(
          new InputStreamReader(file.openStream(), StandardCharsets.UTF_8), LinkedTreeMap.class);

      for (Item item : Item.values()) {
        SimpleItem simpleItem = new SimpleItem();
        map.get(String.valueOf(item.getId()))
            .forEach((key, value) -> simpleItem.getVersionIDs().put(Version.parse(key), Short.parseShort(value)));
        legacyIdMap.put(item, simpleItem);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static SimpleItem fromItem(Item item) {
    return legacyIdMap.get(item);
  }

  public enum Version {
    LEGACY(EnumSet.range(ProtocolVersion.MINECRAFT_1_7_2, ProtocolVersion.MINECRAFT_1_12_2)),
    MINECRAFT_1_13(ProtocolVersion.MINECRAFT_1_13),
    MINECRAFT_1_13_2(ProtocolVersion.MINECRAFT_1_13_1, ProtocolVersion.MINECRAFT_1_13_2),
    MINECRAFT_1_14(EnumSet.range(ProtocolVersion.MINECRAFT_1_14, ProtocolVersion.MINECRAFT_1_14_4)),
    MINECRAFT_1_15(EnumSet.range(ProtocolVersion.MINECRAFT_1_15, ProtocolVersion.MINECRAFT_1_15_2)),
    MINECRAFT_1_16(ProtocolVersion.MINECRAFT_1_16, ProtocolVersion.MINECRAFT_1_16_1),
    MINECRAFT_1_16_2(EnumSet.range(ProtocolVersion.MINECRAFT_1_16_2, ProtocolVersion.MINECRAFT_1_16_4)),
    MINECRAFT_1_17(EnumSet.range(ProtocolVersion.MINECRAFT_1_17, ProtocolVersion.MINECRAFT_1_17_1));

    private static final EnumMap<ProtocolVersion, Version> mcVersionToItemVersions = new EnumMap<>(
        ProtocolVersion.class);

    public static Version parse(String from) {
      switch (from) {
        case "1.13":
          return MINECRAFT_1_13;
        case "1.13.2":
          return MINECRAFT_1_13_2;
        case "1.14":
          return MINECRAFT_1_14;
        case "1.15":
          return MINECRAFT_1_15;
        case "1.16":
          return MINECRAFT_1_16;
        case "1.16.2":
          return MINECRAFT_1_16_2;
        case "1.17":
          return MINECRAFT_1_17;
        default:
          return LEGACY;
      }
    }

    static {
      for (Version version : Version.values()) {
        for (ProtocolVersion protocolVersion : version.getVersions()) {
          mcVersionToItemVersions.put(protocolVersion, version);
        }
      }
    }

    private final Set<ProtocolVersion> versions;

    Version(ProtocolVersion... versions) {
      this.versions = EnumSet.copyOf(Arrays.asList(versions));
    }

    Version(Set<ProtocolVersion> versions) {
      this.versions = versions;
    }

    public Set<ProtocolVersion> getVersions() {
      return versions;
    }

    public static Version map(ProtocolVersion protocolVersion) {
      return mcVersionToItemVersions.get(protocolVersion);
    }
  }
}