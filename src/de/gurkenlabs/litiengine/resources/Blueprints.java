package de.gurkenlabs.litiengine.resources;

import java.net.URL;

import javax.xml.bind.JAXBException;

import de.gurkenlabs.litiengine.environment.tilemap.xml.Blueprint;
import de.gurkenlabs.litiengine.environment.tilemap.xml.TmxException;
import de.gurkenlabs.litiengine.util.io.FileUtilities;
import de.gurkenlabs.litiengine.util.io.XmlUtilities;
import javax.annotation.Nullable;

public class Blueprints extends ResourcesContainer<Blueprint> {

  Blueprints() {
  }

  public static boolean isSupported(String fileName) {
    String extension = FileUtilities.getExtension(fileName);
    return extension != null && !extension.isEmpty() && (extension.equalsIgnoreCase(Blueprint.BLUEPRINT_FILE_EXTENSION) || extension.equalsIgnoreCase(Blueprint.TEMPLATE_FILE_EXTENSION));
  }

  @Nullable @Override
  protected Blueprint load(@Nullable URL resourceName) throws Exception {
    Blueprint blueprint;
    try {
      blueprint = XmlUtilities.read(Blueprint.class, resourceName);
    } catch (JAXBException e) {
      throw new TmxException("could not parse xml data", e);
    }

    return blueprint;
  }
  
  @Nullable @Override
  protected String getAlias(String resourceName, @Nullable Blueprint resource) {
    if (resource == null || resource.getName() == null || resource.getName().isEmpty() || resource.getName().equalsIgnoreCase(resourceName)) {
      return null;
    }

    return resource.getName();
  }
}
