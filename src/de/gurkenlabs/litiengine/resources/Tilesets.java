package de.gurkenlabs.litiengine.resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import de.gurkenlabs.litiengine.environment.tilemap.xml.Tileset;
import de.gurkenlabs.litiengine.environment.tilemap.xml.TmxException;
import de.gurkenlabs.litiengine.util.io.XmlUtilities;
import javax.annotation.Nullable;
import de.gurkenlabs.litiengine.NullUnmarked;

public class Tilesets extends ResourcesContainer<Tileset> {

  Tilesets() {
  }

  @NullUnmarked @Override
  protected Tileset load(@Nullable URL resourceName) throws IOException, URISyntaxException {
    try {
      Tileset tileset = XmlUtilities.read(Tileset.class, resourceName);
      tileset.finish(resourceName);
      return tileset;
    } catch (JAXBException e) {
      throw new TmxException(e);
    }
  }
}
