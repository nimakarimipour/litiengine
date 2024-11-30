package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.tilemap.IMapObject;
import de.gurkenlabs.litiengine.environment.tilemap.MapObjectType;

@XmlRootElement(name = "template")
public class Blueprint extends MapObject {
  /**
   * Templates in this format typically come from the Tiled editor and only support a single MapObject.
   */
  public static final String TEMPLATE_FILE_EXTENSION = "tx";

  /**
   * Blueprint in this format support multiple map objects as children (extended template XML).
   */
  public static final String BLUEPRINT_FILE_EXTENSION = "xtx";

  @XmlElement(name = "object")
  private List<MapObject> items = new ArrayList<>();

  @XmlTransient
  private boolean keepIds;

  /**
   * Initializes a new instance of the {@code Blueprint} map object.
   */
  public Blueprint() {
    super();
  }

  @XmlTransient
  public Iterable<MapObject> getItems() {
    return this.items;
  }

  /**
   * Gets a value that indicates whether the IDs if this blueprint's map-objects
   * should be kept. This is currently used when objects are cut and pasted
   * afterwards.
   * 
   * @return True if the ids for all {@link IMapObject}s of this {@link Blueprint} should be re-applied after building new instances.
   */
  public boolean keepIds() {
    return this.keepIds;
  }
}
