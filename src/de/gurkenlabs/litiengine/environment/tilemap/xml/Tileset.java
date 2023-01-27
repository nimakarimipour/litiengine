package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.gurkenlabs.litiengine.environment.tilemap.ICustomProperty;
import de.gurkenlabs.litiengine.environment.tilemap.IMapImage;
import de.gurkenlabs.litiengine.environment.tilemap.ITerrain;
import de.gurkenlabs.litiengine.environment.tilemap.ITile;
import de.gurkenlabs.litiengine.environment.tilemap.ITileOffset;
import de.gurkenlabs.litiengine.environment.tilemap.ITileset;
import de.gurkenlabs.litiengine.environment.tilemap.ITilesetEntry;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.io.FileUtilities;
import de.gurkenlabs.litiengine.util.io.XmlUtilities;
import javax.annotation.Nullable;

@XmlRootElement(name = "tileset")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tileset extends CustomPropertyProvider implements ITileset {
  private static final Logger log = Logger.getLogger(Tileset.class.getName());
  public static final String FILE_EXTENSION = "tsx";

  @XmlAttribute
  private int firstgid;

  @Nullable @XmlElement
  private MapImage image;

  @Nullable @XmlAttribute
  private Integer margin;

  @Nullable @XmlAttribute
  private String name;

  @Nullable @XmlAttribute
  private Integer tilewidth;

  @Nullable @XmlAttribute
  private Integer tileheight;

  @Nullable @XmlElement(name = "tileoffset")
  private TileOffset tileoffset;

  @Nullable @XmlAttribute
  private Integer tilecount;

  @Nullable @XmlAttribute
  private Integer columns;

  @Nullable @XmlAttribute
  private Integer spacing;

  @Nullable @XmlAttribute
  private String source;

  @Nullable @XmlElementWrapper(name = "terraintypes")
  @XmlElement(name = "terrain")
  private List<Terrain> terrainTypes = null;

  @Nullable @XmlElement(name = "tile")
  private List<TilesetEntry> tiles = null;

  @Nullable @XmlTransient
  private List<TilesetEntry> allTiles;

  @Nullable @XmlTransient
  protected Tileset sourceTileset;

  @Nullable private transient Spritesheet spriteSheet;

  public Tileset() {
    Resources.images().addClearedListener(() -> this.spriteSheet = null);
  }

  public Tileset(Tileset source) {
    this.source = source.getName() + "." + FILE_EXTENSION;
    this.sourceTileset = source;
    this.firstgid = 1;
  }

  @Override
  public Map<String, ICustomProperty> getProperties() {
    return this.sourceTileset != null ? this.sourceTileset.getProperties() : super.getProperties();
  }

  @Override
  public int getFirstGridId() {
    return this.firstgid;
  }

  @Nullable @Override
  public IMapImage getImage() {
    return this.sourceTileset != null ? this.sourceTileset.getImage() : this.image;
  }

  /**
   * Gets the margin.
   *
   * @return the margin
   */
  @Override
  public int getMargin() {
    if (this.sourceTileset != null) {
      return this.sourceTileset.getMargin();
    }

    if (this.margin == null) {
      return 0;
    }

    return this.margin;
  }

  @Nullable @Override
  public String getName() {
    return this.sourceTileset != null ? this.sourceTileset.getName() : this.name;
  }

  @Override
  public void setName(@Nullable String name) {
    this.name = name;
  }

  /**
   * Gets the spacing.
   *
   * @return the spacing
   */
  @Override
  public int getSpacing() {
    if (this.sourceTileset != null) {
      return this.sourceTileset.getSpacing();
    }

    if (this.spacing == null) {
      return 0;
    }

    return this.spacing;
  }

  @Nullable @Override
  @XmlTransient
  public Spritesheet getSpritesheet() {
    if (this.spriteSheet == null && this.getImage() != null) {
      this.spriteSheet = Resources.spritesheets().get(this.getImage().getSource());
      if (this.spriteSheet == null) {
        this.spriteSheet = Resources.spritesheets().load(this);
        if (this.spriteSheet == null) {
          return null;
        }
      }
    }

    return this.spriteSheet;
  }

  @Override
  public Dimension getTileDimension() {
    return this.sourceTileset != null ? this.sourceTileset.getTileDimension() : new Dimension(this.getTileWidth(), this.getTileHeight());
  }

  /**
   * Gets the tile height.
   *
   * @return the tile height
   */
  @Override
  public int getTileHeight() {
    return this.sourceTileset != null ? this.sourceTileset.getTileHeight() : this.tileheight;
  }

  /**
   * Gets the tile width.
   *
   * @return the tile width
   */
  @Override
  public int getTileWidth() {
    return this.sourceTileset != null ? this.sourceTileset.getTileWidth() : this.tilewidth;
  }

  @Override
  public List<ITerrain> getTerrainTypes() {
    if (this.sourceTileset != null) {
      return this.sourceTileset.getTerrainTypes();
    }

    List<ITerrain> types = new ArrayList<>();
    if (this.terrainTypes == null) {
      return types;
    }

    for (int i = 0; i < this.terrainTypes.size(); i++) {
      types.add(i, this.terrainTypes.get(i));
    }

    return types;
  }

  @Override
  public ITerrain[] getTerrain(int tileId) {
    if (this.sourceTileset != null) {
      return this.sourceTileset.getTerrain(tileId);
    }

    ITerrain[] terrains = new ITerrain[4];
    if (!this.containsTile(tileId)) {
      return terrains;
    }

    TilesetEntry tile = this.allTiles.get(tileId);
    int[] tileTerrains = tile.getTerrainIds();
    for (int i = 0; i < 4; i++) {
      if (tileTerrains[i] < 0 || tileTerrains[i] >= this.getTerrainTypes().size()) {
        continue;
      }

      ITerrain terrain = this.getTerrainTypes().get(tileTerrains[i]);
      if (terrain == null) {
        continue;
      }

      terrains[i] = terrain;
    }

    return terrains;
  }

  @Override
  public int getColumns() {
    return this.sourceTileset != null ? this.sourceTileset.getColumns() : this.columns;
  }

  @Nullable @Override
  public ITileOffset getTileOffset() {
    return this.sourceTileset != null ? this.sourceTileset.getTileOffset() : this.tileoffset;
  }

  @Override
  public int getTileCount() {
    if (this.sourceTileset != null) {
      return this.sourceTileset.getTileCount();
    }

    return this.tilecount != null ? this.tilecount : 0;
  }

  @Nullable @Override
  public ITilesetEntry getTile(int id) {
    if (this.sourceTileset != null) {
      return this.sourceTileset.getTile(id);
    }

    if (id < 0 || id >= this.getTileCount()) {
      return null;
    }

    return this.allTiles.get(id);
  }

  @Override
  public boolean containsTile(ITile tile) {
    ITilesetEntry entry = tile.getTilesetEntry();
    return entry == null ? this.containsTile(tile.getGridId()) : this.containsTile(tile.getTilesetEntry());
  }

  @Override
  public boolean containsTile(int tileId) {
    return tileId >= this.firstgid && tileId < this.firstgid + this.getTileCount();
  }

  @Override
  public boolean containsTile(ITilesetEntry entry) {
    if (entry == null) {
      return false;
    }

    if (this.sourceTileset != null) {
      return this.sourceTileset.containsTile(entry);
    }

    return this.allTiles != null && this.allTiles.contains(entry);
  }

  @Override
  public void finish(@Nullable URL location) throws TmxException {
    super.finish(location);
    if (this.source != null) {
      // don't reload the source if it's already been loaded in a resource bundle
      if (this.sourceTileset == null) {
        try {
          URL url = new URL(location, this.source);
          this.sourceTileset = Resources.tilesets().get(url);
          if (this.sourceTileset == null) {
            throw new MissingExternalTilesetException(this.source);
          }
        } catch (MalformedURLException e) {
          throw new MissingExternalTilesetException(e);
        }
      }
    } else {
      super.finish(location);
      if (this.image != null) {
        this.image.finish(location);
      }
      if (this.terrainTypes != null) {
        for (Terrain terrain : this.terrainTypes) {
          terrain.finish(location);
        }
      }
      if (this.tiles != null) {
        // unsaved tiles don't need any post-processing
        for (TilesetEntry entry : this.tiles) {
          entry.finish(location);
        }
      }
    }
  }

  public void saveSource(String basePath) {
    if (this.sourceTileset == null) {
      return;
    }

    XmlUtilities.save(this.sourceTileset, FileUtilities.combine(basePath, this.source), FILE_EXTENSION);
  }

  public boolean isExternal() {
    return this.source != null;
  }

  public void load(List<Tileset> rawTilesets) {
    if (this.source == null) {
      return;
    }

    for (Tileset set : rawTilesets) {
      String fileName = FileUtilities.getFileName(this.source);
      if (set.getName() != null && set.getName().equals(fileName)) {
        this.sourceTileset = set;
        break;
      }
    }
  }

  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller u, Object parent) {
    if (this.source == null) {
      this.allTiles = new ArrayList<>(this.getTileCount());
      if (this.tiles != null) {
        this.allTiles.addAll(this.tiles);
      }
      // add missing entries
      ListIterator<TilesetEntry> iter = this.allTiles.listIterator();
      for (int i = 0; i < this.getTileCount(); i++) {
        if (add(iter)) {
          iter.add(new TilesetEntry(this, iter.nextIndex()));
        }
      }
      if (iter.hasNext()) {
        log.log(Level.WARNING, "tileset \"{0}\" had a tilecount attribute of {1} but had tile IDs going beyond that", new Object[] { this.name, this.getTileCount() });
        while (iter.hasNext()) {
          int nextId = iter.next().getId();
          iter.previous();
          while (iter.nextIndex() < nextId) {
            iter.add(new TilesetEntry(this, iter.nextIndex()));
          }
        }
        this.tilecount = this.allTiles.size();
      }
      this.updateTileTerrain();
    }
  }

  private static boolean add(ListIterator<TilesetEntry> iter) {
    if (!iter.hasNext()) {
      return true;
    }
    if (iter.next().getId() != iter.previousIndex()) {
      iter.previous(); // move the cursor back
      return true;
    }
    return false;
  }

  @SuppressWarnings("unused")
  private void beforeMarshal(Marshaller m) {
    if (this.sourceTileset != null) {
      this.tilewidth = null;
      this.tileheight = null;
      this.tilecount = null;
      this.columns = null;
    } else {
      this.tiles = new ArrayList<>(this.allTiles);
      Iterator<TilesetEntry> iter = this.tiles.iterator();
      while (iter.hasNext()) {
        if (!iter.next().shouldBeSaved()) {
          iter.remove();
        }
      }
    }

    if (this.margin != null && this.margin == 0) {
      this.margin = null;
    }

    if (this.spacing != null && this.spacing == 0) {
      this.spacing = null;
    }

    if (this.getProperties() != null && this.getProperties().isEmpty()) {
      this.setProperties(null);
    }
  }

  private void updateTileTerrain() {
    if (this.sourceTileset == null && this.tiles != null) {
      // only go through saved tiles because unsaved tiles can't have terrains
      for (TilesetEntry entry : this.tiles) {
        entry.setTerrains(this.getTerrain(entry.getId()));
      }
    }
  }
}
